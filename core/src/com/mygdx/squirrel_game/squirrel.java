package com.mygdx.squirrel_game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;


public class squirrel extends GameObject{
    Boolean isRunning;
    Boolean isFacingLeft;
    Boolean isJumping;
    Boolean isFalling;
    Texture outputTexture;
    Texture basicSquirrel;
    ObjectAnimation squirrel_running_animation;

    public squirrel(){
        super(48 * 3, 32 * 3);
        isRunning = false;
        isFacingLeft = false;
        isJumping = false;
        isFalling = false;

        squirrel_running_animation = new ObjectAnimation();
        squirrel_running_animation.loadAnimation("squirrel_running_", 8);
        basicSquirrel = new Texture(Gdx.files.internal("squirrel_basic.png"));
    }

    // returns the texture to be rendered
    public Texture render(float delta) {
        // checks if the player is moving up or down
        /*
        if (super.getDY() > 0) {
            isJumping = true;
        }

        else if (super.getDY() < 0) {
            isFalling = true;
            isJumping = false;
        }

        else {
            isFalling = false;
            isJumping = false;
        }*/

        // checks if the player is moving left or right
        if (super.getDX() != 0) {
            if (!isJumping && !isFalling) {
                isRunning = true;
            }

            if (super.getDX() < 0) {
                isFacingLeft = true;
            }

            else {
                isFacingLeft = false;
            }
        }

        else if (delta != 0) {
            isRunning = false;
        }

        // checks if the player is running
        if (isRunning) {
            outputTexture = squirrel_running_animation.getFrame(delta);
        }

        else {
            squirrel_running_animation.resetAnimation();
            outputTexture = basicSquirrel;
        }

        // checks if the last movement has been to the left and mirrors the texture
        if ((isFacingLeft && super.getWidth() > 0) || (!isFacingLeft && super.getWidth() < 0)) {
            flip();
        }

        return outputTexture;
    }

    // flips the texture
    public void flip(){
        super.setWidth(super.getWidth() * -1);
        super.setXPos(super.getXPos() + super.getWidth() * -1);
    }

    public void dispose() {
        squirrel_running_animation.dispose();
    }
}
