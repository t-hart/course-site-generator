package csg.workspace;

import djf.controller.AppFileController;
import static djf.settings.AppPropertyType.INVALID_EMAIL;
import djf.ui.AppGUI;
import static csg.CourseSiteGeneratorProp.*;
import djf.ui.AppMessageDialogSingleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import jtps.jTPS;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorApp;
import csg.CourseSiteGeneratorProp;
import csg.data.EmailValidator;
import csg.data.Data;
import csg.data.TeachingAssistant;
import csg.style.TAStyle;
import static csg.style.TAStyle.CLASS_HIGHLIGHTED_GRID_CELL;
import static csg.style.TAStyle.CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN;
import static csg.style.TAStyle.CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE;
import csg.workspace.TAWorkspace;
import csg.data.ScheduledItem;
import csg.data.Recitation;
import csg.data.SitePage;
import csg.data.Student;
import csg.data.Team;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.DatePicker;
import javafx.scene.paint.Color;
import javafx.collections.ObservableList;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;


/**
 * This class provides responses to all workspace interactions, meaning
 * interactions with the application controls not including the file
 * toolbar.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class TAController {
    // THE APP PROVIDES ACCESS TO OTHER COMPONENTS AS NEEDED
    CourseSiteGeneratorApp app;
String oldName="";
String oldEmail="";
jTPS j=new jTPS();
    /**
     * Constructor, note that the app must already be constructed.
     */
    public TAController(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
    }
    
    /**
     * This helper method should be called every time an edit happens.
     */    
 public   void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = app.getGUI();
        gui.getFileController().markAsEdited(gui);
    }
    
    /**
     * This method responds to when the user requests to add
     * a new TA via the UI. Note that it must first do some
     * validation to make sure a unique name and email address
     * has been provided.
     */
    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        
        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        Data data = (Data)app.getDataComponent();
        
        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
       if(workspace.getAddButton().getText().equals(props.getProperty(CourseSiteGeneratorProp.UPDATE_BUTTON_TEXT.toString()))){
             TableView taTable = workspace.getTATable();
         Object selectedItem = taTable.getSelectionModel().getSelectedItem();
          TeachingAssistant ta = (TeachingAssistant)selectedItem;
           if(!(name.equals(oldName)&&email.equals(oldEmail))){
         nameTextField.setText("");
               emailTextField.setText("");
               workspace.getAddButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString()));
           jTPS_Transaction change=new change(oldName,oldEmail,name,email,data, ta.getUndergrad().get(), ta.getUndergrad().get());
           j.addTransaction(change );
           }
               else;
       }
       else{ 
             EmailValidator a=new EmailValidator();
        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));            
        }
        // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (email.isEmpty()) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));                        
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name, email)) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));                                    
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else if(a.validate(email)){
            // ADD THE NEW TA TO THE DATA
        
            jTPS_Transaction tt=new addTATr(name,email,data);
            j.addTransaction(tt);
            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");
            
            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();
            
            // WE'VE CHANGED STUFF
            markWorkAsEdited();
        }
        else{
              AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(INVALID_EMAIL), props.getProperty(INVALID_EMAIL)); 
        }
       }
       Collections.sort(data.getTeachingAssistants());
    }
    
    public void handleChangeStartDate(Date oldDate, Date newDate){
        Data data = (Data)app.getDataComponent();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        
        Calendar oldCal = Calendar.getInstance();
        oldCal.setTime(oldDate);  
        
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(newDate);
        
        if(newDate.compareTo(java.sql.Date.valueOf(workspace.getEndingFriday().getValue())) > 0){
            System.out.println("PLACEHOLDER\nStarting date must be before ending date!");
            if(newCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
                System.out.println("PLACEHOLDER\nStarting date must be a Monday!");
            }
            workspace.getStartingMonday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
        }
        else if(newCal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
            System.out.println("PLACEHOLDER\nStarting date must be a Monday!");
            workspace.getStartingMonday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
        }
        else{
            data.setStartingMonday(newDate);
        }
    }
    
    public void handleChangeEndDate(Date oldDate, Date newDate){
        Data data = (Data)app.getDataComponent();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        
        Calendar oldCal = Calendar.getInstance();
        oldCal.setTime(oldDate);  
        
        Calendar newCal = Calendar.getInstance();
        newCal.setTime(newDate);
        
        if(newDate.compareTo(java.sql.Date.valueOf(workspace.getStartingMonday().getValue())) < 0){
            System.out.println("PLACEHOLDER\nEnding date must be after starting date!");
            if(newCal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY){
                System.out.println("PLACEHOLDER\nEnding date must be a Friday!");
            }
            workspace.getEndingFriday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
        }
        else if(newCal.get(Calendar.DAY_OF_WEEK) != Calendar.FRIDAY){
            System.out.println("PLACEHOLDER\nEnding date must be a Friday!");
            workspace.getEndingFriday().setValue(new java.sql.Date(oldDate.getTime()).toLocalDate());
        }
        else{
            data.setEndingFriday(newDate);
        }
    }
    
    /**
     * This function provides a response for when the user presses a
     * keyboard key. Note that we're only responding to Delete, to remove
     * a TA.
     * 
     * @param code The keyboard code pressed.
     */
    public void checkselected(){
       PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
            TableView taTable = workspace.getTATable();
         Object selectedItem = taTable.getSelectionModel().getSelectedItem(); 
            if (selectedItem != null) {
                 TeachingAssistant ta = (TeachingAssistant)selectedItem;
               
                workspace.getNameTextField().setText(ta.getName());
                oldName=ta.getName();
               workspace.getEmailTextField().setText(ta.getEmail());
            oldEmail=ta.getEmail();
                workspace.getAddButton().setText(props.getProperty(CourseSiteGeneratorProp.UPDATE_BUTTON_TEXT.toString()));
            }
   
    }
    
    public void checkTeamSelected(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            Team team = (Team)selectedItem;
            workspace.getTeamName().setText(team.getName());
            workspace.getTeamLink().setText(team.getLink());
            workspace.getTeamColor().setValue(hexToRGB(team.getColor()));
            workspace.getTeamTextColor().setValue(hexToRGB(team.getTextColor()));
            workspace.getAddUpdateTeamButton().setText(props.getProperty(CourseSiteGeneratorProp.UPDATE_TEXT));
        }
    }
    public Color hexToRGB(String hex){
        int r = Integer.valueOf(hex.substring(0,2),16);
        int g = Integer.valueOf(hex.substring(2,4),16);
        int b = Integer.valueOf(hex.substring(4,6),16);
        Color color = Color.rgb(r, g, b);
        return color;
    }
    
    public void checkStudentSelected(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            Student student = (Student)selectedItem;
            workspace.getStudentFirstName().setText(student.getFirstName());
            workspace.getStudentLastName().setText(student.getLastName());
            workspace.getStudentRole().setText(student.getRole());
            
            ObservableList<Team> teams = ((Data)app.getDataComponent()).getTeams();
            String teamName = student.getTeam();
            for(int i = 0; i < teams.size(); i++){
                Team team = teams.get(i);
                if(teamName.equals(team.getName())){
                    workspace.getTeamComboBox().setValue(team);
                }
            }
            workspace.getAddUpdateStudentButton().setText(props.getProperty(CourseSiteGeneratorProp.UPDATE_TEXT));
        }
    }
    public void checkScheduleItemSelected(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView scheduleItemTable = workspace.getScheduleItemsTable();
        Object selectedItem = scheduleItemTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            ScheduledItem item = (ScheduledItem)selectedItem;
            workspace.getScheduleItemType().setValue(item.getType());
            
            String dateString = Integer.toString(item.getMonth()) + "/" + Integer.toString(item.getDay()) + "/" + Integer.toString(item.getYear());
            
            Date date = new Date();
            try {
                DatePicker datePicker = workspace.getScheduleItemDate();
                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                date = df.parse(dateString);
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                datePicker.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)));
            } catch (java.text.ParseException pe) {
                pe.printStackTrace();
            }
            
            workspace.getScheduleItemTime().setText(item.getTime());
            workspace.getScheduleItemTitle().setText(item.getTitle());
            workspace.getScheduleItemTopic().setText(item.getTopic());
            workspace.getScheduleItemLink().setText(item.getLink());
            workspace.getScheduleItemCriteria().setText(item.getCriteria());
            workspace.getAddUpdateScheduleItemButton().setText(props.getProperty(CourseSiteGeneratorProp.UPDATE_TEXT));
        }
    }
    public void checkRecitationSelected(){
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView recitationTable = workspace.getRecitationTable();
        Object selectedItem = recitationTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            Recitation rec = (Recitation)selectedItem;
            workspace.getRecSection().setText(rec.getSection());
            workspace.getRecInstructor().setText(rec.getInstructor());
            workspace.getRecDayTime().setText(rec.getDayTime());
            workspace.getRecLocation().setText(rec.getLocation());
            
            ObservableList<TeachingAssistant> tas = ((Data)app.getDataComponent()).getTeachingAssistants();
            String ta1Name = rec.getSupervisingTA_1();
            String ta2Name = rec.getSupervisingTA_2();
            for(int i = 0; i < tas.size(); i++){
                TeachingAssistant ta = tas.get(i);
                if(ta1Name.equals(ta.getName())){
                    workspace.getRecSupervisingTA1().setValue(ta);
                }
                if(ta2Name.equals(ta.getName())){
                    workspace.getRecSupervisingTA2().setValue(ta);
                }
            }
        }
    }
    
    public void handleDeleteTeam(){
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView teamTable = workspace.getTeamTable();
        Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
        if(selectedItem != null){
            jTPS_Transaction delete = new deleteTeam_Transaction(app, this, selectedItem, workspace);
            j.addTransaction(delete);
        }
    }
    public void handleDeleteStudent(){
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TableView studentTable = workspace.getStudentTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            jTPS_Transaction delete = new deleteStudent_Transaction(app, this, selectedItem, workspace);
            j.addTransaction(delete);
        }
    }
    public void handleAddUpdateScheduledItem(){
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        ComboBox itemType = workspace.getScheduleItemType();
        DatePicker itemDate = workspace.getScheduleItemDate();
        TextField itemTime = workspace.getScheduleItemTime();
        TextField itemTitle = workspace.getScheduleItemTitle();
        TextField itemTopic = workspace.getScheduleItemTopic();
        TextField itemLink = workspace.getScheduleItemLink();
        TextField itemCriteria = workspace.getScheduleItemCriteria();
        
        if(itemType.getSelectionModel().getSelectedItem() == null || itemDate.getValue() == null || itemTitle.getText().equals("")){
            System.out.println("PLACEHOLDER\nPlease specifiy a type, date, time, and title of the scheduled item.");
            return;
        }
        
        String dateOfPicker = itemDate.getValue().getDayOfMonth()+"/"+itemDate.getValue().getMonth().getValue()+"/"+itemDate.getValue().getYear();
        
        TableView itemTable = workspace.getScheduleItemsTable();
        Data data = (Data) app.getDataComponent();
        boolean validItem = true;
        
        ObservableList<ScheduledItem> items = data.getScheduledItems();
        for (int i = 0; i < items.size(); i++) {
            ScheduledItem item = items.get(i);
            String dateOfItem = item.getMonth()+"/"+item.getDay()+"/"+item.getYear();
            if(dateOfItem.equals(dateOfPicker) && item.getTitle().equals(itemTitle.getText()))  
                validItem = false;
        }
        
        /* ADD NEW TEAM */
        if(workspace.getAddUpdateScheduleItemButton().getText().equals(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()))){
            if (validItem) {

                jTPS_Transaction add = new addEditScheduleItem_Transaction(app, this, workspace, itemType.getSelectionModel().getSelectedItem().toString(), ""+itemDate.getValue().getMonth().getValue(), ""+itemDate.getValue().getDayOfMonth(), ""+itemDate.getValue().getYear(), itemTime.getText(), itemTitle.getText(), itemTopic.getText(), itemLink.getText(), itemCriteria.getText(), null);
                j.addTransaction(add);
                workspace.getScheduleItemType().getSelectionModel().clearSelection();
                workspace.getScheduleItemDate().setValue(null);
                workspace.getScheduleItemTime().setText("");
                workspace.getScheduleItemTitle().setText("");
                workspace.getScheduleItemTopic().setText("");
                workspace.getScheduleItemLink().setText("");
                workspace.getScheduleItemCriteria().setText("");
                workspace.getScheduleItemsTable().getSelectionModel().clearSelection();
            }
            else{
//                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
//                dialog.show(props.getProperty(TEAM_NAME_NOT_UNIQUE_TITLE), props.getProperty(TEAM_NAME_NOT_UNIQUE_MESSAGE));
                  System.out.println("PLACEHOLDER\nScheduled item date and title must be unique!");
            }
        }
        /* UPDATE EXISTING TEAM */
        else {
            if (validItem) {
                Object selectedItem = itemTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    ScheduledItem item = (ScheduledItem) selectedItem;

                    jTPS_Transaction add = new addEditScheduleItem_Transaction(app, this, workspace, itemType.getSelectionModel().getSelectedItem().toString(), ""+itemDate.getValue().getMonth().getValue(), ""+itemDate.getValue().getDayOfMonth(), ""+itemDate.getValue().getYear(), itemTime.getText(), itemTitle.getText(), itemTopic.getText(), itemLink.getText(), itemCriteria.getText(), item);
                    j.addTransaction(add);
                    workspace.getScheduleItemType().getSelectionModel().clearSelection();
                    workspace.getScheduleItemDate().setValue(null);
                    workspace.getScheduleItemTime().setText("");
                    workspace.getScheduleItemTitle().setText("");
                    workspace.getScheduleItemTopic().setText("");
                    workspace.getScheduleItemLink().setText("");
                    workspace.getScheduleItemCriteria().setText("");
                    workspace.getScheduleItemsTable().getSelectionModel().clearSelection();
                    workspace.getAddUpdateScheduleItemButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
                }
            } else {
//                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
//                dialog.show(props.getProperty(TEAM_NAME_NOT_UNIQUE_TITLE), props.getProperty(TEAM_NAME_NOT_UNIQUE_MESSAGE));
                  System.out.println("PLACEHOLDER\nScheduled item date and title must be unique!");
            }
        }
    }
    public void handleAddUpdateTeam() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TextField nameTextField = workspace.getTeamName();
        ColorPicker color = workspace.getTeamColor();
        ColorPicker textColor = workspace.getTeamTextColor();
        TextField link = workspace.getTeamLink();
        String hexColor = String.format("%02x%02x%02x", (int)(color.getValue().getRed()*255), (int)(color.getValue().getGreen()*255), (int)(color.getValue().getBlue()*255));
        String hexTextColor = String.format("%02x%02x%02x", (int)(textColor.getValue().getRed()*255), (int)(textColor.getValue().getGreen()*255), (int)(textColor.getValue().getBlue()*255));
        
        if(nameTextField.getText().equals("")){
            System.out.println("PLACEHOLDER\nTeam name must be specified!");
            return;
        }
        
        TableView teamTable = workspace.getTeamTable();
        Data data = (Data) app.getDataComponent();
        boolean validTeam = true;
        
        ObservableList<Team> teams = data.getTeams();
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            if (team.getName().equalsIgnoreCase(nameTextField.getText())) {
                Team selectedTeam = (Team)teamTable.getSelectionModel().getSelectedItem();
                if( !(selectedTeam.getName().equalsIgnoreCase(nameTextField.getText())) )
                    validTeam = false;
            }
        }
        
        /* ADD NEW TEAM */
        if(workspace.getAddUpdateTeamButton().getText().equals(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()))){
            if(validTeam){
                
                jTPS_Transaction add = new addEditTeam_Transaction(app, this, workspace, nameTextField.getText(), hexColor, hexTextColor, link.getText(), null);
                j.addTransaction(add);
                workspace.getTeamName().setText("");
                Color white = Color.rgb(255, 255, 255);
                workspace.getTeamColor().setValue(white);
                workspace.getTeamTextColor().setValue(white);
                workspace.getTeamLink().setText("");
             

            }
            else{
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TEAM_NAME_NOT_UNIQUE_TITLE), props.getProperty(TEAM_NAME_NOT_UNIQUE_MESSAGE));
            }
        }
        /* UPDATE EXISTING TEAM */
        else {
            if (validTeam) {
                Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    Team team = (Team) selectedItem;
                    System.out.println(team.getName());
                    jTPS_Transaction add = new addEditTeam_Transaction(app, this, workspace, nameTextField.getText(), hexColor, hexTextColor, link.getText(), team);
                    j.addTransaction(add);
                    workspace.getTeamName().setText("");
                    Color white = Color.rgb(255, 255, 255);
                    workspace.getTeamColor().setValue(white);
                    workspace.getTeamTextColor().setValue(white);
                    workspace.getTeamLink().setText("");
                    workspace.getTeamTable().getSelectionModel().clearSelection();
                    workspace.getAddUpdateTeamButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
                }
            } else {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TEAM_NAME_NOT_UNIQUE_TITLE), props.getProperty(TEAM_NAME_NOT_UNIQUE_MESSAGE));
            }
        }
    }
    
    public void handleAddUpdateStudent() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
        TextField firstNameTextField = workspace.getStudentFirstName();
        TextField lastNameTextField = workspace.getStudentLastName();
        ComboBox teamComboBox = workspace.getTeamComboBox();
        TextField roleTextField = workspace.getStudentRole();
        TableView studentTable = workspace.getStudentTable();
        
        if(firstNameTextField.getText().equals("") || lastNameTextField.getText().equals("")){
            System.out.println("PLACEHOLDER\nYou must specify a student first name and last name!");
            return;
        }
        
        Data data = (Data) app.getDataComponent();
        boolean validStudent = true;
        
        ObservableList<Student> students = data.getStudents();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            if (student.getFirstName().equalsIgnoreCase(firstNameTextField.getText()) && student.getLastName().equalsIgnoreCase(lastNameTextField.getText())) {
                Student selectedStudent = (Student)studentTable.getSelectionModel().getSelectedItem();
                if( !(selectedStudent.getFirstName().equalsIgnoreCase(firstNameTextField.getText()) && selectedStudent.getLastName().equalsIgnoreCase(lastNameTextField.getText())) )
                    validStudent = false;
            }
        }
        
        /* ADD NEW STUDENT */
        if(workspace.getAddUpdateStudentButton().getText().equals(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()))){
            if(validStudent){
                jTPS_Transaction add = new addEditStudent_Transaction(app, this, workspace, firstNameTextField.getText(), lastNameTextField.getText(), teamComboBox.getSelectionModel().getSelectedItem(), roleTextField.getText(), null);
                j.addTransaction(add);
                workspace.getStudentFirstName().setText("");
                workspace.getStudentLastName().setText("");
                workspace.getTeamComboBox().getSelectionModel().clearSelection();
                workspace.getStudentRole().setText("");
             

            }
            else{
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(STUDENT_NAME_NOT_UNIQUE_TITLE), props.getProperty(STUDENT_NAME_NOT_UNIQUE_MESSAGE));
            }
        }
        
        /* UPDATE EXISTING STUDENT */
        else {
            if (validStudent) {
                Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    Student student = (Student) selectedItem;
                    jTPS_Transaction add = new addEditStudent_Transaction(app, this, workspace, firstNameTextField.getText(), lastNameTextField.getText(), teamComboBox.getSelectionModel().getSelectedItem(), roleTextField.getText(), student);
                    j.addTransaction(add);
                    workspace.getStudentFirstName().setText("");
                    workspace.getStudentLastName().setText("");
                    workspace.getTeamComboBox().getSelectionModel().clearSelection();
                    workspace.getStudentRole().setText("");
                    workspace.getTeamTable().getSelectionModel().clearSelection();
                    workspace.getAddUpdateStudentButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
                }
            } else {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(STUDENT_NAME_NOT_UNIQUE_TITLE), props.getProperty(STUDENT_NAME_NOT_UNIQUE_MESSAGE));
            }
        }
    }
    
    public void undoTransaction() {
        j.undoTransaction();
    }

    public void redoTransaction() {
        j.doTransaction();
    }

    public void handleKeyPress(KeyEvent codee, KeyCode code) {
        // DID THE USER PRESS THE DELETE KEY?
        if (code == KeyCode.DELETE) {
            TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
            TableView taTable = workspace.getTATable();

            // IS A TA SELECTED IN THE TABLE?
            Object selectedItem = taTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                jTPS_Transaction delete = new deleteTA(app, this, selectedItem, workspace);
                j.addTransaction(delete);
            }
        } else if (code == KeyCode.Z && codee.isControlDown()) {
            j.undoTransaction();
        } else if (code == KeyCode.Y && codee.isControlDown()) {
            j.doTransaction();
        }

    }
    
    public void handleWorkspaceKeyPress(KeyEvent event, KeyCode code){
        if (code == KeyCode.Z && event.isControlDown()) {
            j.undoTransaction();
        } else if (code == KeyCode.Y && event.isControlDown()) {
            j.doTransaction();
        }
    }
    
    public void handleKeyPressTeamTable(KeyEvent event, KeyCode code){
        if (code == KeyCode.DELETE) {
            TAWorkspace workspace = (TAWorkspace) app.getWorkspaceComponent();
            TableView teamTable = workspace.getTeamTable();

            // IS A TA SELECTED IN THE TABLE?
            Object selectedItem = teamTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                jTPS_Transaction delete = new deleteTeam_Transaction(app, this, selectedItem, workspace);
                j.addTransaction(delete);
            }
        } else if (code == KeyCode.Z && event.isControlDown()) {
            j.undoTransaction();
        } else if (code == KeyCode.Y && event.isControlDown()) {
            j.doTransaction();
        }
    }
    
    public jTPS getJ() {
        return j;
    }
    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     * 
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        TableView taTable = workspace.getTATable();
        
        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // GET THE TA
            TeachingAssistant ta = (TeachingAssistant)selectedItem;
            String taName = ta.getName();
            Data data = (Data)app.getDataComponent();
            String cellKey = pane.getId();
            
            // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
             jTPS_Transaction tt=new addtoGrid(cellKey,taName,data);
            j.addTransaction(tt);
            
            // WE'VE CHANGED STUFF
            markWorkAsEdited();
        }
    }
    
    void handleGridCellMouseExited(Pane pane) {
        String cellKey = pane.getId();
        Data data = (Data)app.getDataComponent();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();

        Pane mousedOverPane = workspace.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);

        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspace.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // THE MOUSED OVER ROW HEADERS
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        
        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }
    }

    void handleGridCellMouseEntered(Pane pane) {
        String cellKey = pane.getId();
        Data data = (Data)app.getDataComponent();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TAWorkspace workspace = (TAWorkspace)app.getWorkspaceComponent();
        
        // THE MOUSED OVER PANE
        Pane mousedOverPane = workspace.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_CELL);
        
        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = workspace.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        
        // THE MOUSED OVER ROW HEADERS
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = workspace.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        
        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = workspace.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }
    }
}