package de.drachenfrucht1.app;

import de.drachenfrucht1.graphics.MainWindow;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.Getter;

/**
 * Created by Dominik on 30.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public @Data
class OverlayScene {

  private final @Getter String name;
  private final @Getter Color[][] pixels;

  public static OverlayScene getBlankOverlay() {
    Color[][] pixels = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getHeight()];
    for(int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
      for(int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
        pixels[x][y] = Color.TRANSPARENT;
      }
    }
    return new OverlayScene("Blank", pixels);
  }

}
