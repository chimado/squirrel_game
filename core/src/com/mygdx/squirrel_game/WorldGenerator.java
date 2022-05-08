package com.mygdx.squirrel_game;

import com.badlogic.gdx.utils.Array;

// is responsible for generating the world in the game screen
// outputs an array of chunk templates to fill platform objects with
public class WorldGenerator {
    Array<ChunkTemplate> ChunkTemplateArray;
    int endOfWorld; // stores the end of the world's x coordinate

    public WorldGenerator(int newEndOfWorld) {
        ChunkTemplateArray = new Array<ChunkTemplate>();
        setEndOfWorld(newEndOfWorld);
    }

    public void setEndOfWorld(int newEndOfWorld) {endOfWorld = newEndOfWorld;}

    public Array<ChunkTemplate> GenerateChunk(int chunkType) {
        if (!ChunkTemplateArray.isEmpty()) ChunkTemplateArray.clear();

        switch (chunkType) {
            case 1:
                Chunk1();
                break;
        }

        return ChunkTemplateArray;
    }

    private void Chunk1() {
        ChunkTemplateArray.add(new ChunkTemplate(400, 100, 300, 30, endOfWorld, false, false));
        ChunkTemplateArray.add(new ChunkTemplate(500, 200, 300, 30, endOfWorld, true, true));

        setEndOfWorld(endOfWorld + 500);
    }
}
