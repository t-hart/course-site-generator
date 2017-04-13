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
import csg.TAManagerApp;
import csg.data.TAData;
import csg.data.TeachingAssistant;

/**
 *
 * @author moham_000
 */
public class deleteTA  implements jTPS_Transaction{
       TAManagerApp app;
       TAController controller;
       Object selectedItem;
       TAWorkspace workspace;
       String email;
       String name;
       boolean ug = false;
       ArrayList<StringProperty> list;
    deleteTA(TAManagerApp app,TAController controller,Object selectedItem, TAWorkspace workspace){
        this.app=app;
        this.controller=controller;
        this.selectedItem=selectedItem;
        this.workspace=workspace;
       // this.selectedItem=selectedItem
    }
    @Override
    public void doTransaction() {
       // GET THE TA AND REMOVE IT
                TeachingAssistant ta = (TeachingAssistant)selectedItem;
                String taName = ta.getName();
                this.name=ta.getName();
                this.email=ta.getEmail();
                this.ug = ta.getUndergrad().get();
                TAData data = (TAData)app.getDataComponent();
                data.removeTA(taName);
                ArrayList<StringProperty> list=new ArrayList<StringProperty>();
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
                this.list=list;
                // WE'VE CHANGED STUFF
                controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        TAData data=(TAData)app.getDataComponent();
        data.addTA(name, email, ug);
        for(int i=0;i<list.size();i++){
            list.get(i).setValue(list.get(i).getValue()+(list.get(i).getValue().equals("")?name:"\n"+name));
        }
        
    }
    
}
