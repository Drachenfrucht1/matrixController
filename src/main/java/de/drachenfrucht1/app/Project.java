package de.drachenfrucht1.app;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Dominik on 21.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class Project {

    private @Getter
    @Setter
    String name;
    private @Getter
    int width;
    private @Getter
    int height;
    private @Getter
    int leds;
    private @Getter
    final
    ArrayList<Scene> scenes = new ArrayList<>();
    private @Getter
    final
    ArrayList<Effect> effects = new ArrayList<>();
    private @Getter
    final
    ArrayList<OverlayScene> overlayScenes = new ArrayList<>();


    public Project(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
        leds = width * height;
    }

    /**
     * FUNCTION: load()
     * PURPOSE: load a project from a file
     *
     * @param f the file containing the project
     * @return the loaded project
     */
    public static Project load(File f) {
        Gson gson = new Gson();
        try {
            Scanner s = new Scanner(f);
            Project p = gson.fromJson(s.nextLine(), Project.class);
            s.close();
            return p;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * FUNCTION: addScene()
     * PURPOSE: add a scene to the project
     *
     * @param s scene to add
     */
    public void addScene(Scene s) {
        scenes.add(s);
    }

    /**
     * FUNCTION: addEffect()
     * PURPOSE: add a effect to the project
     *
     * @param e effect to add
     */
    public void addEffect(Effect e) {
        effects.add(e);
    }

    /**
     * FUNCTION: save()
     * PURPOSE: save the project to a file
     *
     * @param f file to save to
     */
    public void save(File f) {
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            Gson gson = new Gson();
            String s = gson.toJson(this);
            FileWriter out = new FileWriter(f);
            out.write(s);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*Getter/Setter*/

    /**
     * FUNCTION: setWidth()
     * PURPOSE: set the width of the led matrix and adjust the total amount of leds
     *
     * @param width new width
     */
    public void setWidth(int width) {
        this.width = width;
        leds = this.width * this.height;
    }

    /**
     * FUNCTION: setHeight()
     * PURPOSE: set the height of the led matrix and adjust the total amount of leds
     *
     * @param height new height
     */
    public void setHeight(int height) {
        this.height = height;
        leds = this.width * this.height;
    }
}
