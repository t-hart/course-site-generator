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
import csg.TAManagerApp;
import csg.data.TAData;

/**
 *
 * @author moham_000
 */
public class timec implements jTPS_Transaction {
TAManagerApp app;
 HashMap<String, Label> officeHoursGridTACellLabels;
 HashMap<String,Label>clone;
 TAData taData;
 boolean big;
 int k;
 int dif;
 int oldHour;
 HashMap<String,Label>kk;
    public timec(TAManagerApp app,HashMap<String, Label> officeHoursGridTACellLabels,TAData ta,boolean big,int k,int dif,HashMap<String,Label>kk){
        this.app=app;
        clone=(HashMap<String, Label>) officeHoursGridTACellLabels.clone();
        this.officeHoursGridTACellLabels=officeHoursGridTACellLabels;
        this.taData=ta;
        this.big=big;
        this.k=k;
        this.dif=dif;
        this.kk=kk;
        oldHour=taData.getEndHour();
    }
    @Override
    public void doTransaction() {
          int offset=0;
           taData.initHours(""+taData.getStartHour(), ""+k);
          taData.setEndHour(k);
               app.getWorkspaceComponent().resetWorkspace();
             app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
  
             System.out.print("\n"+ dif+" "+big+"\n");
             Set<String>ne=officeHoursGridTACellLabels.keySet();
             for(String ss:ne){
                 if(!big){
                 String ro=ss.split("_")[0];
                 ro+="_"+(Integer.parseInt(ss.split("_")[1]));
                 if(kk.get(ro)!=null)
              officeHoursGridTACellLabels.get(ss).setText(kk.get(ro).getText());
                 }else{
                 String ro=ss.split("_")[0];
                 ro+="_"+(Integer.parseInt(ss.split("_")[1]));
                 if(kk.get(ro)!=null)
              officeHoursGridTACellLabels.get(ss).setText(kk.get(ro).getText());
                 }     
                 }
    }

    @Override
    public void undoTransaction() {
       taData.initHours(""+taData.getStartHour(),""+ oldHour);
        taData.setEndHour(oldHour);
        app.getWorkspaceComponent().resetWorkspace();
        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());
      
        Set<String>ne=officeHoursGridTACellLabels.keySet();
        for(String ss:ne){
        officeHoursGridTACellLabels.get(ss).setText(clone.get(ss).getText());
        }
    }
    
}
