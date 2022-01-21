package com.mygdx.squirrel_game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;


public class game_screen implements Screen {
    final squirrel_game game;
    squirrel player;
    
    OrthographicCamera camera;
    float deltaTime;
    Boolean isPaused;

    public game_screen(final squirrel_game game) {
        this.game = game;
        player = new squirrel();
        deltaTime = 0;
        isPaused = false;

        // create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 800);

        player.moveTo(640, 300);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();

        // pauses the game
        if (isPaused){
            deltaTime = 0;
        }

        // tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.batch.draw(player.render(deltaTime), player.getXPos(), player.getYPos(), player.getWidth(), player.getHeight());
        game.batch.end();

        // gets player input
        player.moveBy(0, 0);
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) player.moveBy(200 * deltaTime, 0);
        if (Gdx.input.isKeyPressed(Keys.LEFT)) player.moveBy(-200 * deltaTime, 0);
        if (Gdx.input.isKeyPressed(Keys.ENTER)) isPaused = true;
        if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) isPaused = false;

        if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
            dispose();
            Gdx.app.exit();
        }
    }

    @Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
        player.dispose();
	}
}