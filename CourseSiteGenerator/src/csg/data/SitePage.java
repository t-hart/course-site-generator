package csg.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
/**
 *
 * @author tjhha
 */
public class SitePage {
    private final BooleanProperty use;
    private final StringProperty navbarTitle;
    private final StringProperty fileName;
    private final StringProperty script;
    
    public SitePage(boolean use, String navbarTitle, String fileName, String script){
        this.use = new SimpleBooleanProperty(use);
        this.navbarTitle = new SimpleStringProperty(navbarTitle);
        this.fileName = new SimpleStringProperty(fileName);
        this.script = new SimpleStringProperty(script);
    }

    public BooleanProperty isUse() {
        return use;
    }

    public void setUse(Boolean use) {
        this.use.set(use);
    }

    public String getNavbarTitle() {
        return navbarTitle.get();
    }

    public void setNavbarTitle(String navbarTitle) {
        this.navbarTitle.set(navbarTitle);
    }

    public String getFileName() {
        return fileName.get();
    }

    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    public String getScript() {
        return script.get();
    }

    public void setScript(String script) {
        this.script.set(script);
    }
    
    
}
