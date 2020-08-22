package de.drachenfrucht1.graphics;

import de.drachenfrucht1.app.OverlayScene;
import de.drachenfrucht1.app.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Created by Dominik on 23.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class ControlPanel {

    private final Stage window;
    private final GridPane grid;

    private ControllPanelMode mode;

    public ControlPanel() {

        window = new Stage();

        grid = new GridPane();

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 24; y++) {
                Button b = new Button();
                b.setPrefSize(100, 25);
                GridPane.setConstraints(b, x, y);
                grid.getChildren().add(b);
            }
        }

        window.setScene(new javafx.scene.Scene(grid, 600, 600));

        Image icon64 = new Image("file:icons/control-panel.png");
        window.getIcons().addAll(icon64);

        window.setResizable(false);
    }

    public void reload() {
        switch (mode) {
            case Scenes:
                window.setTitle("KontrollPanel - Szenen");
                if (MainWindow.controller.getProject().getScenes().size() == 0) {
                    return;
                }
                try {
                    for (int i = 0; i < MainWindow.controller.getProject().getScenes().size() || i < 144; i++) {
                        Node n = grid.getChildren().get(i);
                        if (n instanceof Button) {
                            Button b = (Button) n;
                            b.setStyle("-fx-font-size: 8px;");
                            b.setText(MainWindow.controller.getProject().getScenes().get(i).getName());
                            final Scene s = MainWindow.controller.getProject().getScenes().get(i);
                            b.setOnAction(e -> s.fade());
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Unknown stupid error ControlPanel:60");
                }
                break;
            case OverlayScenes:
                window.setTitle("KontrollPanel - OverlaySzenen");
                if (MainWindow.controller.getProject().getOverlayScenes().size() == 0) {
                    return;
                }
                try {
                    for (int i = 0; i < MainWindow.controller.getProject().getOverlayScenes().size() || i < 144; i++) {
                        Node n = grid.getChildren().get(i);
                        if (n instanceof Button) {
                            Button b = (Button) n;
                            b.setStyle("-fx-font-size: 8px;");
                            b.setText(MainWindow.controller.getProject().getOverlayScenes().get(i).getName());
                            final OverlayScene s = MainWindow.controller.getProject().getOverlayScenes().get(i);
                            b.setOnAction(e -> MainWindow.controller.setOverlay(s));
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.err.println("Unknown stupid error ControlPanel:60");
                }
                break;
            //TODO EffectPanel
        }
    }

    public void show(ControllPanelMode mode) {
        this.mode = mode;
        window.show();
        reload();
    }

    public enum ControllPanelMode {
        Scenes,
        OverlayScenes,
        Effects
    }
}
