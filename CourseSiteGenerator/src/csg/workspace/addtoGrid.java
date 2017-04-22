/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import jtps.jTPS_Transaction;
import csg.data.Data;

/**
 *
 * @author moham_000
 */
public class addtoGrid implements jTPS_Transaction {
String cellKey="";
String taName="";
Data data;
    public addtoGrid(String cellkey,String taName, Data data){
        this.cellKey=cellkey;
        this.taName=taName;
        this.data=data;
    }
    @Override
    public void doTransaction() {
         data.toggleTAOfficeHours(cellKey, taName);
    }

    @Override
    public void undoTransaction() {
        data.toggleTAOfficeHours(cellKey, taName);
    }
    
}
