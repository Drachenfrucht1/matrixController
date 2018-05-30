package de.drachenfrucht1.custome;

import javafx.scene.control.Button;

/**
 * Created by Dominik on 24.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public interface CustomeEffect {
  Button getButton();
  void run(int width, int height);
  String getName();
}
