package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.Pixel;
import de.drachenfrucht1.app.PixelMode;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Created by Dominik on 23.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class SceneEditor {

    private final Stage window;
    private final ArrayList<Pixel> pixels = new ArrayList<>();

    public SceneEditor(MainWindow mainWindow, ScenenEditorMode mode) {
        window = new Stage();

        //North
        Label nameL = new Label("Name: ");
        TextField nameF = new TextField();
        nameF.setText("Scene 001");

        HBox north = new HBox();
        north.getChildren().addAll(nameL, nameF);

        //Center
        GridPane grid = new GridPane();
        grid.setPrefSize(400, 300);

        HBox south = new HBox();

        //South
        Label fadetimeL = new Label("Fadetime in ms: ");
        TextField fadetimeF = new TextField();
        fadetimeF.setText("1000");
        if (mode == ScenenEditorMode.Scenes) {
            south.getChildren().addAll(fadetimeL, fadetimeF);
        }
        //
        Button save = new Button("Save");
        if (mode == ScenenEditorMode.Scenes) {
            save.setOnAction(e -> {
                int index = 0;
                Color[][] colors = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getHeight()];
                for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
                    for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
                        colors[x][y] = pixels.get(index).getColor();
                        index++;
                    }
                }

                de.drachenfrucht1.app.Scene newScene = new de.drachenfrucht1.app.Scene(nameF.getText(), colors, Integer.parseInt(fadetimeF.getText()));
                MainWindow.controller.getProject().addScene(newScene);
                window.close();
            });
        } else if (mode == ScenenEditorMode.OverlayScenes) {
            save.setOnAction(e -> {
                int index = 0;
                Color[][] colors = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getHeight()];
                for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
                    for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
                        if (pixels.get(index).getColor() == Color.BLACK) {
                            colors[x][y] = Color.TRANSPARENT;
                        } else {
                            colors[x][y] = pixels.get(index).getColor();
                        }
                        index++;
                    }
                }

                de.drachenfrucht1.app.OverlayScene newScene = new de.drachenfrucht1.app.OverlayScene(nameF.getText(), colors);
                MainWindow.controller.getProject().getOverlayScenes().add(newScene);
                window.close();
            });
        }
        //
        Button cancel = new Button("Cancel");
        cancel.setOnAction(e -> window.close());

        south.getChildren().addAll(save, cancel);

        BorderPane pane = new BorderPane();
        pane.setTop(north);
        pane.setCenter(grid);
        pane.setBottom(south);

        Scene scene = new Scene(pane, 400, 400);
        window.setScene(scene);

        Image icon64 = new Image("file:icons/theatre-stage.png");
        window.getIcons().addAll(icon64);

        window.setTitle("Scene Editor");
        window.setResizable(true);
        window.show();

        //fill grid with pixel
        int width = (int) grid.getWidth() / MainWindow.controller.getProject().getWidth();
        int height = (int) grid.getHeight() / MainWindow.controller.getProject().getHeight();
        for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
            for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
                Pixel pixel = new Pixel(window, PixelMode.notMain);
                pixel.setColor(Color.BLACK);
                pixel.setPrefSize(width, height);
                GridPane.setConstraints(pixel, x, y);
                grid.getChildren().add(pixel);
                pixels.add(pixel);
            }
        }
    }

    public enum ScenenEditorMode {
        Scenes,
        OverlayScenes
    }
}
