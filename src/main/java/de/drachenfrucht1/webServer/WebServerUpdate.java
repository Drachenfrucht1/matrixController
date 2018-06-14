package de.drachenfrucht1.webServer;

import javafx.scene.paint.Color;
import lombok.Data;

/**
 * Created by Dominik on 08.06.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public @Data
class WebServerUpdate {

  private final Color[][] pixels;
  private final int width;
  private final int height;
  private final Update mode;

  public enum Update {
    pixel
  }
}
