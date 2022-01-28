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
        InTree
    }

    public float fallTime; // is the time in milliseconds the player is in the air, used for gravity calculations
    float idle_animation_time;
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
        isAffectedByGravity = false;
        canJump = false;
        canClimb = false;
        state = squirrelState.Idle;
        idle_animation_time = 0;

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

        if (state != squirrelState.InTree) {
            if (super.getDY() < 0) {
                fallTime += delta;
                state = squirrelState.Falling;
            }

            else if (delta != 0 && super.getDY() < 0) {
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
                break;
            
            case InTree:
                squirrel_climbing_animation.resetAnimation();
                squirrel_idle_animation.resetAnimation();
                squirrel_running_animation.resetAnimation();
                squirrel_jumping_animation.resetAnimation();
                squirrel_falling_animation.resetAnimation();
                break;
        }

        // checks if the last movement has been to the left and mirrors the texture
        if ((super.isFacingLeft && super.width > 0) || (!super.isFacingLeft && super.width < 0)) {
            flip();
        }

        return outputTexture;
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
