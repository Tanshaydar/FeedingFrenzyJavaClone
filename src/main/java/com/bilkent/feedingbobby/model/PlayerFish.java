package com.bilkent.feedingbobby.model;

import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PlayerFish extends GameObject{

    public PlayerFish() {
        super();
        try {
            image = ImageIO.read(getClass().getResource("/fish64x64.png"));
            width = image.getWidth();
            height = image.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isControlledByMouse = true;
        lookingDirection = Direction.LEFT;
    }
    
    @Override
    public void setPosition( Point point) {
        this.x = point.x - width / 2;
        this.y = point.y - height / 2;
    }
    
    
}
