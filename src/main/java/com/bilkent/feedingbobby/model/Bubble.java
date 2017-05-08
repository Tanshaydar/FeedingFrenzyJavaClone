package com.bilkent.feedingbobby.model;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.bilkent.feedingbobby.controller.GameManager;
import com.bilkent.feedingbobby.controller.GameMapManager;
import com.bilkent.feedingbobby.view.GamePanel;

public class Bubble extends GameObject{
    
    private Random random;

    public Bubble() {
        try {
            image = ImageIO.read(getClass().getResource("/bubble.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        random = new Random();
        isControlledByAi = true;
    }

    @Override
    public void move() {
        if (y + height < 0) {
            y = GamePanel.RESOLUTION.height + height;
            x = random.nextInt((GamePanel.RESOLUTION.width - width * 2)) + width/2;
            return;
        }

        y -= random.nextInt(1) + 1;

        if (random.nextInt(5) == 0) {
            if (random.nextBoolean()) {
                x++;
            } else {
                x--;
            }
        }

        if (x < 5) {
            x = 5;
        }
        if (x + width > GamePanel.RESOLUTION.width - 5) {
            x = GamePanel.RESOLUTION.width - width - 5;
        }
    }

    @Override
    public void updateState( GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish) {}

}
