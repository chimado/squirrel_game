package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;

// gameobject is responsible for location and in the future object detection (rectangles/hitboxes)
public abstract class GameObject {
    private float width, height, xpos, ypos;

    public GameObject(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void moveTo(float xpos2, float ypos2) {
        setXPos(xpos2);
        setYPos(ypos2);
    }

    public void moveBy(float dx, float dy) {
        setXPos(this.getXPos() + dx);
        setYPos(this.getYPos() + dy);
    }

    public float getXPos() {
        return xpos;
    }

    public void setXPos(float xpos) {
        this.xpos = xpos;
    }

    public float getYPos() {
        return ypos;
    }

    public void setYPos(float ypos) {
        this.ypos = ypos;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight(){
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
