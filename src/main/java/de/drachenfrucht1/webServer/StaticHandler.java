package de.drachenfrucht1.webServer;

import org.webbitserver.HttpControl;
import org.webbitserver.HttpHandler;
import org.webbitserver.HttpRequest;
import org.webbitserver.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Dominik on 08.06.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class StaticHandler implements HttpHandler {

  private String err404 = "<!DOCTYPE html>\n" +
          "<html lang=\"en\">\n" +
          "<head>\n" +
          "    <meta charset=\"UTF-8\">\n" +
          "    <title>404- Page not found</title>\n" +
          "</head>\n" +
          "<body>\n" +
          "    <h1 style=\"text-align: center\">404</h1>\n" +
          "    <p style=\"text-align: center\">The page you're looking for seems to be not available.</p>\n" +
          "</body>\n" +
          "</html>";

  @Override
  public void handleHttpRequest(HttpRequest request, HttpResponse response, HttpControl control) {
    if(request.uri().equals("/")) {
      String content = "";
      try {
        Scanner scanner = new Scanner(new File("webinterface/index.html"));
        while(scanner.hasNextLine()) {
          content += scanner.nextLine() + "\n";
        }
        scanner.close();
      } catch (IOException e) {
        content = err404;
        response.status(404);
      }
      response.header("Content-type", "text/html").content(content).end();
    } else if(request.uri().equals("/ws_connect")) {
      control.nextHandler();
    } else {
      String content = "";
      try {
        Scanner scanner = new Scanner(new File("webinterface/" + request.uri().substring(1)));
        while(scanner.hasNextLine()) {
          content += scanner.nextLine() + "\n";
        }
        scanner.close();
      } catch (IOException e) {
        content = err404;
        response.status(404);
      }
      response.header("Content-type", "text/html").content(content).end();
    }
  }
}
