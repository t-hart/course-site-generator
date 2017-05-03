/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

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
    
    changeStartingEndingDate_Transaction(TAController controller, TAWorkspace workspace, Date oldDate, Date newDate, String startEnd){
        this.workspace = workspace;
        this.oldDate = oldDate;
        this.newDate = newDate;
        this.startEnd = startEnd;
        this.controller = controller;
    }
    
    @Override
    public void doTransaction(){
        if(startEnd.equals("start")){
            controller.setIgnoreStart(true);
            workspace.getStartingMonday().setValue(new java.sql.Date(newDate.getTime()).toLocalDate());
        }
        else if(startEnd.equals("end")){
            controller.setIgnoreEnd(true);
            workspace.getEndingFriday().setValue(new java.sql.Date(newDate.getTime()).toLocalDate());
        }
    }
    
    @Override
    public void undoTransaction(){
        if(startEnd.equals("start")){
            controller.setIgnoreStart(true);
            workspace.getStartingMonday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
        }
        else if(startEnd.equals("end")){
            controller.setIgnoreEnd(true);
            workspace.getEndingFriday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
        }
    }
}
