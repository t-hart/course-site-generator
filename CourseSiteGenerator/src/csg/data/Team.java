package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author tjhha
 */
public class Team {
    private final StringProperty name;
    private final Color color;
    private final Color textColor;
    private final StringProperty link;
    
    public Team(String initName, Color initColor, Color initTextColor, String initLink){
        name = new SimpleStringProperty(initName);
        color = initColor;
        textColor = initTextColor;
        link = new SimpleStringProperty(initLink);
    }

    public StringProperty getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Color getTextColor() {
        return textColor;
    }

    public StringProperty getLink() {
        return link;
    }
    
    
}
