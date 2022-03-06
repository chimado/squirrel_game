package com.mygdx.squirrel_game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

// is the buttons for the main menu screen
public class TreeLeafsButton extends Rectangle {
    ObjectAnimation Leafs;
    Rectangle bounds;
    Boolean canBeAnimated;
    String text;

    public TreeLeafsButton(String text, float x, float y, float width, float height){
        // update the rectangle's parameters
        super.x = x;
        super.y = y;
        super.width = width;
        super.height = height;
        this.text = new String();
        this.text = text;

        canBeAnimated = true;

        // create the bounds for the button
        bounds = new Rectangle(x + width / 2 - 85, y + height / 2 - 10, width / 2f + 15, height / 2.5f);

        // initialize the Leafs object and load textures into it
        Leafs = new ObjectAnimation();
        Leafs.loadAnimation(text + "_tree", 12);
    }

    // gets the correct texture for rendering
    public Texture getLeafTexture(float delta) {
        return Leafs.getFrame(delta);
    }

    public void dispose() {
        Leafs.dispose();
    }
}
