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
/**
 *
 * @author tjhha
 */
public class AboutPopup {
    public static void display()
    {
        Stage popupwindow = new Stage();
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("PLACEHOLDER\nAbout");
        Label label1 = new Label("PLACEHOLDER\nCouse Site Generator™\n"
                + "Created by: Timothy Hart\n"
                + "CSE 219 Final Project\n"
                + "Spring 2017\n\n"
                + "Contact: tjh.hart@gmail.com");
        label1.setTextAlignment(TextAlignment.CENTER);
        Button button1 = new Button("Close");
        button1.setOnAction(e -> popupwindow.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 250);
        popupwindow.setScene(scene1);
        popupwindow.showAndWait();
    }
}
