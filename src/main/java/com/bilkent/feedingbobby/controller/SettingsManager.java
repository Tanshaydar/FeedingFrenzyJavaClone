package com.bilkent.feedingbobby.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.bilkent.feedingbobby.model.Settings;

public class SettingsManager {

    private static final String SETTINGS_FILE_NAME = "settings.dat";

    private static SettingsManager instance = null;

    private Settings settings;

    public static synchronized SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    public void writeSettingsToFile( Settings settings) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SETTINGS_FILE_NAME))) {
            oos.writeObject(settings);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readSettingsFromFile() {
        if (new File(SETTINGS_FILE_NAME).exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SETTINGS_FILE_NAME))) {
                settings = (Settings) ois.readObject();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public Settings getSettings() {
        readSettingsFromFile();
        return settings;
    }
}
