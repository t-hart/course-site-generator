package csg.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import csg.CourseSiteGeneratorApp;
import csg.data.Data;
import csg.data.TeachingAssistant;
import csg.workspace.TAWorkspace;
import csg.data.Recitation;
import csg.data.SitePage;
import csg.data.ScheduledItem;
import csg.data.Team;
import csg.data.Student;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javafx.scene.paint.Color;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.DatePicker;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * This class serves as the file component for the TA manager app. It provides
 * all saving and loading services for the application.
 *
 * @author Richard McKenna
 */
public class TAFiles implements AppFileComponent {

    // THIS IS THE APP ITSELF
    CourseSiteGeneratorApp app;

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
    static final String JSON_REC = "recitations";
    static final String JSON_REC_SECTION = "section";
    static final String JSON_REC_INSTRUCTOR = "instructor";
    static final String JSON_REC_DAYTIME = "day_time";
    static final String JSON_REC_LOCATION = "location";
    static final String JSON_REC_SUPERVISINGTA_1 = "ta_1";
    static final String JSON_REC_SUPERVISINGTA_2 = "ta_2";
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
    static final String JSON_SCHEDULE_STARTINGMONDAYMONTH = "startingMondayMonth";
    static final String JSON_SCHEDULE_STARTINGMONDAYDAY = "startingMondayDay";
    static final String JSON_SCHEDULE_STARTINGMONDAYYEAR = "startingMondayYear";   
    static final String JSON_SCHEDULE_ENDINGFRIDAYMONTH = "endingFridayMonth";
    static final String JSON_SCHEDULE_ENDINGFRIDAYDAY = "endingFridayDay";
    static final String JSON_SCHEDULE_ENDINGFRIDAYYEAR = "endingFridayYear";
    static final String JSON_SCHEDULEDITEMS = "scheduled_items";
    static final String JSON_SCHEDULEDITEM_TYPE = "type";
    static final String JSON_SCHEDULEDITEM_DATE = "date";
    static final String JSON_SCHEDULEDITEM_TIME = "time";
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
    
    public TAFiles(CourseSiteGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
        Data dataManager = (Data) data;
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        // LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);
        
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
        
        ComboBox subjectComboBox = workspace.getSubjectComboBox();
        subjectComboBox.getSelectionModel().select(subject);
        ComboBox sesmterComboBox = workspace.getSemesterComboBox();
        sesmterComboBox.getSelectionModel().select(semester);
        ComboBox numberComboBox = workspace.getNumberComboBox();
        numberComboBox.getSelectionModel().select(number);
        ComboBox yearComboBox = workspace.getYearComboBox();
        yearComboBox.getSelectionModel().select(year);
        TextField courseTitleField = workspace.getCourseTitle();
        courseTitleField.setText(title);
        TextField instructorHomeField = workspace.getInstructorHome();
        instructorHomeField.setText(instructorHome);
        TextField instructorNameField = workspace.getInstructorName();
        instructorNameField.setText(instructorName);
        Label exportDirLabel = workspace.getExportDir();
        
        if (courseExportDir.length() > 30) {
                courseExportDir = "..." + courseExportDir.substring(courseExportDir.length() - 30, courseExportDir.length());
            } else {
                while (courseExportDir.length() < 33) {
                    courseExportDir += " ";
                }
            }

        exportDirLabel.setText(courseExportDir);
        
        String siteTemplateDir = json.getString(JSON_SITETEMPLATE_DIR);
        dataManager.setSiteTemplateDir(siteTemplateDir);
        Label siteTemplatDirLabel = workspace.getSiteTemplateDir();
        
        if (siteTemplateDir.length() > 60) {
            siteTemplateDir = "..." + siteTemplateDir.substring(siteTemplateDir.length() - 60, siteTemplateDir.length());
        } else {
            while (siteTemplateDir.length() < 63) {
                siteTemplateDir += " ";
            }
        }
        workspace.getSiteTemplateDir().setText(siteTemplateDir);

        siteTemplatDirLabel.setText(siteTemplateDir);
        JsonArray jsonSitePagesArray = json.getJsonArray(JSON_SITEPAGES);
        for(int i = 0; i < jsonSitePagesArray.size(); i++){
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
        
        ImageView bannerSchool = workspace.getBannerSchool();
        bannerSchool.setImage(new Image("file:"+bannerSchoolImageDir));
        ImageView rightFooter = workspace.getRightFooter();
        rightFooter.setImage(new Image("file:"+rightFooterImageDir));
        ImageView leftFooter = workspace.getLeftFooter();
        leftFooter.setImage(new Image("file:"+leftFooterImageDir));
        ComboBox style = workspace.getStylesheet();
        style.getSelectionModel().select(stylesheetDir);
        
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
        ComboBox a = (ComboBox) workspace.getOfficeHoursSubheaderBox().getChildren().get(2);
        ComboBox c = (ComboBox) workspace.getOfficeHoursSubheaderBox().getChildren().get(4);
        a.getSelectionModel().select(Integer.parseInt(startHour));
        c.getSelectionModel().select(Integer.parseInt(endHour));
        dataManager.initHours(startHour, endHour);

        // NOW RELOAD THE WORKSPACE WITH THE LOADED DATA
        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());


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
        for(int i = 0; i < jsonRecitationsArray.size(); i++){
            JsonObject jsonRecitation = jsonRecitationsArray.getJsonObject(i);
            String section = jsonRecitation.getString(JSON_REC_SECTION);
            String instructor = jsonRecitation.getString(JSON_REC_INSTRUCTOR);
            String dayTime = jsonRecitation.getString(JSON_REC_DAYTIME);
            String location = jsonRecitation.getString(JSON_REC_LOCATION);
            String supervisingTA_1 = jsonRecitation.getString(JSON_REC_SUPERVISINGTA_1);
            String supervisingTA_2 = jsonRecitation.getString(JSON_REC_SUPERVISINGTA_2);
            dataManager.addRecitation(section, instructor, dayTime, location, supervisingTA_1, supervisingTA_2);
        }
        
        String startingMondayMonth = json.getString(JSON_SCHEDULE_STARTINGMONDAYMONTH);
        String startingMondayDay = json.getString(JSON_SCHEDULE_STARTINGMONDAYDAY);
        String startingMondayYear = json.getString(JSON_SCHEDULE_STARTINGMONDAYYEAR);
        String startingMonday = startingMondayMonth+"/"+startingMondayDay+"/"+startingMondayYear;
        String endingFridayMonth = json.getString(JSON_SCHEDULE_ENDINGFRIDAYMONTH);
        String endingFridayDay = json.getString(JSON_SCHEDULE_ENDINGFRIDAYDAY);
        String endingFridayYear = json.getString(JSON_SCHEDULE_ENDINGFRIDAYYEAR);
        String endingFriday = endingFridayMonth+"/"+endingFridayDay+"/"+endingFridayYear;
        
        Date startingDate = new Date();
        Date endingDate = new Date();
        try {
            DatePicker startingMondayPicker = workspace.getStartingMonday();
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            startingDate = df.parse(startingMonday);
            Calendar cal = Calendar.getInstance();
            cal.setTime(startingDate);
            startingMondayPicker.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
            DatePicker endingFridayPicker = workspace.getEndingFriday();
            endingDate = df.parse(endingFriday);
            cal.setTime(endingDate);
            endingFridayPicker.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
        } catch (java.text.ParseException pe) {
            pe.printStackTrace();
        }
        
        dataManager.setStartingMonday(startingDate);
        dataManager.setEndingFriday(endingDate);
        
        JsonArray jsonScheduledItemsArray = json.getJsonArray(JSON_SCHEDULEDITEMS);
        for(int i = 0; i < jsonScheduledItemsArray.size(); i++){
            JsonObject jsonScheduledItem = jsonScheduledItemsArray.getJsonObject(i);
            String type = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TYPE);
            String date = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_DATE);
            String itemTime = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TIME);
            String itemTitle = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TITLE);
            String topic = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_TOPIC);
            String link = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_LINK);
            String criteria = jsonScheduledItem.getString(JSON_SCHEDULEDITEM_CRITERIA);
            try{
                dataManager.addScheduledItem(type, new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(date), itemTime, itemTitle, topic, link, criteria);
            }catch(java.text.ParseException pe){
                pe.printStackTrace();
            }
        }
        
        JsonArray jsonTeamsArray = json.getJsonArray(JSON_TEAMS);
        for(int i = 0; i < jsonTeamsArray.size(); i++){
            JsonObject jsonTeam = jsonTeamsArray.getJsonObject(i);
            String name = jsonTeam.getString(JSON_TEAM_NAME);
            String color = jsonTeam.getString(JSON_TEAM_COLOR);
            String textColor = jsonTeam.getString(JSON_TEAM_TEXTCOLOR);
            String link = jsonTeam.getString(JSON_TEAM_LINK);
            dataManager.addTeam(name, color, textColor, link);
        }
        
        JsonArray jsonStudentsArray = json.getJsonArray(JSON_STUDENTS);
        for(int i = 0; i < jsonStudentsArray.size(); i++){
            JsonObject jsonStudent = jsonStudentsArray.getJsonObject(i);
            String firstName = jsonStudent.getString(JSON_STUDENT_FIRSTNAME);
            String lastName = jsonStudent.getString(JSON_STUDENT_LASTNAME);
            String team = jsonStudent.getString(JSON_STUDENT_TEAM);
            String role = jsonStudent.getString(JSON_STUDENT_ROLE);
            dataManager.addStudent(firstName, lastName, team, role);
        }
    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        // GET THE DATA
        Data dataManager = (Data) data;

        // NOW BUILD THE TA JSON OBJCTS TO SAVE
        JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
        ObservableList<TeachingAssistant> tas = dataManager.getTeachingAssistants();
        for (TeachingAssistant ta : tas) {
            JsonObject taJson = Json.createObjectBuilder()
                    .add(JSON_NAME, ta.getName())
                    .add(JSON_EMAIL, ta.getEmail())
                    .add(JSON_UG, String.valueOf(ta.getUndergrad().get())).build();
            taArrayBuilder.add(taJson);
        }
        JsonArray undergradTAsArray = taArrayBuilder.build();

        // NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
        JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
        ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(dataManager);
        for (TimeSlot ts : officeHours) {
            JsonObject tsJson = Json.createObjectBuilder()
                    .add(JSON_DAY, ts.getDay())
                    .add(JSON_TIME, ts.getTime())
                    .add(JSON_NAME, ts.getName()).build();
            timeSlotArrayBuilder.add(tsJson);
        }
        JsonArray timeSlotsArray = timeSlotArrayBuilder.build();
        
        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
        ObservableList<Recitation> recs = dataManager.getRecitations();
        for(Recitation rec : recs){
            JsonObject recJson = Json.createObjectBuilder()
                    .add(JSON_REC_SECTION, rec.getSection())
                    .add(JSON_REC_INSTRUCTOR, rec.getInstructor())
                    .add(JSON_REC_DAYTIME, rec.getDayTime())
                    .add(JSON_REC_LOCATION, rec.getLocation())
                    .add(JSON_REC_SUPERVISINGTA_1, rec.getSupervisingTA_1())
                    .add(JSON_REC_SUPERVISINGTA_2, rec.getSupervisingTA_2()).build();
            recitationArrayBuilder.add(recJson);
        }
        JsonArray recitationArray = recitationArrayBuilder.build();
       
        JsonArrayBuilder sitePagesArrayBuilder = Json.createArrayBuilder();
        ObservableList<SitePage> sitePages = dataManager.getSitePages();
        for(SitePage sp : sitePages){
            JsonObject spJson = Json.createObjectBuilder()
                    .add(JSON_SITEPAGE_USE, String.valueOf(sp.isUse().getValue()))
                    .add(JSON_SITEPAGE_NAVBARTITLE, sp.getNavbarTitle())
                    .add(JSON_SITEPAGE_FILENAME, sp.getFileName())
                    .add(JSON_SITEPAGE_SCRIPT, sp.getScript()).build();
            sitePagesArrayBuilder.add(spJson);
        }
        JsonArray sitePageArray = sitePagesArrayBuilder.build();
        
        JsonArrayBuilder scheduledItemsArrayBuilder = Json.createArrayBuilder();
        ObservableList<ScheduledItem> scheduledItems = dataManager.getScheduledItems();
        for(ScheduledItem si : scheduledItems){
            JsonObject siJson = Json.createObjectBuilder()
                    .add(JSON_SCHEDULEDITEM_TYPE, si.getType())
                    .add(JSON_SCHEDULEDITEM_DATE, si.getDate().toString())
                    .add(JSON_SCHEDULEDITEM_TIME, si.getTime())
                    .add(JSON_SCHEDULEDITEM_TITLE, si.getTitle())
                    .add(JSON_SCHEDULEDITEM_TOPIC, si.getTopic())
                    .add(JSON_SCHEDULEDITEM_LINK, si.getLink())
                    .add(JSON_SCHEDULEDITEM_CRITERIA, si.getCriteria()).build();
            scheduledItemsArrayBuilder.add(siJson);
        }
        JsonArray scheduledItemsArray = scheduledItemsArrayBuilder.build();
        
        JsonArrayBuilder teamsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Team> teams = dataManager.getTeams();
        for(Team t : teams){
            JsonObject tJson = Json.createObjectBuilder()
                    .add(JSON_TEAM_NAME, t.getName())
                    .add(JSON_TEAM_COLOR, t.getColor().toString())
                    .add(JSON_TEAM_TEXTCOLOR, t.getTextColor().toString())
                    .add(JSON_TEAM_LINK, t.getLink()).build();
            teamsArrayBuilder.add(tJson);
        }
        JsonArray teamsArray = teamsArrayBuilder.build();
        
        JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> students = dataManager.getStudents();
        for(Student s : students){
            JsonObject sJson = Json.createObjectBuilder()
                    .add(JSON_STUDENT_FIRSTNAME, s.getFirstName())
                    .add(JSON_STUDENT_LASTNAME, s.getLastName())
                    .add(JSON_STUDENT_TEAM, s.getTeam())
                    .add(JSON_STUDENT_ROLE, s.getRole()).build();
            studentsArrayBuilder.add(sJson);
        }
        JsonArray studentsArray = studentsArrayBuilder.build();
        
        // THEN PUT IT ALL TOGETHER IN A JsonObject
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataManager.getStartingMonday());
        String startingMondayMonth = ""+(cal.get(Calendar.MONTH)+1);
        String startingMondayDay = ""+cal.get(Calendar.DAY_OF_MONTH);
        String startingMondayYear = ""+cal.get(Calendar.YEAR);
        cal.setTime(dataManager.getEndingFriday());
        String endingFridayMonth = ""+(cal.get(Calendar.MONTH)+1);
        String endingFridayDay = ""+cal.get(Calendar.DAY_OF_MONTH);
        String endingFridayYear = ""+cal.get(Calendar.YEAR);
        
        JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_COURSE_SUBJECT, dataManager.getSubject())
                .add(JSON_COURSE_NUMBER, dataManager.getNumber())
                .add(JSON_COURSE_SEMESTER, dataManager.getSemester())
                .add(JSON_COURSE_YEAR, dataManager.getYear())
                .add(JSON_COURSE_TITLE, dataManager.getTitle())
                .add(JSON_COURSE_INSTRUCTOR_NAME, dataManager.getInstructorName())
                .add(JSON_COURSE_INSTRUCTOR_HOME, dataManager.getInstructorHome())
                .add(JSON_COURSE_EXPORT_DIR, dataManager.getExportDir())
                .add(JSON_SITETEMPLATE_DIR, dataManager.getSiteTemplateDir())
                .add(JSON_SITEPAGES, sitePageArray)
                .add(JSON_PAGESTYLE_BANNERSCHOOL_DIR, dataManager.getBannerSchoolImageDir())
                .add(JSON_PAGESTYLE_LEFTFOOTER_DIR, dataManager.getLeftFooterImageDir())
                .add(JSON_PAGESTYLE_RIGHTFOOTER_DIR, dataManager.getRightFooterImageDir())
                .add(JSON_PAGESTYLE_STYLESHEET_DIR, dataManager.getStylesheetDir())
                .add(JSON_UNDERGRAD_TAS, undergradTAsArray)
                .add(JSON_START_HOUR, "" + dataManager.getStartHour())
                .add(JSON_END_HOUR, "" + dataManager.getEndHour())
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
                .add(JSON_REC, recitationArray)
                .add(JSON_SCHEDULE_STARTINGMONDAYMONTH, startingMondayMonth)
                .add(JSON_SCHEDULE_STARTINGMONDAYDAY, startingMondayDay)
                .add(JSON_SCHEDULE_STARTINGMONDAYYEAR, startingMondayYear)
                .add(JSON_SCHEDULE_ENDINGFRIDAYMONTH, endingFridayMonth)
                .add(JSON_SCHEDULE_ENDINGFRIDAYDAY, endingFridayDay)
                .add(JSON_SCHEDULE_ENDINGFRIDAYYEAR, endingFridayYear)  
                .add(JSON_SCHEDULEDITEMS, scheduledItemsArray)
                .add(JSON_TEAMS, teamsArray)
                .add(JSON_STUDENTS, studentsArray)
                .build();

        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(dataManagerJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(dataManagerJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }
    
    // IMPORTING/EXPORTING DATA IS USED WHEN WE READ/WRITE DATA IN AN
    // ADDITIONAL FORMAT USEFUL FOR ANOTHER PURPOSE, LIKE ANOTHER APPLICATION
    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        Data dataManager = (Data) data;
        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
        ObservableList<Recitation> recs = dataManager.getRecitations();
        for(Recitation rec : recs){
            JsonObject recJson = Json.createObjectBuilder()
                    .add(JSON_REC_SECTION, "<strong>"+rec.getSection()+"</strong> ("+rec.getInstructor()+")")
                    .add(JSON_REC_DAYTIME, rec.getDayTime())
                    .add(JSON_REC_LOCATION, rec.getLocation())
                    .add(JSON_REC_SUPERVISINGTA_1, rec.getSupervisingTA_1())
                    .add(JSON_REC_SUPERVISINGTA_2, rec.getSupervisingTA_2()).build();
            recitationArrayBuilder.add(recJson);
        }
        JsonArray recitationArray = recitationArrayBuilder.build();
        
        JsonObject dataManagerJSO_rec = Json.createObjectBuilder()
                .add(JSON_REC, recitationArray)
                .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties_rec = new HashMap<>(1);
        properties_rec.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory_rec = Json.createWriterFactory(properties_rec);
        StringWriter sw_rec = new StringWriter();
        JsonWriter jsonWriter_rec = writerFactory_rec.createWriter(sw_rec);
        jsonWriter_rec.writeObject(dataManagerJSO_rec);
        jsonWriter_rec.close();

        // INIT THE WRITER
        OutputStream os_rec = new FileOutputStream(filePath+"RecitationsData.json");
        JsonWriter jsonFileWriter_rec = Json.createWriter(os_rec);
        jsonFileWriter_rec.writeObject(dataManagerJSO_rec);
        String prettyPrinted_rec = sw_rec.toString();
        PrintWriter pw_rec = new PrintWriter(filePath+"RecitationsData.json");
        pw_rec.write(prettyPrinted_rec);
        pw_rec.close();
        
        JsonArrayBuilder scheduledItemsArrayBuilder = Json.createArrayBuilder();
        ObservableList<ScheduledItem> scheduledItems = dataManager.getScheduledItems();
        for(ScheduledItem si : scheduledItems){
            JsonObject siJson = Json.createObjectBuilder()
                    .add(JSON_SCHEDULEDITEM_TYPE, si.getType())
                    .add(JSON_SCHEDULEDITEM_DATE, si.getDate().toString())
                    .add(JSON_SCHEDULEDITEM_TIME, si.getTime())
                    .add(JSON_SCHEDULEDITEM_TITLE, si.getTitle())
                    .add(JSON_SCHEDULEDITEM_TOPIC, si.getTopic())
                    .add(JSON_SCHEDULEDITEM_LINK, si.getLink())
                    .add(JSON_SCHEDULEDITEM_CRITERIA, si.getCriteria()).build();
            scheduledItemsArrayBuilder.add(siJson);
        }
        JsonArray scheduledItemsArray = scheduledItemsArrayBuilder.build();
        
        JsonArrayBuilder sitePagesArrayBuilder = Json.createArrayBuilder();
        ObservableList<SitePage> sitePages = dataManager.getSitePages();
        for(SitePage sp : sitePages){
            JsonObject spJson = Json.createObjectBuilder()
                    .add(JSON_SITEPAGE_USE, String.valueOf(sp.isUse().getValue()))
                    .add(JSON_SITEPAGE_NAVBARTITLE, sp.getNavbarTitle())
                    .add(JSON_SITEPAGE_FILENAME, sp.getFileName())
                    .add(JSON_SITEPAGE_SCRIPT, sp.getScript()).build();
            sitePagesArrayBuilder.add(spJson);
        }
        JsonArray sitePageArray = sitePagesArrayBuilder.build();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dataManager.getStartingMonday());
        String startingMondayMonth = ""+(cal.get(Calendar.MONTH)+1);
        String startingMondayDay = ""+(cal.get(Calendar.DAY_OF_MONTH));
        cal.setTime(dataManager.getEndingFriday());
        String endingFridayMonth = ""+(cal.get(Calendar.MONTH)+1);
        String endingFridayDay = ""+(cal.get(Calendar.DAY_OF_MONTH));
        
        JsonObject dataManagerJSO_sch = Json.createObjectBuilder()
                .add(JSON_PAGESTYLE_STYLESHEET_DIR, dataManager.getStylesheetDir())
                .add(JSON_PAGESTYLE_BANNERSCHOOL_DIR, dataManager.getBannerSchoolImageDir())
                .add(JSON_PAGESTYLE_LEFTFOOTER_DIR, dataManager.getLeftFooterImageDir())
                .add(JSON_PAGESTYLE_RIGHTFOOTER_DIR, dataManager.getRightFooterImageDir())
                .add(JSON_SITEPAGES, sitePageArray)
                .add(JSON_COURSE_SUBJECT, dataManager.getSubject())
                .add(JSON_COURSE_NUMBER, dataManager.getNumber())
                .add(JSON_COURSE_SEMESTER, dataManager.getSemester())
                .add(JSON_COURSE_YEAR, dataManager.getYear())
                .add(JSON_COURSE_TITLE, dataManager.getTitle())
                .add(JSON_COURSE_SUBJECT, dataManager.getSubject())
                .add(JSON_COURSE_NUMBER, dataManager.getNumber())
                .add(JSON_SCHEDULE_STARTINGMONDAYMONTH, startingMondayMonth)
                .add(JSON_SCHEDULE_STARTINGMONDAYDAY, startingMondayDay)
                .add(JSON_SCHEDULE_ENDINGFRIDAYMONTH, endingFridayMonth)
                .add(JSON_SCHEDULE_ENDINGFRIDAYDAY, endingFridayDay)
                .add(JSON_SCHEDULEDITEMS, scheduledItemsArray)
                .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties_sch = new HashMap<>(1);
        properties_sch.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory_sch = Json.createWriterFactory(properties_sch);
        StringWriter sw_sch = new StringWriter();
        JsonWriter jsonWriter_sch = writerFactory_sch.createWriter(sw_sch);
        jsonWriter_sch.writeObject(dataManagerJSO_sch);
        jsonWriter_sch.close();

        // INIT THE WRITER
        OutputStream os_sch = new FileOutputStream(filePath+"ScheduleData.json");
        JsonWriter jsonFileWriter_sch = Json.createWriter(os_sch);
        jsonFileWriter_sch.writeObject(dataManagerJSO_sch);
        String prettyPrinted_sch = sw_sch.toString();
        PrintWriter pw_sch = new PrintWriter(filePath+"ScheduleData.json");
        pw_sch.write(prettyPrinted_sch);
        pw_sch.close();
        
        JsonArrayBuilder teamsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Team> teams = dataManager.getTeams();
        for(Team t : teams){
            JsonObject tJson = Json.createObjectBuilder()
                    .add(JSON_TEAM_NAME, t.getName())
                    .add(JSON_TEAM_COLOR, t.getColor().toString())
                    .add(JSON_TEAM_TEXTCOLOR, t.getTextColor().toString())
                    .add(JSON_TEAM_LINK, t.getLink()).build();
            teamsArrayBuilder.add(tJson);
        }
        JsonArray teamsArray = teamsArrayBuilder.build();
        
        JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> students = dataManager.getStudents();
        for(Student s : students){
            JsonObject sJson = Json.createObjectBuilder()
                    .add(JSON_STUDENT_FIRSTNAME, s.getFirstName())
                    .add(JSON_STUDENT_LASTNAME, s.getLastName())
                    .add(JSON_STUDENT_TEAM, s.getTeam())
                    .add(JSON_STUDENT_ROLE, s.getRole()).build();
            studentsArrayBuilder.add(sJson);
        }
        JsonArray studentsArray = studentsArrayBuilder.build();
        
        JsonObject dataManagerJSO_pro = Json.createObjectBuilder()
                .add(JSON_PAGESTYLE_STYLESHEET_DIR, dataManager.getStylesheetDir())
                .add(JSON_PAGESTYLE_BANNERSCHOOL_DIR, dataManager.getBannerSchoolImageDir())
                .add(JSON_PAGESTYLE_LEFTFOOTER_DIR, dataManager.getLeftFooterImageDir())
                .add(JSON_PAGESTYLE_RIGHTFOOTER_DIR, dataManager.getRightFooterImageDir())
                .add(JSON_SITEPAGES, sitePageArray)
                .add(JSON_COURSE_SUBJECT, dataManager.getSubject())
                .add(JSON_COURSE_NUMBER, dataManager.getNumber())
                .add(JSON_COURSE_SEMESTER, dataManager.getSemester())
                .add(JSON_COURSE_YEAR, dataManager.getYear())
                .add(JSON_COURSE_TITLE, dataManager.getTitle())
                .add(JSON_COURSE_YEAR, dataManager.getYear())
                .add(JSON_TEAMS, teamsArray)
                .add(JSON_STUDENTS, studentsArray)
                .build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties_pro = new HashMap<>(1);
        properties_pro.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory_pro = Json.createWriterFactory(properties_pro);
        StringWriter sw_pro = new StringWriter();
        JsonWriter jsonWriter_pro = writerFactory_pro.createWriter(sw_pro);
        jsonWriter_pro.writeObject(dataManagerJSO_pro);
        jsonWriter_pro.close();

        // INIT THE WRITER
        OutputStream os_pro = new FileOutputStream(filePath+"ProjectsData.json");
        JsonWriter jsonFileWriter_pro = Json.createWriter(os_pro);
        jsonFileWriter_pro.writeObject(dataManagerJSO_pro);
        String prettyPrinted_pro = sw_pro.toString();
        PrintWriter pw_pro = new PrintWriter(filePath+"ProjectsData.json");
        pw_pro.write(prettyPrinted_pro);
        pw_pro.close();
    }
}
