package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import java.lang.Math;

import static com.mygdx.squirrel_game.game_screen.worldStart;

// is the enemy of the squirrel, every few seconds it tries to dive and catch the squirrel (player)
public class eagle extends GameObject{
    final float attackInterval = 3f, defaultY = 600, incrementalMovementModifier = 300;
    float attackCounter = 0, deltaTime = 0;
    Boolean isDiving;
    Texture diveTexture;
    ObjectAnimation flightAnimation;
    Rectangle attackPosition;

    public eagle(float x) {
        super(128 * 2, 128 * 2);
        super.x = x;
        super.y = defaultY;
        super.bounds.set(super.x + 90, super.y + 25, super.width / 3, super.height / 8);

        isDiving = false;
        attackPosition = new Rectangle(0, 0, 0, 0);

        diveTexture = new Texture(Gdx.files.internal("bald_eagle2.png"));
        flightAnimation = new ObjectAnimation("bald_eagle", 6);
    }

    public Texture getEagleTexture(float delta) {
        if(!isDiving) return flightAnimation.getFrame(delta);
        else return diveTexture;
    }

    public Texture getEagleTexture(float delta, Rectangle viewBox, squirrel player) {
        deltaTime = delta;

        if(delta > 0){
            attackCounter += delta;
            if (attackCounter >= attackInterval || isDiving) {
                attackCounter = 0;
                isDiving = true;
            }

            // check if it's diving and dive/patrol accordingly
            if (!isDiving) {
                attackPosition.x = 0;
                moveIncrementallyToPosition(x, defaultY);

                if ((x > viewBox.x + viewBox.width || (getDX() <= 0 && x > viewBox.x)) && x > worldStart + width)
                    moveXBy(-incrementalMovementModifier * deltaTime);
                else moveXBy(incrementalMovementModifier * deltaTime);
            }

            // is starting dive
            else if (attackPosition.x < worldStart) {
                attackPosition.set(getAttackPosition(player));
            }

            // is ending dive
            else if (isCloseEnough(attackPosition.x, x) && isCloseEnough(attackPosition.y, y)) {
                isDiving = false;
            }

            // is mid-dive
            else {
                if (!isCloseEnough(attackPosition.y, y))
                    moveIncrementallyToPosition(attackPosition.x, attackPosition.y);
                else isDiving = false;
            }
        }

        updateBounds();

        if(!isDiving) return flightAnimation.getFrame(delta);
        else return diveTexture;
    }

    // checks if two floats are too far apart
    private Boolean isCloseEnough(float x1, float x2) {
        return Math.abs(x1 - x2) < 5;
    }

    // moves the eagle towards a position by a small increment
    private void moveIncrementallyToPosition(float x, float y) {
        if(isCloseEnough(x, super.x)) moveTo(x, super.y);
        else moveXBy(super.x > x ? -incrementalMovementModifier * deltaTime : incrementalMovementModifier * deltaTime);

        if(isCloseEnough(y, super.y)) moveTo(super.x, y);
        else moveYBy(super.y > y ? -incrementalMovementModifier * deltaTime : incrementalMovementModifier * deltaTime);
    }

    // gets the position it thinks the player will be at during attack time
    private Rectangle getAttackPosition(squirrel player) {
        Rectangle retRectangle = new Rectangle(player.bounds);

        switch(player.state) {
            case Running:
                retRectangle.x += player.getDX() * 10;
                break;

            case Falling:
                retRectangle.y += player.getDY() * 20;
                retRectangle.x += player.getDX() * 10;
                break;

            case Jumping:
                retRectangle.y += player.getDY() * 10;
                retRectangle.x += player.getDX() * 10;
                break;

            case Idle:
                break;

            default:
                retRectangle.x = 0;
                break;
        }

        return retRectangle;
    }

    private void updateBounds() {
        bounds.x = super.x + 90;
        bounds.y = super.y + 25;
    }

    public void dispose() {
        diveTexture.dispose();
        flightAnimation.dispose();
    }
}
