package com.bilkent.feedingbobby.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bilkent.feedingbobby.model.Direction;
import com.bilkent.feedingbobby.model.EnemyFish;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.JellyFish;
import com.bilkent.feedingbobby.view.GamePanel;

public class GameMapManager {

    private int level = 1;
    private int initialNumberOfFish = 6;

    public GameMapManager() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel( int level) {
        this.level = level;
    }

    public int getNumberOfFish() {
        return initialNumberOfFish;
    }

    public void setNumberOfFish( int numberOfFish) {
        this.initialNumberOfFish = numberOfFish;
    }

    public GameObject getSpecialFish() {
        Random random = new Random();
        switch (random.nextInt(10)) {
        case 0:
            return new JellyFish();
        }
        return null;
    }

    public List<GameObject> getInitialMapObjects() {
        List<GameObject> initialGameObjects = new ArrayList<>();
        int spaceBetweenFish = GamePanel.RESOLUTION.height / initialNumberOfFish;

        for (int i = 0; i < initialNumberOfFish; i++) {
            boolean onLeft = i % 2 == 0;
            EnemyFish enemyFish = new EnemyFish(0);
            if (onLeft) {
                enemyFish.setPositon(5, i * spaceBetweenFish + 10);
                enemyFish.setDirection(Direction.RIGHT);
            } else {
                enemyFish.setPositon(GamePanel.RESOLUTION.width - enemyFish.getWidth() - 5, i * spaceBetweenFish + 10);
                enemyFish.setDirection(Direction.LEFT);
            }
            initialGameObjects.add(enemyFish);
        }
        return initialGameObjects;
    }
}
