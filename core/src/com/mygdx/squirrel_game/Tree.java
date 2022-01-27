package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

// this object stores information about individual Tree components of platforms that have a Tree
public class Tree extends GameObject{
    Texture treeTexture;
    Boolean hasTree;

    public Tree(float x, float y) {
        super(64 * 3, 64 * 3);
        super.x = x;
        super.y = y;

        treeTexture = new Texture(Gdx.files.internal("tree.png"));
    }

    public Texture getTreeTexture(){
        return treeTexture;
    }
    
    public void dispose(){
        treeTexture.dispose();
    }
}
