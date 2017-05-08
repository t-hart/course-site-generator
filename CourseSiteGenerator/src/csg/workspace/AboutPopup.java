/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
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
import static djf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static djf.settings.AppStartupConstants.PATH_WORK;
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
import org.apache.commons.io.FileUtils;
import javafx.stage.DirectoryChooser;
import java.io.File;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
/**
 *
 * @author tjhha
 */
public class AboutPopup {
    public static void display()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle(props.getProperty(ABOUT_POPUP_TITLE));
        Label label1 = new Label(props.getProperty(ABOUT1)+"\n"+props.getProperty(ABOUT2)+"\n"+props.getProperty(ABOUT3)+"\n"+props.getProperty(ABOUT4)+"\n\n"+props.getProperty(ABOUT5));
        label1.setTextAlignment(TextAlignment.CENTER);
        Button button1 = new Button(props.getProperty(CLOSE));
        button1.setOnAction(e -> popupwindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}
