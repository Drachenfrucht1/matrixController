package de.drachenfrucht1.app;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import de.drachenfrucht1.graphics.MainWindow;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class SerialComm {

  private @Getter boolean connected = false;
  private SerialPort port;
  private ArrayList<String> queue = new ArrayList<>();
  private ArrayList<Color[][]> queue2 = new ArrayList<>();
  private String last = "";
  private Color[][] last2 = new Color[0][0];
  Thread sendThread = new Thread(() -> {
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    while (connected) {
      ZonedDateTime time1 = ZonedDateTime.now();
      if (!queue.isEmpty()) {
        try {
          last = queue.get(0);
          last2 = queue2.get(0);
          queue.remove(0);
          queue2.remove(0);
        } catch (Exception e) {
          e.printStackTrace();
        }
      } else if (last.equals("")) {
        String msg = "";
        for (int i = 0; i < MainWindow.controller.getProject().getLeds(); i++) {
          msg += "255.255.255:";
        }
        msg += "!";
        last = msg;
      }
      byte[] b = last.getBytes();
      port.writeBytes(b, b.length);
      MainWindow.controller.updatePixel(last2);
      try {
        int needed = (int) ChronoUnit.MILLIS.between(time1, ZonedDateTime.now());
        System.out.println("Needed: " + needed);
        if (needed < 16) Thread.sleep(16 - needed);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  });

  /**
   * FUNCTION: getPorts()
   * PURPOSE: lists all ports
   *
   * @return an array with all ports
   */
  public String[] getPorts() {
    String[] r = new String[SerialPort.getCommPorts().length];

    for (int i = 0; i < SerialPort.getCommPorts().length; i++) {
      r[i] = SerialPort.getCommPorts()[i].getSystemPortName();
    }
    return r;
  }

  /**
   * FUNCTION: connect()
   * PURPOSE: connect to a serial port
   *
   * @param dName descriptive name of port
   */
  public void connect(String dName) {
    if (connected) {
      disconnect();
    }
    port = SerialPort.getCommPort(dName);
    port.setBaudRate(250000);
    port.openPort();
    connected = true;
    sendThread.start();
    port.addDataListener(new SerialPortDataListener() {
      @Override
      public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
      @Override
      public void serialEvent(SerialPortEvent event)
      {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
          return;
        byte[] newData = new byte[port.bytesAvailable()];
        int numRead = port.readBytes(newData, newData.length);
        System.out.println("Message: " + new String(newData, StandardCharsets.UTF_8));
      }
    });
    System.out.println("Connected to " + dName);
  }

  public void disconnect() {
    if (!connected) return;
    port.closePort();
    connected = false;
    sendAllBlack();
    try {
      sendThread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * FUNCTION: addUpdate()
   * PURPOSE: add an update for the arduino to the queue
   *
   * @param pixels the colors of the pixels (2D-Array)
   */
  public void addUpdate(Color[][] pixels) {
    if (connected) {
      String msg = "";
      for (int x = 0; x < pixels.length; x++) {
        Color[] row = pixels[x];
        for (int y = 0; y < row.length; y++) {
          int r = (int) (row[y].getRed() * 255);
          int g = (int) (row[y].getGreen() * 255);
          int b = (int) (row[y].getBlue() * 255);
          msg += r + "." + g + "." + b + ":";
        }
      }
      msg += "!";
      queue.add(msg);
      System.out.println(msg);
      System.out.println("Size: " + queue.size());
      queue2.add(pixels);
    }
  }

  public void clearQueue() {
    queue.clear();
    queue2.clear();
  }

  public void sendAllBlack() {
    Color[][] colors = new Color[MainWindow.controller.getProject().getWidth()][MainWindow.controller.getProject().getWidth()];
    for (int x = 0; x < MainWindow.controller.getProject().getWidth(); x++) {
      for (int y = 0; y < MainWindow.controller.getProject().getHeight(); y++) {
        colors[x][y] = Color.BLACK;
      }
    }
    addUpdate(colors);
  }

  public int queueSize() {
    return queue.size();
  }
}
