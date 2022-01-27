package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

// this object stores information about individual dirt components of platforms that have dirt
public class Dirt extends GameObject{
    Texture dirtTexture;
    Boolean hasDirt;

    public Dirt(float width, float height, float x, float y) {
        super(width, height);
        super.x = x;
        super.y = y;

        dirtTexture = new Texture(Gdx.files.internal("dirt.png"));
    }

    public Texture getDirtTexture(){
        return dirtTexture;
    }
    
    public void dispose(){
        dirtTexture.dispose();
    }
}
