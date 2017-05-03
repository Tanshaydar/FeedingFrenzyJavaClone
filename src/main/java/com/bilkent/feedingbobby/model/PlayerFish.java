package com.bilkent.feedingbobby.model;

import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bilkent.feedingbobby.controller.GameManager;

public class PlayerFish extends GameObject{

	private int score = 0;
	private int size = 0;
	private int frenzy = 0;
	
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

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getFrenzy() {
		return frenzy;
	}

	public void setFrenzy(int frenzy) {
		this.frenzy = frenzy;
	}

	@Override
	public void move() {}

	@Override
	public void updateState(GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish) {}
    
}
