package com.mygdx.squirrel_game;

import com.badlogic.gdx.utils.Array;

import static com.mygdx.squirrel_game.game_screen.basePlatformHeight;
import static com.mygdx.squirrel_game.game_screen.worldStart;

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
            case 0:
                Chunk0();
                break;
        }

        return ChunkTemplateArray;
    }

    private void Chunk0() {
        addChunkTemplate(worldStart, 200, 300, true, true);

        setEndOfWorld(endOfWorld + 500);
    }

    // adds a new chunk to the chunk template array
    private void addChunkTemplate(int x, int y, int width, Boolean hasDirt, Boolean hasTree){
        if (x + endOfWorld < worldStart) x = worldStart + endOfWorld; // makes sure no chunks will be created outside the world
        ChunkTemplateArray.add(new ChunkTemplate(x, y, width, basePlatformHeight, hasDirt, hasTree));}
}
