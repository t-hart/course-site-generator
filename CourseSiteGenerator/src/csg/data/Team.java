package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.paint.Color;

/**
 *
 * @author tjhha
 */
public class Team<E extends Comparable<E>> implements Comparable<E> {
    private final StringProperty name;
    private final StringProperty color;
    private final StringProperty textColor;
    private final StringProperty link;
    
    public Team(String initName, String initColor, String initTextColor, String initLink){
        name = new SimpleStringProperty(initName);
        color = new SimpleStringProperty(initColor.toLowerCase());
        textColor = new SimpleStringProperty(initTextColor.toLowerCase());
        link = new SimpleStringProperty(initLink);
    }

    public String getName() {
        return name.get();
    }

    public String getColor() {
        return color.get();
    }

    public String getTextColor() {
        return textColor.get();
    }

    public String getLink() {
        return link.get();
    }
    
    @Override
    public String toString(){
        return name.get();
    }
    
    @Override
    public int compareTo(E otherTeam) {
        return getName().compareTo(((Team)otherTeam).getName());
    }
    
}
