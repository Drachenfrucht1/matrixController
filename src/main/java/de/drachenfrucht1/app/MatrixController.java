package de.drachenfrucht1.app;

import de.drachenfrucht1.graphics.MainWindow;
import de.drachenfrucht1.webServer.Server;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class MatrixController {

  private @Getter @Setter Project project;
  private @Getter SerialComm serial;
  private @Getter ArrayList<Pixel> pixels = new ArrayList<>();

  private @Getter @Setter OverlayScene overlay;

  private @Getter @Setter Server server;

  public MatrixController() {
    project = new Project("Unnamed", 13, 10);
    serial = new SerialComm();
  }

  /**
   * FUNCTION: sendLiveUpdate()
   * PURPOSE: send the live status of the viewport to the arduino
   */
  public void sendLiveUpdate() {
    Color[][] colors = new Color[project.getWidth()][project.getHeight()];
    int index = 0;
    for (int x = 0; x < project.getWidth(); x++) {
      for (int y = 0; y < project.getWidth(); y++) {
        colors[x][y] = pixels.get(index).getColor();
        index++;
      }
    }
    serial.addUpdate(applyOverlay(colors));
  }

  public Color[][] applyOverlay(Color[][] input) {
    if(overlay == null) overlay = OverlayScene.getBlankOverlay();
    Color[][] pixels = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getHeight()];
    for(int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
      for(int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
        if(overlay.getPixels()[x][y] == Color.TRANSPARENT) {
          pixels[x][y] = input[x][y];
        } else {
          pixels[x][y] = overlay.getPixels()[x][y];
        }
      }
    }
    return pixels;
  }

  public void updatePixel(Color[][] colors) {
    if (colors.length == 0) return;
    Platform.runLater(() -> {
      int index = 0;
      for (int x = 0; x < getProject().getWidth(); x++) {
        for (int y = 0; y < getProject().getHeight(); y++) {
          getPixels().get(index).setColor(colors[x][y]);
          index++;
        }
      }
    });
  }
}
