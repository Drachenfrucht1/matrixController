package de.drachenfrucht1.custome;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Created by Dominik on 24.05.2018.
 * Version: 0.0.1
 * Project: matrixController
 */
public class EffectLoader {

    public static List<CustomeEffect> loadEffects() {
        File[] jars = new File("customeEffects").listFiles(new JARFileFilter());
        ClassLoader cl = new URLClassLoader(fileArrayToURLArray(jars));
        List<Class<CustomeEffect>> effectClasses = extractClassesFromJARs(jars, cl);

        ClassLoader cl2 = ClassLoader.getSystemClassLoader();
        try {
            effectClasses.add((Class<CustomeEffect>) cl2.loadClass("de.drachenfrucht1.effects.TestText"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return createCustomeEffects(effectClasses);
    }

    private static List<CustomeEffect> createCustomeEffects(List<Class<CustomeEffect>> classes) {
        List<CustomeEffect> effects = new ArrayList<>(classes.size());
        for (Class<CustomeEffect> c : classes) {
            try {
                effects.add(c.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return effects;
    }

    private static URL[] fileArrayToURLArray(File[] files) {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < files.length; i++) {
            try {
                urls[i] = files[i].toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    private static List<Class<CustomeEffect>> extractClassesFromJARs(File[] jar, ClassLoader cl) {
        List<Class<CustomeEffect>> classes = new ArrayList<>();
        for (File f : jar) {
            classes.addAll(extractClassesFromJAR(f, cl));
        }

        return classes;
    }

    private static List<Class<CustomeEffect>> extractClassesFromJAR(File f, ClassLoader cl) {
        List<Class<CustomeEffect>> classes = new ArrayList<>();
        try {
            JarInputStream jaris = new JarInputStream(new FileInputStream(f));
            JarEntry ent = null;
            while ((ent = jaris.getNextJarEntry()) != null) {
                if (ent.getName().toLowerCase().endsWith(".class")) {
                    Class<?> cls = cl.loadClass(ent.getName().substring(0, ent.getName().length() - 6).replace('/', '.'));
                    if (isClassEffect(cls)) {
                        classes.add((Class<CustomeEffect>) cls);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }

    private static boolean isClassEffect(Class<?> cls) {
        for (Class<?> i : cls.getInterfaces()) {
            if (i.equals(CustomeEffect.class)) {
                return true;
            }
        }
        return false;
    }

    private static class JARFileFilter implements FileFilter {

        @Override
        public boolean accept(File f) {
            return f.getName().toLowerCase().endsWith(".jar");
        }
    }
}
