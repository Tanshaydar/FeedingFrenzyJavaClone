package com.bilkent.feedingbobby.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bilkent.feedingbobby.model.Direction;
import com.bilkent.feedingbobby.model.EnemyFish;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;
import com.bilkent.feedingbobby.view.GamePanel;

public class GameManager {
    // FOR CAPPING GAME AT 60 FPS
    private static final long REFRESH_INTERVAL_MS = 17;

    private boolean isGameRunning = false;

    private GamePanel gamePanel;
    private GameMapManager gameMapManager;

    private List<GameObject> gameObjects = new ArrayList<>();
    private PlayerFish playerFish;

    public GameManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gameMapManager = new GameMapManager();

        if (playerFish == null) {
            playerFish = new PlayerFish();
        }
        gameObjects.add(playerFish);

        this.gamePanel.setLevel(gameMapManager.getLevel());
    }

    public void initialize() {

        int spaceBetweenFish = GamePanel.RESOLUTION.height / gameMapManager.getNumberOfFish();

        for (int i = 0; i < gameMapManager.getNumberOfFish(); i++) {
            boolean onLeft = i % 2 == 0;
            EnemyFish enemyFish = new EnemyFish(0);
            if (onLeft) {
                enemyFish.setPositon(5, i * spaceBetweenFish + 10);
                enemyFish.setDirection(Direction.RIGHT);
            } else {
                enemyFish.setPositon(GamePanel.RESOLUTION.width - enemyFish.getWidth() - 5, i * spaceBetweenFish + 10);
                enemyFish.setDirection(Direction.LEFT);
            }
            gameObjects.add(enemyFish);
        }
        isGameRunning = true;
    }

    public void startGame() {
        initialize();
        Thread thread = new Thread(new GameLoop());
        thread.start();
    }

    public void stopGame() {
        isGameRunning = false;
    }

    private class GameLoop implements Runnable {

        @Override
        public void run() {

            long startTime = System.currentTimeMillis();

            while (isGameRunning) {
                long oneTickOfGame = System.currentTimeMillis();
                // Handle input
                handleInput();
                // Handle logic
                if (System.currentTimeMillis() - startTime > 3000) {
                    handleLogic();
                } else {

                }
                // Handle drawing
                handleDrawing();

                try {
                    Thread.sleep(REFRESH_INTERVAL_MS - (System.currentTimeMillis() - oneTickOfGame));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void handleInput() {
        for (GameObject gameObject : gameObjects) {
            if (gameObject.isControlledByMouse()) {
                gameObject.setDirection(InputManager.getInstance().getChangeDirection());
                gameObject.setPosition(InputManager.getInstance().getMousePoint());
            }
        }

        for (GameObject gameObject : gameObjects) {
            if (gameObject.isControlledByAi()) {
                gameObject.move();
            }
        }
    }

    private void handleLogic() {
        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (gameObject.isMarkedForDestroying()) {
                iterator.remove();
            }
        }

        for (GameObject gameObject : gameObjects) {
            if (gameObject != playerFish && gameObject.intersects(playerFish.getBoundingBox())) {
                gameObject.setMarkedForDestroying(true);
                gameObject.updateState(this, gameMapManager, playerFish);
            }
        }
    }

    private void handleDrawing() {
        gamePanel.draw(gameObjects);
    }

}
