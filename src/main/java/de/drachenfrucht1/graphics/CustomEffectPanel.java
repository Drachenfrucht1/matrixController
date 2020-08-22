package de.drachenfrucht1.graphics;

import de.drachenfrucht1.custome.CustomeEffect;
import de.drachenfrucht1.custome.EffectLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 24.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class CustomEffectPanel {

    private final Stage window;
    private final GridPane grid;

    private List<CustomeEffect> effects = new ArrayList<>();

    public CustomEffectPanel() {
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

        Button reload = new Button();
        reload.setPrefSize(50, 50);
        try {
            Image image = new Image(new FileInputStream(new File("icons/reload.png")), 30, 30, false, true);
            ImageView imageV = new ImageView(image);
            reload.setGraphic(imageV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        reload.setOnAction(e -> {
            effects.clear();
            effects = EffectLoader.loadEffects();
            reload();
        });

        HBox pane = new HBox();
        pane.getChildren().addAll(grid, reload);

        window.setScene(new Scene(pane, 600, 600));

        Image icon64 = new Image("file:icons/confetti.png");
        window.getIcons().addAll(icon64);

        window.setResizable(false);
        window.setTitle("KontrollPanel - Custom Effekte");
    }

    public void reload() {
        if (effects.size() == 0) {
            return;
        }
        try {
            for (int i = 0; i < effects.size() || i < 144; i++) {
                Node n = grid.getChildren().get(i);
                if (n instanceof Button) {
                    Button b = (Button) n;
                    b.setStyle("-fx-font-size: 8px;");
                    b.setText(effects.get(i).getName());
                    final CustomeEffect effect = effects.get(i);
                    b.setOnAction(e -> {
                        if (effect.isRunning()) {
                            effect.stop();
                        } else {
                            effect.run(MainWindow.controller.getProject().getWidth(), MainWindow.controller.getProject().getHeight());
                        }
                    });
                }
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Unknown stupid error CustomEffectPanel:60");
        }
    }

    public void show() {
        window.show();
    }
}
