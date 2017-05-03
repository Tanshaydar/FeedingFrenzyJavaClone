package com.bilkent.feedingbobby.controller;

import java.util.ArrayList;
import java.util.List;

import com.bilkent.feedingbobby.model.GameObject;
import com.bilkent.feedingbobby.model.PlayerFish;
import com.bilkent.feedingbobby.view.GamePanel;

public class GameManager {
    // FOR CAPPING GAME AT 60 FPS
    private static final long REFRESH_INTERVAL_MS = 17;

    private boolean isGameRunning;

    private GamePanel gamePanel;

    private List<GameObject> gameObjects;
    private PlayerFish playerFish;

    public GameManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        isGameRunning = false;
        gameObjects = new ArrayList<>();

        if (playerFish == null) {
            playerFish = new PlayerFish();
        }
        gameObjects.add(playerFish);
    }

    public void startGame() {
        isGameRunning = true;
        Thread thread = new Thread(new GameLoop());
        thread.start();
    }

    public void stopGame() {
        isGameRunning = false;
    }

    private class GameLoop implements Runnable {

        @Override
        public void run() {

            while (isGameRunning) {
                long oneTickOfGame = System.currentTimeMillis();
                // Handle input
                handleInput();
                // Handle logic
                handleLogic();
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
        gameObjects.stream().filter(gameObject -> gameObject.isControlledByMouse()).forEach(gameObject -> {
            gameObject.setDirection(InputManager.getInstance().getChangeDirection());
            gameObject.setPosition(InputManager.getInstance().getMousePoint());
        });
    }

    private void handleLogic() {
    }

    private void handleDrawing() {
        gamePanel.draw(gameObjects);
    }

}
