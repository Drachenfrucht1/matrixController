package de.drachenfrucht1.app;

import javafx.scene.paint.Color;
import lombok.Data;

/**
 * Created by Dominik on 23.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public @Data
class Led {
  private Color color;

  public Led() {
    color = Color.BLACK;
  }

  public Led(Color color) {
    this.color = color;
  }
}
