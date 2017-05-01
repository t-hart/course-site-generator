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
public class deleteTeam_Transaction implements jTPS_Transaction {

    CourseSiteGeneratorApp app;
    TAController controller;
    Object selectedItem;
    TAWorkspace workspace;
    String name;
    String color;
    String textColor;
    String link;
    ArrayList<Student> studentChangeList = new ArrayList<>();

    deleteTeam_Transaction(CourseSiteGeneratorApp app, TAController controller, Object selectedItem, TAWorkspace workspace) {
        this.app = app;
        this.controller = controller;
        this.selectedItem = selectedItem;
        this.workspace = workspace;
    }

    @Override
    public void doTransaction() {
        // GET THE TA AND REMOVE IT
        Team team = (Team) selectedItem;
        String teamName = team.getName();
        this.name = team.getName();
        this.color = team.getColor();
        this.textColor = team.getTextColor();
        this.link = team.getLink();
        Data data = (Data) app.getDataComponent();
        
        ObservableList<Student> students = data.getStudents();
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getTeam().equals(team.getName())) {
                Student student = students.get(i);
                studentChangeList.add(student);
                Student newStudent = new Student(student.getFirstName(), student.getLastName(), "", "");
                students.remove(student);
                students.add(i, newStudent);
            }
        }
        
        data.removeTeam(teamName);
        Collections.sort(students);
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.addTeam(name, color, textColor, link);
        Collections.sort(data.getTeams());
        
        ObservableList<Student> students = data.getStudents();
        System.out.println(students.size());
        for (int i = 0; i < students.size(); i++) {
            for(int j = 0; j < studentChangeList.size(); j++){
                if(students.get(i).getFirstName().equals(studentChangeList.get(j).getFirstName()) && students.get(i).getLastName().equals(studentChangeList.get(j).getLastName())){
                    data.removeStudent(students.get(i).getFirstName(), students.get(i).getLastName());
                    data.addStudent(studentChangeList.get(j).getFirstName(), studentChangeList.get(j).getLastName(), studentChangeList.get(j).getTeam(), studentChangeList.get(j).getRole());
                }
            }
        }
        
        studentChangeList.clear();
        Collections.sort(students);
        controller.markWorkAsEdited();
    }

}
