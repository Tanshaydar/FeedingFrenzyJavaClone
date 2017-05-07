package com.bilkent.feedingbobby.model;

import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import com.bilkent.feedingbobby.controller.GameManager;
import com.bilkent.feedingbobby.controller.GameMapManager;
import com.bilkent.feedingbobby.view.GamePanel;

public class PufferFish extends SpecialFish{

    private static final int size = 5;
    
    public PufferFish() {
        try {
            image = ImageIO.read(getClass().getResource("/fishes/pufferfish.png"));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        if(random.nextBoolean()){
            
        }
        x = random.nextInt((GamePanel.RESOLUTION.width - width * 2)) + width/2;
        y = GamePanel.RESOLUTION.height + 1;
        lookingDirection = Direction.UP;        
    }
    
    @Override
    public void move() {
        
    }

    @Override
    public void updateState( GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish) {
        // TODO Auto-generated method stub
        
    }

}
