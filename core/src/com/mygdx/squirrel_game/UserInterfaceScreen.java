package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static com.mygdx.squirrel_game.game_screen.worldWidth;
import static com.mygdx.squirrel_game.game_screen.worldHeight;
// import com.badlogic.gdx.audio.Music; reserved for later use
// import com.badlogic.gdx.audio.Sound;

public class UserInterfaceScreen implements Screen{
    final squirrel_game game;

    static final int playerStartingX = 200, playerStartingY = 100;
    squirrel player;
    Array<Platform> platforms; // stores the platforms for the main menu screen
    OrthographicCamera camera;
    Viewport viewport;
    float deltaTime;
    ShapeRenderer shapeRenderer; // is responsible for rendering the hitboxes for debugging purposes
    Array<ButtonManager.Action> chosenActions;
    ButtonManager buttonManager;

    public UserInterfaceScreen(final squirrel_game game){
        this.game = game;
        deltaTime = 0;
        player = new squirrel(playerStartingX, playerStartingY);
        platforms = new Array<Platform>();
        chosenActions = new Array<ButtonManager.Action>();

        // create the camera and the viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);
        viewport = new StretchViewport(worldWidth, worldHeight, camera);
        shapeRenderer = new ShapeRenderer();

        // initialize the button manager
        chosenActions.add(ButtonManager.Action.start, ButtonManager.Action.options, ButtonManager.Action.exit);
        buttonManager = new ButtonManager(this.game, this.camera, this.chosenActions);

        generatePlatforms();
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // Render objects for background
        game.batch.begin();

        // show hitboxes for debugging purposes
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);

            for (Platform platform : platforms) {
                shapeRenderer.rect(platform.bounds.x, platform.bounds.y, platform.bounds.width, platform.bounds.height);
                shapeRenderer.rect(platform.getDirt().bounds.x, platform.getDirt().bounds.y,
                        platform.getDirt().bounds.width, platform.getDirt().bounds.height);
            }

            shapeRenderer.end();
        }

        buttonManager.renderButtons(0, 0);

        game.batch.draw(player.render(deltaTime +
                        (buttonManager.mouseClick(player.bounds) ? 2f : (player.idle_animation_time <= 2f ? -deltaTime : 0))
                        ), player.x, player.y, player.width, player.height);

        for (Platform platform : platforms) {
            game.batch.draw(platform.getPlatformTexture(), platform.x, platform.y, platform.width, platform.height);
            game.batch.draw(platform.getDirt().getDirtTexture(), platform.getDirt().x,
                    platform.getDirt().y, platform.getDirt().width, platform.getDirt().height);
        }

        game.batch.end();

        // check which action to do according to the buttons
        switch (buttonManager.currentAction) {
            case start:
                dispose();
                game.setScreen(new game_screen(game));
                break;

            case options:
                break;

            case exit:
                dispose();
                Gdx.app.exit();
                break;
        }
    }

    // generates the platforms for the main menu screen
    public void generatePlatforms(){
        for (int i = 0; i < 5; i++){
            platforms.add(new Platform(300, 30, i * 300, 127, false, false, 0, 0));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        game.batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        for (Platform platform : platforms) {
            platform.dispose();
        }

        buttonManager.dispose();

        player.dispose();
    }
}
