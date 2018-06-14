package de.drachenfrucht1.webServer;

import de.drachenfrucht1.graphics.MainWindow;
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
  private @Getter WSHandler handler;

  public Server(MainWindow mainWindow) {
    handler = new WSHandler(mainWindow);

    webServer = WebServers.createWebServer(8080);
    webServer.add(new StaticHandler());
    webServer.add("/ws_connect", handler);
    webServer.start();
  }
}
