package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

// this object stores information about individual platforms
public class Platform extends GameObject{
    Texture platformTexture;
    Boolean hasTree;
    Dirt dirt;
    Tree tree;

    public Platform(float width, float height, float x, float y, Boolean hasTree) {
        super(width, height);
        super.x = x;
        super.y = y;

        this.hasTree = hasTree;
        platformTexture = new Texture(Gdx.files.internal("grass_platform.png"));

        dirt = new Dirt(width, y, x, 0);

        if (hasTree) {
            tree = new Tree(x, y);
        }

        super.bounds.x = super.x;
        super.bounds.y = super.y;
        super.bounds.width = super.width;
        super.bounds.height = super.height / 2;
    }

    public Texture getPlatformTexture(){
        return platformTexture;
    }

    public Dirt getDirt(){
        return dirt;
    }

    public Tree getTree(){
        return tree;
    }

    public void dispose(){
        platformTexture.dispose();

        dirt.dispose();

        if (hasTree) {
            tree.dispose();
        }
    }
}
