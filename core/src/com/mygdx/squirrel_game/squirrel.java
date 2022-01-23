package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


public class squirrel extends GameObject{
    public enum squirrelState {
        Running,
        Falling,
        Jumping,
        Idle
    }

    public float fallTime;
    Boolean isFacingLeft;
    Texture outputTexture;
    Texture basicSquirrel;
    ObjectAnimation squirrel_running_animation;
    ObjectAnimation squirrel_jumping_animation;
    squirrelState state;

    public squirrel(float x, float y){
        super(48 * 3, 32 * 3);
        super.x = x;
        super.y = y;
        fallTime = 1f;
        isFacingLeft = false;
        state = squirrelState.Idle;

        squirrel_running_animation = new ObjectAnimation();
        squirrel_running_animation.loadAnimation("squirrel_running_", 8);
        squirrel_jumping_animation = new ObjectAnimation();
        squirrel_jumping_animation.loadAnimation("squirrel_running_", 6);
        basicSquirrel = new Texture(Gdx.files.internal("squirrel_basic.png"));
    }

    // returns the texture to be rendered
    public Texture render(float delta) {
        outputTexture = basicSquirrel;

        // checks if the player is moving up or down
        if (super.getDY() > 0) {
            fallTime += delta;
            state = squirrelState.Jumping;
        }

        else if (super.getDY() < 0) {
            fallTime += delta;
            state = squirrelState.Falling;
        }

        else if (delta != 0) {
            state = squirrelState.Idle;
        }

        // checks if the player is moving left or right
        if (super.getDX() != 0) {
            if (super.getDY() == 0) {
                state = squirrelState.Running;
            }

            if (super.getDX() < 0) {
                isFacingLeft = true;
            }

            else {
                isFacingLeft = false;
            }
        }

        else if (delta != 0 && state == squirrelState.Running) {
            state = squirrelState.Idle;
        }

        // checks if the player is running
        if (state == squirrelState.Running) {
            outputTexture = squirrel_running_animation.getFrame(delta);
        }

        else {
            squirrel_running_animation.resetAnimation();
        }

        // checks if the last movement has been to the left and mirrors the texture
        if ((isFacingLeft && super.width > 0) || (!isFacingLeft && super.width < 0)) {
            flip();
        }

        return outputTexture;
    }

    // flips the texture
    public void flip(){
        super.width = (super.width * -1);
        super.x = (super.x + super.width * -1);
    }

    public void dispose() {
        squirrel_running_animation.dispose();
        squirrel_jumping_animation.dispose();
    }
}
