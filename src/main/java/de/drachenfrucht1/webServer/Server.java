package de.drachenfrucht1.webServer;

import lombok.Getter;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

/**
 * Created by Dominik on 08.06.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class Server {

  private @Getter WebServer webServer;

  public Server() {
    webServer = WebServers.createWebServer(8080);

    webServer.add("/ws_connect", new WSHandler());
    webServer.start();
  }
}
