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
import csg.CourseSiteGeneratorProp;
import csg.data.Data;
import csg.data.Team;
import csg.data.Student;
import java.util.Collections;
import javafx.collections.ObservableList;
import java.util.Date;
import csg.data.ScheduledItem;
import properties_manager.PropertiesManager;

/**
 *
 * @author tjhhar
 */
public class deleteScheduledItem_Transaction implements jTPS_Transaction {

    CourseSiteGeneratorApp app;
    TAController controller;
    Object selectedItem;
    TAWorkspace workspace;
    
    String type;
    Date date;
    String time;
    String title;
    String topic;
    String link;
    String criteria;

    deleteScheduledItem_Transaction(CourseSiteGeneratorApp app, TAController controller, Object selectedItem, TAWorkspace workspace) {
        this.app = app;
        this.controller = controller;
        this.selectedItem = selectedItem;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        ScheduledItem item = (ScheduledItem)selectedItem;
        this.type = item.getType();
        this.date = item.getDate();
        this.time = item.getTime();
        this.title = item.getTitle();
        this.topic = item.getTopic();
        this.link = item.getLink();
        this.criteria = item.getCriteria();
        
        Data data = (Data) app.getDataComponent();
        data.removeScheduledItem(date, topic);
        workspace.getScheduleItemsTable().getSelectionModel().clearSelection();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        workspace.getAddUpdateScheduleItemButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
        Collections.sort(data.getScheduledItems());
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.addScheduledItem(type, date, time, title, topic, link, criteria);
        
        Collections.sort(data.getScheduledItems());
    }

}
