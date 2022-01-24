package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Platform extends GameObject{
    Texture platformTexture;
    Boolean hasDirt;
    Dirt dirt;

    public Platform(float width, float height, float x, float y, Boolean hasDirt) {
        super(width, height);
        super.x = x;
        super.y = y;
        this.hasDirt = hasDirt;
        platformTexture = new Texture(Gdx.files.internal("grass_platform.png"));

        if (hasDirt) {
            dirt = new Dirt(width, y, x, 0);
        }
    }

    public Texture getPlatformTexture(){
        return platformTexture;
    }

    public Dirt getDirt(){
        return dirt;
    }

    public void dispose(){
        platformTexture.dispose();

        if (hasDirt) {
            dirt.dispose();
        }
    }
}
