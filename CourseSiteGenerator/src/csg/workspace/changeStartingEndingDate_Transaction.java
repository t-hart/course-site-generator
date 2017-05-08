/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.data.Data;
import jtps.jTPS_Transaction;
import java.util.Date;

/**
 *
 * @author tjhha
 */
public class changeStartingEndingDate_Transaction implements jTPS_Transaction {
    Date oldDate;
    Date newDate;
    TAWorkspace workspace;
    TAController controller;
    String startEnd;
    CourseSiteGeneratorApp app;
    
    changeStartingEndingDate_Transaction(TAController controller, TAWorkspace workspace, Date oldDate, Date newDate, String startEnd, CourseSiteGeneratorApp app){
        this.workspace = workspace;
        this.oldDate = oldDate;
        this.newDate = newDate;
        this.startEnd = startEnd;
        this.controller = controller;
        this.app = app;
    }
    
    @Override
    public void doTransaction(){
        Data data = (Data) app.getDataComponent();
        if(startEnd.equals("start")){
            controller.setIgnoreStart(true);
            workspace.getStartingMonday().setValue(new java.sql.Date(newDate.getTime()).toLocalDate());
            data.setStartingMonday(newDate);
        }
        else if(startEnd.equals("end")){
            controller.setIgnoreEnd(true);
            workspace.getEndingFriday().setValue(new java.sql.Date(newDate.getTime()).toLocalDate());
            data.setEndingFriday(newDate);
        }
    }
    
    @Override
    public void undoTransaction(){
        Data data = (Data) app.getDataComponent();
        if(startEnd.equals("start")){
            controller.setIgnoreStart(false);
            workspace.getStartingMonday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
            data.setStartingMonday(oldDate);
        }
        else if(startEnd.equals("end")){
            controller.setIgnoreEnd(false);
            workspace.getEndingFriday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
            data.setEndingFriday(oldDate);
        }
    }
}
