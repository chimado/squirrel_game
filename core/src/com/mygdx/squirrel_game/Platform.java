package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

// this object stores information about individual platforms
public class Platform extends GameObject{
    Texture platformTexture;
    Boolean hasTree;
    Boolean hasAcorn;
    Dirt dirt;
    Tree tree;
    Acorn acorn;

    public Platform(float width, float height, float x, float y, Boolean hasTree, Boolean hasAcorn, int acornX, int acornY) {
        super(width, height);
        super.x = x;
        super.y = y;

        this.hasTree = hasTree;
        this.hasAcorn = hasAcorn;
        platformTexture = new Texture(Gdx.files.internal("grass_platform.png"));

        dirt = new Dirt(width, y, x, 0);

        // checks if the platform has any additional objects on it
        if (hasTree) tree = new Tree(x, y);
        if (hasAcorn) acorn = new Acorn((int) (acornX + x), (int) (acornY + y));

        super.bounds.x = super.x;
        super.bounds.y = super.y;
        super.bounds.width = super.width;
        super.bounds.height = super.height / 2;
    }

    public Texture getPlatformTexture(){ return platformTexture; }

    public Dirt getDirt(){ return dirt; }

    public Tree getTree(){ return tree; }

    public Acorn getAcorn() { return acorn; }

    public void dispose(){
        platformTexture.dispose();

        dirt.dispose();

        if (hasTree) tree.dispose();

        if (hasAcorn) acorn.dispose();
    }
}
