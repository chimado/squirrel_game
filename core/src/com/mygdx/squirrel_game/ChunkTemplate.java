package com.mygdx.squirrel_game;

// stores the data used to generate platforms for chunks very efficiently
public class ChunkTemplate {
    public int width, height, x, y, endOfWorld;
    public Boolean hasDirt, hasTree;

    public ChunkTemplate(int x, int y, int width, int height, int endOfWorld, Boolean hasDirt, Boolean hasTree) {
        this.x = x + endOfWorld;
        this.y = y;
        this.width = width;
        this.height = height;
        this.hasDirt = hasDirt;
        this.hasTree = hasTree;
    }
}