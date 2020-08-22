package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.Project;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Dominik on 01.06.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class CreateProject {

    public CreateProject(MainWindow mainWindow) {
        Stage window = new Stage();

        Label nameL = new Label("Name");
        TextField nameF = new TextField();

        HBox name = new HBox();
        name.getChildren().addAll(nameL, nameF);

        Label widthL = new Label("Breite");
        TextField widthF = new TextField("3");
        widthF.setMaxWidth(50);

        Label heightL = new Label("Höhe");
        TextField heightF = new TextField("3");
        heightF.setMaxWidth(50);

        HBox dimensions = new HBox();
        dimensions.getChildren().addAll(widthL, widthF, heightL, heightF);

        Button save = new Button("Save");
        save.setOnAction(e -> {
            window.close();

            MainWindow.controller.setProject(new Project(nameF.getText(), Integer.parseInt(widthF.getText()), Integer.parseInt(heightF.getText())));

            mainWindow.reload();
            mainWindow.getWindow().setTitle("Matrix Controller - " + nameF.getText());
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> window.close());

        HBox buttons = new HBox();
        buttons.getChildren().addAll(save, cancel);

        VBox pane = new VBox(name, dimensions, buttons);

        Scene scene = new Scene(pane, 200, 100);
        window.setScene(scene);

        window.initModality(Modality.APPLICATION_MODAL);

        window.setTitle("Erstelle ein neues Projekt");
        window.setResizable(false);

        window.show();
    }
}
