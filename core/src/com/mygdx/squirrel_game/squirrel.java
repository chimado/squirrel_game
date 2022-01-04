package com.mygdx.squirrel_game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.Texture;


public class squirrel extends GameObject{
    final squirrel_game game;

    float elapsedTime;
    int squirrel_running_animation_index;
    Array<Texture> squirrel_running_animation;

    public squirrel(final squirrel_game game){
        super(32 * 3, 49 * 3);
        this.game = game;
        elapsedTime = 0;
        squirrel_running_animation_index = 0;

        squirrel_running_animation = new Array<Texture>();
        loadRunningAnimation();
    }

    // returns the texture to be rendered
    public Texture render(float delta) {
        elapsedTime += delta;

        if (elapsedTime > 1 / 15f){
            squirrel_running_animation_index++;
            elapsedTime = 0;
        }

        if (squirrel_running_animation_index > 7){
            squirrel_running_animation_index = 0;
        }

        return squirrel_running_animation.get(squirrel_running_animation_index);
    }

    public void loadRunningAnimation() {
        for (int i = 0; i < 8; i++) {
            squirrel_running_animation.add(new Texture(Gdx.files.internal("squirrel_running_1.png")));//"squirrel_running_" + (i + 1) + ".png")));
        }
    }
}
