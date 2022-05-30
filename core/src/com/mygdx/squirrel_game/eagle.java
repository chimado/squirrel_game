package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import static com.mygdx.squirrel_game.game_screen.worldStart;

// is the enemy of the squirrel, every few seconds it tries to dive and catch the squirrel (player)
public class eagle extends GameObject{
    final float attackInterval = 3f, defaultY = 550;
    float attackCounter = 0, attackX, attackY;
    Boolean isDiving;
    Texture diveTexture;
    ObjectAnimation flightAnimation;

    public eagle(float x) {
        super(128 * 2, 128 * 2);
        super.x = x;
        super.y = defaultY;
        super.bounds.set(super.x + 50, super.y, super.width / 2, super.height / 2);

        isDiving = false;

        diveTexture = new Texture(Gdx.files.internal("bald_eagle2.png"));
        flightAnimation = new ObjectAnimation("bald_eagle", 6);
    }

    public Texture getEagleTexture(float delta) {
        if(!isDiving) return flightAnimation.getFrame(delta);
        else return diveTexture;
    }

    public Texture getEagleTexture(float delta, Rectangle viewBox) {
        if((x > viewBox.x + viewBox.width || (getDX() <= 0 && x > viewBox.x)) && x > worldStart + width) moveXBy(-5);
        else moveXBy(5);

        if(!isDiving) return flightAnimation.getFrame(delta);
        else return diveTexture;
    }

    public void dispose() {
        diveTexture.dispose();
        flightAnimation.dispose();
    }
}
