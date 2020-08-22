package de.drachenfrucht1.app;

import com.sun.javafx.scene.control.skin.CustomColorDialog;
import de.drachenfrucht1.graphics.MainWindow;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * Created by Dominik on 22.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class Pixel extends Button {

    private @Getter
    Color color;

    public Pixel(Stage window, PixelMode mode) {
        this.setStyle("-fx-border-radius: 0, 0, 0; -fx-background-radius: 0, 0, 0, 0; -fx-border-color: #fff; -fx-border-style: solid; -fx-border-width: 4px");
        this.setOnAction(e -> {
            CustomColorDialog colorPicker = new CustomColorDialog(window);
            colorPicker.setShowOpacitySlider(false);
            colorPicker.setCurrentColor(color);
            colorPicker.setOnSave(() -> {
                setColor(colorPicker.getCustomColor());
                if (mode.equals(PixelMode.mainWindow)) {
                    MainWindow.controller.sendLiveUpdate();
                }
            });
            colorPicker.show();
        });
    }

    /**
     * FUNCTION: setColor()
     * PURPOSE: set the color of the pixel
     *
     * @param c color to set
     */
    public void setColor(Color c) {
        color = c;
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        //String hex = Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);
        this.setStyle("-fx-background-color: rgb(" + r + "," + g + "," + b + ");");
    }
}