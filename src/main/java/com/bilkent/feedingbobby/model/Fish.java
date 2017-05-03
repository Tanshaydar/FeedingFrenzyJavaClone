package com.bilkent.feedingbobby.model;

public abstract class Fish extends GameObject {

	public Fish() {
		isControlledByAi = true;
		isControlledByKeyBoard = false;
		isControlledByMouse = false;
	}
	
}
