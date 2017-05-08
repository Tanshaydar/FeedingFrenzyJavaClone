package com.bilkent.feedingbobby.model;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.bilkent.feedingbobby.controller.GameManager;
import com.bilkent.feedingbobby.controller.GameMapManager;
import com.bilkent.feedingbobby.view.GamePanel;

public class PufferFish extends SpecialFish {

    private static final int size = 5;

    public PufferFish() {
        try {
            image = ImageIO.read(getClass().getResource("/fishes/pufferfish.png"));
            width = image.getWidth();
            height = image.getHeight();
            lookingDirection = Direction.LEFT;
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        if (random.nextBoolean()) {
            x = -width;
            y = random.nextInt(GamePanel.RESOLUTION.height - height * 2) + height / 2;
            setDirection(Direction.RIGHT);
        } else {
            x = GamePanel.RESOLUTION.width + width;
            y = random.nextInt(GamePanel.RESOLUTION.height - height * 2) + height / 2;
            setDirection(Direction.LEFT);
        }
    }

    @Override
    public void move() {
        if (y + height < 0 || y > GamePanel.RESOLUTION.height || x + width + 5 < 0
                || x - width - 5 > GamePanel.RESOLUTION.width) {
            setMarkedForDestroying(true);
            return;
        }

        Random random = new Random();
        if (lookingDirection == Direction.LEFT) {
            x -= random.nextInt(1) + 1;
        } else {
            x += random.nextInt(1) + 1;
        }

        if (random.nextInt(5) == 0) {
            if (random.nextBoolean()) {
                y++;
            } else {
                y--;
            }
        }
    }

    @Override
    public void updateState( GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish) {
        if (playerFish.getFrenzy() == 100) {
            playerFish.setScore(playerFish.getScore() + size * 5);
            setMarkedForDestroying(true);
        } else {
            playerFish.setLives(playerFish.getLives() - 1);
            playerFish.setFrenzy(0);
            playerFish.setDamaged(true);
            playerFish.setCurrentlyActive(false);
        }
    }

}
