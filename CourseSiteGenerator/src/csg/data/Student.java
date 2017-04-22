package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import csg.data.Team;

/**
 *
 * @author tjhha
 */
public class Student {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final Team team;
    private final StringProperty role;
    
    public Student(String initFirstName, String initLastName, Team initTeam, String initRole){
        firstName = new SimpleStringProperty(initFirstName);
        lastName = new SimpleStringProperty(initLastName);
        team = initTeam;
        role = new SimpleStringProperty(initRole);
    }

    public StringProperty getFirstName() {
        return firstName;
    }

    public StringProperty getLastName() {
        return lastName;
    }

    public Team getTeam() {
        return team;
    }

    public StringProperty getRole() {
        return role;
    }
    
    
}
