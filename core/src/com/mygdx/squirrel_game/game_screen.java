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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.GL20;


public class game_screen implements Screen {
    final squirrel_game game;

    TextureAtlas squirrelAtlas;
    Animation<TextureRegion> squirrelIdle;
    Animation<TextureRegion> squirrelRunning;
    Animation<TextureRegion> squirrelJumping;
    Animation<TextureRegion> squirrelFalling;
    Animation<TextureRegion> squirrelStanding;
    TextureRegion[] squirrelIdleFrames;
    TextureRegion[] squirrelRunningFrames;
    TextureRegion[] squirrelJumpingFrames;
    TextureRegion[] squirrelFallingFrames;
    TextureRegion[] squirrelStandingFrames;
    Vector3 position;
    float elapsedTime;
    
    OrthographicCamera camera;

    public game_screen(final squirrel_game game){
        this.game = game;
        elapsedTime = 0;

        // load the squirrel's textures, animations and rectangle
        squirrelAtlas = new TextureAtlas(Gdx.files.internal("squirrel.txt"));
        squirrelIdleFrames = new TextureRegion[4];
        squirrelRunningFrames = new TextureRegion[8];
        squirrelJumpingFrames = new TextureRegion[4];
        squirrelFallingFrames = new TextureRegion[7];
        squirrelStandingFrames = new TextureRegion[1];

        // import the specific sprites from the atlas to the texture regions
        squirrelIdleFrames[0] = (squirrelAtlas.findRegion("squirrel_basic"));
        squirrelIdleFrames[1] = (squirrelAtlas.findRegion("squirrel_idle", 1));
        squirrelIdleFrames[2] = (squirrelAtlas.findRegion("squirrel_idle", 2));
        squirrelIdleFrames[3] = (squirrelAtlas.findRegion("squirrel_idle", 3));

        for (int i = 0; i < squirrelIdleFrames.length; i++){
            squirrelRunningFrames[i] = (squirrelAtlas.findRegion("squirrel_running", i + 1));
        }

        for (int i = 0; i < squirrelJumpingFrames.length; i++){
            squirrelJumpingFrames[i] = (squirrelAtlas.findRegion("squirrel_running", i + 3));

        }

        for (int i = 0; i < squirrelFallingFrames.length; i++){
            squirrelFallingFrames[i] = (squirrelAtlas.findRegion("squirrel_falling", i + 1));
        }

        squirrelStandingFrames[0] = (squirrelAtlas.findRegion("0001"));

        squirrelIdle = new Animation<TextureRegion>(1/15f, squirrelIdleFrames);
        squirrelRunning = new Animation<TextureRegion>(1/15f, squirrelRunningFrames);
        squirrelJumping = new Animation<TextureRegion>(1/15f, squirrelJumpingFrames);
        squirrelFalling = new Animation<TextureRegion>(1/15f, squirrelFallingFrames);
        squirrelStanding = new Animation<TextureRegion>(1/15f, squirrelStandingFrames);

        // create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1280, 800);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        elapsedTime += Gdx.graphics.getDeltaTime();
        game.batch.draw(squirrelRunning.getKeyFrame(elapsedTime, true), 640, 400, 48 * 3, 32 * 3);
        game.batch.end();

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
        squirrelAtlas.dispose();
	}
}