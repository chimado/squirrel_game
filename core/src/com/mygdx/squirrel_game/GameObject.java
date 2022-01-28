package com.mygdx.squirrel_game;

import com.badlogic.gdx.math.Rectangle;

// gameobject is responsible for location and in the future object detection (rectangles/hitboxes)
public abstract class GameObject extends Rectangle{
    private float dx, dy;
    Boolean isFacingLeft;
    Rectangle bounds; // used for collision detection

    public GameObject(float width, float height) {
        super.width = width;
        super.height = height;
        dx = 0;
        dy = 0;
        isFacingLeft = false;
        bounds = new Rectangle();
    }

    public void moveTo(float x, float y) {
        super.x = x;
        super.y = y;
    }

    public void moveBy(float dx, float dy) {
        super.x += dx;
        super.y += dy;
        this.dx = dx;
        this.dy = dy;
    }

    public void moveXBy(float dx) {
        super.x += dx;
        this.dx = dx;
    }

    public void moveYBy(float dy) {
        super.y += dy;
        this.dy = dy;
    }

    public float getDX() {
        return dx;
    }

    public float getDY() {
        return dy;
    }
}
