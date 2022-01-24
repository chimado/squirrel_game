package com.mygdx.squirrel_game;

import java.lang.Math;

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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class game_screen implements Screen {
    final squirrel_game game;
    squirrel player;
    // store the world's size
    static final int worldWidth = 1280, worldHeight = 800;
    
    OrthographicCamera camera;
    Viewport viewport;
    float deltaTime;
    Boolean isPaused;
    Array<Platform> platforms; // stores the platforms for the game
    ShapeRenderer shapeRenderer;

    public game_screen(final squirrel_game game) {
        this.game = game;
        player = new squirrel(640, 300);
        deltaTime = 0;
        isPaused = false;
        platforms = new Array<Platform>();

        // temporary initialization of the platforms array (in the future this will be replaced by a world generation algorithm)
        for (int i = 0; i < 1; i++){
            platforms.add(new Platform(300, 30, 400 + i * 100, 100 + i * 100, false));
        }

        // create the camera and the viewport
		camera = new OrthographicCamera();
		camera.setToOrtho(false, worldWidth, worldHeight);
        viewport = new StretchViewport(worldWidth, worldHeight, camera);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0, 0, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();

        // pauses the game
        if (isPaused){
            deltaTime = 0;
        }

        // tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.CYAN);
        shapeRenderer.rect(player.x, player.y, player.width, player.height);
        
        game.batch.begin();
        game.batch.draw(player.render(deltaTime), player.x, player.y, player.width, player.height);
        for (Platform platform : platforms) {
            shapeRenderer.rect(platform.x, platform.y, platform.width, platform.height);
            game.batch.draw(platform.getPlatformTexture(), platform.x, platform.y, platform.width, platform.height);

            if (platform.hasDirt) {
                game.batch.draw(platform.getDirtTexture(), platform.x, platform.y - platform.height * 4.2f, platform.width, platform.y + platform.height);
            }
        }
        game.batch.end();

        shapeRenderer.end();

        // gets player input and updates the player's position
        if (player.isFacingLeft) {
            player.flip();
        }

        for (Platform platform : platforms) {
            if (player.overlaps(platform) && (!(player.state == squirrelState.Jumping) || player.fallTime > 1.2f) && player.x - platform.x < platform.width - 60 && platform.x < player.x + 80) {
                if (platform.y - player.y > 0) player.moveBy(0, platform.y - player.y - 3);
                player.moveBy(0, platform.y - player.y - 3);
                player.fallTime = 1f;
            }
            else player.isAffectedByGravity = true;

            if ((player.overlaps(platform) || player.state == squirrelState.Jumping) && 10 * deltaTime * (float)Math.pow(player.fallTime, 4) < 250 * deltaTime) player.canJump = true;
            else player.canJump = false;
        }

        if (player.isFacingLeft) {
            player.flip();
        }

        if (player.isAffectedByGravity) {
            player.moveBy(0, -10 * deltaTime * (float)Math.pow(player.fallTime, 4));
            player.isAffectedByGravity = false;
        }

        if (Gdx.input.isKeyPressed(Keys.UP) && player.canJump) {
            player.moveYBy(250 * deltaTime);
        }

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
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
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