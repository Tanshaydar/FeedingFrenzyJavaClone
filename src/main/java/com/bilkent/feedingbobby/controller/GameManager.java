package com.bilkent.feedingbobby.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.bilkent.feedingbobby.model.Bubble;
import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;
import com.bilkent.feedingbobby.view.GamePanel;

public class GameManager {
    /*
     * FOR CAPPING GAME AT 40 FPS
     */
    private static final long REFRESH_INTERVAL_MS = 25;
    /*
     * To calculate Game Time
     */
    private long startTime;

    /*
     * If game is running
     */
    private boolean isGameRunning = false;
    /*
     * If game is paused
     */
    private boolean isGamePaused = false;
    /*
     * If game is over
     */
    private boolean isGameOver = false;
    /*
     * If game is in level transition
     */
    private boolean isInLevelTransition = false;

    /*
     * If game should add a special fish
     */
    private boolean readyForSpecialFish = false;

    /**
     * If game should add a bubble effect
     */
    private boolean shouldAddBubble = false;

    /**
     * Game View component, that will be used to draw game objects to screen
     */
    private GamePanel gamePanel;
    /**
     * Game Map Manager, that will decide the locations and numbers of game
     * objects
     */
    private GameMapManager gameMapManager;
    /**
     * Game main thread
     */
    private Thread gameThread;
    /**
     * List of game objects
     */
    private List<GameObject> gameObjects = new ArrayList<>();
    /**
     * Main character. For various reasons, this is not in the regular game
     * object list.
     */
    private PlayerFish playerFish;

    /**
     * Game Manager Constructor
     * 
     * @param gamePanel
     */
    public GameManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gameMapManager = new GameMapManager();
    }

    /**
     * Initialization function
     */
    public void initialize() {

        isGameRunning = true;
        isGamePaused = false;
        isGameOver = false;

        gameObjects.clear();
        gameObjects.addAll(gameMapManager.getMapObjects());

        if (playerFish == null) {
            playerFish = new PlayerFish();
        } else if (isInLevelTransition) {
            playerFish.resetForNewLevel();
            isInLevelTransition = false;
        } else {
            playerFish.reset();
        }

        this.gamePanel.setLevel(playerFish, gameObjects, gameMapManager.getLevel());
    }

    /**
     * Starts game.
     *
     * @param isNewGame
     *            the is new game
     */
    public void startGame( boolean isNewGame) {
        if (isNewGame) {
            playerFish = new PlayerFish();
            gameMapManager.setLevel(0);
        }
        initialize();
        if (gameThread != null && gameThread.isAlive()) {
            gameThread.interrupt();
        }
        gameThread = new Thread(new GameLoop());
        gameThread.start();
        playerFish.setCurrentlyActive(false);
    }

    /**
     * Stops game.
     */
    public void stopGame() {
        isGamePaused = false;
        isGameRunning = false;
    }

    /**
     * Pauses game.
     */
    public void pauseGame() {
        isGamePaused = true;
    }

    /**
     * Resumes game.
     */
    public void resumeGame() {
        isGamePaused = false;
    }

    /**
     * Game Loop runnable.
     */
    private class GameLoop implements Runnable {

        @Override
        public void run() {

            startTime = System.currentTimeMillis();

            while (isGameRunning) {

                while (isGamePaused) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // Check game state (if game is over)
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
            if (playerFish.getLives() == 0) {
                gamePanel.showGameOverScreen();
            }
        }
    }

    /**
     * Check game state.
     *
     * @return true, if game is over or next level
     */
    private synchronized boolean checkGameState() {
        if (playerFish.getLives() == 0) {
            isGamePaused = false;
            isGameRunning = false;
            isGameOver = true;
            return true;
        } else if (playerFish.getSize() == 8) {
            isGamePaused = false;
            isGameRunning = true;
            isGameOver = false;
            isInLevelTransition = true;
            gamePanel.setInLevelTransition(true);
            gamePanel.paintImmediately(0, 0, GamePanel.RESOLUTION.width, GamePanel.RESOLUTION.height);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            gamePanel.setInLevelTransition(false);
            initialize();
            return true;
        }
        return false;
    }

    /**
     * Handle input.
     */
    private synchronized void handleInput() {
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

    /**
     * Handle logic.
     */
    private synchronized void handleLogic() {
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
            gameObjects.add(gameMapManager.getNewEnemyFish(gameObjects, playerFish.getSize()));
        }

        // Should add special enemies?
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) % 10 == 0 && !readyForSpecialFish) {
            GameObject specialFish = gameMapManager.getSpecialFish();
            if (specialFish != null) {
                gameObjects.add(specialFish);
                readyForSpecialFish = true;
            }
        } else if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) % 10 != 0) {
            readyForSpecialFish = false;
        }

        // Should add bubbles?
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) % 5 == 0
                && gameObjects.stream().filter(gameObject -> gameObject instanceof Bubble).count() < 20
                && !shouldAddBubble) {
            gameObjects.addAll(gameMapManager.addBubbles());
            shouldAddBubble = true;
        } else if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - startTime) % 5 != 0) {
            shouldAddBubble = false;
        }
    }

    /**
     * Handle drawing.
     */
    private synchronized void handleDrawing() {
        long currentTime = System.currentTimeMillis() - startTime;
        gamePanel.setMinutes(TimeUnit.MILLISECONDS.toMinutes(currentTime));
        gamePanel.setSeconds(TimeUnit.MILLISECONDS.toSeconds(currentTime));
        gamePanel.paintImmediately(0, 0, GamePanel.RESOLUTION.width, GamePanel.RESOLUTION.height);
        // gamePanel.repaint();
    }

    /**
     * Adds the new object.
     *
     * @param gameObject
     *            the game object
     */
    public synchronized void addNewObject( GameObject gameObject) {
        gameObjects.add(gameObject);
    }

    /**
     * Gets the score.
     *
     * @return the score
     */
    public int getScore() {
        return playerFish.getScore();
    }

    /**
     * Checks if is game over.
     *
     * @return true, if is game over
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Checks if is game paused.
     *
     * @return true, if is game paused
     */
    public boolean isGamePaused() {
        return isGamePaused;
    }

    /**
     * Checks if is game running.
     *
     * @return true, if is game running
     */
    public boolean isGameRunning() {
        return isGameRunning;
    }

}
