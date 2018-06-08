package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.MatrixController;
import de.drachenfrucht1.app.Pixel;
import de.drachenfrucht1.app.PixelMode;
import de.drachenfrucht1.app.Project;
import de.drachenfrucht1.graphics.ControlPanel.ControllPanelMode;
import de.drachenfrucht1.graphics.SceneEditor.ScenenEditorMode;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
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
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class MainWindow extends Application {

  public static MatrixController controller;
  private @Getter Stage window;
  private GridPane grid;
  private @Getter ControlPanel controlPanel;
  private CustomEffectPanel customPanel;

  private MainWindow instance;

  public void start(Stage primaryStage) throws Exception {
    instance = this;

    window = primaryStage;

    controlPanel = new ControlPanel();
    customPanel = new CustomEffectPanel();

    //Menubar
    MenuBar menu = new MenuBar();
    menu.setBackground(Background.EMPTY);
    //
    Menu project = new Menu("Projekt");
    //
    MenuItem newI = new MenuItem("Neu...");
    newI.setAccelerator(KeyCombination.keyCombination("SHORTCUT+N"));
    newI.setOnAction(e -> new CreateProject(instance));
    //
    MenuItem openI = new MenuItem("Öffnen...");
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
    MenuItem saveI = new MenuItem("Speichern...");
    saveI.setAccelerator(KeyCombination.keyCombination("SHORTCUT+S"));
    saveI.setOnAction(e -> {
      FileChooser fc = new FileChooser();
      fc.getExtensionFilters().add(new ExtensionFilter("Matrix Controller Project", "*.mcp"));
      fc.setInitialDirectory(new File("."));
      File selected = fc.showSaveDialog(window);
      if(selected != null) {
        controller.getProject().save(selected);
      }
    });
    //
    MenuItem exitI = new MenuItem("Schließen");
    exitI.setOnAction(e -> {
      controller.getSerial().disconnect();
      System.exit(0);
    });
    //
    project.getItems().addAll(newI, openI, saveI, new SeparatorMenuItem(), exitI);
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

    ContextMenu controllPanelM = new ContextMenu();
    MenuItem scenesC = new MenuItem("Szenen");
    scenesC.setOnAction(e -> controlPanel.show(ControllPanelMode.Scenes));
    MenuItem oScenesC = new MenuItem("OverlaySzenen");
    oScenesC.setOnAction(e -> controlPanel.show(ControllPanelMode.OverlayScenes));
    MenuItem effectC = new MenuItem("Effekte");
    effectC.setOnAction(e -> controlPanel.show(ControllPanelMode.Effects));

    controllPanelM.getItems().addAll(scenesC, oScenesC, effectC);

    controlPanelB.setOnAction(e -> controllPanelM.show(window));

    controlPanelB.setContextMenu(controllPanelM);
    Tooltip controlPanelT = new Tooltip();
    controlPanelT.setText("Kontrollpanel");
    controlPanelB.setTooltip(controlPanelT);

    //Custome Effect Panel
    Button customPanelB = new Button();
    customPanelB.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\confetti.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      customPanelB.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }
    customPanelB.setOnAction(e -> customPanel.show());

    Tooltip customPanelT = new Tooltip();
    customPanelT.setText("Custom Effekte");
    customPanelB.setTooltip(customPanelT);

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

    ContextMenu scenenEditorM = new ContextMenu();
    MenuItem scenesE = new MenuItem("Szenen");
    scenesE.setOnAction(e -> new SceneEditor(instance, ScenenEditorMode.Scenes));
    MenuItem oScenesE = new MenuItem("OverlaySzenen");
    oScenesE.setOnAction(e -> new SceneEditor(instance, ScenenEditorMode.OverlayScenes));

    scenenEditorM.getItems().addAll(scenesE, oScenesE);

    sceneEditorB.setOnAction(e -> scenenEditorM.show(window));

    sceneEditorB.setContextMenu(scenenEditorM);

    Tooltip sceneEditorT = new Tooltip();
    sceneEditorT.setText("Szenen Editor");
    sceneEditorB.setTooltip(sceneEditorT);
    //Party module
    Button partyModuleB = new Button();
    partyModuleB.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\dj.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      partyModuleB.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }
    partyModuleB.setOnAction(e -> new PartyModule());

    Tooltip partyModuleT = new Tooltip();
    partyModuleT.setText("Party Module");
    partyModuleB.setTooltip(partyModuleT);
    //All Black
    Button allBlackB = new Button();
    allBlackB.setPrefSize(50, 50);
    try {
      Image image = new Image(new FileInputStream(new File("icons\\rounded-black-square-shape.png")), 30, 30, false, true);
      ImageView imageV = new ImageView(image);
      allBlackB.setGraphic(imageV);
    } catch (Exception e) {
      e.printStackTrace();
    }
    allBlackB.setOnAction(e -> controller.getSerial().sendAllBlack());

    Tooltip allBlackT = new Tooltip();
    allBlackT.setText("Alles schwarz");
    allBlackB.setTooltip(allBlackT);
    //
    HBox toolbar = new HBox();
    toolbar.getChildren().addAll(controlPanelB, customPanelB, sceneEditorB, partyModuleB, allBlackB);

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

    //load all pixel tiles
    reload();

    makeResponsive();

    window.show();
  }

  private void makeResponsive() {
    window.widthProperty().addListener(e -> {
      grid.setPrefSize(window.getWidth()-50, window.getHeight()-100);
      int width = (int) (window.getWidth()-50) / controller.getProject().getWidth();
      int height = (int) (window.getHeight()-100) / controller.getProject().getHeight();
      for(Pixel p : controller.getPixels()) {
        p.setPrefWidth(width);
        p.setPrefHeight(height);
      }
    });

    window.heightProperty().addListener(e -> {
      int width = (int) grid.getWidth() / controller.getProject().getWidth();
      int height = (int) grid.getHeight() / controller.getProject().getHeight();
      for(Pixel p : controller.getPixels()) {
        p.setPrefWidth(width);
        p.setPrefHeight(height);
      }
    });
  }

  /**
   * FUNCTION: reload()
   * PURPOSE: create a pixels matching the ones on the project
   */
  protected void reload() {
    grid.getChildren().clear();
    controller.getPixels().clear();
    int width = (int) (window.getWidth()-50) / controller.getProject().getWidth();
    int height = (int) (window.getHeight()-100) / controller.getProject().getHeight();
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
