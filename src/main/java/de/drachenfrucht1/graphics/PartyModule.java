package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.Led;
import de.drachenfrucht1.app.Pixel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Dominik on 23.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class PartyModule {

  private Stage window;
  private boolean activated = false;

  //Settings
  private int maxR;
  private int maxG;
  private int maxB;

  private boolean black; // wenn false keine schwarzen
  private boolean mix;
  private int fadeTime = 0;
  private int beat = 300;

  private Led[][] next = new Led[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getWidth()];
  //private boolean[][] activatedLeds = new boolean[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getWidth()];
  private boolean[][] activatedLeds = {{true, true, true}, {true, true, true}, {true, true, true}};

  //party send thread
  Thread party;

  //Beat
  boolean recording = false;
  ZonedDateTime firstT;

  //ui elements
  CheckBox blackC;
  CheckBox mixC;
  Slider redS;
  Slider greenS;
  Slider blueS;
  TextField fadeT;

  public PartyModule() {
    window = new Stage();

    Label redL = new Label("Rot: ");
    redS = new Slider();
    redS.setMin(1);
    redS.setMax(255);
    redS.setValue(255);
    redS.setBlockIncrement(1);
    redS.setShowTickLabels(true);

    Label greenL = new Label("Grün: ");
    greenS = new Slider();
    greenS.setMin(1);
    greenS.setMax(255);
    greenS.setValue(255);
    greenS.setBlockIncrement(1);
    greenS.setShowTickLabels(true);

    Label blueL = new Label("Blau: ");
    blueS = new Slider();
    blueS.setMin(1);
    blueS.setMax(255);
    blueS.setValue(255);
    blueS.setBlockIncrement(1);
    blueS.setShowTickLabels(true);

    Label blackL = new Label("Schwarze Leds: ");
    blackC = new CheckBox();
    blackC.setSelected(false);

    Label mixL = new Label("Mischfarben: ");
    mixC = new CheckBox();
    mixC.setSelected(true);

    Label fadeL = new Label("Fadetime in ms");
    fadeT = new TextField();
    fadeT.setText("0");

    Label beatL = new Label("Beat");
    Button beatB = new Button();
    beatB.setStyle("-fx-background-color: #ccd4e2;");
    beatB.setOnAction(e -> {
      if(recording) {
        setBeat((int)ChronoUnit.MILLIS.between(firstT, ZonedDateTime.now()));
        int bpm = 1000/beat * 60;
        window.setTitle("Party Module - BPM: " + bpm);
        beatB.setStyle("-fx-background-color: #ccd4e2;");
        recording = false;
      } else {
        firstT = ZonedDateTime.now();
        beatB.setStyle("-fx-background-color: #000000;");
        recording = true;
      }
    });

    Button save = new Button("Save");
    save.setPrefSize(50, 50);
    save.setOnAction(e -> save());

    Button start = new Button();
    start.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\dj.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      start.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }

    start.setOnAction(e -> {
      if(activated) {
        activated = false;
        try {
          Image image = new Image(new FileInputStream(new File("icons\\dj.png")), 30, 30, false, true);
          ImageView imageV = new ImageView(image);
          start.setGraphic(imageV);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        try {
          party.join();
          System.out.println("Party stopped");
        } catch (InterruptedException e1) {
          e1.printStackTrace();
        }
      } else {
        activated = true;
        try {
          Image image = new Image(new FileInputStream(new File("icons\\dj-bunt.png")), 30, 30, false, true);
          ImageView imageV = new ImageView(image);
          start.setGraphic(imageV);
        } catch (Exception ex) {
          ex.printStackTrace();
        }
        save();
        createParty();
        party.start();
      }
    });

    GridPane grid = new GridPane();
    GridPane.setConstraints(redL, 0, 0);
    GridPane.setConstraints(redS, 1, 0);
    GridPane.setConstraints(greenL, 0, 1);
    GridPane.setConstraints(greenS, 1, 1);
    GridPane.setConstraints(blueL, 0, 2);
    GridPane.setConstraints(blueS, 1, 2);
    GridPane.setConstraints(blackL, 0, 3);
    GridPane.setConstraints(blackC, 1, 3);
    GridPane.setConstraints(mixL, 0, 4);
    GridPane.setConstraints(mixC, 1, 4);
    GridPane.setConstraints(fadeL, 0, 5);
    GridPane.setConstraints(fadeT, 1, 5);
    GridPane.setConstraints(beatL, 0, 6);
    GridPane.setConstraints(beatB, 1, 6);
    GridPane.setConstraints(save, 0, 7);
    GridPane.setConstraints(start, 1, 7);

    grid.getChildren().addAll(save, start, redL, redS, blueS, blueL, greenS, greenL, blackC, blackL, mixL, mixC, fadeL, fadeT, beatL, beatB);

    Scene scene = new Scene(grid, 300, 300);
    window.setScene(scene);

    Image icon64 = new Image("file:icons\\dj.png");
    window.getIcons().addAll(icon64);

    window.setOnCloseRequest(e -> activated = false);
    window.setTitle("Party Module");
    window.show();
  }

  private void save() {
    mix = mixC.isSelected();
    black = blackC.isSelected();
    maxR = (int)redS.getValue();
    maxG = (int)greenS.getValue();
    maxB = (int)blueS.getValue();
    fadeTime = Integer.parseInt(fadeT.getText());
  }

  private void createParty() {
    party = new Thread(() -> {
      Random random = new Random();

      for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
        for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
          next[x][y] = new Led();
        }
      }
      while(activated) {
        ZonedDateTime time1 = ZonedDateTime.now();
        for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
          for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
            if(activatedLeds[x][y]) {
              if(!mix) {
                do {
                  int color = random.nextInt(3);
                  switch (color) {
                    case 0:
                      next[x][y].setColor(Color.rgb(random.nextInt(maxR), 0, 0));
                      break;
                    case 1:
                      next[x][y].setColor(Color.rgb(0, random.nextInt(maxG), 0));
                      break;
                    case 2:
                      next[x][y].setColor(Color.rgb(0, 0, random.nextInt(maxB)));
                      break;
                  }
                } while(next[x][y].getColor() == Color.BLACK && !black);
              } else {
                do {
                  next[x][y].setColor(Color.rgb(random.nextInt(maxR), random.nextInt(maxG), random.nextInt(maxB)));
                } while(next[x][y].getColor() == Color.BLACK && !black);
              }
            }
          }
        }

        if(fadeTime > beat) {
          fadeTime = beat-20;
        }

        int steps = fadeTime/16;

        if(steps == 0) steps = 1;

        ArrayList<Pixel> old = MainWindow.controller.getPixels();
        for (int i = 1; i <= steps; i++) {
          int index = 0;
          Color[][] step = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getHeight()];
          for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
            for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
              double differenceR = next[x][y].getColor().getRed() - old.get(index).getColor().getRed();
              double stepR = differenceR / steps * i;
              double r = old.get(index).getColor().getRed() + stepR;

              double differenceG = next[x][y].getColor().getGreen() - old.get(index).getColor().getGreen();
              double stepG = differenceG / steps * i;
              double g = old.get(index).getColor().getGreen() + stepG;

              double differenceB = next[x][y].getColor().getBlue() - old.get(index).getColor().getBlue();
              double stepB = differenceB / steps * i;
              double b = old.get(index).getColor().getBlue() + stepB;

              if(r < 0) r = 0;
              if(g < 0) g = 0;
              if(b < 0) b = 0;

              step[x][y] = Color.color(r, g, b);
              index++;
            }
          }
          MainWindow.controller.getSerial().addUpdate(step);
        }
        long needed;
        if((needed = ChronoUnit.MILLIS.between(time1, ZonedDateTime.now())) < beat) {
          try {
            Thread.sleep(beat-needed-30);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
      MainWindow.controller.getSerial().clearQueue();
    });
  }

  private void setBeat(int b) {
    beat = b;
  }
}
