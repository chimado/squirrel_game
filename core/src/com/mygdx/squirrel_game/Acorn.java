package com.mygdx.squirrel_game;

import com.badlogic.gdx.graphics.Texture;

// the player collects these in order to win
public class Acorn extends GameObject{

    ObjectAnimation acornAnimation;
    Boolean isAnimated;
    int animationCounter;

    public Acorn(int x, int y) {
        super(40, 60);
        super.x = x;
        super.y = y;
        isAnimated = false;
        animationCounter = 0;

        acornAnimation = new ObjectAnimation();
        acornAnimation.loadAnimation("acorn", 4);

        // set acorn bounds
        super.bounds.x = x;
        super.bounds.y = y;
        super.bounds.width = width;
        super.bounds.height = height;
    }

    public Texture getAcornTexture(float delta) {
        if (animationCounter > 3) dispose();

        if (isAnimated) {
            animationCounter++;
            return acornAnimation.getFrame(delta);
        }
        else return acornAnimation.getSpecifiedFrame(0);
    }

    public void animateAcorn() { isAnimated = true; }

    public void dispose() {
        super.x = -9000;
        super.y = -9000;
        super.bounds.x = -9000;
        super.bounds.y = -9000;
        acornAnimation.dispose();
    }
}
