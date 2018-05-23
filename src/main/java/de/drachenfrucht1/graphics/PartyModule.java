package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.Led;
import de.drachenfrucht1.app.Pixel;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
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
  private int maxR = 255;
  private int maxG = 1;
  private int maxB = 255;

  private boolean black = false; // wenn false keine schwarzen
  private boolean mix = true;
  private int fadeTime = 150;
  private int beat = 300;

  private Led[][] next = new Led[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getWidth()];
  //private boolean[][] activatedLeds = new boolean[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getWidth()];
  private boolean[][] activatedLeds = {{true, true, true}, {true, true, true}, {true, true, true}};

  public PartyModule() {
    window = new Stage();

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
        party.start();
      }
    });

    BorderPane pane = new BorderPane();
    pane.setBottom(start);

    Scene scene = new Scene(pane, 300, 300);
    window.setScene(scene);

    window.setOnCloseRequest(e -> activated = false);
    window.setTitle("Party Module");
    window.show();
  }

  private void save() {

  }

  Thread party = new Thread(() -> {
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
                int color = random.nextInt(2);
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
