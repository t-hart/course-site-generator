package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.data.Data;
import csg.data.Team;
import java.util.Collections;
import jtps.jTPS_Transaction;
import javafx.collections.ObservableList;
import csg.data.Recitation;
import csg.data.TeachingAssistant;
/**
 *
 * @author tjhha
 */
public class addEditRec_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TAController controller;
    TAWorkspace workspace;
    
    String section;
    String instructor;
    String dayTime;
    String location;
    TeachingAssistant ta1;
    TeachingAssistant ta2;
    Recitation selectedRec;

    addEditRec_Transaction(CourseSiteGeneratorApp app, TAController controller, TAWorkspace workspace, String section, String instructor, String dayTime, String location, Object ta1, Object ta2, Recitation selectedRec) {
        this.app = app;
        this.controller = controller;
        this.workspace = workspace;
        
        this.section = section;
        this.instructor = instructor;
        this.dayTime = dayTime;
        this.location = location;
        
        if(ta1 != null){
            this.ta1 = (TeachingAssistant)ta1;
        }
        
        if(ta2 != null){
            this.ta2 = (TeachingAssistant)ta2;
        }
        
        if(selectedRec != null){
            this.selectedRec = selectedRec;
        }
    }

    @Override
    public void doTransaction() {
        Data data = (Data) app.getDataComponent();
        ObservableList<Recitation> recs = data.getRecitations();
        
        if(selectedRec != null){
            for(int i = 0; i < recs.size(); i++){
                if(recs.get(i).getSection().equals(selectedRec.getSection())){
                    data.removeRecitation(selectedRec.getSection());
                    break;
                }
            }
            data.addRecitation(section, instructor, dayTime, location, ta1 == null ? "":ta1.getName(), ta2 == null ? "":ta2.getName());
        } else {
            data.addRecitation(section, instructor, dayTime, location, ta1 == null ? "":ta1.getName(), ta2 == null ? "":ta2.getName());
        }


        Collections.sort(data.getRecitations());
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.removeRecitation(section);
        if(selectedRec != null){
            data.addRecitation(selectedRec.getSection(), selectedRec.getInstructor(), selectedRec.getDayTime(), selectedRec.getLocation(), selectedRec.getSupervisingTA_1(), selectedRec.getSupervisingTA_2());
        }
        
        Collections.sort(data.getRecitations());
    }
}
