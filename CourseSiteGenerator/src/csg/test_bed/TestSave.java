package csg.test_bed;
import csg.CourseSiteGeneratorApp;
import static djf.settings.AppStartupConstants.*;
import csg.data.Data;
import csg.data.TeachingAssistant;
import csg.data.Recitation;
import csg.file.TAFiles;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import csg.data.SitePage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Date;
import csg.data.ScheduledItem;
import javafx.scene.paint.Color;
import csg.data.Team;

/**
 *
 * @author tjhha
 */

/*************************** NOTE *********************************/
/* LINES 140-161 in AppTemplate.java must be commented out first! */
/******************************************************************/

public class TestSave {
    
    public static void main(String[] args){
        System.out.println("TestSave started!");

        CourseSiteGeneratorApp app = new CourseSiteGeneratorApp();
        app.loadProperties(APP_PROPERTIES_FILE_NAME);
        Data data = new Data(app);
        TAFiles file = new TAFiles(app);
        initTimes(data);
        
        /* COURSE INFO */
        data.setSubject("CME");
        data.setNumber("101");
        data.setSemester("Fall");
        data.setYear("2017");
        data.setTitle("Chemical Enigneering Fundamentals");
        data.setInstructorName("Rickey Bobby");
        data.setInstructorHome("http://www.google.com");
        data.setExportDir("C:\\wamp64\\www\\CSE380\\CSE219");
        
        /* SITE TEMPLATE */
        data.setSiteTemplateDir("C:\\Users\\rmckenna\\Documents\\Courses\\Templates\\CSE219");
        SitePage sp1 = new SitePage(true, "Home", "index.html", "HomeBuilder.js");
        SitePage sp2 = new SitePage(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
        SitePage sp3 = new SitePage(true, "Schedule", "schedule.html", "ScheduleBuilder.js");
        SitePage sp4 = new SitePage(true, "HWs", "hws.html", "HWsBuilder.js");
        SitePage sp5 = new SitePage(false, "Projects", "projects.html", "ProjectsBuilder.js");
        data.getSitePages().addAll(sp1, sp2, sp3, sp4, sp5);
        
        /* PAGE STYLE */
        data.setBannerSchoolImageDir("C:\\Users\\tjhha\\Desktop\\Applications\\Spring2017\\CSE219\\CourseSiteGenerator\\CourseSiteGenerator\\images\\SBUDarkRedShieldLogo.png");
        data.setLeftFooterImageDir("C:\\Users\\tjhha\\Desktop\\Applications\\Spring2017\\CSE219\\CourseSiteGenerator\\CourseSiteGenerator\\images\\SBUWhiteShieldLogo.jpg");
        data.setRightFooterImageDir("C:\\Users\\tjhha\\Desktop\\cat.png");
        data.setStylesheetDir("sea_cat.css");
        
        /* TAS */
        data.addTA("Timothy Hart", "tjh.hart@gmail.com", true);
        data.addTA("Hikari Oshiro", "hoshiro@stonybrookusg.org", false);
       
        /* ADD TAS TO OFFICE HOURS GRID */
        for (int row = 1; row < data.getNumRows(); row++) {
            for (int col = 2; col < 7; col++) {
                data.setCellProperty(col, row, new SimpleStringProperty(""));
            }
        }
        data.setCellProperty(6, 6, new SimpleStringProperty("Timothy Hart"));
        data.setCellProperty(3, 9, new SimpleStringProperty("Hikari Oshiro"));
        data.setEndHour(6);
        
        /* RECITATIONS */
        data.addRecitation("R01", "McKenna", "Wed 3:30-4:23pm", "Old CS 2114", "Timothy Hart", "Hikari Oshiro");
        data.addRecitation("R02", "McKenna", "Fri 1:30-2:23pm", "Old CS 2114", "Timothy Hart", "Hikari Oshiro");
        
        /* SCHEDULE */
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try{
            data.setStartingMonday(format.parse("May 29, 2017"));
            data.setEndingFriday(format.parse("August 25, 2017"));
            
            data.addScheduledItem("Holiday", format.parse("July 4, 2017"), "Independence Day", "", "", "");
            data.addScheduledItem("Lecture", format.parse("July 12, 2017"), "Lecture 3", "Event Programming", "https://eventprogramming.com/", "");
            data.addScheduledItem("Holiday", format.parse("August 1, 2017"), "NO LECTURE", "", "", "");
            data.addScheduledItem("Homework", format.parse("August 8, 2017"), "HW3", "UML", "https://umlhelp.org/", "");
        }catch(java.text.ParseException pe){
            pe.printStackTrace();
        }
        
        /* TEAMS */
        data.addTeam("Atomic Comics", "552211", "FFFFFF", "http://atomicomic.com");
        data.addTeam("C4 Comics", "235399", "FFFFFF", "https://c4-comics.appspot.com");
        
        /* STUDENTS */
        data.addStudent("Beau", "Brummell", "Atomic Comics", "Lead Designer");
        data.addStudent("Jane", "Doe", "C4 Comics", "Lead Programmer");
        data.addStudent("Nooian", "Soong", "Atomic Comics", "Data Designer");
        
        /* SAVE DATA */
        try{
            file.saveData(data, "C:\\Users\\tjhha\\Desktop\\SiteSaveTest.json");
            System.out.println("TestSave completed!");
        }catch(Exception e){
            System.out.println("TestSave failed!"); 
        }
    }
    
    public static void initTimes(Data data){
        data.setCellProperty(0, 1, new SimpleStringProperty("12:00am"));
        data.setCellProperty(0, 2, new SimpleStringProperty("12:30am"));
        data.setCellProperty(0, 3, new SimpleStringProperty("1:00am"));
        data.setCellProperty(0, 4, new SimpleStringProperty("1:30am"));
        data.setCellProperty(0, 5, new SimpleStringProperty("2:00am"));
        data.setCellProperty(0, 6, new SimpleStringProperty("2:30am"));
        data.setCellProperty(0, 7, new SimpleStringProperty("3:00am"));
        data.setCellProperty(0, 8, new SimpleStringProperty("3:30am"));
        data.setCellProperty(0, 9, new SimpleStringProperty("4:00am"));
        data.setCellProperty(0, 10, new SimpleStringProperty("4:30am"));
        data.setCellProperty(0, 11, new SimpleStringProperty("5:00am"));
        data.setCellProperty(0, 12, new SimpleStringProperty("5:30am"));
        data.setCellProperty(0, 13, new SimpleStringProperty("6:00am"));
        data.setCellProperty(0, 14, new SimpleStringProperty("6:30am"));
        data.setCellProperty(0, 15, new SimpleStringProperty("7:00am"));
        data.setCellProperty(0, 16, new SimpleStringProperty("7:30am"));
        data.setCellProperty(0, 17, new SimpleStringProperty("8:00am"));
        data.setCellProperty(0, 18, new SimpleStringProperty("8:30am"));
        data.setCellProperty(0, 19, new SimpleStringProperty("9:00am"));
        data.setCellProperty(0, 20, new SimpleStringProperty("9:30am"));
        data.setCellProperty(0, 21, new SimpleStringProperty("10:00am"));
        data.setCellProperty(0, 22, new SimpleStringProperty("10:30am"));
        data.setCellProperty(0, 23, new SimpleStringProperty("11:00am"));
        data.setCellProperty(0, 24, new SimpleStringProperty("11:30am"));
        data.setCellProperty(0, 25, new SimpleStringProperty("12:00pm"));
        data.setCellProperty(0, 26, new SimpleStringProperty("12:30pm"));
        data.setCellProperty(0, 27, new SimpleStringProperty("1:00pm"));
        data.setCellProperty(0, 28, new SimpleStringProperty("1:30pm"));
        data.setCellProperty(0, 29, new SimpleStringProperty("2:00pm"));
        data.setCellProperty(0, 30, new SimpleStringProperty("2:30pm"));
        data.setCellProperty(0, 31, new SimpleStringProperty("3:00pm"));
        data.setCellProperty(0, 32, new SimpleStringProperty("3:30pm"));
        data.setCellProperty(0, 33, new SimpleStringProperty("4:00pm"));
        data.setCellProperty(0, 34, new SimpleStringProperty("4:30pm"));
        data.setCellProperty(0, 35, new SimpleStringProperty("5:00pm"));
        data.setCellProperty(0, 36, new SimpleStringProperty("5:30pm"));
        data.setCellProperty(0, 37, new SimpleStringProperty("6:00pm"));
        data.setCellProperty(0, 38, new SimpleStringProperty("6:30pm"));
        data.setCellProperty(0, 39, new SimpleStringProperty("7:00pm"));
        data.setCellProperty(0, 40, new SimpleStringProperty("7:30pm"));
        data.setCellProperty(0, 41, new SimpleStringProperty("8:00pm"));
        data.setCellProperty(0, 42, new SimpleStringProperty("8:30pm"));
        data.setCellProperty(0, 43, new SimpleStringProperty("9:00pm"));
        data.setCellProperty(0, 44, new SimpleStringProperty("9:30pm"));
        data.setCellProperty(0, 45, new SimpleStringProperty("10:00pm"));
        data.setCellProperty(0, 46, new SimpleStringProperty("10:30pm"));
        data.setCellProperty(0, 47, new SimpleStringProperty("11:00pm"));
        data.setCellProperty(0, 48, new SimpleStringProperty("11:30pm"));
    }

}
