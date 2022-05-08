package com.mygdx.squirrel_game;

import com.badlogic.gdx.graphics.Texture;

// the player collects these in order to win
public class Acorn extends GameObject{

    ObjectAnimation acornAnimation;
    Boolean isAnimated;

    public Acorn(float width, float height, int x, int y) {
        super(width, height);
        super.x = x;
        super.y = y;
        isAnimated = false;

        acornAnimation = new ObjectAnimation();
        acornAnimation.loadAnimation("acorn", 4);

        // set acorn bounds
        super.bounds.x = x;
        super.bounds.y = y;
        super.bounds.width = width;
        super.bounds.height = height;
    }

    public Texture getAcornTexture(float delta) {
        if (acornAnimation.currentFrame == 3) dispose();

        if (isAnimated) return acornAnimation.getFrame(delta);
        else return acornAnimation.getSpecifiedFrame(0);
    }

    public void dispose() { acornAnimation.dispose(); }
}
