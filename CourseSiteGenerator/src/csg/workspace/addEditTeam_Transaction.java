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
public class addEditTeam_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TAController controller;
    TAWorkspace workspace;
    
    String name;
    String color;
    String textColor;
    String link;
    
    Team team;
    
    ArrayList<Student> affectedStudents = new ArrayList<>();

    addEditTeam_Transaction(CourseSiteGeneratorApp app, TAController controller, TAWorkspace workspace, String name, String color, String textColor, String link, Team team) {
        this.app = app;
        this.controller = controller;
        this.workspace = workspace;
        
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.link = link;
        
        this.team = team;
    }

    @Override
    public void doTransaction() {
        Data data = (Data) app.getDataComponent();
        ObservableList<Team> teams = data.getTeams();
        ObservableList<Student> students = data.getStudents();
        
        if(team != null){
            for(int i = 0; i < students.size(); i++){   
                if(students.get(i).getTeam().equals(team.getName())){
                    affectedStudents.add(students.get(i));
                }
            }
            for(int i = 0; i < teams.size(); i++){
                if(teams.get(i).getName().equals(team.getName())){
                    data.removeTeam(team.getName());
                    break;
                }
            }
            data.addTeam(name, color, textColor, link);
            for(int i = 0; i < affectedStudents.size(); i++){
                String stuFirstName = affectedStudents.get(i).getFirstName();
                String stuLastName = affectedStudents.get(i).getLastName();
                data.removeStudent(stuFirstName, stuLastName);
                data.addStudent(stuFirstName, stuLastName, name, affectedStudents.get(i).getRole());
                Collections.sort(data.getStudents());
            }
        }
        
        else{
            data.addTeam(name, color, textColor, link);
        }
        
        Collections.sort(data.getTeams());
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.removeTeam(name);
        if(team != null){
            for(int i = 0; i < affectedStudents.size(); i++){
                data.removeStudent(affectedStudents.get(i).getFirstName(), affectedStudents.get(i).getLastName());
                data.addStudent(affectedStudents.get(i).getFirstName(), affectedStudents.get(i).getLastName(), affectedStudents.get(i).getTeam(), affectedStudents.get(i).getRole());
                Collections.sort(data.getStudents());
            }
            data.addTeam(team.getName(), team.getColor(), team.getTextColor(), team.getLink());
        }
        
        affectedStudents.clear();
        Collections.sort(data.getTeams());
        controller.markWorkAsEdited();
    }
}
