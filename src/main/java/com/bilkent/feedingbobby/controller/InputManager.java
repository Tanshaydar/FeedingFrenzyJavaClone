package com.bilkent.feedingbobby.controller;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.bilkent.feedingbobby.model.Direction;

public class InputManager extends MouseAdapter implements KeyListener {

    private static InputManager instance = null;

    public static synchronized InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    private Point mousePoint;
    private Direction changeDirection;

    public InputManager() {
        mousePoint = new Point();
    }

    public Point getMousePoint() {
        return mousePoint;
    }

    public Direction getChangeDirection() {
        return changeDirection;
    }

    @Override
    public void mouseMoved( MouseEvent e) {
        if (mousePoint.x > e.getPoint().x) {
            changeDirection = Direction.LEFT;
        } else {
            changeDirection = Direction.RIGHT;
        }
        mousePoint = e.getPoint();
    }

    @Override
    public void keyTyped( KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed( KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased( KeyEvent e) {
        // TODO Auto-generated method stub

    }

}
