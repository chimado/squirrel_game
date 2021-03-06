package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

// stores information about the player
public class squirrel extends GameObject{
    // is responsible for storing the player's state information
    public enum squirrelState {
        Running,
        Falling,
        Jumping,
        Idle,
        Climbing,
        InTree,
        Dead
    }

    public float fallTime; // is the time in milliseconds the player is in the air, used for gravity calculations
    float idle_animation_time;
    float inTreeTime;
    Boolean isFacingLeft; // is not changed at flip()
    Boolean isAffectedByGravity;
    Boolean canJump;
    Boolean canClimb;
    Texture outputTexture;
    Texture basicSquirrel;
    ObjectAnimation squirrel_running_animation;
    ObjectAnimation squirrel_jumping_animation;
    ObjectAnimation squirrel_falling_animation;
    ObjectAnimation squirrel_idle_animation;
    ObjectAnimation squirrel_climbing_animation;
    squirrelState state;

    public squirrel(float x, float y){
        super(48 * 3, 48 * 3);
        super.x = x;
        super.y = y;
        fallTime = 1f;
        isFacingLeft = false;
        isAffectedByGravity = false;
        canJump = false;
        canClimb = false;
        state = squirrelState.Idle;
        idle_animation_time = 0;
        inTreeTime = 0;

        // initializes the animations and loads all the textures into them
        squirrel_running_animation = new ObjectAnimation();
        squirrel_running_animation.loadAnimation("squirrel_running_", 8);
        squirrel_jumping_animation = new ObjectAnimation();
        squirrel_jumping_animation.loadAnimation("squirrel_running_", 6);
        squirrel_falling_animation = new ObjectAnimation();
        squirrel_falling_animation.loadAnimation("squirrel_falling_", 7);
        squirrel_idle_animation = new ObjectAnimation();
        squirrel_idle_animation.loadAnimation("squirrel_idle_", 3);
        squirrel_climbing_animation = new ObjectAnimation();
        squirrel_climbing_animation.loadAnimation("squirrel_climbing_", 3);
        basicSquirrel = new Texture(Gdx.files.internal("squirrel_basic.png"));
    }

    // returns the texture to be rendered
    public Texture render(float delta) {
        updateBounds();

        if (state != squirrelState.InTree) {
            inTreeTime = 0;

            // checks if the player is moving up or down
            if (super.getDY() > 0) {
                if (canClimb && state != squirrelState.InTree){
                    state = squirrelState.Climbing;
                    fallTime = 1f;
                }

                else {
                    fallTime += delta;
                    state = squirrelState.Jumping;
                }
            }

            else if (super.getDY() < 0) {
                fallTime += delta;
                state = squirrelState.Falling;
            }

            else if (delta != 0 && super.getDY() == 0) {
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
        }

        // checks if the player is dead
        if (y < 0) state = squirrelState.Dead;

        // checks which animation should play according to the state enum
        // resets all other animations (not a good solution for that but it works well enough for now)
        switch (state) {
            case Running:
                outputTexture = squirrel_running_animation.getFrame(delta);
                squirrel_falling_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_idle_animation.resetAnimation();
                squirrel_climbing_animation.resetAnimation();
                idle_animation_time = 0;
                break;
            
            case Falling:
                outputTexture = squirrel_falling_animation.getFrame(delta);
                squirrel_running_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_idle_animation.resetAnimation();
                squirrel_climbing_animation.resetAnimation();
                idle_animation_time = 0;
                break;
            
            case Jumping:
                if (squirrel_jumping_animation.currentFrame >= squirrel_jumping_animation.frames.size - 2){
                    outputTexture = squirrel_jumping_animation.frames.get(squirrel_jumping_animation.currentFrame);
                }

                else {
                    outputTexture = squirrel_jumping_animation.getFrame(delta);
                }
                squirrel_falling_animation.resetAnimation();
                squirrel_running_animation.resetAnimation();
                squirrel_idle_animation.resetAnimation();
                squirrel_climbing_animation.resetAnimation();
                idle_animation_time = 0;
                break;
            
            case Idle:
                if (idle_animation_time > 2f) {
                    outputTexture = squirrel_idle_animation.getFrame(delta);
                    
                    if (idle_animation_time > 3f) {
                        idle_animation_time = 0;
                    }
                }

                else outputTexture = basicSquirrel;

                squirrel_running_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_falling_animation.resetAnimation();
                squirrel_climbing_animation.resetAnimation();
                idle_animation_time += delta;
                break;
            
            case Climbing:
                outputTexture = squirrel_climbing_animation.getFrame(delta);

                squirrel_idle_animation.resetAnimation();
                squirrel_running_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_falling_animation.resetAnimation();
                idle_animation_time = 0;
                break;
            
            case InTree:
                squirrel_climbing_animation.resetAnimation();
                squirrel_idle_animation.resetAnimation();
                squirrel_running_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_falling_animation.resetAnimation();
                idle_animation_time = 0;
                break;

            case Dead:
                outputTexture = squirrel_running_animation.getSpecifiedFrame(5);
                squirrel_climbing_animation.resetAnimation();
                squirrel_idle_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_falling_animation.resetAnimation();
                idle_animation_time = 0;
                break;

        }

        // checks if the last movement has been to the left and mirrors the texture
        if ((isFacingLeft && super.width > 0) || (!isFacingLeft && super.width < 0)) {
            flip();
        }

        return outputTexture;
    }

    // updates the squirrel's hitbox/bounds
    public void updateBounds(){
        // set squirrel bounds
        super.bounds.x = super.x + 15;
        super.bounds.y = super.y + 15;
        super.bounds.width = super.width - 40;
        super.bounds.height = super.height / 3 + 10;

        if ((state == squirrelState.Climbing || state == squirrelState.InTree) && !isFacingLeft) {
            super.bounds.x -= 50;
            super.bounds.y -= 30;
            super.bounds.width /= 2;
            super.bounds.height *= 2;
        }

        else if (state == squirrelState.Climbing || state == squirrelState.InTree) {
            super.bounds.x -= 100;
            super.bounds.y -= 30;
            super.bounds.width /= 2;
            super.bounds.height *= 2;

            if (state == squirrelState.InTree && isFacingLeft) super.bounds.x += 50;
        }
    }

    // flips the squirrel
    public void flip(){
        if ((state == squirrelState.Climbing && isFacingLeft) || state != squirrelState.Climbing) {
            super.width = super.width * -1;
            super.x = super.x + super.width * -1;

            if (isFacingLeft){
                super.bounds.width = (super.bounds.width * -1) / 1.3f;
                super.bounds.x = super.bounds.x + super.bounds.width * -1;
            }
        }
    }

    public void dispose(){
        squirrel_running_animation.dispose();
        squirrel_jumping_animation.dispose();
        squirrel_falling_animation.dispose();
        squirrel_idle_animation.dispose();
        squirrel_climbing_animation.dispose();
        basicSquirrel.dispose();
        outputTexture.dispose();
    }
}
