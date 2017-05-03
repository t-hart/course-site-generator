/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import jtps.jTPS_Transaction;
import csg.CourseSiteGeneratorApp;
import csg.data.Data;
import csg.data.TeachingAssistant;
import csg.data.Recitation;
import java.util.Collections;
import javafx.collections.ObservableList;
/**
 *
 * @author moham_000
 */
public class deleteTA implements jTPS_Transaction {

    CourseSiteGeneratorApp app;
    TAController controller;
    Object selectedItem;
    TAWorkspace workspace;
    String email;
    String name;
    boolean ug = false;
    ArrayList<StringProperty> list;
    ArrayList<Recitation> affectedRecs = new ArrayList<>();
    ArrayList<Recitation> toAddRecs = new ArrayList<>();
    
    deleteTA(CourseSiteGeneratorApp app, TAController controller, Object selectedItem, TAWorkspace workspace) {
        this.app = app;
        this.controller = controller;
        this.selectedItem = selectedItem;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        // GET THE TA AND REMOVE IT
        TeachingAssistant ta = (TeachingAssistant) selectedItem;
        String taName = ta.getName();
        this.name = ta.getName();
        this.email = ta.getEmail();
        this.ug = ta.getUndergrad().get();
        Data data = (Data) app.getDataComponent();
        data.removeTA(taName);
        
        /* CHECK FOR RECS WITH THE TA IN IT */
        ObservableList<Recitation> recs = data.getRecitations();
        for(Recitation rec : recs){
            if(rec.getSupervisingTA_1().equals(taName)){
                toAddRecs.add(new Recitation(rec.getSection(), rec.getInstructor(), rec.getDayTime(), rec.getLocation(), "",rec.getSupervisingTA_2()));
                affectedRecs.add(rec);
            }
            else if(rec.getSupervisingTA_2().equals(taName)){
                toAddRecs.add(new Recitation(rec.getSection(), rec.getInstructor(), rec.getDayTime(), rec.getLocation(), rec.getSupervisingTA_1(), ""));
                affectedRecs.add(rec);
            }
        }
        
        for(Recitation rec : affectedRecs){
            data.removeRecitation(rec.getSection());
        }
        
        for(Recitation rec : toAddRecs){
            data.getRecitations().add(rec);
        }
        
        ArrayList<StringProperty> list = new ArrayList<StringProperty>();
        // AND BE SURE TO REMOVE ALL THE TA'S OFFICE HOURS
        HashMap<String, Label> labels = workspace.getOfficeHoursGridTACellLabels();
        for (Label label : labels.values()) {
            if (label.getText().equals(taName)
                    || (label.getText().contains(taName + "\n"))
                    || (label.getText().contains("\n" + taName))) {
                data.removeTAFromCell(label.textProperty(), taName);
                list.add(label.textProperty());
            }
        }
        this.list = list;
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.addTA(name, email, ug);
        
        for(Recitation rec : affectedRecs){
            data.removeRecitation(rec.getSection());
        }
        
        for(Recitation rec : affectedRecs){
            data.getRecitations().add(rec);
        }
        
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setValue(list.get(i).getValue() + (list.get(i).getValue().equals("") ? name : "\n" + name));
        }
        
        affectedRecs.clear();
        toAddRecs.clear();
        Collections.sort(data.getRecitations());
    }

}
