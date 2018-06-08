package de.drachenfrucht1.webServer;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;

/**
 * Created by Dominik on 08.06.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class WSHandler extends BaseWebSocketHandler {

  private ArrayList<WebSocketConnection> connections = new ArrayList<>();

  public void sendUpdate(String msg) {
    for(WebSocketConnection conn: connections) {
      conn.send(msg.getBytes());
    }
  }

  @Override
  public void onOpen(WebSocketConnection connection) throws Throwable {
    connections.add(connection);
  }

  @Override
  public void onClose(WebSocketConnection connection) throws Throwable {
    connections.remove(connection);
  }

  @Override
  public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
    String[]split = new
  }
}
