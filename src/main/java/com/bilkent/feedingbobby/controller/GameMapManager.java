package com.bilkent.feedingbobby.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bilkent.feedingbobby.model.Bubble;
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
        int spaceBetweenFish = GamePanel.RESOLUTION.height / (initialNumberOfFish + 1);

        for (int i = 0; i < initialNumberOfFish; i++) {
            boolean onLeft = i % 2 == 0;
            EnemyFish enemyFish = new EnemyFish(0);
            if (onLeft) {
                enemyFish.setPositon(-enemyFish.getWidth(), (i + 1) * spaceBetweenFish);
                enemyFish.setDirection(Direction.RIGHT);
            } else {
                enemyFish.setPositon(GamePanel.RESOLUTION.width + enemyFish.getWidth(), i * spaceBetweenFish + 10);
                enemyFish.setDirection(Direction.LEFT);
            }
            initialGameObjects.add(enemyFish);
        }
        return initialGameObjects;
    }

    public GameObject getNewEnemyFish( List<GameObject> gameObjects, int size) {
        Random random = new Random();

        boolean hasAnyEatable = false;
        for (GameObject gameObject : gameObjects) {
            if (gameObject instanceof EnemyFish && ((EnemyFish) gameObject).getSize() < size) {
                hasAnyEatable = true;
            }
        }

        EnemyFish enemyFish;
        if (hasAnyEatable) {
            enemyFish = new EnemyFish(random.nextInt(size + 1));
        } else {
            enemyFish = new EnemyFish(random.nextInt(size));
        }

        if (random.nextBoolean()) {
            enemyFish.setPositon(-enemyFish.getWidth(), random.nextInt(GamePanel.RESOLUTION.height) + 10);
            enemyFish.setDirection(Direction.RIGHT);
        } else {
            enemyFish.setPositon(GamePanel.RESOLUTION.width + enemyFish.getWidth(),
                    random.nextInt(GamePanel.RESOLUTION.height) + 10);
            enemyFish.setDirection(Direction.LEFT);
        }

        return enemyFish;
    }

    public List<GameObject> addBubbles() {
        List<GameObject> bubbles = new ArrayList<>();
        Random random = new Random();
        int bubbleSize = random.nextInt(7) + 7;
        int y = GamePanel.RESOLUTION.height + bubbleSize;
        int x = random.nextInt((GamePanel.RESOLUTION.width - bubbleSize * 2)) + bubbleSize / 2;
        for (int i = 0; i < random.nextInt(4) + 2; i++) {
            Bubble bubble = new Bubble();
            bubble.setWidth(bubbleSize);
            bubble.setHeight(bubbleSize);
            bubble.setX(x);
            bubble.setY(y + i * bubbleSize + 3);
            bubbles.add(bubble);
        }
        return bubbles;
    }
}
