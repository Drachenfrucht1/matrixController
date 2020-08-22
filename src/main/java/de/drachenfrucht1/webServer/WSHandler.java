package de.drachenfrucht1.webServer;

import com.google.gson.Gson;
import de.drachenfrucht1.graphics.MainWindow;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;

/**
 * Created by Dominik on 08.06.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class WSHandler extends BaseWebSocketHandler {

    private final ArrayList<WebSocketConnection> connections = new ArrayList<>();
    private final Gson gson = new Gson();

    MainWindow mainWindow;

    public WSHandler(MainWindow main) {
        mainWindow = main;
    }

    public void sendUpdate(WebServerUpdate update) {
        String msg = gson.toJson(update);
        for (WebSocketConnection conn : connections) {
            conn.send(msg.getBytes());
        }
    }

    @Override
    public void onOpen(WebSocketConnection connection) {
        connections.add(connection);
    }

    @Override
    public void onClose(WebSocketConnection connection) {
        connections.remove(connection);
    }

    @Override
    public void onMessage(WebSocketConnection connection, String msg) {
        String[] split = msg.split(":");

        switch (split[0]) {
            case "party":
                break;
            case "":
                break;
            default:
                break;
        }
    }
}
