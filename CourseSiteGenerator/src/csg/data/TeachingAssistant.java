package csg.data;

import csg.workspace.TAWorkspace;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import csg.CourseSiteGeneratorApp;
import csg.workspace.TAController;
import csg.workspace.change;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class TeachingAssistant<E extends Comparable<E>> implements Comparable<E>  {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email;
    private final BooleanProperty undergrad;

    /**
     * Constructor initializes both the TA name and email.
     */
    public TeachingAssistant(String initName, String initEmail, boolean ug, CourseSiteGeneratorApp app) {
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
        undergrad = new SimpleBooleanProperty(ug);
        undergrad.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasSelected, Boolean isSelected) {
                TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
                TAController controller = workspace.getController();
                Data data = (Data)app.getDataComponent();
                app.getGUI().getFileController().markAsEdited(app.getGUI());
                controller.getJ().addTransaction(new change(name.get(), email.get(), name.get(), email.get(), data, !isSelected, isSelected));
            }
        });
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES
    
    public BooleanProperty getUndergrad(){
        return undergrad;
    }
    
    public void setUndergrad(Boolean initUndergrad){
        undergrad.set(initUndergrad);
    }
    
    public String getName() {
        return name.get();
    }

    public void setName(String initName) {
        name.set(initName);
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String initEmail) {
        email.set(initEmail);
    }

    @Override
    public int compareTo(E otherTA) {
        return getName().compareTo(((TeachingAssistant)otherTA).getName());
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
}