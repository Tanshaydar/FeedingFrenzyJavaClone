package com.bilkent.feedingbobby.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.bilkent.feedingbobby.model.Direction;
import com.bilkent.feedingbobby.model.EnemyFish;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;
import com.bilkent.feedingbobby.view.GamePanel;

public class GameManager {
    // FOR CAPPING GAME AT 60 FPS
    private static final long REFRESH_INTERVAL_MS = 17;

    private boolean isGameRunning = false;
    private boolean isGamePaused = false;
    private boolean isGameOver = false;

    private GamePanel gamePanel;
    private GameMapManager gameMapManager;
    private Thread gameThread;
    private List<GameObject> gameObjects = new ArrayList<>();
    private PlayerFish playerFish;

    public GameManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gameMapManager = new GameMapManager();
    }

    public void initialize() {

        isGameRunning = true;
        isGamePaused = false;
        isGameOver = false;

        gameObjects.clear();
        gameObjects.addAll(gameMapManager.getInitialMapObjects());

        if (playerFish == null) {
            playerFish = new PlayerFish();
        }

        this.gamePanel.setLevel(playerFish, gameObjects, gameMapManager.getLevel());
    }

    public void startGame() {
        initialize();
        gameThread = new Thread(new GameLoop());
        gameThread.start();
        playerFish.setCurrentlyActive(false);
    }

    public void stopGame() {
        isGameRunning = false;
    }

    public void pauseGame() {
        isGamePaused = true;
    }

    public void resumeGame() {
        isGamePaused = false;
    }

    private class GameLoop implements Runnable {

        @Override
        public void run() {

            long startTime = System.currentTimeMillis();

            while (isGameRunning) {

                while (isGamePaused) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (checkGameState()) {
                    continue;
                }

                long oneTickOfGame = System.currentTimeMillis();
                // Handle input
                handleInput();
                // Handle logic if player is playable
                if (playerFish.isCurrentlyActive()) {
                    handleLogic();
                }
                // Handle drawing
                handleDrawing();

                if (REFRESH_INTERVAL_MS - (System.currentTimeMillis() - oneTickOfGame) > 0) {
                    try {
                        Thread.sleep(REFRESH_INTERVAL_MS - (System.currentTimeMillis() - oneTickOfGame));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("FPS is decreasing!");
                }
            }

            gamePanel.showGameOverScreen();
        }
    }

    private boolean checkGameState() {
        if (playerFish.getLives() == 0) {
            isGamePaused = false;
            isGameRunning = false;
            isGameOver = true;
            return true;
        }
        return false;
    }

    private void handleInput() {
        playerFish.setDirection(InputManager.getInstance().getChangeDirection());
        playerFish.setPosition(InputManager.getInstance().getMousePoint());

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
        boolean shouldAddNewEnemyFish = false;

        // Remove game objects if they should be removed
        Iterator<GameObject> iterator = gameObjects.iterator();
        while (iterator.hasNext()) {
            GameObject gameObject = iterator.next();
            if (gameObject.isMarkedForDestroying()) {
                iterator.remove();
                shouldAddNewEnemyFish = true;
            }
        }

        // Update game object states
        for (GameObject gameObject : gameObjects) {
            if (gameObject != playerFish && gameObject.intersects(playerFish.getBoundingBox())) {
                gameObject.updateState(this, gameMapManager, playerFish);
            }
        }

        // Should add new enemies?
        if (shouldAddNewEnemyFish) {
            Random random = new Random();

            boolean hasAnyEatable = false;
            for (GameObject gameObject : gameObjects) {
                if (gameObject instanceof EnemyFish && ((EnemyFish) gameObject).getSize() <= playerFish.getSize()) {
                    hasAnyEatable = true;
                }
            }

            EnemyFish enemyFish;
            if (hasAnyEatable) {
                enemyFish = new EnemyFish(random.nextInt(playerFish.getSize() + 1));
            } else {
                enemyFish = new EnemyFish(random.nextInt(playerFish.getSize()));
            }

            if (random.nextBoolean()) {
                enemyFish.setPositon(5, random.nextInt(GamePanel.RESOLUTION.height) + 10);
                enemyFish.setDirection(Direction.RIGHT);
            } else {
                enemyFish.setPositon(GamePanel.RESOLUTION.width - enemyFish.getWidth() - 5,
                        random.nextInt(GamePanel.RESOLUTION.height) + 10);
                enemyFish.setDirection(Direction.LEFT);
            }
            gameObjects.add(enemyFish);
        }

        // Should add special enemies?

    }

    private void handleDrawing() {
        gamePanel.repaint();
    }

    public void addNewObject( GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGamePaused() {
        return isGamePaused;
    }

    public boolean isGameRunning() {
        return isGameRunning;
    }

}
