package com.bilkent.feedingbobby.model;

import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import com.bilkent.feedingbobby.controller.GameManager;
import com.bilkent.feedingbobby.controller.GameMapManager;

public class PlayerFish extends GameObject {

    private int score = 0;
    private int size = 1;
    private int frenzy = 0;
    private int growth = 0;
    private int lives = 3;
    private boolean isCurrentlyActive = false;
    private boolean isDamaged = false;
    private Timer playerStartTimer;
    private Timer frenzyStopTimer;

    public PlayerFish() {
        super();
        try {
            image = ImageIO.read(getClass().getResource("/fish.png"));
            width = height = 50;
        } catch (IOException e) {
            e.printStackTrace();
        }
        isControlledByMouse = true;
        lookingDirection = Direction.LEFT;

        playerStartTimer = new Timer(3000, ae -> {
            setCurrentlyActive(true);
            if (isDamaged) {
                isDamaged = false;
            }
        });

        frenzyStopTimer = new Timer(5000, ae -> {
            setFrenzy(0);
        });
    }

    public void reset() {
        score = 0;
        size = 1;
        frenzy = 0;
        growth = 0;
        lives = 3;
        width = height = 50;
        isDamaged = false;
    }

    @Override
    public void setPosition( Point point) {
        this.x = point.x - width / 2;
        this.y = point.y - height / 2;
    }

    public int getScore() {
        return score;
    }

    public void setScore( int score) {
        this.score = score;
    }

    public int getSize() {
        return size;
    }

    public void setSize( int size) {
        this.size = size;
    }

    public int getFrenzy() {
        return frenzy;
    }

    public void setFrenzy( int frenzy) {
        if (this.frenzy == 100 && frenzy != 0) {
            return;
        }
        this.frenzy = frenzy;
        if (this.frenzy > 100) {
            this.frenzy = 100;
            frenzyStopTimer.restart();
        }
    }

    public int getGrowth() {
        return growth;
    }

    public void setGrowth( int growth) {
        this.growth = growth;
        if (this.growth >= 100) {
            width = height = width + 10;
            this.growth = 0;
            this.size++;
        }
    }

    public int getLives() {
        return lives;
    }

    public synchronized void setLives( int lives) {
        this.lives = lives;
    }

    public boolean isCurrentlyActive() {
        return isCurrentlyActive;
    }

    public void setCurrentlyActive( boolean isCurrentlyActive) {
        this.isCurrentlyActive = isCurrentlyActive;
        if (!this.isCurrentlyActive) {
            playerStartTimer.restart();
        }
    }

    public boolean isDamaged() {
        return isDamaged;
    }

    public void setDamaged( boolean isDamaged) {
        this.isDamaged = isDamaged;
    }

    @Override
    public void move() {
    }

    @Override
    public void updateState( GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish) {
    }

}
