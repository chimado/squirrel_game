package com.mygdx.squirrel_game;

import java.lang.Math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
// import com.badlogic.gdx.audio.Music; reserved for later use
// import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
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
    // stores the world's size
    static final int worldWidth = 1280, worldHeight = 800;
    
    OrthographicCamera camera;
    Viewport viewport;
    float deltaTime;
    Boolean isPaused;
    Array<Platform> platforms; // stores the platforms for the game
    ShapeRenderer shapeRenderer; // is responsible for rendering the hitboxes for debugging purposes
    CameraView viewBox; // is followed by the camera, loosely follows the player
    Array<ButtonManager.Action> chosenActions;
    ButtonManager buttonManager;

    public game_screen(final squirrel_game game) {
        this.game = game;
        player = new squirrel(640, 300);
        deltaTime = 0;
        isPaused = false;
        platforms = new Array<Platform>();
        viewBox = new CameraView(500, 400, player.x - 150, player.y - 150);
        chosenActions = new Array<ButtonManager.Action>();

        // temporary initialization of the platforms array (in the future this will be replaced by a world generation algorithm)
        for (int i = 0; i < 2; i++) platforms.add(new Platform(300, 30, 400 + i * 200, 100 + i * 200, true, false));


        platforms.add(new Platform(300, 30, 800, 100, true, true));

        // create the camera and the viewport
		camera = new OrthographicCamera();
		camera.setToOrtho(false, worldWidth, worldHeight);
        viewport = new StretchViewport(worldWidth, worldHeight, camera);
        shapeRenderer = new ShapeRenderer();

        // initialize the button manager
        chosenActions.add(ButtonManager.Action.resume, ButtonManager.Action.main_menu);
        buttonManager = new ButtonManager(this.game, this.camera, this.chosenActions);
    }

    @Override
    public void render(float delta){
        ScreenUtils.clear(0, 0, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        deltaTime = Gdx.graphics.getDeltaTime();

        // pauses the game
        if (isPaused) deltaTime = 0;

        // tell the camera to update its matrices.
        camera.position.set(viewBox.x, 300, 0);
		camera.update();
        buttonManager.setCamera(camera);

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // show hitboxes for debugging purposes
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);
        }

        game.batch.begin();
        if (Gdx.input.isKeyPressed(Keys.SPACE)) shapeRenderer.rect(viewBox.x, viewBox.y, viewBox.width, viewBox.height);
        for (Platform platform : platforms) {
            if (Gdx.input.isKeyPressed(Keys.SPACE)) shapeRenderer.rect(platform.bounds.x, platform.bounds.y, platform.bounds.width, platform.bounds.height);
            game.batch.draw(platform.getPlatformTexture(), platform.x, platform.y, platform.width, platform.height);

            if (platform.hasDirt) {
                game.batch.draw(platform.getDirt().getDirtTexture(), platform.getDirt().x, platform.getDirt().y, platform.getDirt().width, platform.getDirt().height);
                if (Gdx.input.isKeyPressed(Keys.SPACE)) shapeRenderer.rect(platform.getDirt().x, platform.getDirt().y, platform.getDirt().width, platform.getDirt().height);
            }

            if (platform.hasTree) {
                game.batch.draw(platform.getTree().getTreeAnimation(deltaTime), platform.getTree().x, platform.getTree().y - 45, platform.getTree().width, platform.getTree().height);
                if (Gdx.input.isKeyPressed(Keys.SPACE)) shapeRenderer.rect(platform.getTree().bounds.x, platform.getTree().bounds.y - 45, platform.getTree().bounds.width, platform.getTree().bounds.height);
            }
        }

        if (player.state != squirrelState.InTree && player.state != squirrelState.Climbing) game.batch.draw(player.render(deltaTime), player.x, player.y - 23, player.width, player.height);
        else if (player.state == squirrelState.Climbing && player.state != squirrelState.InTree) game.batch.draw(player.render(deltaTime), player.x - 80, player.y - 23, player.width, player.height);

        // renders the buttons if the game is paused
        if (isPaused) {
            buttonManager.renderButtons(viewBox.x - 600, 0);
            buttonManager.moveButtonBoundsXTo((int) viewBox.x);
            buttonManager.changeButtonActivation(true);
        }
        else {
            buttonManager.renderButtons(-9000, -9000);
            buttonManager.changeButtonActivation(false);
        }

        game.batch.end();

        if (Gdx.input.isKeyPressed(Keys.SPACE)) shapeRenderer.end();

        // the player needs to be facing right when calculating its position in order for the overlaps function to work
        if (player.isFacingLeft) player.flip();

        player.isAffectedByGravity = true;
        player.canJump = false;
        player.canClimb = false;
        player.inTreeTime += deltaTime;

        // updates the viewBox's position
        if (!viewBox.contains(player.bounds)) {
            viewBox.moveBy(player.getDX(), player.getDY());

            if (!viewBox.contains(player.bounds)) viewBox.moveTo(player.x - 150, player.y - 150);
        }

        // checks which platforms the player is touching and moves it accordingly
        for (Platform platform : platforms) {
            // checks if the player is touching any of the platforms with some fixes to make the movement look smoother and more realistic (aka making sure the player doesn't teleport or walk on the air)
            if (player.bounds.overlaps(platform.bounds) && (!(player.state == squirrelState.Jumping) || player.fallTime > 1.2f) && player.x - platform.x < platform.width - 60 && platform.x < player.x + 80 && platform.y - player.y < 10) {
                if (platform.y - player.y > 0) player.moveYBy(platform.y - player.y - 3);
                player.moveYBy(platform.y - player.y - 3);
                player.fallTime = 1f;
                player.isAffectedByGravity = false;
            }

            if (player.state == squirrelState.Climbing || player.state == squirrelState.InTree) player.isAffectedByGravity = false;

            // checks if the player can jump
            if (((player.bounds.overlaps(platform.bounds) || player.state == squirrelState.Jumping) && 10 * deltaTime * (float)Math.pow(player.fallTime, 4) < 250 * deltaTime) || player.state == squirrelState.InTree) player.canJump = true;
            else if (!player.canJump) player.canJump = false;

            // checks if the player is touching the dirt in order for it to not move through it
            if (platform.hasDirt && player.bounds.overlaps(platform.getDirt().bounds) && platform.y - player.y > 10 && (Math.abs(player.x - platform.x) < 125 || Math.abs(player.x - (platform.x + platform.width)) > 20 && player.x > platform.x)) {
                player.moveXBy(player.getDX() * -1);
                
                // makes sure the player won't get stuck in the dirt
                // this is for the left side of the dirt
                if (platform.x < player.x + 110 && Math.abs(player.x - platform.x) < 110){
                    player.moveXBy(player.getDX() * -1);
                    // this is for if the player is coming from below the platform
                    if (player.getDX() > 0) player.moveBy(20, 10);
                    // this is for if the player is coming from above the platform and the same goes for block of code on the right side of the dirt part of this if statement
                    else player.moveXBy(-40);
                }

                // this is for the right side of the dirt
                else if (Math.abs(player.x - (platform.x + platform.width)) > 20 && player.x > platform.x && platform.x + platform.width > player.x + 20) {
                    player.moveXBy(player.getDX() * -1);
                    if (player.getDX() < 0) player.moveBy(-20, 10);
                    else player.moveXBy(10);
                }
            }

            if (platform.hasTree) {
                // checks if the player can climb up a tree
                if (player.bounds.overlaps(platform.getTree().bounds) && platform.getTree().bounds.y + platform.getTree().bounds.height > player.bounds.y * 1.2f) player.canClimb = true;
                else if (player.state != squirrelState.Climbing && !player.canClimb) player.canClimb = false;

                // checks if the player wants to jump out of the tree
                if (player.inTreeTime > 1f && player.bounds.overlaps(platform.getTree().bounds) && player.state == squirrelState.InTree && Gdx.input.isKeyPressed(Keys.UP)) {
                    player.state = squirrelState.Jumping;
                    platform.getTree().animateTree();
                }

                // makes sure the player is at the x value of the tree it's climbing 
                if (player.state == squirrelState.Climbing && player.bounds.overlaps(platform.getTree().bounds)) {
                    player.moveXBy(platform.getTree().bounds.x + platform.getTree().bounds.width / 2 - player.x / 1.016f);

                    if (player.y < platform.getTree().y) {
                        player.moveYBy(50);
                    }
                }

                // checks if the player has reached the top of the tree
                if (platform.getTree().bounds.y + platform.getTree().bounds.height < player.bounds.y * 1.2f && player.state == squirrelState.Climbing && player.bounds.overlaps(platform.getTree().bounds)) player.state = squirrelState.InTree;
            }
        }

        if (player.state == squirrelState.InTree) {
            player.isAffectedByGravity = false;
            player.canClimb = false;
            player.canJump = false;

            if (player.isFacingLeft) player.updateBounds();
        }

        // changes the player's position if it's affected by gravity
        if (player.isAffectedByGravity) {
            player.moveYBy(-10 * deltaTime * (float)Math.pow(player.fallTime, 4));
            player.isAffectedByGravity = false;
        }

        // changes the player's position if it's jumping or climbing
        if (Gdx.input.isKeyPressed(Keys.UP) && (player.canJump || player.canClimb)) player.moveYBy(250 * deltaTime);

        // flips the player back after the use of overlaps is over
        if (player.isFacingLeft) player.flip();

        // the next two if statements are for debugging purposes only
        // they reduce the number of game restarts necessary for development by making sure the player won't go out of bounds
        if (player.y < -200) player.moveTo(player.x, 340);

        if (player.x > worldWidth || player.x < 0) player.moveTo(600, player.y);


        // gets player input and updates the player's position
        player.moveXBy(0);
        if (player.state != squirrelState.Climbing && player.state != squirrelState.InTree){    
            if (Gdx.input.isKeyPressed(Keys.RIGHT) && !Gdx.input.isKeyPressed(Keys.LEFT)) player.moveXBy(200 * deltaTime);
            if (Gdx.input.isKeyPressed(Keys.LEFT) && !Gdx.input.isKeyPressed(Keys.RIGHT)) player.moveXBy(-200 * deltaTime);
        }

        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) isPaused = !isPaused;

        // check which action to do according to the buttons
        switch (buttonManager.currentAction) {
            case main_menu:
                dispose();
                game.setScreen(new UserInterfaceScreen(game));
                break;

            case resume:
                isPaused = false;
                break;
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

        buttonManager.dispose();

        for (Platform platform : platforms) platform.dispose();
	}
}