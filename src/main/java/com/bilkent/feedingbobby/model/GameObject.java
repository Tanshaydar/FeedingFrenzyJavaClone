package com.bilkent.feedingbobby.model;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import com.bilkent.feedingbobby.controller.GameManager;
import com.bilkent.feedingbobby.controller.GameMapManager;

public abstract class GameObject {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected BufferedImage image;

    protected Direction lookingDirection;

    protected boolean isControlledByMouse;
    protected boolean isControlledByKeyBoard;
    protected boolean isControlledByAi;

    /**
     * When a game object should be removed from list i.e. eaten fish or special
     * fish out of screen, it is marked for destroying so it will be removed in
     * another loop to avoid concurrency (list modification while iterating)
     * problems.
     */
    private boolean isMarkedForDestroying;

    public GameObject() {
        isControlledByMouse = false;
        isControlledByKeyBoard = false;
        isControlledByAi = false;
        isMarkedForDestroying = false;
    }

    public Rectangle getBoundingBox() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Uses Java 2D's Rectangle.intersects method to see if two objects in the
     * screen intersects
     * 
     * @param rectangle
     *            Object to check if this object intersects with
     * @return true, if two objects intersect
     */
    public boolean intersects( Rectangle rectangle) {
        return getBoundingBox().intersects(rectangle);
    }

    public void setDirection( Direction changeDirection) {
        if ((changeDirection == Direction.LEFT && lookingDirection == Direction.RIGHT)
                || (changeDirection == Direction.RIGHT && lookingDirection == Direction.LEFT)) {
            flipHorizontally();
            lookingDirection = changeDirection;
        }

        if ((changeDirection == Direction.UP && lookingDirection == Direction.DOWN)
                || (changeDirection == Direction.DOWN && lookingDirection == Direction.UP)) {
            flipVertically();
            lookingDirection = changeDirection;
        }
    }

    public void flipVertically() {
        AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
        tx.translate(0, -image.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        if (lookingDirection == Direction.LEFT) {
            lookingDirection = Direction.RIGHT;
        } else if (lookingDirection == Direction.RIGHT) {
            lookingDirection = Direction.LEFT;
        }
    }

    public void flipHorizontally() {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-image.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        if (lookingDirection == Direction.UP) {
            lookingDirection = Direction.DOWN;
        } else if (lookingDirection == Direction.DOWN) {
            lookingDirection = Direction.UP;
        }
    }

    public abstract void move();

    public abstract void updateState( GameManager gameManager, GameMapManager gameMapManager, PlayerFish playerFish);

    public void setPositon( int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setPosition( Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public int getX() {
        return x;
    }

    public void setX( int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY( int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth( int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight( int height) {
        this.height = height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage( BufferedImage image) {
        this.image = image;
    }

    public boolean isControlledByMouse() {
        return isControlledByMouse;
    }

    public void setControlledByMouse( boolean isControlledByMouse) {
        this.isControlledByMouse = isControlledByMouse;
    }

    public boolean isControlledByKeyBoard() {
        return isControlledByKeyBoard;
    }

    public void setControlledByKeyBoard( boolean isControlledByKeyBoard) {
        this.isControlledByKeyBoard = isControlledByKeyBoard;
    }

    public boolean isControlledByAi() {
        return isControlledByAi;
    }

    public void setControlledByAi( boolean isControlledByAi) {
        this.isControlledByAi = isControlledByAi;
    }

    public boolean isMarkedForDestroying() {
        return isMarkedForDestroying;
    }

    public void setMarkedForDestroying( boolean isMarkedForDestroying) {
        this.isMarkedForDestroying = isMarkedForDestroying;
    }
}
