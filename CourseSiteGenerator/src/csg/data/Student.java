package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tjhha
 */
public class Student {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty team;
    private final StringProperty role;
    
    public Student(String initFirstName, String initLastName, String initTeam, String initRole){
        firstName = new SimpleStringProperty(initFirstName);
        lastName = new SimpleStringProperty(initLastName);
        team = new SimpleStringProperty(initTeam);
        role = new SimpleStringProperty(initRole);
    }

    public StringProperty getFirstName() {
        return firstName;
    }

    public StringProperty getLastName() {
        return lastName;
    }

    public StringProperty getTeam() {
        return team;
    }

    public StringProperty getRole() {
        return role;
    }
    
    
}
