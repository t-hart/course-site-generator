/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import java.util.HashMap;
import java.util.Set;
import javafx.scene.control.Label;
import jtps.jTPS_Transaction;
import csg.CourseSiteGeneratorApp;
import csg.data.Data;

/**
 *
 * @author moham_000
 */
public class javaa implements jTPS_Transaction{
    Data taData;
    CourseSiteGeneratorApp app;
    boolean big;
 int dif;
 int oldHour;
 int k;
 HashMap<String,Label>kk;
     HashMap<String, Label> officeHoursGridTACellLabels;
 HashMap<String,Label>clone;
    public javaa(Data tadata,CourseSiteGeneratorApp app, boolean big, int dif, int oldHour, int k, HashMap<String,Label>kk,HashMap<String, Label> officeHoursGridTACellLabels){
        this.app=app;
        this.big=big;
        this.dif=dif;
        this.oldHour=oldHour;
        this.k=k;
        this.kk=kk;
        this.taData=tadata;
        this.officeHoursGridTACellLabels=officeHoursGridTACellLabels;
    }
    @Override
    public void doTransaction() {
          int offset=0;
           taData.setStartHour(k);
           taData.initHours(""+k, ""+taData.getEndHour());
         
           app.getWorkspaceComponent().resetWorkspace();
             app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
       
             Set<String>ne=officeHoursGridTACellLabels.keySet();
             for(String ss:ne){
                 if(big){
                 String ro=ss.split("_")[0];
                 ro+="_"+(Integer.parseInt(ss.split("_")[1])+dif);
                 if(kk.get(ro)!=null)
              officeHoursGridTACellLabels.get(ss).setText(kk.get(ro).getText());
                 }else{
                 String ro=ss.split("_")[0];
                 ro+="_"+(Integer.parseInt(ss.split("_")[1])-dif);
                 if(kk.get(ro)!=null)
              officeHoursGridTACellLabels.get(ss).setText(kk.get(ro).getText());
                  
                 }
             }    }

    @Override
    public void undoTransaction() {
           taData.setStartHour(oldHour);
           taData.initHours(""+oldHour, ""+taData.getEndHour());
         
           app.getWorkspaceComponent().resetWorkspace();
             app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
             Set<String>ne=officeHoursGridTACellLabels.keySet();
             for(String ss:ne){
                 officeHoursGridTACellLabels.get(ss).setText(kk.get(ss).getText());
             }

    }
    
}
