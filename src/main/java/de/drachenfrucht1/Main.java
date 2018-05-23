package de.drachenfrucht1;

import de.drachenfrucht1.app.MatrixController;
import de.drachenfrucht1.graphics.MainWindow;
import javafx.application.Application;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class Main {

  public static void main(String[] args) {
    MainWindow.controller = new MatrixController();
    Application.launch(MainWindow.class, args);
  }
}
