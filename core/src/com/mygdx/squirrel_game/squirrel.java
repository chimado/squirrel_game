package com.mygdx.squirrel_game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;


public class squirrel extends GameObject{

    Boolean isRunning;
    Boolean isFacingLeft;
    Texture outputTexture;
    Texture basicSquirrel;
    float elapsedTime;
    int squirrel_running_animation_index;
    Array<Texture> squirrel_running_animation;

    public squirrel(){
        super(48 * 3, 32 * 3);
        elapsedTime = 0;
        squirrel_running_animation_index = 0;
        isRunning = false;
        isFacingLeft = false;

        squirrel_running_animation = new Array<Texture>();
        loadRunningAnimation();
        basicSquirrel = new Texture(Gdx.files.internal("squirrel_basic.png"));
    }

    // returns the texture to be rendered
    public Texture render(float delta) {
        // checks if the player is moving left or right
        if (super.getDX() != 0){
            isRunning = true;

            if (super.getDX() < 0) {
                isFacingLeft = true;
            }

            else {
                isFacingLeft = false;
            }
        }

        else {
            isRunning = false;
        }

        if (isRunning) {
            elapsedTime += delta;

            // counts the time elapsed since the last frame change
            // 1 / 20f is 20fps
            if (elapsedTime > 1 / 20f){
                squirrel_running_animation_index++;
                elapsedTime = 0;
            }

            if (squirrel_running_animation_index > squirrel_running_animation.size - 1){
                squirrel_running_animation_index = 0;
            }

            outputTexture = squirrel_running_animation.get(squirrel_running_animation_index);
        }

        else {
            elapsedTime = 0;
            squirrel_running_animation_index = 0;
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

    // load all of the sprites onto the texture array for animating the player
    public void loadRunningAnimation() {
        for (int i = 0; i < 8; i++) {
            squirrel_running_animation.add(new Texture(Gdx.files.internal("squirrel_running_" + (i + 1) + ".png")));
        }
    }

    public void dispose() {
        for (Texture frame : squirrel_running_animation){
            frame.dispose();
        }
    }
}
