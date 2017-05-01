package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.data.Data;
import csg.data.Team;
import java.util.Collections;
import jtps.jTPS_Transaction;
import javafx.collections.ObservableList;
import csg.data.Student;
import java.util.ArrayList;
/**
 *
 * @author tjhha
 */
public class addEditStudent_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TAController controller;
    TAWorkspace workspace;
    
    String firstName;
    String lastName;
    Team team;
    String role;
    Student student;

    addEditStudent_Transaction(CourseSiteGeneratorApp app, TAController controller, TAWorkspace workspace, String firstName, String lastName, Object selectedTeam, String role, Student student) {
        this.app = app;
        this.controller = controller;
        this.workspace = workspace;
        
        this.firstName = firstName;
        this.lastName = lastName;
        
        if(selectedTeam != null){
            team = (Team)selectedTeam;
        }
        
        this.role = role;
        this.student = student;
    }

    @Override
    public void doTransaction() {
        Data data = (Data) app.getDataComponent();
        ObservableList<Student> students = data.getStudents();
        
        if(student != null){
            for(int i = 0; i < students.size(); i++){
                if(students.get(i).getFirstName().equals(student.getFirstName()) && students.get(i).getLastName().equals(student.getLastName())){
                    data.removeStudent(student.getFirstName(), student.getLastName());
                    break;
                }
            }
            data.addStudent(firstName, lastName, team.getName(), role);
        }
        
        else{
            data.addStudent(firstName, lastName, team.getName(), role);
        }
        
        Collections.sort(data.getStudents());
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.removeStudent(firstName, lastName);
        if(student != null){
            data.addStudent(student.getFirstName(), student.getLastName(), student.getTeam(), student.getRole());
        }
        
        Collections.sort(data.getStudents());
        controller.markWorkAsEdited();
    }
}
