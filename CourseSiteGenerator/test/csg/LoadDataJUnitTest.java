package csg;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import csg.data.Data;
import csg.data.SitePage;;
import static djf.settings.AppStartupConstants.APP_PROPERTIES_FILE_NAME;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleStringProperty;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import csg.data.TeachingAssistant;
import csg.file.TimeSlot;
import csg.data.Recitation;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;
import csg.data.ScheduledItem;
import java.util.ArrayList;
import csg.data.Team;
import csg.data.Student;
import javafx.scene.paint.Color;

/**
 *
 * @author tjhha
 */
public class LoadDataJUnitTest {

    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_NAME = "name";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_EMAIL = "email";
    static final String JSON_UG = "ug";
    static final String JSON_REC = "recitaions";
    static final String JSON_REC_SECTION = "rec_section";
    static final String JSON_REC_INSTRUCTOR = "rec_instructor";
    static final String JSON_REC_DAYTIME = "rec_daytime";
    static final String JSON_REC_LOCATION = "rec_location";
    static final String JSON_REC_SUPERVISINGTA_1 = "supervisingTA_1";
    static final String JSON_REC_SUPERVISINGTA_2 = "supervisingTA_2";
    static final String JSON_COURSE_SUBJECT = "subject";
    static final String JSON_COURSE_NUMBER = "number";
    static final String JSON_COURSE_SEMESTER = "semester";
    static final String JSON_COURSE_YEAR = "year";
    static final String JSON_COURSE_TITLE = "title";
    static final String JSON_COURSE_INSTRUCTOR_NAME = "instructor_name";
    static final String JSON_COURSE_INSTRUCTOR_HOME = "instructor_home";
    static final String JSON_COURSE_EXPORT_DIR = "course_export_dir";
    static final String JSON_SITETEMPLATE_DIR = "site_template_dir";
    static final String JSON_SITEPAGES = "site_pages";
    static final String JSON_SITEPAGE_USE = "use";
    static final String JSON_SITEPAGE_NAVBARTITLE = "navbar_title";
    static final String JSON_SITEPAGE_FILENAME = "file_name";
    static final String JSON_SITEPAGE_SCRIPT = "script";
    static final String JSON_PAGESTYLE_BANNERSCHOOL_DIR = "banner_school_image_dir";
    static final String JSON_PAGESTYLE_LEFTFOOTER_DIR = "left_footer_image_dir";
    static final String JSON_PAGESTYLE_RIGHTFOOTER_DIR = "right_footer_image_dir";
    static final String JSON_PAGESTYLE_STYLESHEET_DIR = "stylesheet_dir";
    static final String JSON_SCHEDULE_STARTINGMONDAY = "starting_monday";
    static final String JSON_SCHEDULE_ENDINGFRIDAY = "ending_friday";
    static final String JSON_SCHEDULEDITEMS = "scheduled_items";
    static final String JSON_SCHEDULEDITEM_TYPE = "type";
    static final String JSON_SCHEDULEDITEM_DATE = "date";
    static final String JSON_SCHEDULEDITEM_TITLE = "title";
    static final String JSON_SCHEDULEDITEM_TOPIC = "topic";
    static final String JSON_SCHEDULEDITEM_LINK = "link";
    static final String JSON_SCHEDULEDITEM_CRITERIA = "criteria";
    static final String JSON_TEAMS = "teams";
    static final String JSON_TEAM_NAME = "name";
    static final String JSON_TEAM_COLOR = "color";
    static final String JSON_TEAM_TEXTCOLOR = "text_color";
    static final String JSON_TEAM_LINK = "link";
    static final String JSON_STUDENTS = "students";
    static final String JSON_STUDENT_FIRSTNAME = "first_name";
    static final String JSON_STUDENT_LASTNAME = "last_name";
    static final String JSON_STUDENT_TEAM = "team";
    static final String JSON_STUDENT_ROLE = "role";
    
    CourseSiteGeneratorApp app;
    Data dataManager;
    boolean init = false;
    
    @Before
    public void LoadDataJUnitTest() {
        try {

            app = new CourseSiteGeneratorApp();
            app.loadProperties(APP_PROPERTIES_FILE_NAME);
            app.setLangChoice("eng");
            
            // CLEAR THE OLD DATA OUT
            dataManager = new Data(app);
          

            // LOAD THE JSON FILE WITH ALL THE DATA
            JsonObject json = loadJSONFile("C:\\Users\\tjhha\\Desktop\\SiteSaveTest.json");

            String subject = json.getString(JSON_COURSE_SUBJECT);
            String number = json.getString(JSON_COURSE_NUMBER);
            String semester = json.getString(JSON_COURSE_SEMESTER);
            String year = json.getString(JSON_COURSE_YEAR);
            String title = json.getString(JSON_COURSE_TITLE);
            String instructorName = json.getString(JSON_COURSE_INSTRUCTOR_NAME);
            String instructorHome = json.getString(JSON_COURSE_INSTRUCTOR_HOME);
            String courseExportDir = json.getString(JSON_COURSE_EXPORT_DIR);

            dataManager.setSubject(subject);
            dataManager.setNumber(number);
            dataManager.setSemester(semester);
            dataManager.setYear(year);
            dataManager.setTitle(title);
            dataManager.setInstructorName(instructorName);
            dataManager.setInstructorHome(instructorHome);
            dataManager.setExportDir(courseExportDir);

            String siteTemplateDir = json.getString(JSON_SITETEMPLATE_DIR);
            dataManager.setSiteTemplateDir(siteTemplateDir);
            JsonArray jsonSitePagesArray = json.getJsonArray(JSON_SITEPAGES);
            for (int i = 0; i < jsonSitePagesArray.size(); i++) {
                JsonObject jsonSitePage = jsonSitePagesArray.getJsonObject(i);
                boolean use = Boolean.parseBoolean(jsonSitePage.getString(JSON_SITEPAGE_USE));
                String navbarTitle = jsonSitePage.getString(JSON_SITEPAGE_NAVBARTITLE);
                String fileName = jsonSitePage.getString(JSON_SITEPAGE_FILENAME);
                String script = jsonSitePage.getString(JSON_SITEPAGE_SCRIPT);
                dataManager.addSitePage(use, navbarTitle, fileName, script);
            }

            String bannerSchoolImageDir = json.getString(JSON_PAGESTYLE_BANNERSCHOOL_DIR);
            String leftFooterImageDir = json.getString(JSON_PAGESTYLE_LEFTFOOTER_DIR);
            String rightFooterImageDir = json.getString(JSON_PAGESTYLE_RIGHTFOOTER_DIR);
            String stylesheetDir = json.getString(JSON_PAGESTYLE_STYLESHEET_DIR);

            dataManager.setBannerSchoolImageDir(bannerSchoolImageDir);
            dataManager.setLeftFooterImageDir(leftFooterImageDir);
            dataManager.setRightFooterImageDir(rightFooterImageDir);
            dataManager.setStylesheetDir(stylesheetDir);

            // NOW LOAD ALL THE UNDERGRAD TAs
            JsonArray jsonTAArray = json.getJsonArray(JSON_UNDERGRAD_TAS);
            for (int i = 0; i < jsonTAArray.size(); i++) {
                JsonObject jsonTA = jsonTAArray.getJsonObject(i);
                String name = jsonTA.getString(JSON_NAME);
                String email = jsonTA.getString(JSON_EMAIL);
                String ug = jsonTA.getString(JSON_UG);
                dataManager.addTA(name, email, Boolean.valueOf(ug));
            }

            // LOAD THE START AND END HOURS
            String startHour = json.getString(JSON_START_HOUR);
            String endHour = json.getString(JSON_END_HOUR);  
            dataManager.initHours(startHour, endHour);
              
            setCellProps();


            // AND THEN ALL THE OFFICE HOURS
            JsonArray jsonOfficeHoursArray = json.getJsonArray(JSON_OFFICE_HOURS);
            for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
                JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
                String day = jsonOfficeHours.getString(JSON_DAY);
                String time = jsonOfficeHours.getString(JSON_TIME);
                String name = jsonOfficeHours.getString(JSON_NAME);
                dataManager.addOfficeHoursReservation(day, time, name);
            }

            JsonArray jsonRecitationsArray = json.getJsonArray(JSON_REC);
            for (int i = 0; i < jsonRecitationsArray.size(); i++) {
                JsonObject jsonRecitation = jsonRecitationsArray.getJsonObject(i);
                String section = jsonRecitation.getString(JSON_REC_SECTION);
                String instructor = jsonRecitation.getString(JSON_REC_INSTRUCTOR);
                String dayTime = jsonRecitation.getString(JSON_REC_DAYTIME);
                String location = jsonRecitation.getString(JSON_REC_LOCATION);
                String supervisingTA_1 = jsonRecitation.getString(JSON_REC_SUPERVISINGTA_1);
                String supervisingTA_2 = jsonRecitation.getString(JSON_REC_SUPERVISINGTA_2);
                dataManager.addRecitation(section, instructor, dayTime, location, supervisingTA_1, supervisingTA_2);
            }

            String startingMonday = json.getString(JSON_SCHEDULE_STARTINGMONDAY);
            String endingFriday = json.getString(JSON_SCHEDULE_ENDINGFRIDAY);

            try {
                dataManager.setStartingMonday(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(startingMonday));
                dataManager.setEndingFriday(new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(endingFriday));
            } catch (java.text.ParseException pe) {
                fail("Starting/ending dates incorrectly parsed!");
            }

            JsonArray jsonScheduledItemsArray = json.getJsonArray(JSON_SCHEDULEDITEMS);
            for (int i = 0; i < jsonScheduledItemsArray.size(); i++) {
                JsonObject jsonScheduledItem = jsonScheduledItemsArray.getJsonObject(i);
                String type = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TYPE);
                String date = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_DATE);
                String itemTitle = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TITLE);
                String topic = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TOPIC);
                String link = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_LINK);
                String criteria = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_CRITERIA);
                try {
                    dataManager.addScheduledItem(type, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date), itemTitle, topic, link, criteria);
                } catch (java.text.ParseException pe) {
                    fail("Schedule date incorrectly parsed!");
                }
            }

            JsonArray jsonTeamsArray = json.getJsonArray(JSON_TEAMS);
            for (int i = 0; i < jsonTeamsArray.size(); i++) {
                JsonObject jsonTeam = jsonTeamsArray.getJsonObject(i);
                String name = jsonTeam.getString(JSON_TEAM_NAME);
                String color = jsonTeam.getString(JSON_TEAM_COLOR);
                String textColor = jsonTeam.getString(JSON_TEAM_TEXTCOLOR);
                String link = jsonTeam.getString(JSON_TEAM_LINK);
                dataManager.addTeam(name, Color.web(color), Color.web(textColor), link);
            }

            JsonArray jsonStudentsArray = json.getJsonArray(JSON_STUDENTS);
            for (int i = 0; i < jsonStudentsArray.size(); i++) {
                JsonObject jsonStudent = jsonStudentsArray.getJsonObject(i);
                String firstName = jsonStudent.getString(JSON_STUDENT_FIRSTNAME);
                String lastName = jsonStudent.getString(JSON_STUDENT_LASTNAME);
                String team = jsonStudent.getString(JSON_STUDENT_TEAM);
                String role = jsonStudent.getString(JSON_STUDENT_ROLE);
                dataManager.addStudent(firstName, lastName, team, role);
            }
        } catch (IOException ioe) {
            fail("IOException encountered!");
        }
    }

    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }
    
    private void setCellProps(){
        dataManager.setCellProperty(0, 1, new SimpleStringProperty("12:00am"));
        dataManager.setCellProperty(0, 2, new SimpleStringProperty("12:30am"));
        dataManager.setCellProperty(0, 3, new SimpleStringProperty("1:00am"));
        dataManager.setCellProperty(0, 4, new SimpleStringProperty("1:30am"));
        dataManager.setCellProperty(0, 5, new SimpleStringProperty("2:00am"));
        dataManager.setCellProperty(0, 6, new SimpleStringProperty("2:30am"));
        dataManager.setCellProperty(0, 7, new SimpleStringProperty("3:00am"));
        dataManager.setCellProperty(0, 8, new SimpleStringProperty("3:30am"));
        dataManager.setCellProperty(0, 9, new SimpleStringProperty("4:00am"));
        dataManager.setCellProperty(0, 10, new SimpleStringProperty("4:30am"));
        dataManager.setCellProperty(0, 11, new SimpleStringProperty("5:00am"));
        dataManager.setCellProperty(0, 12, new SimpleStringProperty("5:30am"));
        dataManager.setCellProperty(0, 13, new SimpleStringProperty("6:00am"));
        dataManager.setCellProperty(0, 14, new SimpleStringProperty("6:30am"));
        dataManager.setCellProperty(0, 15, new SimpleStringProperty("7:00am"));
        dataManager.setCellProperty(0, 16, new SimpleStringProperty("7:30am"));
        dataManager.setCellProperty(0, 17, new SimpleStringProperty("8:00am"));
        dataManager.setCellProperty(0, 18, new SimpleStringProperty("8:30am"));
        dataManager.setCellProperty(0, 19, new SimpleStringProperty("9:00am"));
        dataManager.setCellProperty(0, 20, new SimpleStringProperty("9:30am"));
        dataManager.setCellProperty(0, 21, new SimpleStringProperty("10:00am"));
        dataManager.setCellProperty(0, 22, new SimpleStringProperty("10:30am"));
        dataManager.setCellProperty(0, 23, new SimpleStringProperty("11:00am"));
        dataManager.setCellProperty(0, 24, new SimpleStringProperty("11:30am"));
        dataManager.setCellProperty(0, 25, new SimpleStringProperty("12:00pm"));
        dataManager.setCellProperty(0, 26, new SimpleStringProperty("12:30pm"));
        dataManager.setCellProperty(0, 27, new SimpleStringProperty("1:00pm"));
        dataManager.setCellProperty(0, 28, new SimpleStringProperty("1:30pm"));
        dataManager.setCellProperty(0, 29, new SimpleStringProperty("2:00pm"));
        dataManager.setCellProperty(0, 30, new SimpleStringProperty("2:30pm"));
        dataManager.setCellProperty(0, 31, new SimpleStringProperty("3:00pm"));
        dataManager.setCellProperty(0, 32, new SimpleStringProperty("3:30pm"));
        dataManager.setCellProperty(0, 33, new SimpleStringProperty("4:00pm"));
        dataManager.setCellProperty(0, 34, new SimpleStringProperty("4:30pm"));
        dataManager.setCellProperty(0, 35, new SimpleStringProperty("5:00pm"));
        dataManager.setCellProperty(0, 36, new SimpleStringProperty("5:30pm"));
        dataManager.setCellProperty(0, 37, new SimpleStringProperty("6:00pm"));
        dataManager.setCellProperty(0, 38, new SimpleStringProperty("6:30pm"));
        dataManager.setCellProperty(0, 39, new SimpleStringProperty("7:00pm"));
        dataManager.setCellProperty(0, 40, new SimpleStringProperty("7:30pm"));
        dataManager.setCellProperty(0, 41, new SimpleStringProperty("8:00pm"));
        dataManager.setCellProperty(0, 42, new SimpleStringProperty("8:30pm"));
        dataManager.setCellProperty(0, 43, new SimpleStringProperty("9:00pm"));
        dataManager.setCellProperty(0, 44, new SimpleStringProperty("9:30pm"));
        dataManager.setCellProperty(0, 45, new SimpleStringProperty("10:00pm"));
        dataManager.setCellProperty(0, 46, new SimpleStringProperty("10:30pm"));
        dataManager.setCellProperty(0, 47, new SimpleStringProperty("11:00pm"));
        dataManager.setCellProperty(0, 48, new SimpleStringProperty("11:30pm"));
        
        for(int col = 2; col < 7; col++){
            for(int row = 1; row < dataManager.getNumRows(); row++){
                dataManager.setCellProperty(col, row, new SimpleStringProperty(""));
            }
        }
    }
    
    @Test
    public void testSubject(){
        assertEquals("CSE", dataManager.getSubject());
    }
    
    @Test
    public void testNumber(){
        assertEquals("219", dataManager.getNumber());
    }
    
    @Test
    public void testSemester(){
        assertEquals("Fall", dataManager.getSemester());
    }
    
    @Test
    public void testYear(){
        assertEquals("2017", dataManager.getYear());
    }
    
    @Test
    public void testTitle(){
        assertEquals("Computer Science III", dataManager.getTitle());
    }
    
    @Test
    public void testInstructorName(){
        assertEquals("Richard McKenna", dataManager.getInstructorName());
    }
    
    @Test
    public void testInstructorHome(){
        assertEquals("http://www.cs.stonybrook.edu/~richard", dataManager.getInstructorHome());
    }
    
    @Test
    public void testExportDir(){
        assertEquals("C:\\Users\\rmckenna\\Documents\\Courses\\CSE219\\Summer2017\\public", dataManager.getExportDir());
    }
    
    @Test
    public void testSiteTemplateDir(){
        assertEquals("C:\\Users\\rmckenna\\Documents\\Courses\\Templates\\CSE219", dataManager.getSiteTemplateDir());
    }
    
    @Test
    public void testSitePages(){
        SitePage sp1 = new SitePage(true, "Home", "index.html", "HomeBuilder.js");
        SitePage sp2 = new SitePage(true, "Syllabus", "syllabus.html", "SyllabusBuilder.js");
        SitePage sp3 = new SitePage(true, "Schedule", "schedule.html", "ScheduleBuilder.js");
        SitePage sp4 = new SitePage(true, "HWs", "hws.html", "HWsBuilder.js");
        SitePage sp5 = new SitePage(false, "Projects", "projects.html", "ProjectsBuilder.js");
        ObservableList<SitePage> sitePages = FXCollections.observableArrayList();
        sitePages.addAll(sp1, sp2, sp3, sp4, sp5);
        for(int i = 0; i < dataManager.getSitePages().size(); i++){
            SitePage sp = dataManager.getSitePages().get(i);
            assertEquals(sitePages.get(i).isUse(), sp.isUse());
            assertEquals(sitePages.get(i).getNavbarTitle(), sp.getNavbarTitle());
            assertEquals(sitePages.get(i).getFileName(), sp.getFileName());
            assertEquals(sitePages.get(i).getScript(), sp.getScript());
        }
    }
    
    @Test
    public void testBannerSchoolImageDir(){
        assertEquals("C:\\Users\\rmckenna\\Documents\\Courses\\CSE219\\Summer2017\\images\\banner_school.png", dataManager.getBannerSchoolImageDir());
    }
    
    @Test
    public void testLeftFooterImageDir(){
        assertEquals("C:\\Users\\rmckenna\\Documents\\Courses\\CSE219\\Summer2017\\images\\left_footer.png", dataManager.getLeftFooterImageDir());
    }
    
    @Test
    public void testRightFooterImageDir(){
        assertEquals("C:\\Users\\rmckenna\\Documents\\Courses\\CSE219\\Summer2017\\images\\right_footer.png", dataManager.getRightFooterImageDir());
    }
    
    @Test
    public void testStylesheetDir(){
        assertEquals("C:\\Users\\rmckenna\\Documents\\Courses\\CSE219\\Summer2017\\work\\css\\sea_wolf.css", dataManager.getStylesheetDir());
    }
    
    @Test
    public void testUndergradTAs(){
        TeachingAssistant ta1 = new TeachingAssistant("Hikari Oshiro", "hoshiro@stonybrookusg.org", false, app);
        TeachingAssistant ta2 = new TeachingAssistant("Timothy Hart", "tjh.hart@gmail.com", true, app);
        ObservableList<TeachingAssistant> teachingAssistants = FXCollections.observableArrayList();
        teachingAssistants.addAll(ta1, ta2);
        for(int i = 0; i < dataManager.getTeachingAssistants().size(); i++){
            TeachingAssistant ta = (TeachingAssistant)dataManager.getTeachingAssistants().get(i);
            assertEquals(teachingAssistants.get(i).getName(), ta.getName());
            assertEquals(teachingAssistants.get(i).getEmail(), ta.getEmail());
            assertEquals(teachingAssistants.get(i).getUndergrad().getValue(), ta.getUndergrad().getValue());
        }
    }

    @Test
    public void testStartHour(){
        assertEquals(0, dataManager.getStartHour());
    }
    
    @Test
    public void testEndHour(){
        assertEquals(24, dataManager.getEndHour());
    }
    
    @Test
    public void testOfficeHours(){
        TimeSlot ts1 = new TimeSlot("FRIDAY", "2_30am", "Timothy Hart");
        TimeSlot ts2 = new TimeSlot("TUESDAY", "4_00am", "Hikari Oshiro");
        TimeSlot ts3 = new TimeSlot("MONDAY", "9_00pm", "");
        assertEquals(ts1.getName(), dataManager.getOfficeHours().get("6_6").getValue());
        assertEquals(ts2.getName(), dataManager.getOfficeHours().get("3_9").getValue());
        assertEquals(ts3.getName(), dataManager.getOfficeHours().get("2_34").getValue());
    }
    
    @Test
    public void testRecitations(){
        Recitation rec1 = new Recitation("R01", "McKenna", "Wed 3:30-4:23pm", "Old CS 2114", "Timothy Hart", "Hikari Oshiro");
        Recitation rec2 = new Recitation("R02", "McKenna", "Fri 1:30-2:23pm", "Old CS 2114", "Timothy Hart", "Hikari Oshiro");
        ObservableList<Recitation> recitations = dataManager.getRecitations();
        
        assertEquals(rec1.getSection().getValue(), recitations.get(0).getSection().getValue());
        assertEquals(rec1.getInstructor().getValue(), recitations.get(0).getInstructor().getValue());
        assertEquals(rec1.getDayTime().getValue(), recitations.get(0).getDayTime().getValue());
        assertEquals(rec1.getLocation().getValue(), recitations.get(0).getLocation().getValue());
        assertEquals(rec1.getSupervisingTA_1().getValue(), recitations.get(0).getSupervisingTA_1().getValue());
        assertEquals(rec1.getSupervisingTA_2().getValue(), recitations.get(0).getSupervisingTA_2().getValue());
        
        assertEquals(rec2.getSection().getValue(), recitations.get(1).getSection().getValue());
        assertEquals(rec2.getInstructor().getValue(), recitations.get(1).getInstructor().getValue());
        assertEquals(rec2.getDayTime().getValue(), recitations.get(1).getDayTime().getValue());
        assertEquals(rec2.getLocation().getValue(), recitations.get(1).getLocation().getValue());
        assertEquals(rec2.getSupervisingTA_1().getValue(), recitations.get(1).getSupervisingTA_1().getValue());
        assertEquals(rec2.getSupervisingTA_2().getValue(), recitations.get(1).getSupervisingTA_2().getValue());
    }
    
    @Test
    public void testStartingMonday(){
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date startingMonday = new Date();
        Date endingFriday = new Date();
        try{
            startingMonday = format.parse("May 29, 2017");
            endingFriday = format.parse("August 25, 2017");
        }catch(java.text.ParseException pe){
            fail("ParseException thrown!");
        }
        assertEquals(startingMonday, dataManager.getStartingMonday());
        assertEquals(endingFriday, dataManager.getEndingFriday());
    }
    
    @Test
    public void testScheduledItems(){
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date d1 = new Date();
        Date d2 = new Date();
        Date d3 = new Date();
        Date d4 = new Date();
        try{
            d1 = format.parse("July 4, 2017");
            d2 = format.parse("July 12, 2017");
            d3 = format.parse("August 1, 2017");
            d4 = format.parse("August 8, 2017");
        }catch(java.text.ParseException pe){
            fail("ParseException thrown!");
        }
        ScheduledItem si1 = new ScheduledItem("Holiday", d1, "Independence Day", "", "", "" );
        ScheduledItem si2 = new ScheduledItem("Lecture", d2, "Lecture 3", "Event Programming", "https://eventprogramming.com/", "" );
        ScheduledItem si3 = new ScheduledItem("Holiday", d3, "NO LECTURE", "", "", "" );
        ScheduledItem si4 = new ScheduledItem("Homework", d4, "HW3", "UML", "https://umlhelp.org/", "" );
        ArrayList<ScheduledItem> scheduledItems = new ArrayList();
        scheduledItems.add(si1);
        scheduledItems.add(si2);
        scheduledItems.add(si3);
        scheduledItems.add(si4);
        
        ObservableList<ScheduledItem> items = dataManager.getScheduledItems();
        for(int i = 0; i < items.size(); i++){
            assertEquals(scheduledItems.get(i).getType().getValue(), items.get(i).getType().getValue());
            assertEquals(scheduledItems.get(i).getDate(), items.get(i).getDate());
            assertEquals(scheduledItems.get(i).getTitle().getValue(), items.get(i).getTitle().getValue());
            assertEquals(scheduledItems.get(i).getTopic().getValue(), items.get(i).getTopic().getValue());
            assertEquals(scheduledItems.get(i).getLink().getValue(), items.get(i).getLink().getValue());
            assertEquals(scheduledItems.get(i).getCriteria().getValue(), items.get(i).getCriteria().getValue());  
        }
    }
    
    @Test
    public void testTeams(){
        Team t1 = new Team("Atomic Comics", Color.web("0x552211"), Color.web("0xffffff"), "http://atomicomic.com");
        Team t2 = new Team("C4 Comics", Color.web("0x235399"), Color.web("0xffffff"), "https://c4-comics.appspot.com");
        ArrayList<Team> teams = new ArrayList();
        teams.add(t1);
        teams.add(t2);
        
        ObservableList<Team> ts = dataManager.getTeams();
        for(int i = 0; i < ts.size(); i++){
            assertEquals(teams.get(i).getName().getValue(), ts.get(i).getName().getValue());
            assertEquals(teams.get(i).getColor(), ts.get(i).getColor());
            assertEquals(teams.get(i).getTextColor(), ts.get(i).getTextColor());
            assertEquals(teams.get(i).getLink().getValue(), ts.get(i).getLink().getValue());  
        }
    }
    
    @Test
    public void testStudents(){
        Student s1 = new Student("Beau", "Brummell", "Atomic Comics", "Lead Designer");
        Student s2 = new Student("Jane", "Doe", "C4 Comics", "Lead Programmer");
        Student s3 = new Student("Nooian", "Soong", "Atomic Comics", "Data Designer");
        ArrayList<Student> students = new ArrayList();
        students.add(s1);
        students.add(s2);
        students.add(s3);
        
        ObservableList<Student> ss = dataManager.getStudents();
        for(int i = 0; i < ss.size(); i++){
            assertEquals(students.get(i).getFirstName().getValue(), ss.get(i).getFirstName().getValue());
            assertEquals(students.get(i).getLastName().getValue(), ss.get(i).getLastName().getValue());
            assertEquals(students.get(i).getTeam().getValue(), ss.get(i).getTeam().getValue());
            assertEquals(students.get(i).getRole().getValue(), ss.get(i).getRole().getValue());       
        }
    }
}
