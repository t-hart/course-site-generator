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
import csg.data.Team;
import csg.data.Student;
import java.util.Collections;
import javafx.collections.ObservableList;

/**
 *
 * @author moham_000
 */
public class deleteStudent_Transaction implements jTPS_Transaction {

    CourseSiteGeneratorApp app;
    TAController controller;
    Object selectedItem;
    TAWorkspace workspace;
    String firstName;
    String lastName;
    String team;
    String role;

    deleteStudent_Transaction(CourseSiteGeneratorApp app, TAController controller, Object selectedItem, TAWorkspace workspace) {
        this.app = app;
        this.controller = controller;
        this.selectedItem = selectedItem;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        // GET THE TA AND REMOVE IT
        Student student = (Student) selectedItem;
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.team = student.getTeam();
        this.role = student.getRole();
        
        Data data = (Data) app.getDataComponent();
        data.removeStudent(firstName, lastName);
        Collections.sort(data.getStudents());
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.addStudent(firstName, lastName, team, role);
        
        Collections.sort(data.getStudents());
        controller.markWorkAsEdited();
    }

}
