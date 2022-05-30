package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

// is the enemy of the squirrel, every few seconds it tries to dive and catch the squirrel (player)
public class eagle{
    Boolean isDiving;
    Rectangle talons;
    Texture diveTexture;
    ObjectAnimation flightAnimation;

    public eagle() {

    }
}
