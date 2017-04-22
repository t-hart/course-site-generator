package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.Date;

/**
 *
 * @author tjhha
 */
public class ScheduledItem {
    private final StringProperty type;
    private final Date date;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty link;
    private final StringProperty criteria;
   
    public ScheduledItem(String initType, Date initDate, String initTitle, String initTopic, String initLink, String initCriteria) {
        type = new SimpleStringProperty(initType);
        date = initDate;
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);
        criteria = new SimpleStringProperty(initCriteria);
    }

    public StringProperty getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public StringProperty getTitle() {
        return title;
    }

    public StringProperty getTopic() {
        return topic;
    }

    public StringProperty getLink() {
        return link;
    }

    public StringProperty getCriteria() {
        return criteria;
    }
    
    
}
