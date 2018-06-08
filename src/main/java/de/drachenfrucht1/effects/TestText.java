package de.drachenfrucht1.effects;
import de.drachenfrucht1.custome.CustomeEffect;
import de.drachenfrucht1.graphics.MainWindow;
import javafx.scene.paint.Color;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Dominik on 01.06.2018.
 * Version: 0.0.1
 * Project: matrixControllerCEffects
 */
public class TestText implements CustomeEffect {

  private Color aaaaaaaaaaa = Color.GREEN;
  private Color bbbbbbbbbbb = Color.BLACK;
  private int fadeTime = 2000;//fadetime in ms
  private int width = 14;//14 px in width

  private boolean activated = false;
  Thread t;

  private Color[][] preset = new Color[][] //preset[y][x]
          {{bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb},
           {bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb},
           {aaaaaaaaaaa, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, aaaaaaaaaaa},
           {bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb},
           {bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb},
           {bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb},
           {bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, aaaaaaaaaaa, aaaaaaaaaaa, aaaaaaaaaaa, bbbbbbbbbbb, bbbbbbbbbbb, aaaaaaaaaaa, bbbbbbbbbbb},
           {bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb},
           {bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb},
           {bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb, bbbbbbbbbbb}};

  public void run(int width, int height) {
    if(height == 10 && width == 13) {
      activated = true;
      t = new Thread(() -> {
        ZonedDateTime time1 = ZonedDateTime.now();
        int startIndex = 0;
        int steps = fadeTime/16;
        int subSteps = steps/(this.width-13);
        for(int stepIndex = 0; stepIndex < steps; stepIndex++) {
          Color[][] step = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getWidth()];
          for(int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
            for(int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
              step[x][y] = preset[y][x+startIndex];
            }
          }
          for (int subStepIndex = 0; subStepIndex < subSteps; subStepIndex++) {
            MainWindow.controller.getSerial().addUpdate(MainWindow.controller.applyOverlay(step));
          }
          startIndex++;
        }
        long needed;
        if((needed = ChronoUnit.MILLIS.between(time1, ZonedDateTime.now())) < fadeTime) {
          try {
            Thread.sleep(fadeTime-needed-10);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      });
      t.start();
    }
  }

  public void stop() {
    activated = false;
    t.stop();
  }

  public boolean isRunning() {
    return activated;
  }


  public String getName() {
    return "Test Text";
  }
}
