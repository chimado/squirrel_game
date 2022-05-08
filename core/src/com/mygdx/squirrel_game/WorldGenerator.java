package com.mygdx.squirrel_game;

import com.badlogic.gdx.utils.Array;

import static com.mygdx.squirrel_game.game_screen.*;

// is responsible for generating the world in the game screen
// outputs an array of chunk templates to fill platform objects with
public class WorldGenerator {
    Array<ChunkTemplate> ChunkTemplateArray;
    private int endOfWorld; // stores the end of the world's x coordinate

    public WorldGenerator(int newEndOfWorld) {
        ChunkTemplateArray = new Array<ChunkTemplate>();
        endOfWorld = newEndOfWorld;
    }

    public void changeEndOfWorld(int newEndOfWorld) {endOfWorld += newEndOfWorld;}
    public int getEndOfWorld() {return endOfWorld;}

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

            case 2:
                Chunk2();
                break;

            case 3:
                Chunk3();
                break;

            case 4:
                Chunk4();
                break;

            case 5:
                Chunk5();
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
        addChunkTemplate(400, 500, false);
    }

    // big jump over void
    private void Chunk1() {
        addChunkTemplate(200, 400, false);
        addChunkTemplate(400, 400, true);
        addChunkTemplate(700, 100, false);
        changeEndOfWorld(300);
    }

    // nice hill with trees
    private void Chunk2() {
        addChunkTemplate(0, 500, true);
        addChunkTemplate(100, 450, true);
    }

    // jump to higher place
    private void Chunk3() {
        addChunkTemplate(100, 400, false);
        changeEndOfWorld(300);
        addChunkTemplate(200, 400, false);
    }

    // double jump stairs
    private void Chunk4() {
        addChunkTemplate(100, 400, false);
        changeEndOfWorld(300);
        addChunkTemplate(200, 400, false);
        changeEndOfWorld(300);
        addChunkTemplate(300, 400, false);
    }

    // double jump stairs with trees and big jump at the end
    private void Chunk5() {
        addChunkTemplate(0, 400, true);
        addChunkTemplate(300, 400, true);
        addChunkTemplate(600, 400, true);
        addChunkTemplate(800, 100, false);
        changeEndOfWorld(200);
    }

    // adds a new chunk to the chunk template array
    // x and y coordinates are relative to the current position of the end of the world
    private void addChunkTemplate(int y, int width, Boolean hasTree){
        ChunkTemplateArray.add(new ChunkTemplate(worldStart + endOfWorld, y + baseY, width, basePlatformHeight, hasTree));
        changeEndOfWorld(width);
    }

}
