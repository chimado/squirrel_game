package com.mygdx.squirrel_game;

import com.badlogic.gdx.graphics.Texture;

// this object stores information about individual Tree components of platforms that have a Tree
public class Tree extends GameObject{
    ObjectAnimation treeAnimation;
    Boolean hasTree;
    Boolean isAnimated;

    public Tree(float x, float y) {
        super(128 * 3, 128 * 3);
        super.x = x;
        super.y = y;
        isAnimated = false;

        treeAnimation = new ObjectAnimation();
        treeAnimation.loadAnimation("tree", 12);
    }

    public Texture getTreeAnimation(float delta){
        if (isAnimated && treeAnimation.currentFrame != 11) return treeAnimation.getFrame(delta);
        else return treeAnimation.frames.get(0);
    }

    public void animateTree(){
        isAnimated = true;
        treeAnimation.currentFrame = 0;
    }
    
    public void dispose(){
        treeAnimation.dispose();
    }
}
