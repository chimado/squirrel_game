package com.mygdx.squirrel_game;

import com.badlogic.gdx.utils.Array;

import static com.mygdx.squirrel_game.game_screen.*;

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

        ChunkConnector();

        switch (chunkType) {
            case 0:
                Chunk0();
                break;

            case 1:
                Chunk1();
                break;
        }

        ChunkConnector();

        return ChunkTemplateArray;
    }

    // connects the chunks together, must be present at the end and start of each chunk
    private void ChunkConnector() {addChunkTemplate(0, 500, false);}

    // big jump with tree
    private void Chunk0() {
        addChunkTemplate(100, 500, true);
        addChunkTemplate(450, 500, false);
    }

    private void Chunk1() {
        addChunkTemplate(200, 400, false);
        addChunkTemplate(400, 100, true);
        addChunkTemplate(400, 400, false);
    }

    // adds a new chunk to the chunk template array
    // x and y coordinates are relative to the current position of the end of the world
    private void addChunkTemplate(int y, int width, Boolean hasTree){
        ChunkTemplateArray.add(new ChunkTemplate(worldStart + endOfWorld, y + baseY, width, basePlatformHeight, hasTree));
        setEndOfWorld(endOfWorld + width);
    }

}
