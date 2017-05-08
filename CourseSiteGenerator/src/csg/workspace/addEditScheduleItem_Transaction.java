package csg.workspace;

import csg.CourseSiteGeneratorApp;
import csg.data.Data;
import csg.data.Team;
import java.util.Collections;
import jtps.jTPS_Transaction;
import javafx.collections.ObservableList;
import csg.data.ScheduledItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.scene.control.DatePicker;
/**
 *
 * @author tjhha
 */
public class addEditScheduleItem_Transaction implements jTPS_Transaction {
    CourseSiteGeneratorApp app;
    TAController controller;
    TAWorkspace workspace;
    
    String type;
    String month;
    String day;
    String year;
    String time;
    String title;
    String topic;
    String link;
    String criteria;
    
    ScheduledItem item;
    
    Date itemDate;

    addEditScheduleItem_Transaction(CourseSiteGeneratorApp app, TAController controller, TAWorkspace workspace, String type, String month, String day, String year, String time, String title, String topic, String link, String criteria, ScheduledItem selectedItem) {
        this.app = app;
        this.controller = controller;
        this.workspace = workspace;
        
        this.type = type;
        this.month = month;
        this.day = day;
        this.year = year;
        this.time = time;
        this.title = title;
        this.topic = topic;
        this.link = link;
        this.criteria = criteria;
        
        if(selectedItem != null){
            item = (ScheduledItem)selectedItem;
        }
    }

    @Override
    public void doTransaction() {
        Data data = (Data) app.getDataComponent();
        ObservableList<ScheduledItem> items = data.getScheduledItems();
        String date = month+"/"+day+"/"+year;
        this.itemDate = new Date();
        
        try {
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            itemDate = df.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(itemDate);
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
            
        if(item != null){
            data.removeScheduledItem(item.getDate(), item.getTopic());
         
            
            data.addScheduledItem(type, itemDate, time, title, topic, link, criteria);
        }
        
        else{
            data.addScheduledItem(type, itemDate, time, title, topic, link, criteria);
        }
        
        Collections.sort(data.getScheduledItems());
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        Data data = (Data) app.getDataComponent();
        data.removeScheduledItem(itemDate, topic);
        if(item != null){
            data.addScheduledItem(item.getType(), item.getDate(), item.getTime(), item.getTitle(), item.getTopic(), item.getLink(), item.getCriteria());
        }
        
        Collections.sort(data.getScheduledItems());
        controller.markWorkAsEdited();
    }
}
