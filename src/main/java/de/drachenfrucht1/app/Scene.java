package de.drachenfrucht1.app;

import de.drachenfrucht1.graphics.MainWindow;
import javafx.scene.paint.Color;
import lombok.Data;

import java.util.ArrayList;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public @Data
class Scene {
    private final String name;
    private final Color[][] pixels;
    private final int fadeTime; //fadetime in ms

    public void fade() {
        int steps = fadeTime / MatrixController.SENDTIME;

        ArrayList<Pixel> old = MainWindow.controller.getPixels();
        for (int i = 1; i <= steps; i++) {
            int index = 0;
            Color[][] step = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getHeight()];
            for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
                for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
                    double differenceR = pixels[x][y].getRed() - old.get(index).getColor().getRed();
                    double stepR = differenceR / steps * i;
                    double r = old.get(index).getColor().getRed() + stepR;

                    double differenceG = pixels[x][y].getGreen() - old.get(index).getColor().getGreen();
                    double stepG = differenceG / steps * i;
                    double g = old.get(index).getColor().getGreen() + stepG;

                    double differenceB = pixels[x][y].getBlue() - old.get(index).getColor().getBlue();
                    double stepB = differenceB / steps * i;
                    double b = old.get(index).getColor().getBlue() + stepB;

                    step[x][y] = Color.color(r, g, b);
                    index++;
                }
            }
            MainWindow.controller.getSerial().addUpdate(MainWindow.controller.applyOverlay(step));
        }
    }
}
