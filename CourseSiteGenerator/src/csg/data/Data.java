package csg.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import djf.components.AppDataComponent;
import djf.ui.AppMessageDialogSingleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ComboBox;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.workspace.TAWorkspace;
import static djf.settings.AppPropertyType.INVALID_EMAIL;
import java.util.Date;
import javafx.scene.paint.Color;

/**
 * This is the data component for CourseSiteGeneratorApp. It has all the data
 * needed to be set by the user via the User Interface and file I/O can set and
 * get all the data from this object
 *
 * @author Richard McKenna
 */
public class Data implements AppDataComponent {

    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteGeneratorApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;

    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;

    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;

    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    int startHour;
    int endHour;
    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 0;
    public static final int MAX_END_HOUR = 24;
    
    ObservableList<Recitation> recitations;
    ObservableList<ScheduledItem> scheduledItems;
    
    String subject;
    String number;
    String semester;
    String year;
    String title;
    String instructorName;
    String instructorHome;
    String exportDir;
    
    String siteTemplateDir;
    ObservableList<SitePage> sitePages;
    ObservableList<Team> teams;
    ObservableList<Student> students;
    
    String bannerSchoolImageDir;
    String leftFooterImageDir;
    String rightFooterImageDir;
    String stylesheetDir;
    
    Date startingMonday;
    Date endingFriday;

    /**
     * This constructor will setup the required data structures for use, but
     * will have to wait on the office hours grid, since it receives the
     * StringProperty objects from the Workspace.
     *
     * @param initApp The application this data manager belongs to.
     */
    public Data(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();
        recitations = FXCollections.observableArrayList();
        scheduledItems = FXCollections.observableArrayList();
        
        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;

        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();

        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ArrayList<String> timeHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.OFFICE_HOURS_TABLE_HEADERS);
        ArrayList<String> dowHeaders = props.getPropertyOptionsList(CourseSiteGeneratorProp.DAYS_OF_WEEK);
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
        
        subject = "";
        number = "";
        semester = "";
        year = "";
        title = "";
        instructorName = "";
        instructorHome = "";
        exportDir = "";
        
        siteTemplateDir = "";
        sitePages = FXCollections.observableArrayList();
        teams = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
        
        bannerSchoolImageDir = "";
        leftFooterImageDir = "";
        rightFooterImageDir = "";
        stylesheetDir = "";
        
        startingMonday = new Date();
        endingFriday = new Date(); 
    }

    /**
     * Called each time new work is created or loaded, it resets all data and
     * data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {

        startHour = MIN_START_HOUR;
        endHour = MAX_END_HOUR;
        teachingAssistants.clear();
        officeHours.clear();
        
        sitePages.clear();
        teams.clear();
        students.clear();
        recitations.clear();
        scheduledItems.clear();
        
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        ComboBox a = (ComboBox) workspace.getOfficeHoursSubheaderBox().getChildren().get(2);
        ComboBox c = (ComboBox) workspace.getOfficeHoursSubheaderBox().getChildren().get(4);

        a.getSelectionModel().select(0);
        c.getSelectionModel().select(47);
    }

    // ACCESSOR METHODS
    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int st) {
        this.startHour = st;
    }

    public void setEndHour(int et) {
        this.endHour = et;
    }

    public int getEndHour() {
        return endHour;
    }

    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList getTeachingAssistants() {
        return teachingAssistants;
    }

    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }

    public int getNumRows() {
        return ((endHour - startHour) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }

        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour <= 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    public String getCellKey(String day, String time) {
        String langChoice = app.getLangChoice();
        if (langChoice.equals("lux")) {
            if (day.equals("MONDAY")) {
                day = "MÉINDEG";
            } else if (day.equals("TUESDAY")) {
                day = "DËNSCHDEG";
            } else if (day.equals("WEDNESDAY")) {
                day = "MËTTWOCH";
            } else if (day.equals("THURSDAY")) {
                day = "DONNESCHDEG";
            } else if (day.equals("FRIDAY")) {
                day = "FREIDEG";
            }
        } else if (langChoice.equals("eng")) {
            if (day.equals("MÉINDEG")) {
                day = "MONDAY";
            } else if (day.equals("DËNSCHDEG")) {
                day = "TUESDAY";
            } else if (day.equals("MËTTWOCH")) {
                day = "WEDNESDAY";
            } else if (day.equals("DONNESCHDEG")) {
                day = "THURSDAY";
            } else if (day.equals("FREIDEG")) {
                day = "FRIDAY";
            }
        }
        int col = gridHeaders.indexOf(day);
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour;
        if (hour < startHour) {
            milHour += 12;
        }
        row += (milHour - startHour) * 2;
        if (time.contains("_30")) {
            row += 1;
        }
        return getCellKey(col, row);
    }

    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return ta;
            }
        }
        return null;
    }

    /**
     * This method is for giving this data manager the string property for a
     * given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }

    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid,
            int column, int row, StringProperty prop) {
        grid.get(row).set(column, prop);
    }

    private void initOfficeHours(int initStartHour, int initEndHour) {
        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour = initStartHour;
        endHour = initEndHour;

        // EMPTY THE CURRENT OFFICE HOURS VALUES
        officeHours.clear();
        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO

    }

    public void initHours(String startHourText, String endHourText) {
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);

        if ((initStartHour >= MIN_START_HOUR)
                && (initEndHour <= MAX_END_HOUR)
                && (initStartHour <= initEndHour)) {
            // THESE ARE VALID HOURS SO KEEP THEM
            initOfficeHours(initStartHour, initEndHour);
        }
    }

    public boolean containsTA(String testName, String testEmail) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return true;
            }
            if (ta.getEmail().equals(testEmail)) {
                return true;
            }
        }
        return false;
    }

    public void addTA(String initName, String initEmail, boolean ug) {

        // MAKE THE TA
        EmailValidator a = new EmailValidator();
        if (a.validate(initEmail)) {
            TeachingAssistant ta = new TeachingAssistant(initName, initEmail, ug, app);

            // ADD THE TA
            if (!containsTA(initName, initEmail)) {

                teachingAssistants.add(ta);
            }

            // SORT THE TAS
        } else {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(INVALID_EMAIL), props.getProperty(INVALID_EMAIL));

        }
        Collections.sort(teachingAssistants);
    }
    
    public void addSitePage(boolean initUse, String initNavbarTitle, String initFileName, String initScript){
        SitePage sp = new SitePage(initUse, initNavbarTitle, initFileName, initScript);
        sitePages.add(sp);
    }
    
    public void addRecitation(String initSection, String initInstructor, String initDayTime, String initLocation, String initSupervisingTA_1, String initSupervisingTA_2){
        Recitation rec = new Recitation(initSection, initInstructor, initDayTime, initLocation, initSupervisingTA_1, initSupervisingTA_2);
        recitations.add(rec);
    }
    
    public void addScheduledItem(String initType, Date initDate, String initTime, String initTitle, String initTopic, String initLink, String initCriteria){
        ScheduledItem si = new ScheduledItem(initType, initDate, initTime, initTitle, initTopic, initLink, initCriteria);
        scheduledItems.add(si);
    }
    
    public void addTeam(String initName, String initColor, String initTextColor, String initLink){
        Team team = new Team(initName, initColor, initTextColor, initLink);
        teams.add(team);
    }
    
    public void addStudent(String initFirstName, String initLastName, String initTeam, String initRole){
        Student stu = new Student(initFirstName, initLastName, initTeam, initRole);
        students.add(stu);
    }

    public void removeTA(String name) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (name.equals(ta.getName())) {
                teachingAssistants.remove(ta);
                return;
            }
        }
    }
    
    public void removeTeam(String name){
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        for(Team team : teams){
            if(name.equals(team.getName())){
                teams.remove(team);
              
                if(workspace.getTeamName().getText().equals(team.getName())){
                    workspace.getTeamName().setText("");
                    Color color = Color.rgb(255, 255, 255);
                    workspace.getTeamColor().setValue(color);
                    workspace.getTeamTextColor().setValue(color);
                    workspace.getTeamLink().setText("");
                    
                }

                workspace.getTeamTable().getSelectionModel().clearSelection();
                workspace.getStudentTable().getSelectionModel().clearSelection();
                workspace.getStudentFirstName().setText("");
                workspace.getStudentLastName().setText("");
                workspace.getTeamComboBox().getSelectionModel().clearSelection();
                workspace.getStudentRole().setText("");

                return;
            }
        }
        

        
    }
    
    public void removeStudent(String firstName, String lastName){
        for (Student student : students) {
            if (firstName.equals(student.getFirstName()) && lastName.equals(student.getLastName())) {
                students.remove(student);
                TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
                workspace.getStudentTable().getSelectionModel().clearSelection();
                workspace.getStudentFirstName().setText("");
                workspace.getStudentLastName().setText("");
                workspace.getTeamComboBox().getSelectionModel().clearSelection();
                workspace.getStudentRole().setText("");
                return;
            }
        }
    }
    
    public void addOfficeHoursReservation(String day, String time, String taName) {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }

    /**
     * This function toggles the taName in the cell represented by cellKey.
     * Toggle means if it's there it removes it, if it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        StringProperty cellProp = officeHours.get(cellKey);
        String cellText = cellProp.getValue();

        // IF IT ALREADY HAS THE TA, REMOVE IT
        if (cellText.contains(taName)) {
            removeTAFromCell(cellProp, taName);
        } // OTHERWISE ADD IT
        else if (cellText.length() == 0) {
            cellProp.setValue(taName);
        } else {
            cellProp.setValue(cellText + "\n" + taName);
        }
    }

    /**
     * This method removes taName from the office grid cell represented by
     * cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        } // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        } // IS IT IN THE MIDDLE OF A LIST OF TAs
        else if (cellText.indexOf(taName) < cellText.indexOf("\n", cellText.indexOf(taName))) {
            int startIndex = cellText.indexOf("\n" + taName);
            int endIndex = startIndex + taName.length() + 1;
            cellText = cellText.substring(0, startIndex) + cellText.substring(endIndex);
            cellProp.setValue(cellText);
        } // IT MUST BE THE LAST TA
        else {
            int startIndex = cellText.indexOf("\n" + taName);
            cellText = cellText.substring(0, startIndex);
            cellProp.setValue(cellText);
        }
    }
    
    public ObservableList getRecitations(){
        return recitations;
    }
    
    public ObservableList getScheduledItems(){
        return scheduledItems;
    }
    
    public ObservableList getTeams(){
        return teams;
    }
    
    public ObservableList getStudents(){
        return students;
    }
    
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public void setInstructorHome(String instructorHome) {
        this.instructorHome = instructorHome;
    }

    public void setExportDir(String exportDir) {
        this.exportDir = exportDir;
    }
    
    public void setSiteTemplateDir(String siteTemplateDir){
        this.siteTemplateDir = siteTemplateDir;
    }
    
    public String getSubject(){
        return subject;
    }
    
    public String getNumber() {
        return number;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public String getTitle() {
        return title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public String getInstructorHome() {
        return instructorHome;
    }

    public String getExportDir() {
        return exportDir;
    }
    
    public String getSiteTemplateDir(){
        return siteTemplateDir;
    }

    public ObservableList<SitePage> getSitePages() {
        return sitePages;
    }

    public void setSitePages(ObservableList<SitePage> sitePages) {
        this.sitePages = sitePages;
    }

    public String getBannerSchoolImageDir() {
        return bannerSchoolImageDir;
    }

    public void setBannerSchoolImageDir(String bannerSchoolImageDir) {
        this.bannerSchoolImageDir = bannerSchoolImageDir;
    }

    public String getLeftFooterImageDir() {
        return leftFooterImageDir;
    }

    public void setLeftFooterImageDir(String leftFooterImageDir) {
        this.leftFooterImageDir = leftFooterImageDir;
    }

    public String getRightFooterImageDir() {
        return rightFooterImageDir;
    }

    public void setRightFooterImageDir(String rightFooterImageDir) {
        this.rightFooterImageDir = rightFooterImageDir;
    }

    public String getStylesheetDir() {
        return stylesheetDir;
    }

    public void setStylesheetDir(String stylesheetDir) {
        this.stylesheetDir = stylesheetDir;
    }

    public Date getStartingMonday() {
        return startingMonday;
    }

    public void setStartingMonday(Date startingMonday) {
        this.startingMonday = startingMonday;
    }

    public Date getEndingFriday() {
        return endingFriday;
    }

    public void setEndingFriday(Date endingFriday) {
        this.endingFriday = endingFriday;
    }
    
    public String getCSSDirForExport(){
        return stylesheetDir;
    }
    
    public String getSchoolBannerDirForExport(){
        return bannerSchoolImageDir;
    }
    
    public String getLeftFooterDirForExport(){
        return leftFooterImageDir;
    }
    
    public String getRightFooterDirForExport(){
        return rightFooterImageDir;
    }
}