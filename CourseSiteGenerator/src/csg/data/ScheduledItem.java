package csg.data;

import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *
 * @author tjhha
 */
public class ScheduledItem {
    private final StringProperty type;
    private final Date date;
    private final StringProperty time;
    private final StringProperty title;
    private final StringProperty topic;
    private final StringProperty link;
    private final StringProperty criteria;
   
    public ScheduledItem(String initType, Date initDate, String initTime, String initTitle, String initTopic, String initLink, String initCriteria) {
        type = new SimpleStringProperty(initType);
        date = initDate;
        time = new SimpleStringProperty(initTime);
        title = new SimpleStringProperty(initTitle);
        topic = new SimpleStringProperty(initTopic);
        link = new SimpleStringProperty(initLink);
        criteria = new SimpleStringProperty(initCriteria);
    }

    public String getType() {
        return type.get();
    }

    public Date getDate() {
        return date;
    }
    
    public String getDateString(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }

    public String getTitle() {
        return title.get();
    }

    public String getTopic() {
        return topic.get();
    }

    public String getLink() {
        return link.get();
    }

    public String getCriteria() {
        return criteria.get();
    }
    
    public String getTime(){
        return time.get();
    }
    
    public int getYear(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        String[] dateStringArray = dateString.split("/");
        return Integer.parseInt(dateStringArray[2]);
    }
    
    public int getMonth(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        String[] dateStringArray = dateString.split("/");
        return Integer.parseInt(dateStringArray[0]);        
    }
    
    public int getDay(){
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String dateString = sdf.format(date);
        String[] dateStringArray = dateString.split("/");
        return Integer.parseInt(dateStringArray[1]);        
    }
    
}
