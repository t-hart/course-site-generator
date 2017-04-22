package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author tjhha
 */
public class Recitation {

    private final StringProperty section;
    private final StringProperty instructor;
    private final StringProperty dayTime;
    private final StringProperty location;
    private final TeachingAssistant supervisingTA_1;
    private final TeachingAssistant supervisingTA_2;

    public Recitation(String initSection, String initInstructor, String initDayTime, String initLocation, TeachingAssistant initSupervisingTA_1, TeachingAssistant initSupervisingTA_2) {
        section = new SimpleStringProperty(initSection);
        instructor = new SimpleStringProperty(initInstructor);
        dayTime = new SimpleStringProperty(initDayTime);
        location = new SimpleStringProperty(initLocation);
        supervisingTA_1 = initSupervisingTA_1;
        supervisingTA_2 = initSupervisingTA_2;
    }

    public StringProperty getSection() {
        return section;
    }

    public StringProperty getInstructor() {
        return instructor;
    }

    public StringProperty getDayTime() {
        return dayTime;
    }

    public StringProperty getLocation() {
        return location;
    }

    public TeachingAssistant getSupervisingTA_1() {
        return supervisingTA_1;
    }

    public TeachingAssistant getSupervisingTA_2() {
        return supervisingTA_2;
    }
}
