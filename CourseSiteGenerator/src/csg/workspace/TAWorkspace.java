package csg.workspace;

import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import static djf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static djf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import static djf.settings.AppPropertyType.TIME_CONF;
import static djf.settings.AppPropertyType.TIME_CONF_TITLE;
import static djf.settings.AppPropertyType.TIME_EXIST;
import static djf.settings.AppPropertyType.TIME_EXIST_TITLE;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javafx.beans.property.StringProperty;
import csg.CourseSiteGeneratorApp;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;
import csg.CourseSiteGeneratorProp;
import static csg.CourseSiteGeneratorProp.MISSING_TA_EMAIL_MESSAGE;
import static csg.CourseSiteGeneratorProp.MISSING_TA_EMAIL_TITLE;
import static csg.CourseSiteGeneratorProp.MISSING_TA_NAME_MESSAGE;
import static csg.CourseSiteGeneratorProp.MISSING_TA_NAME_TITLE;
import csg.style.TAStyle;
import csg.data.Data;
import csg.data.TeachingAssistant;
import static djf.settings.AppPropertyType.ABOUT_ICON;
import static djf.settings.AppPropertyType.ABOUT_TOOLTIP;
import static djf.settings.AppPropertyType.REDO_ICON;
import static djf.settings.AppPropertyType.REDO_TOOLTIP;
import static djf.settings.AppPropertyType.UNDO_ICON;
import static djf.settings.AppPropertyType.UNDO_TOOLTIP;
import static djf.settings.AppStartupConstants.FILE_PROTOCOL;
import static djf.settings.AppStartupConstants.PATH_IMAGES;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.Tab;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.FlowPane;
import java.lang.Boolean;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.beans.value.ObservableValue;
import csg.data.*;
import static djf.settings.AppStartupConstants.PATH_WORK;
import djf.ui.AppGUI;
import java.io.File;
import java.util.Date;
import java.time.LocalDate;
import javafx.util.StringConverter;
import java.time.format.DateTimeFormatter;
import javafx.scene.paint.Color;
import java.util.Calendar;
import javafx.beans.value.ChangeListener;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import java.util.Collection;


/**
 * This class serves as the workspace component for the TA Manager application.
 * It provides all the user interface controls in the workspace area.
 *
 * @author Richard McKenna
 */
public class TAWorkspace extends AppWorkspaceComponent {

    // THIS PROVIDES US WITH ACCESS TO THE APP COMPONENTS
    CourseSiteGeneratorApp app;

    // THIS PROVIDES RESPONSES TO INTERACTIONS WITH THIS WORKSPACE
    TAController controller;

    // NOTE THAT EVERY CONTROL IS PUT IN A BOX TO HELP WITH ALIGNMENT
    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;

    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    TableColumn<TeachingAssistant, Boolean> ugColumn;

    // THE TA INPUT
    HBox addBox;
    TextField nameTextField;
    TextField emailTextField;
    Button addButton;
    Button clearButton;
    Button undoButton;
    Button redoButton;
    Button aboutButton;
    FlowPane rightToolbar;
    

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;

    ComboBox a;
    ComboBox c;
    // THE OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;
    
    boolean fullyLoaded = false;
    
    ComboBox subjectComboBox;
    ComboBox semesterComboBox;
    ComboBox numberComboBox;
    ComboBox yearComboBox;
    TextField courseTitle = new TextField();
    TextField instructorHome = new TextField();
    TextField instructorName = new TextField();;
    Label exportDir = new Label();
    
    Label siteTemplateDir = new Label();
    
    ImageView bannerSchool = new ImageView();
    ImageView rightFooter = new ImageView();
    ImageView leftFooter = new ImageView();
    ComboBox stylesheetComboBox;
    
    DatePicker startingMonday = new DatePicker();
    DatePicker endingFriday = new DatePicker();
    
    TableView teamTable;
    TextField teamName;
    TextField teamLink;
    ColorPicker teamColor;
    ColorPicker teamTextColor;
    
    TableView studentTable;
    TextField studentFirstName;
    TextField studentLastName;
    ComboBox teamComboBox;
    TextField studentRole;
    
    TableView scheduleItemsTable;
    ComboBox scheduleItemType;
    DatePicker scheduleItemDate;
    TextField scheduleItemTime;
    TextField scheduleItemTitle;
    TextField scheduleItemTopic;
    TextField scheduleItemLink;
    TextField scheduleItemCriteria;
    
    TableView recitationTable;
    TextField recSection;
    TextField recInstructor;
    TextField recDayTime;
    TextField recLocation;
    ComboBox recSupervisingTA1;
    ComboBox recSupervisingTA2;
    
    Button deleteTeamButton;
    Button deleteStudentButton;
    Button deleteScheduleItemButton;
    
    Button addUpdateTeamButton;
    Button addUpdateStudentButton;
    Button addUpdateScheduleItemButton;
    Button addUpdateRecitationButton;
    
    Button changeCourseInfoButton;
    Button selectTemplateButton;
    
    /**
     * The constructor initializes the user interface, except for the full
     * office hours grid, since it doesn't yet know what the hours will be until
     * a file is loaded or a new one is created.
     */
    public TAWorkspace(CourseSiteGeneratorApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;
        
        BorderPane toolbarPane = app.getGUI().getHeaderPane();
        rightToolbar = new FlowPane();
        rightToolbar.setAlignment(Pos.CENTER_RIGHT);
        undoButton = initChildButton(rightToolbar,	UNDO_ICON.toString(),	    UNDO_TOOLTIP.toString(),	false);
        redoButton = initChildButton(rightToolbar,	REDO_ICON.toString(),	    REDO_TOOLTIP.toString(),	false);
        aboutButton = initChildButton(rightToolbar,	ABOUT_ICON.toString(),	    ABOUT_TOOLTIP.toString(),	false);
        toolbarPane.setRight(rightToolbar);
        
        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox();
        String tasHeaderText = props.getProperty(CourseSiteGeneratorProp.TAS_HEADER_TEXT.toString());
        tasHeaderLabel = new Label(tasHeaderText);
        Button deleteTAButton = new Button(props.getProperty(CourseSiteGeneratorProp.DELETE_TEXT.toString()));
        tasHeaderBox.setAlignment(Pos.CENTER_LEFT);
        tasHeaderBox.getChildren().addAll(tasHeaderLabel, deleteTAButton);
        

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Data data = (Data) app.getDataComponent();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setEditable(true);
        taTable.setItems(tableData);
        String ugColumnText = props.getProperty(CourseSiteGeneratorProp.UG_COLUMN_TEXT.toString());
        String nameColumnText = props.getProperty(CourseSiteGeneratorProp.NAME_COLUMN_TEXT.toString());
        String emailColumnText = props.getProperty(CourseSiteGeneratorProp.EMAIL_COLUMN_TEXT.toString());
        ugColumn = new TableColumn(ugColumnText);
        nameColumn = new TableColumn(nameColumnText);
        emailColumn = new TableColumn(emailColumnText);
        ugColumn.setCellValueFactory(
                new Callback<CellDataFeatures<TeachingAssistant, Boolean>, ObservableValue<Boolean>>() {
            
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<TeachingAssistant, Boolean> param) {
                return param.getValue().getUndergrad();
            }
        });
        ugColumn.setCellFactory( CheckBoxTableCell.forTableColumn(ugColumn) );
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        ugColumn.setMinWidth(110);
        nameColumn.setMinWidth(210);
        emailColumn.setMinWidth(310);
        taTable.getColumns().add(ugColumn);
        taTable.getColumns().add(nameColumn);
        taTable.getColumns().add(emailColumn);

        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(CourseSiteGeneratorProp.NAME_PROMPT_TEXT.toString());
        String emailPromptText = props.getProperty(CourseSiteGeneratorProp.EMAIL_PROMPT_TEXT.toString());
        String addButtonText = props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString());
        String clearButtonText = props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON_TEXT.toString());
        nameTextField = new TextField();
        emailTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        emailTextField.setPromptText(emailPromptText);
        addButton = new Button(addButtonText);
        clearButton = new Button(clearButtonText);
        clearButton.setStyle("-fx-font-size: 12pt;\n" +
                             "-fx-font-weight: bold;\n" +
                             "-fx-border-radius: 10 10 10 10;\n" +
                             "-fx-background-radius: 10 10 10 10;");
        
        addBox = new HBox();
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));

        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        officeHoursHeaderBox.setAlignment(Pos.CENTER_LEFT);
        String officeHoursGridText = props.getProperty(CourseSiteGeneratorProp.OFFICE_HOURS_SUBHEADER.toString());
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        a = new ComboBox();
        int h = 1;
        int m = 0;
        String option = "";
        a.getItems().addAll("12:00AM");
        for (int i = 0; i < 11; i++) {
            option = h + ":00AM";
            h++;
            a.getItems().add(option);
        }
        a.getItems().addAll("12:00PM");
        h = 1;
        m = 0;
        for (int j = 0; j < 11; j++) {
            option = h + ":00PM";
            h++;
            a.getItems().add(option);
        }
        m = 0;
        h = 1;
        c = new ComboBox();
        c.getItems().addAll("12:00AM");
        for (int i = 0; i < 11; i++) {
            option = h + ":00AM";
            h++;
            c.getItems().add(option);
        }
        c.getItems().addAll("12:00PM");
        h = 1;
        m = 0;
        for (int j = 0; j < 11; j++) {
            option = h + ":00PM";
            h++;
            c.getItems().add(option);
        }
        Label start = new Label(officeHoursGridText);
        Label end = new Label(officeHoursGridText);
        
        ArrayList<String> comboBoxLabels = props.getPropertyOptionsList(CourseSiteGeneratorProp.OFFICE_HOURS_TABLE_HEADERS);
        end.setText(comboBoxLabels.get(1)+": ");
        end.setStyle(" -fx-font-weight:bold;\n"
                + "    -fx-font-size:12pt;  \n"
                + "    -fx-padding: 5 10 5 10;");
        start.setText("\t "+comboBoxLabels.get(0)+": ");
        start.setStyle(" -fx-font-weight:bold;\n"
                + "    -fx-font-size:12pt;  \n"
                + "    -fx-padding: 5 10 5 10;");
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);
        officeHoursHeaderBox.getChildren().add(start);
        officeHoursHeaderBox.getChildren().add(a);
        officeHoursHeaderBox.getChildren().add(end);
        officeHoursHeaderBox.getChildren().add(c);
        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        taTable.setPadding(new Insets(0, 0, 10, 10));
        taTable.setStyle("-fx-background-color: #FFD8AD;");
        leftPane.getChildren().add(tasHeaderBox);
        leftPane.getChildren().add(taTable);
        leftPane.getChildren().add(addBox);
        addBox.setStyle("-fx-background-color: #FFD8AD;");
        VBox rightPane = new VBox();

        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);
        
        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        ScrollPane sp = new ScrollPane(rightPane);
        sp.setStyle("-fx-background: #FFD8AD;");
        SplitPane sPane = new SplitPane(leftPane, sp);
        sPane.setDividerPositions(0.45);
        workspace = new BorderPane();

        // CREATE TABBED INTERFACE
        
        VBox courseDetailsPane = new VBox(10);
        courseDetailsPane.setPadding(new Insets(10, 10, 10, 10));
        
        /***** COURSE DETAILS : COURSE INFO *****/
        GridPane courseInfoPane = new GridPane();
        courseInfoPane.setHgap(10);
        courseInfoPane.setVgap(10);
        courseInfoPane.setPadding(new Insets(10, 10, 10, 10));
        Label courseInfoTitle = new Label(props.getProperty(CourseSiteGeneratorProp.COURSE_INFO_TEXT.toString()));
        courseInfoTitle.getStyleClass().add("section-subheader");
        
        courseInfoPane.add(courseInfoTitle, 0, 0, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.SUBJECT_TEXT.toString())+":"), 0, 1, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.SEMESTER_TEXT.toString())+":"), 0, 2, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TITLE_TEXT.toString())+":"), 0, 3, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.INSTRUCTOR_NAME_TEXT.toString())+":"), 0, 4, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.INSTRUCTOR_HOME_TEXT.toString())+":"), 0, 5, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.EXPORT_DIR_TEXT.toString())+":"), 0, 6, 1, 1);
        courseInfoPane.add(exportDir, 1, 6, 1, 1);
        
        ArrayList<String> departments = props.getPropertyOptionsList(CourseSiteGeneratorProp.COURSE_OPTIONS);
        ObservableList subjectList = FXCollections.observableArrayList(departments);
        subjectComboBox = new ComboBox(subjectList);
        subjectComboBox.setPrefWidth(80);
        subjectComboBox.getSelectionModel().selectFirst();
        courseInfoPane.add(subjectComboBox, 1, 1, 2, 1);
        
        
        ArrayList<String> semesterList = props.getPropertyOptionsList(CourseSiteGeneratorProp.SEMESTER_LIST);
        ObservableList<String> oSemesterList = FXCollections.observableArrayList(semesterList);
        semesterComboBox = new ComboBox(oSemesterList);
        semesterComboBox.setPrefWidth(80);
        semesterComboBox.getSelectionModel().selectFirst();
        courseInfoPane.add(semesterComboBox, 1, 2, 2, 1);
        
        courseTitle.textProperty().addListener((ov, oldValue, newValue) ->{
            data.setTitle(newValue);
            AppGUI gui = app.getGUI();
            gui.getFileController().markAsEdited(gui);
        });
        instructorName.textProperty().addListener((ov, oldValue, newValue) ->{
            data.setInstructorName(newValue);
            AppGUI gui = app.getGUI();
            gui.getFileController().markAsEdited(gui);
        });
        instructorHome.textProperty().addListener((ov, oldValue, newValue) ->{
            data.setInstructorHome(newValue);
            AppGUI gui = app.getGUI();
            gui.getFileController().markAsEdited(gui);
        });
        
        courseInfoPane.add(courseTitle, 1, 3, 5, 1);
        courseInfoPane.add(instructorName, 1, 4, 5, 1);
        courseInfoPane.add(instructorHome, 1, 5, 5, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.NUMBER_TEXT.toString())+":"), 3, 1, 1, 1);
        courseInfoPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.YEAR_TEXT.toString())+":"), 3, 2, 1, 1);
        
        ArrayList<String> numList = new ArrayList<>();
        for(int i = 100; i <= 499; i++){
            numList.add(Integer.toString(i));
        }
        
        numberComboBox = new ComboBox(FXCollections.observableArrayList(numList));
        numberComboBox.setPrefWidth(80);
        numberComboBox.getSelectionModel().selectFirst();
        courseInfoPane.add(numberComboBox, 5, 1, 1, 1);
        
        ArrayList yearArrayList = new ArrayList();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = year-1; i <year+9; i++){
            yearArrayList.add(Integer.toString(i));
        }
        
        ObservableList yearList = FXCollections.observableArrayList(yearArrayList);
        yearComboBox = new ComboBox(yearList);
        yearComboBox.setPrefWidth(80);
        yearComboBox.getSelectionModel().selectFirst();
        courseInfoPane.add(yearComboBox, 5, 2, 1, 1);
        
        changeCourseInfoButton = new Button(props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON_TEXT.toString()));
        changeCourseInfoButton.setPrefWidth(80);
        courseInfoPane.add(changeCourseInfoButton, 5, 6, 1, 1);
        courseInfoPane.setStyle("-fx-background-color: #EBEBEB");
        
        /***** COURSE DETAILS : SITE TEMPLATE *****/
        VBox siteTemplatePane = new VBox(15);
        Label siteTemplateTitle = new Label(props.getProperty(CourseSiteGeneratorProp.SITE_TEMPLATE_TEXT.toString()));
        siteTemplateTitle.getStyleClass().add("section-subheader");
        
        siteTemplatePane.getChildren().add(siteTemplateTitle);
        siteTemplatePane.getChildren().add(new Label(props.getProperty(CourseSiteGeneratorProp.SITE_TEMPLATE_DISC_TEXT.toString())));
        siteTemplatePane.getChildren().add(siteTemplateDir);
        
        selectTemplateButton = new Button(props.getProperty(CourseSiteGeneratorProp.SELECT_TEMPLATE_BUTTON_TEXT.toString()));
        siteTemplatePane.getChildren().add(selectTemplateButton);
        
        siteTemplatePane.getChildren().add(new Label(props.getProperty(CourseSiteGeneratorProp.SITE_PAGES_TEXT.toString())+":"));
        
        TableView sitePagesTable = new TableView();
        ObservableList<SitePage> sitePageData = data.getSitePages();
        sitePagesTable.setEditable(true);
        sitePagesTable.setItems(sitePageData);
        TableColumn useColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.USE_COLUMN_TEXT.toString()));
        TableColumn navbarTitleColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.NAVBAR_COLUMN_TEXT.toString()));
        TableColumn fileNameColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.FILENAME_COLUMN_TEXT.toString()));
        TableColumn scriptColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.SCRIPT_COLUMN_TEXT.toString()));
        useColumn.setCellValueFactory(
                new Callback<CellDataFeatures<SitePage, Boolean>, ObservableValue<Boolean>>() {
            
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<SitePage, Boolean> param) {
                return param.getValue().isUse();
            }
        });
        useColumn.setCellFactory( CheckBoxTableCell.forTableColumn(useColumn) );
        navbarTitleColumn.setCellValueFactory(
                new PropertyValueFactory<SitePage, String>("navbarTitle")
        );
        fileNameColumn.setCellValueFactory(
                new PropertyValueFactory<SitePage, String>("fileName")
        );
        scriptColumn.setCellValueFactory(
                new PropertyValueFactory<SitePage, String>("script")
        );
        sitePagesTable.getColumns().addAll(useColumn, navbarTitleColumn, fileNameColumn, scriptColumn);
        sitePagesTable.setMaxWidth(550);
        sitePagesTable.setPrefHeight(200);
        siteTemplatePane.getChildren().add(sitePagesTable);
        
        siteTemplatePane.setStyle("-fx-background-color: #EBEBEB");
        siteTemplatePane.setPadding(new Insets(10, 10, 10, 10));
        
        /***** COURSE DETAILS : PAGE STYLE *****/
        VBox pageStylePane = new VBox(10);
        GridPane pageStyleGridPane = new GridPane();
        pageStyleGridPane.setHgap(10);
        pageStyleGridPane.setVgap(10);
        Label pageStyleTitle = new Label(props.getProperty(CourseSiteGeneratorProp.PAGE_STYLE_TEXT.toString()));
        pageStyleTitle.getStyleClass().add("section-subheader");
        
        pageStyleGridPane.add(pageStyleTitle, 0, 0, 2, 1);
        pageStyleGridPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.BANNER_SCHOOL_IMAGE_TEXT.toString())+":"), 0, 1, 1, 1);
        pageStyleGridPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.LEFT_FOOTER_IMAGE_TEXT.toString())+":"), 0, 2, 1, 1);
        pageStyleGridPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.RIGHT_FOOTER_IMAGE_TEXT.toString())+":"), 0, 3, 1, 1);
        pageStyleGridPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.STYLESHEET_TEXT.toString())+":"), 0, 4, 1, 1);
        
        ObservableList stylesheetList = getCSSFiles();
        stylesheetComboBox = new ComboBox(stylesheetList);
        stylesheetComboBox.setPrefWidth(160);
        stylesheetComboBox.getSelectionModel().selectFirst();
        pageStyleGridPane.add(stylesheetComboBox, 1, 4, 1, 1);
        
        bannerSchool.setPreserveRatio(true);
        bannerSchool.setFitHeight(45);
        leftFooter.setPreserveRatio(true);
        leftFooter.setFitHeight(45);
        rightFooter.setPreserveRatio(true);
        rightFooter.setFitHeight(45);
        
        pageStyleGridPane.add(bannerSchool, 1, 1, 1, 1);
        pageStyleGridPane.add(leftFooter, 1, 2, 1, 1);
        pageStyleGridPane.add(rightFooter, 1, 3, 1, 1);
        
        Button changeBannerSchoolImageButton = new Button(props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON_TEXT.toString()));
        Button changeLeftFooterImageButton = new Button(props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON_TEXT.toString()));
        Button changeRightFooterImageButton = new Button(props.getProperty(CourseSiteGeneratorProp.CHANGE_BUTTON_TEXT.toString()));
        changeBannerSchoolImageButton.setPrefWidth(80);
        changeLeftFooterImageButton.setPrefWidth(80);
        changeRightFooterImageButton.setPrefWidth(80);
        
        pageStyleGridPane.add(changeBannerSchoolImageButton, 2, 1, 1, 1);
        pageStyleGridPane.add(changeLeftFooterImageButton, 2, 2, 1, 1);
        pageStyleGridPane.add(changeRightFooterImageButton, 2, 3, 1, 1);
        
        pageStylePane.getChildren().add(pageStyleGridPane);
        
        pageStylePane.getChildren().add(new Label(props.getProperty(CourseSiteGeneratorProp.STYLESHEET_DISC_TEXT.toString())));
        
        pageStylePane.setStyle("-fx-background-color: #EBEBEB");
        pageStylePane.setPadding(new Insets(10, 10, 10, 10));
        
        courseDetailsPane.getChildren().addAll(courseInfoPane, siteTemplatePane, pageStylePane);
        
        /******* RECITATION DATA *******/
        VBox recitationDataPane = new VBox(10);
        recitationDataPane.setPadding(new Insets(10, 10, 10, 10));
        
        HBox recitationDataHeader = new HBox(10);
        Button deleteRecitationButton = new Button(props.getProperty(CourseSiteGeneratorProp.DELETE_TEXT.toString()));
        Label recitationDataHeaderLabel = new Label(props.getProperty(CourseSiteGeneratorProp.RECITATIONS_TEXT.toString()));
        recitationDataHeaderLabel.getStyleClass().add("tab-header");
        recitationDataHeader.getChildren().addAll(recitationDataHeaderLabel, deleteRecitationButton);
        recitationDataHeader.setAlignment(Pos.CENTER_LEFT);
        
        recitationDataPane.getChildren().add(recitationDataHeader);
        TableColumn sectionColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.SECTION_TEXT.toString()));
        TableColumn instructorColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.INSTRUCTOR_TEXT.toString()));
        TableColumn dayTimeColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.DAYTIME_TEXT.toString()));
        TableColumn locationColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.LOCATION_TEXT.toString()));
        TableColumn TA1Column = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TA_TEXT.toString()));
        TableColumn TA2Column = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TA_TEXT.toString()));
        sectionColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("section")
        );
        instructorColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("instructor")
        );
        dayTimeColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("dayTime")
        );
        locationColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("location")
        );
        TA1Column.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("supervisingTA_1")
        );
        TA2Column.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("supervisingTA_2")
        );
        recitationTable = new TableView();
        ObservableList<Recitation> recitationData = data.getRecitations();
        recitationTable.setEditable(true);
        recitationTable.setItems(recitationData);
        recitationTable.getColumns().addAll(sectionColumn, instructorColumn, dayTimeColumn, locationColumn, TA1Column, TA2Column);
        
        recitationDataPane.getChildren().add(recitationTable);
        
        GridPane addEditPane = new GridPane();
        addEditPane.setHgap(10);
        addEditPane.setVgap(10);
        addEditPane.setPadding(new Insets(10, 10, 10, 10));
        Label addEditRecitationLabel = new Label(props.getProperty(CourseSiteGeneratorProp.ADDEDIT_TEXT.toString()));
        addEditRecitationLabel.getStyleClass().add("section-subheader");
        addEditPane.add(addEditRecitationLabel, 0, 0, 2, 1);
        addEditPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.SECTION_TEXT.toString())+":"), 0, 1, 1, 1);
        addEditPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.INSTRUCTOR_TEXT.toString())+":"), 0, 2, 1, 1);
        addEditPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.DAYTIME_TEXT.toString())+":"), 0, 3, 1, 1);
        addEditPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.LOCATION_TEXT.toString())+":"), 0, 4, 1, 1);
        addEditPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.SUPERVISING_TA_TEXT.toString())+":"), 0, 5, 1, 1);
        addEditPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.SUPERVISING_TA_TEXT.toString())+":"), 0, 6, 1, 1);
        
        recSection = new TextField();
        recInstructor = new TextField();
        recDayTime = new TextField();
        recLocation = new TextField();
        
        addEditPane.add(recSection, 1, 1, 10, 1);
        addEditPane.add(recInstructor, 1, 2, 10, 1);
        addEditPane.add(recDayTime, 1, 3, 10, 1);
        addEditPane.add(recLocation, 1, 4, 10, 1);
        recSupervisingTA1 = new ComboBox();
        recSupervisingTA1.setItems(data.getTeachingAssistants());
        recSupervisingTA2 = new ComboBox();
        recSupervisingTA2.setItems(data.getTeachingAssistants());
        recSupervisingTA1.setPrefWidth(200);
        recSupervisingTA2.setPrefWidth(200);
        addEditPane.add(recSupervisingTA1, 1, 5, 10, 1);
        addEditPane.add(recSupervisingTA2, 1, 6, 10, 1);
        
        addEditPane.setStyle("-fx-background-color: #EBEBEB");
        
        addUpdateRecitationButton = new Button(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()));
        Button clearRecitationButton = new Button(props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON_TEXT.toString()));
        addUpdateRecitationButton.setPrefWidth(110);
        clearRecitationButton.setPrefWidth(110);
        
        addEditPane.add(addUpdateRecitationButton, 0, 7, 1, 1);
        addEditPane.add(clearRecitationButton, 1, 7, 1, 1);
        
        recitationDataPane.getChildren().add(addEditPane);
        
        /******* SCHEDULE DATA *******/
        startingMonday.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
        startingMonday.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(oldValue != null){
                controller.handleChangeStartDate(java.sql.Date.valueOf(oldValue), java.sql.Date.valueOf(newValue));
                AppGUI gui = app.getGUI();
            gui.getFileController().markAsEdited(gui);
            }
        });
        endingFriday.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
        endingFriday.valueProperty().addListener((ov, oldValue, newValue) -> {
            if(oldValue != null){
                controller.handleChangeEndDate(java.sql.Date.valueOf(oldValue), java.sql.Date.valueOf(newValue));
                AppGUI gui = app.getGUI();
            gui.getFileController().markAsEdited(gui);
            }
        });
        VBox schedulePane = new VBox(10);
        schedulePane.setPadding(new Insets(10, 10, 10, 10));
        Label scheduleTabHeader = new Label(props.getProperty(CourseSiteGeneratorProp.SCHEDULE_TEXT.toString()));
        scheduleTabHeader.getStyleClass().add("tab-header");
        schedulePane.getChildren().add(scheduleTabHeader);
        GridPane calBoundariesPane = new GridPane();
        calBoundariesPane.setHgap(10);
        calBoundariesPane.setVgap(10);
        calBoundariesPane.setStyle("-fx-background-color: #EBEBEB");
        Label calBoundsLabel = new Label(props.getProperty(CourseSiteGeneratorProp.CAL_BOUNDS_TEXT.toString()));
        calBoundsLabel.getStyleClass().add("section-subheader");
        calBoundariesPane.add(calBoundsLabel, 0, 0, 2, 1);
        calBoundariesPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.STARTING_MON_TEXT.toString())+":"), 0, 1, 1, 1);
        calBoundariesPane.add(startingMonday, 1, 1, 1, 1);
        calBoundariesPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.ENDING_FRI_TEXT.toString())+":"), 2, 1, 1, 1);
        calBoundariesPane.add(endingFriday, 3, 1, 1, 1);
        calBoundariesPane.setPadding(new Insets(10, 10, 10, 10));
        
        VBox scheduleItemsPane = new VBox(10);
        scheduleItemsPane.setPadding(new Insets(10, 10, 10, 10));
        scheduleItemsPane.setStyle("-fx-background-color: #EBEBEB");
        
        HBox scheduleItemsHeader = new HBox(10);
        scheduleItemsHeader.setAlignment(Pos.CENTER_LEFT);
        
        deleteScheduleItemButton = new Button(props.getProperty(CourseSiteGeneratorProp.DELETE_TEXT.toString()));
        
        Label scheduleItemsLabel = new Label(props.getProperty(CourseSiteGeneratorProp.SCHEDULE_ITEMS_TEXT.toString()));
        scheduleItemsLabel.getStyleClass().add("section-subheader");
        scheduleItemsHeader.getChildren().addAll(scheduleItemsLabel, deleteScheduleItemButton);
        
        scheduleItemsPane.getChildren().add(scheduleItemsHeader);
        
        TableColumn typeColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TYPE_TEXT.toString()));
        TableColumn dateColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.DATE_TEXT.toString()));
        TableColumn titleColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TITLE_TEXT.toString()));
        TableColumn topicColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TOPIC_TEXT.toString()));
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("type")
        );
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("dateString")
        );
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("title")
        );
        topicColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("topic")
        );
        scheduleItemsTable = new TableView();
        ObservableList<ScheduledItem> scheduleData = data.getScheduledItems();
        scheduleItemsTable.setEditable(true);
        scheduleItemsTable.setItems(scheduleData);
        
        scheduleItemsTable.getColumns().addAll(typeColumn, dateColumn, titleColumn, topicColumn);
        
        scheduleItemsPane.getChildren().add(scheduleItemsTable);
        
        GridPane addEditSchedulePane = new GridPane();
        addEditSchedulePane.setHgap(10);
        addEditSchedulePane.setVgap(10);
        
        Label addEditScheduleItemsLabel = new Label(props.getProperty(CourseSiteGeneratorProp.ADDEDIT_TEXT.toString()));
        addEditScheduleItemsLabel.getStyleClass().add("section-subheader");
        addEditSchedulePane.add(addEditScheduleItemsLabel, 0, 0, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TYPE_TEXT.toString())+":"), 0, 1, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.DATE_TEXT.toString())+":"), 0, 2, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TIME_TEXT.toString())+":"), 0, 3, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TITLE_TEXT.toString())+":"), 0, 4, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TOPIC_TEXT.toString())+":"), 0, 5, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.LINK_TEXT.toString())+":"), 0, 6, 1, 1);
        addEditSchedulePane.add(new Label(props.getProperty(CourseSiteGeneratorProp.CRITERIA_TEXT.toString())+":"), 0, 7, 1, 1);
        
        scheduleItemType = new ComboBox();
        scheduleItemType.setPrefWidth(100);
        ArrayList<String> scheduleItemTypes = props.getPropertyOptionsList(CourseSiteGeneratorProp.SCHEDULE_ITEM_TYPES);
        ObservableList<String> oListTypes = FXCollections.observableArrayList(scheduleItemTypes);
        scheduleItemType.setItems(oListTypes);
        
        scheduleItemDate = new DatePicker();
        scheduleItemTime = new TextField();
        scheduleItemTitle = new TextField();
        scheduleItemTopic = new TextField();
        scheduleItemLink = new TextField();
        scheduleItemCriteria = new TextField();
        
        addEditSchedulePane.add(scheduleItemType, 1, 1, 1, 1);
        addEditSchedulePane.add(scheduleItemDate, 1, 2, 1, 1);
        addEditSchedulePane.add(scheduleItemTime, 1, 3, 1, 1);
        addEditSchedulePane.add(scheduleItemTitle, 1, 4, 30, 1);
        addEditSchedulePane.add(scheduleItemTopic, 1, 5, 30, 1);
        addEditSchedulePane.add(scheduleItemLink, 1, 6, 30, 1);
        addEditSchedulePane.add(scheduleItemCriteria, 1, 7, 30, 1);
        
        addUpdateScheduleItemButton = new Button(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()));
        Button clearScheduleItemButton = new Button(props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON_TEXT.toString()));
        addUpdateScheduleItemButton.setPrefWidth(100);
        clearScheduleItemButton.setPrefWidth(100);
        
        addEditSchedulePane.add(addUpdateScheduleItemButton, 0, 8, 1, 1);
        addEditSchedulePane.add(clearScheduleItemButton, 1, 8, 1, 1);
        
        scheduleItemsPane.getChildren().add(addEditSchedulePane);
        
        schedulePane.getChildren().addAll(calBoundariesPane, scheduleItemsPane);
        
        /******* PROJECT DATA ********/
        VBox projectDataPane = new VBox(10);
        projectDataPane.setPadding(new Insets(10, 10, 10, 10));
        
        VBox teamPane = new VBox(10);
        teamPane.setPadding(new Insets(10, 10, 10, 10));
        teamPane.setStyle("-fx-background-color: #EBEBEB");
        
        HBox projectDataHeader = new HBox(10);
        projectDataHeader.setAlignment(Pos.CENTER_LEFT);
        
        deleteTeamButton = new Button(props.getProperty(CourseSiteGeneratorProp.DELETE_TEXT.toString()));
        Label teamsHeaderLabel = new Label(props.getProperty(CourseSiteGeneratorProp.TEAMS_TEXT.toString()));
        teamsHeaderLabel.getStyleClass().add("section-subheader");
        projectDataHeader.getChildren().addAll(teamsHeaderLabel, deleteTeamButton);   
        
        teamPane.getChildren().add(projectDataHeader);
        
        TableColumn teamNameColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.NAME_TEXT.toString()));
        TableColumn colorColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.COLOR_TEXT.toString())+" (hex#)");
        TableColumn textColorColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TEXT_COLOR_TEXT.toString())+" (hex#)");
        TableColumn linkColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.LINK_TEXT.toString()));
        teamNameColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("name")
        );
        colorColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("color")
        );
        textColorColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("textColor")
        );
        linkColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("link")
        );
        teamTable = new TableView();
        ObservableList<Team> teamData = data.getTeams();
        teamTable.setEditable(true);
        teamTable.setItems(teamData);
        
        teamTable.getColumns().addAll(teamNameColumn, colorColumn, textColorColumn, linkColumn);
        
        teamPane.getChildren().add(teamTable);
        
        GridPane addEditProjectPane = new GridPane();
        addEditProjectPane.setHgap(10);
        addEditProjectPane.setVgap(10);
        
        Label addEditTeamsLabel = new Label(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()));
        addEditTeamsLabel.getStyleClass().add("section-subheader");
        addEditProjectPane.add(addEditTeamsLabel, 0, 0, 1, 1);
        addEditProjectPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.NAME_TEXT.toString())+":"), 0, 1, 1, 1);
        addEditProjectPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.COLOR_TEXT.toString())+":"), 0, 2, 1, 1);
        addEditProjectPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.LINK_TEXT.toString())+":"), 0, 3, 1, 1);
        
        teamName = new TextField();
        teamLink = new TextField();
        
        teamColor = new ColorPicker();
        teamTextColor = new ColorPicker();
        
        addEditProjectPane.add(teamName, 1, 1, 1, 1);
        addEditProjectPane.add(teamColor, 1, 2, 1, 1);
        addEditProjectPane.add(teamLink, 1, 3, 3, 1);
        
        addEditProjectPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TEXT_COLOR_TEXT.toString())+":"), 2, 2, 1, 1);
        addEditProjectPane.add(teamTextColor, 3, 2, 1, 1);
        
        addUpdateTeamButton = new Button(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()));
        Button clearTeamButton = new Button(props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON_TEXT.toString()));
        addUpdateTeamButton.setPrefWidth(100);
        clearTeamButton.setPrefWidth(100);
        
        addEditProjectPane.add(addUpdateTeamButton, 0, 4, 1, 1);
        addEditProjectPane.add(clearTeamButton, 1, 4, 1, 1);
        
        teamPane.getChildren().add(addEditProjectPane);
        
        VBox studentPane = new VBox(10);
        studentPane.setPadding(new Insets(10, 10, 10, 10));
        studentPane.setStyle("-fx-background-color: #EBEBEB");
        
        HBox studentDataHeader = new HBox(10);
        studentDataHeader.setAlignment(Pos.CENTER_LEFT);
        
        deleteStudentButton = new Button(props.getProperty(CourseSiteGeneratorProp.DELETE_TEXT.toString()));
        Label studentsLabel = new Label(props.getProperty(CourseSiteGeneratorProp.STUDENTS_TEXT.toString()));
        studentsLabel.getStyleClass().add("section-subheader");
        studentDataHeader.getChildren().addAll(studentsLabel, deleteStudentButton);   
        
        studentPane.getChildren().add(studentDataHeader);
        
        TableColumn firstNameColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.FIRSTNAME_TEXT.toString()));
        TableColumn lastNameColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.LASTNAME_TEXT.toString()));
        TableColumn teamColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.TEAM_TEXT.toString()));
        TableColumn roleColumn = new TableColumn(props.getProperty(CourseSiteGeneratorProp.ROLE_TEXT.toString()));
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("firstName")
        );
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("lastName")
        );
        teamColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("team")
        );
        roleColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("role")
        );
        studentTable = new TableView();
        ObservableList<Student> studentData = data.getStudents();
        studentTable.setEditable(true);
        studentTable.setItems(studentData);
        
        studentTable.getColumns().addAll(firstNameColumn, lastNameColumn, teamColumn, roleColumn);
        
        studentPane.getChildren().add(studentTable);
        
        GridPane addEditStudentPane = new GridPane();
        addEditStudentPane.setHgap(10);
        addEditStudentPane.setVgap(10);
        
        Label addEditStudentsLabel = new Label(props.getProperty(CourseSiteGeneratorProp.ADDEDIT_TEXT.toString()));
        addEditStudentsLabel.getStyleClass().add("section-subheader");
        addEditStudentPane.add(addEditStudentsLabel, 0, 0, 1, 1);
        addEditStudentPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.FIRSTNAME_TEXT.toString())+":"), 0, 1, 1, 1);
        addEditStudentPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.LASTNAME_TEXT.toString())+":"), 0, 2, 1, 1);
        addEditStudentPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.TEAM_TEXT.toString())+":"), 0, 3, 1, 1);
        addEditStudentPane.add(new Label(props.getProperty(CourseSiteGeneratorProp.ROLE_TEXT.toString())+":"), 0, 4, 1, 1);
        
        studentFirstName = new TextField();
        studentLastName = new TextField();
        studentRole = new TextField();
        
        addEditStudentPane.add(studentFirstName, 1, 1, 10, 1);
        addEditStudentPane.add(studentLastName, 1, 2, 10, 1);
        addEditStudentPane.add(studentRole, 1, 4, 10, 1);
        
        teamComboBox = new ComboBox();
        teamComboBox.setItems(data.getTeams());
        teamComboBox.setPrefWidth(200);
        
        addEditStudentPane.add(teamComboBox, 1, 3, 10, 1);
        
        addUpdateStudentButton = new Button(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT.toString()));
        Button clearStudentButton = new Button(props.getProperty(CourseSiteGeneratorProp.CLEAR_BUTTON_TEXT.toString()));
        addUpdateStudentButton.setPrefWidth(100);
        clearStudentButton.setPrefWidth(100);
        
        addEditStudentPane.add(addUpdateStudentButton, 0, 5, 1, 1);
        addEditStudentPane.add(clearStudentButton, 1, 5, 1, 1);
        
        studentPane.getChildren().add(addEditStudentPane);
        
        teamTable.setMaxHeight(150);
        studentTable.setMaxHeight(150);
        
        Label projectsHeader = new Label(props.getProperty(CourseSiteGeneratorProp.PROJECTS_TEXT.toString()));
        projectsHeader.getStyleClass().add("tab-header");
        projectDataPane.getChildren().addAll(projectsHeader, teamPane, studentPane);
        
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        
        Tab courseDetailsTab = new Tab(props.getProperty(CourseSiteGeneratorProp.COURSE_DETAILS_TEXT.toString()));
        courseDetailsTab.setContent(courseDetailsPane);
        
        Tab taDataTab = new Tab(props.getProperty(CourseSiteGeneratorProp.TA_DATA_TEXT.toString()));
        sPane.setPadding(new Insets(10, 10, 10, 10));
        taDataTab.setContent(sPane);
        
        Tab recitationDataTab = new Tab(props.getProperty(CourseSiteGeneratorProp.RECITATION_DATA_TEXT.toString()));
        recitationDataTab.setContent(recitationDataPane);
        
        Tab scheduleDataTab = new Tab(props.getProperty(CourseSiteGeneratorProp.SCHEDULE_DATA_TEXT.toString()));
        scheduleDataTab.setContent(schedulePane);
        
        Tab projectDataTab = new Tab(props.getProperty(CourseSiteGeneratorProp.PROJECT_DATA_TEXT.toString()));
        projectDataTab.setContent(projectDataPane);
        
        BorderPane appPane = new BorderPane();
        appPane.setPadding(new Insets(10, 10, 10, 10));
        appPane.setCenter(tabPane);
        
        projectDataPane.setStyle("-fx-background-color: #FFD8AD");
        schedulePane.setStyle("-fx-background-color: #FFD8AD");
        recitationDataPane.setStyle("-fx-background-color: #FFD8AD");
        sPane.setStyle("-fx-background-color: #FFD8AD");
        courseDetailsPane.setStyle("-fx-background-color: #FFD8AD");
        tabPane.getStyleClass().add("tab-pane");
        appPane.setStyle("-fx-background-color: #FFECD7");
        taDataTab.setOnSelectionChanged(e -> {
            fullyLoaded = true;
        });
        tabPane.getTabs().addAll(courseDetailsTab, taDataTab, recitationDataTab, scheduleDataTab, projectDataTab);
        
        // AND PUT EVERYTHING IN THE WORKSPACE
        ((BorderPane) workspace).setCenter(appPane);

        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        taTable.prefHeightProperty().bind(workspace.heightProperty().multiply(1.9));
        
        
        
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new TAController(app);
        
        undoButton.setOnAction(e -> {
           controller.undoTransaction();
        });
        redoButton.setOnAction(e -> {
            controller.redoTransaction();
        });
        aboutButton.setOnAction(e -> {
            AboutPopup.display();
        });
        // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        addButton.setOnAction(e -> {
            controller.handleAddTA();
        });
        deleteTAButton.setOnAction(e -> {
           controller.handleKeyPress(null, KeyCode.DELETE);
        });
        taTable.setFocusTraversable(true);
        taTable.setOnKeyPressed(e -> {
            controller.handleKeyPress(e, e.getCode());
        });
        taTable.setOnMouseClicked(e -> {
            controller.checkselected();
        });
        clearButton.setOnAction(e -> {
            addButton.setText(props.getProperty(CourseSiteGeneratorProp.ADD_BUTTON_TEXT.toString()));
            nameTextField.setText("");
            emailTextField.setText("");
            nameTextField.requestFocus();
        });
        c.setOnAction(e -> {
            HashMap<String, Label> kk = (HashMap<String, Label>) officeHoursGridTACellLabels.clone();
            Data taData = (Data) app.getDataComponent();
            String f = (String) c.getSelectionModel().getSelectedItem();
            String[] z = f.split(":");
            int k = 0;
            k = Integer.parseInt(z[0]);
            if (z[1].charAt(2) == 'P' && k != 12) {
                k += 12;
            }
            if (z[0].equals("12") && z[1].charAt(2) == 'A') {
                k += -12;
            }
            int oldbeg = taData.getEndHour();
            if (z[1].charAt(2) == 'A' && z[0].equals("12")) {
                k = Integer.parseInt(z[0]) - 12;
            } else if (z[1].charAt(2) == 'P' && k != 12) {
                k = Integer.parseInt(z[0]) + 12;
            } else {
                k = Integer.parseInt(z[0]);
            }
            if (k < taData.getStartHour()) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TIME_CONF_TITLE), props.getProperty(TIME_CONF));

            } else {
                int dif = 0;
                boolean big = false;
                if (taData.getEndHour() > k || taData.getEndHour() == k) {
                    if (taData.getEndHour() != k) {
                        big = false;
                        dif = (taData.getEndHour() - k) * 2;
                    }

                } else {
                    dif = (Integer.parseInt(z[0]) - taData.getEndHour()) * 2;
                    big = true;
                }
                int rows = taData.getNumRows();
                String selection = "";
                for (int u = rows; u >= rows - dif; u--) {
                    for (int uu = 2; uu < 7; uu++) {
                        String syntac = uu + "_" + u;
                        if (officeHoursGridTACellLabels.get(syntac) != null) {
                            if (!officeHoursGridTACellLabels.get(syntac).getText().equals("")) {
                                AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
                                yesNoDialog.show(props.getProperty(TIME_EXIST_TITLE), props.getProperty(TIME_EXIST));
                                selection = yesNoDialog.getSelection();
                                break;

                                // AND NOW GET THE USER'S SELECTION
                                // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
                            }
                        }
                        if (!selection.equals("")) {
                            break;
                        }
                    }
                    if (!selection.equals("")) {
                        break;
                    }
                }
                if (selection.equals(AppYesNoCancelDialogSingleton.YES) || selection.equals("")) {

                    jTPS_Transaction time = new timec(app, officeHoursGridTACellLabels, taData, big, k, dif, kk);
                    controller.getJ().addTransaction(time);

                }
            }
        });
        a.setOnAction(e -> {
            HashMap<String, Label> kk = (HashMap<String, Label>) officeHoursGridTACellLabels.clone();
            Data taData = (Data) app.getDataComponent();
            String f = (String) a.getSelectionModel().getSelectedItem();
            int oldbeg = taData.getStartHour();
            String[] z = f.split(":");
            int k = 0;
            if (z[1].charAt(2) == 'A' && z[0].equals("12")) {
                k = Integer.parseInt(z[0]) - 12;
            } else if (z[1].charAt(2) == 'P' && !(z[0].equals("12"))) {
                k = Integer.parseInt(z[0]) + 12;

            } else {
                k = Integer.parseInt(z[0]);
            }
            if (k > taData.getEndHour()) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TIME_CONF_TITLE), props.getProperty(TIME_CONF));

            } else {
                int dif = 0;
                boolean big = false;
                if (taData.getEndHour() > k || taData.getEndHour() == k) {
                    if (taData.getStartHour() == 0 && k == 1) {
                        System.out.print("DECREASING BY ONE ROW");
                        dif = 1;
                        big = true;
                    } else if (taData.getStartHour() != k) {
                        big = false;
                        dif = (taData.getStartHour() - k) * 2;

                    }

                } else {
                    dif = (Integer.parseInt(z[0]) - taData.getStartHour()) * 2;
                    big = true;

                }

                int rows = taData.getNumRows();
                boolean dial = true;
                String selection = "";
                for (int u = 0; u <= dif * -1; u++) {
                    for (int uu = 2; uu < 7; uu++) {
                        if (dial) {
                            String syntac = uu + "_" + u;
                            if (officeHoursGridTACellLabels.get(syntac) != null) {
                                if (!officeHoursGridTACellLabels.get(syntac).getText().equals("")) {
                                    AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
                                    yesNoDialog.show(props.getProperty(TIME_EXIST_TITLE), props.getProperty(TIME_EXIST));
                                    dial = false;
                                    // AND NOW GET THE USER'S SELECTION
                                    selection = yesNoDialog.getSelection();
                                    break;
                                    // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON

                                }
                            }
                        }
                    }
                }
                if (selection.equals(AppYesNoCancelDialogSingleton.YES) || selection.equals("")) {

                    jTPS_Transaction javaa = new javaa(taData, app, big, dif, taData.getStartHour(), k, kk, officeHoursGridTACellLabels);
                    controller.getJ().addTransaction(javaa);

                }
            }
        });
        teamTable.setOnMouseClicked(e -> {
            controller.checkTeamSelected();
        });
        recitationTable.setOnKeyPressed(e -> {
            controller.handleRecTableKeyPress(e, e.getCode());
        });
        scheduleItemsTable.setOnKeyPressed(e ->{
            controller.handleKeyPressScheduledItemsTable(e, e.getCode());
        });
        teamTable.setOnKeyPressed(e ->{
            controller.handleKeyPressTeamTable(e, e.getCode());
        });
        studentTable.setOnKeyPressed(e ->{
            controller.handleKeyPressStudentTable(e, e.getCode());
        });
        studentTable.setOnMouseClicked(e -> {
            controller.checkStudentSelected();
        });
        scheduleItemsTable.setOnMouseClicked(e -> {
            controller.checkScheduleItemSelected();
        });
        recitationTable.setOnMouseClicked(e -> {
            controller.checkRecitationSelected();
        });
        deleteRecitationButton.setOnAction(e -> {
            controller.handleDeleteRecitation();
        });
        deleteScheduleItemButton.setOnAction(e -> {
            controller.handleDeleteScheduledItem();
        });
        deleteTeamButton.setOnAction(e ->{
            controller.handleDeleteTeam();
        });
        deleteStudentButton.setOnAction(e ->{
            controller.handleDeleteStudent();
        });
        addUpdateRecitationButton.setOnAction(e ->{
            controller.handleAddUpdateRecitation();
        });
        addUpdateScheduleItemButton.setOnAction(e -> {
            controller.handleAddUpdateScheduledItem();
        });
        addUpdateTeamButton.setOnAction(e ->{
            controller.handleAddUpdateTeam();
        });
        addUpdateStudentButton.setOnAction(e ->{
            controller.handleAddUpdateStudent();
        });
        clearRecitationButton.setOnAction(e -> {
            getRecitationTable().getSelectionModel().clearSelection();
            getRecSection().setText("");
            getRecInstructor().setText("");
            getRecDayTime().setText("");
            getRecLocation().setText("");
            getRecSupervisingTA1().getSelectionModel().clearSelection();
            getRecSupervisingTA2().getSelectionModel().clearSelection();
            getAddUpdateRecitationButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
        });
        clearScheduleItemButton.setOnAction(e -> {
            getScheduleItemType().getSelectionModel().clearSelection();
            getScheduleItemDate().setValue(null);
            getScheduleItemTime().setText("");
            getScheduleItemTitle().setText("");
            getScheduleItemTopic().setText("");
            getScheduleItemLink().setText("");
            getScheduleItemCriteria().setText("");
            getScheduleItemsTable().getSelectionModel().clearSelection();
            getAddUpdateScheduleItemButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
        });
        clearTeamButton.setOnAction(e -> {
            getTeamName().setText("");
            Color color = Color.rgb(255, 255, 255);
            getTeamColor().setValue(color);
            getTeamTextColor().setValue(color);
            getTeamLink().setText("");
            getTeamTable().getSelectionModel().clearSelection();
            getAddUpdateTeamButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
        });
        clearStudentButton.setOnAction(e -> {
            getStudentFirstName().setText("");
            getStudentLastName().setText("");
            getTeamComboBox().getSelectionModel().clearSelection();
            getStudentRole().setText("");
            getStudentTable().getSelectionModel().clearSelection();
            getAddUpdateStudentButton().setText(props.getProperty(CourseSiteGeneratorProp.ADD_TEXT));
        });
        workspace.setOnKeyPressed(e ->{
            controller.handleWorkspaceKeyPress(e, e.getCode());
        });
        changeCourseInfoButton.setOnAction(e -> {
            controller.handleChangeExportDir();
        });
        selectTemplateButton.setOnAction(e -> {
            controller.handleChangeTemplateDir();
        });
        changeBannerSchoolImageButton.setOnAction(e ->{
           controller.handleChangeBannerSchoolImage(); 
        });
        changeLeftFooterImageButton.setOnAction(e -> {
            controller.handleChangeLeftFooterImage();
        });
        changeRightFooterImageButton.setOnAction(e -> {
            controller.handleChangeRightFooterImage();
        });
        stylesheetComboBox.setOnAction(e -> {
            controller.handleChangeCSS();
        });
        subjectComboBox.setOnAction(e -> {
            controller.handleChangeSubject();
        });
        numberComboBox.setOnAction(e ->{
            controller.handleChangeNumber();
        });
        semesterComboBox.setOnAction(e ->{
            controller.handleChangeSemester();
        });
        yearComboBox.setOnAction(e -> {
            controller.handleChangeYear();
        });
    }

    // WE'LL PROVIDE AN ACCESSOR METHOD FOR EACH VISIBLE COMPONENT
    // IN CASE A CONTROLLER OR STYLE CLASS NEEDS TO CHANGE IT
    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public ComboBox getStartTime() {
        return a;
    }

    public ComboBox getEndTime() {
        return c;
    }

    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }

    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "";
        if (militaryHour == 0) {
            cellText += "12:" + minutes;
        } else {
            cellText += hour + ":" + minutes;
        }
        if (militaryHour < 12 || militaryHour == 0) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();

        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();

    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        Data taData = (Data) dataComponent;
        reloadOfficeHoursGrid(taData);

    }

    public void reloadOfficeHoursGrid(Data dataComponent) {
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();
        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }

        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }

        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row + 1);
            dataComponent.getCellTextProperty(col, row + 1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row + 1);
            dataComponent.getCellTextProperty(col, row + 1).set(buildCellText(endHour + 1, "00"));
            col++;

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row + 1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setFocusTraversable(true);
            p.setOnKeyTyped(e -> {
                controller.handleKeyPress(e, e.getCode());
            });
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
            p.setOnMouseExited(e -> {
                controller.handleGridCellMouseExited((Pane) e.getSource());
            });
            p.setOnMouseEntered(e -> {
                controller.handleGridCellMouseEntered((Pane) e.getSource());
            });
        }

        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        TAStyle taStyle = (TAStyle) app.getStyleComponent();
        taStyle.initOfficeHoursGridStyle();
    }

    public void addCellToGrid(Data dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);

        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);

        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);

        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());
    }
    
    private Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);
	
	// NOW MAKE THE BUTTON
        Button button = new Button();
        button.setPrefSize(35, 35);
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
	
	// PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);
	
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
    public TAController getController(){
        return controller;
    }

    public ComboBox getSubjectComboBox() {
        return subjectComboBox;
    }

    public ComboBox getSemesterComboBox() {
        return semesterComboBox;
    }

    public ComboBox getNumberComboBox() {
        return numberComboBox;
    }

    public ComboBox getYearComboBox() {
        return yearComboBox;
    }

    public TextField getCourseTitle(){
        return courseTitle;
    }
    
    public TextField getInstructorHome() {
        return instructorHome;
    }

    public TextField getInstructorName() {
        return instructorName;
    }

    public Label getExportDir() {
        return exportDir;
    }
    
    public Label getSiteTemplateDir(){
        return siteTemplateDir;
    }

    public ImageView getBannerSchool() {
        return bannerSchool;
    }

    public ImageView getRightFooter() {
        return rightFooter;
    }

    public ImageView getLeftFooter() {
        return leftFooter;
    }
    
    public ComboBox getStylesheet(){
        return stylesheetComboBox;
    }
    
    public DatePicker getStartingMonday(){
        return startingMonday;
    }
    
    public DatePicker getEndingFriday(){
        return endingFriday;
    }
    
    public TableView getTeamTable(){
        return teamTable;
    }

    public TextField getTeamName() {
        return teamName;
    }

    public TextField getTeamLink() {
        return teamLink;
    }

    public ColorPicker getTeamColor() {
        return teamColor;
    }

    public ColorPicker getTeamTextColor() {
        return teamTextColor;
    }

    public TextField getStudentFirstName() {
        return studentFirstName;
    }

    public TextField getStudentLastName() {
        return studentLastName;
    }

    public ComboBox getTeamComboBox() {
        return teamComboBox;
    }

    public TextField getStudentRole() {
        return studentRole;
    }
    public TableView getStudentTable(){
        return studentTable;
    }

    public TableView getScheduleItemsTable() {
        return scheduleItemsTable;
    }

    public ComboBox getScheduleItemType() {
        return scheduleItemType;
    }

    public DatePicker getScheduleItemDate() {
        return scheduleItemDate;
    }

    public TextField getScheduleItemTime() {
        return scheduleItemTime;
    }

    public TextField getScheduleItemTitle() {
        return scheduleItemTitle;
    }

    public TextField getScheduleItemTopic() {
        return scheduleItemTopic;
    }

    public TextField getScheduleItemLink() {
        return scheduleItemLink;
    }

    public TextField getScheduleItemCriteria() {
        return scheduleItemCriteria;
    }

    public TableView getRecitationTable() {
        return recitationTable;
    }

    public TextField getRecSection() {
        return recSection;
    }

    public TextField getRecInstructor() {
        return recInstructor;
    }

    public TextField getRecDayTime() {
        return recDayTime;
    }

    public TextField getRecLocation() {
        return recLocation;
    }

    public ComboBox getRecSupervisingTA1() {
        return recSupervisingTA1;
    }

    public ComboBox getRecSupervisingTA2() {
        return recSupervisingTA2;
    }
    
    public Button getAddUpdateTeamButton(){
        return addUpdateTeamButton;
    }
    public Button getAddUpdateStudentButton(){
        return addUpdateStudentButton;
    }

    public Button getAddUpdateScheduleItemButton() {
        return addUpdateScheduleItemButton;
    }
    
    public Button getAddUpdateRecitationButton(){
        return addUpdateRecitationButton;
    }
    
    public ObservableList getCSSFiles(){
        File cssDir = new File(PATH_WORK+"\\css");
        Collection<File> files = FileUtils.listFiles(cssDir, new RegexFileFilter("^(.*css)$"), DirectoryFileFilter.DIRECTORY);
        ArrayList fileList = new ArrayList();
        for(File file : files){
            fileList.add(file.getName());
        }
        return FXCollections.observableArrayList(fileList);
    }
}
