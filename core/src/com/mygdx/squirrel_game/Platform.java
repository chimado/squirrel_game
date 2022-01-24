package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Platform extends GameObject{
    Texture platformTexture;
    Texture dirtTexture;
    Boolean hasDirt;

    public Platform(float width, float height, float x, float y, Boolean hasDirt) {
        super(width, height);
        super.x = x;
        super.y = y;
        this.hasDirt = hasDirt;
        platformTexture = new Texture(Gdx.files.internal("grass_platform.png"));

        if (hasDirt) {
            dirtTexture = new Texture(Gdx.files.internal("dirt.png"));
        }
    }

    public Texture getPlatformTexture(){
        return platformTexture;
    }

    public Texture getDirtTexture(){
        return dirtTexture;
    }
    
    public void dispose(){
        platformTexture.dispose();

        if (hasDirt) {
            dirtTexture.dispose();
        }
    }
}
