package com.bilkent.feedingbobby.controller;

public class GameMapManager {

	private int level = 1;
	private int numberOfFish = 6;
	
	public GameMapManager() {
		// TODO Auto-generated constructor stub
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public int getNumberOfFish() {
		return numberOfFish;
	}

	public void setNumberOfFish(int numberOfFish) {
		this.numberOfFish = numberOfFish;
	}
}
