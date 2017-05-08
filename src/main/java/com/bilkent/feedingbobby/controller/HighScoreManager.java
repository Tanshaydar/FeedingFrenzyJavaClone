package com.bilkent.feedingbobby.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.bilkent.feedingbobby.model.HighScore;

public class HighScoreManager {

    private static final String HIGH_SCORE_FILE_NAME = "highscores.dat";

    private static HighScoreManager instance = null;

    private List<HighScore> highScores = new ArrayList<>();

    public static synchronized HighScoreManager getInstance() {
        if (instance == null) {
            instance = new HighScoreManager();
        }
        return instance;
    }

    public void writeScoreToFile( HighScore highScore) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE_NAME))) {
            highScores.add(highScore);
            oos.writeObject(highScores);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void readScoresFromFile() {
        if (new File(HIGH_SCORE_FILE_NAME).exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE_NAME))) {
                highScores.clear();
                highScores.addAll((List<HighScore>) ois.readObject());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<HighScore> getHighScores() {
        readScoresFromFile();
        return highScores;
    }

}
