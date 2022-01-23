package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.squirrel_game.squirrel.squirrelState;
import com.badlogic.gdx.graphics.GL20;


public class game_screen implements Screen {
    final squirrel_game game;
    squirrel player;
    
    Rectangle testRect;
    OrthographicCamera camera;
    float deltaTime;
    Boolean isPaused;

    public game_screen(final squirrel_game game) {
        this.game = game;
        player = new squirrel(640, 300);
        deltaTime = 0;
        isPaused = false;

        testRect = new Rectangle();
        testRect.x = 0;
        testRect.y = 200;
        testRect.width = 1280;
        testRect.height = 10;

        // create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 800);
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
        game.batch.draw(player.render(deltaTime), player.x, player.y, player.width, player.height);
        game.batch.end();

        // gets player input and updates the player's position
        if (player.overlaps(testRect) && (!(player.state == squirrelState.Jumping) || player.fallTime > 2f)) {
            player.moveBy(0, testRect.y - player.y);
            player.fallTime = 1f;}
        else player.moveBy(0, -70 * deltaTime * player.fallTime);
        if (Gdx.input.isKeyPressed(Keys.UP) && (player.overlaps(testRect) || player.state == squirrelState.Jumping) && player.getDY() * -1 < 200 * deltaTime) player.moveYBy(200 * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)) player.moveXBy(200 * deltaTime);
        if (Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)) player.moveXBy(-200 * deltaTime);
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