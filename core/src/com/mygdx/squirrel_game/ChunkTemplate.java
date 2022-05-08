package com.mygdx.squirrel_game;

// stores the data used to generate platforms for chunks very efficiently
public class ChunkTemplate {
    public int width, height, x, y, acornX, acornY;
    public Boolean hasTree, hasAcorn;

    public ChunkTemplate(int x, int y, int width, int height, Boolean hasTree, Boolean hasAcorn, int acornX, int acornY) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hasTree = hasTree;
        this.hasAcorn = hasAcorn;
        this.acornX = acornX;
        this.acornY = acornY;
    }
}