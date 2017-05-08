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
import csg.data.Recitation;

/**
 *
 * @author tjhhar
 */
public class deleteRecitation_Transaction implements jTPS_Transaction {

    CourseSiteGeneratorApp app;
    TAController controller;
    Object selectedItem;
    TAWorkspace workspace;
    
    String section;
    String instructor;
    String dayTime;
    String location;
    String ta1;
    String ta2;

    deleteRecitation_Transaction(CourseSiteGeneratorApp app, TAController controller, Object selectedItem, TAWorkspace workspace) {
        this.app = app;
        this.controller = controller;
        this.selectedItem = selectedItem;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        Recitation rec = (Recitation)selectedItem;
        this.section = rec.getSection();
        this.instructor = rec.getInstructor();
        this.dayTime = rec.getDayTime();
        this.location = rec.getLocation();
        this.ta1 = rec.getSupervisingTA_1();
        this.ta2 = rec.getSupervisingTA_2();
        
        Data data = (Data) app.getDataComponent();
        data.removeRecitation(section);
        workspace.getRecitationTable().getSelectionModel().clearSelection();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        workspace.getAddUpdateRecitationButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
        Collections.sort(data.getRecitations());
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.addRecitation(section, instructor, dayTime, location, ta1, ta2);
        
        Collections.sort(data.getRecitations());
    }

}
