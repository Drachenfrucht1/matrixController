package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.MatrixController;
import de.drachenfrucht1.app.Pixel;
import de.drachenfrucht1.app.PixelMode;
import de.drachenfrucht1.app.Project;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class MainWindow extends Application {

  public static MatrixController controller;
  private Stage window;
  private GridPane grid;

  public void start(Stage primaryStage) throws Exception {
    window = primaryStage;

    //Menubar
    MenuBar menu = new MenuBar();
    menu.setBackground(Background.EMPTY);
    //
    Menu project = new Menu("Projekt");
    //
    MenuItem newI = new MenuItem("New");
    newI.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
    newI.setOnAction(e -> {
      controller.setProject(new Project("Unnamed", 10, 10));
      reload();
      window.setTitle("Matrix Controller - " + controller.getProject().getName());
    });
    //
    MenuItem openI = new MenuItem("Open");
    openI.setAccelerator(KeyCombination.keyCombination("SHORTCUT+O"));
    openI.setOnAction(e -> {
      FileChooser fc = new FileChooser();
      fc.getExtensionFilters().add(new ExtensionFilter("Matrix Controller Project", "*.mcp"));
      fc.setInitialDirectory(new File("."));
      File selected = fc.showOpenDialog(window);
      if (selected.exists()) {
        controller.setProject(Project.load(selected));
        reload();
        window.setTitle("Matrix Controller - " + controller.getProject().getName());
      }
    });
    //
    MenuItem exitI = new MenuItem("Exit");
    exitI.setOnAction(e -> {
      controller.getSerial().disconnect();
      System.exit(0);
    });
    //
    project.getItems().addAll(newI, openI, new SeparatorMenuItem(), exitI);
    menu.getMenus().add(project);

    //Toolbar
    //Controlpanel
    Button controlPanelB = new Button();
    controlPanelB.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\control-panel.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      controlPanelB.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }
    controlPanelB.setOnAction(e -> new ControlPanel());

    Tooltip controlPanelT = new Tooltip();
    controlPanelT.setText("Kontrollpanel");
    controlPanelB.setTooltip(controlPanelT);

    //Scene editor
    Button sceneEditorB = new Button();
    sceneEditorB.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\theatre-stage.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      sceneEditorB.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }
    sceneEditorB.setOnAction(e -> new SceneEditor());

    Tooltip scenenEditorT = new Tooltip();
    sceneEditorB.setText("Szenen Editor");
    sceneEditorB.setTooltip(scenenEditorT);
    //Party module
    Button partyModuleB = new Button();
    sceneEditorB.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\dj.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      partyModuleB.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }
    partyModuleB.setOnAction(e -> new PartyModule());

    Tooltip partyModuleT = new Tooltip();
    partyModuleT.setText("Szenen Editor");
    partyModuleB.setTooltip(scenenEditorT);
    //
    HBox toolbar = new HBox();
    toolbar.getChildren().addAll(controlPanelB, sceneEditorB, partyModuleB);

    //North
    VBox north = new VBox();
    north.getChildren().addAll(menu, toolbar);

    //Center
    grid = new GridPane();
    grid.setMinSize(750, 700);

    //South
    ObservableList<String> ports = FXCollections.observableArrayList(controller.getSerial().getPorts());
    ComboBox<String> portSelector = new ComboBox<>(ports);
    portSelector.setOnAction(e -> {
      System.out.println(portSelector.getValue());
      controller.getSerial().connect(portSelector.getValue());
    });
    //
    BorderPane pane = new BorderPane();
    pane.setMinSize(800, 800);
    pane.setTop(north);
    pane.setCenter(grid);
    pane.setBottom(portSelector);

    Scene scene = new Scene(pane, 800, 800);
    window.setScene(scene);

    window.setOnCloseRequest(e -> {
      e.consume();
      controller.getSerial().disconnect();
      System.exit(0);
    });

    Image icon64 = new Image("file:icons\\led-bulb_64.png");
    window.getIcons().addAll(icon64);
    window.setTitle("Matrix Controller - " + controller.getProject().getName());

    window.setResizable(true);
    window.show();

    //load all pixel tiles
    reload();
  }

  /**
   * FUNCTION: reload()
   * PURPOSE: create a pixels matching the ones on the project
   */
  private void reload() {
    grid.getChildren().clear();
    int width = (int) grid.getWidth() / controller.getProject().getWidth();
    int height = (int) grid.getHeight() / controller.getProject().getHeight();
    for (int x = 0; x < controller.getProject().getWidth(); x++) {
      for (int y = 0; y < controller.getProject().getHeight(); y++) {
        Pixel pixel = new Pixel(window, PixelMode.mainWindow);
        pixel.setColor(Color.BLACK);
        pixel.setPrefSize(width, height);
        GridPane.setConstraints(pixel, x, y);
        grid.getChildren().add(pixel);
        controller.getPixels().add(pixel);
      }
    }
  }
}
