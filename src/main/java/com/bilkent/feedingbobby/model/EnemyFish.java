package com.bilkent.feedingbobby.model;

import java.awt.Point;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.bilkent.feedingbobby.controller.GameManager;
import com.bilkent.feedingbobby.controller.GameMapManager;
import com.bilkent.feedingbobby.view.GamePanel;

public class EnemyFish extends Fish {

    private int size;

    public EnemyFish(int size) {
        this.size = size;
        try {
            image = ImageIO.read(getClass().getResource("/fishes/enemy_fish_" + size + ".png"));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        lookingDirection = Direction.LEFT;
    }

    public EnemyFish(int size, Point position) {
        this(size);
        this.x = position.x;
        this.y = position.y;
    }

    public void move() {
        Random random = new Random();
        if (lookingDirection == Direction.LEFT) {
            x -= random.nextInt(1) + 1;
        } else if (lookingDirection == Direction.RIGHT) {
            x += random.nextInt(1) + 1;
        }

        if (random.nextInt(5) == 0) {
            if (random.nextBoolean()) {
                y++;
            } else {
                y--;
            }
        }

        if (x > GamePanel.RESOLUTION.width) {
            x = -width;
        }
        if (x + width < 0) {
            x = GamePanel.RESOLUTION.width;
        } else {
            if (y > GamePanel.RESOLUTION.height || y + height < 0) {
                setMarkedForDestroying(true);
            }
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize( int size) {
        this.size = size;
    }

    @Override
    public void updateState( GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish) {
        if (size < playerFish.getSize() || playerFish.getFrenzy() == 100) {
            playerFish.setGrowth(playerFish.getGrowth() + (size + 1) * 5);
            playerFish.setFrenzy(playerFish.getFrenzy() + (size + 1) * 2);
            playerFish.setScore(playerFish.getScore() + (size + 1) * 5);
            setMarkedForDestroying(true);
        } else {
            playerFish.setLives(playerFish.getLives() - 1);
            playerFish.setFrenzy(0);
            playerFish.setDamaged(true);
            playerFish.setCurrentlyActive(false);
        }
    }

}
