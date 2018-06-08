package de.drachenfrucht1.custome;

/**
 * Created by Dominik on 24.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public interface CustomeEffect {
  void run(int width, int height);
  void stop();
  String getName();
  boolean isRunning();
}
