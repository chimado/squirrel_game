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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class UserInterfaceScreen implements Screen{
    final squirrel_game game;

    static final int playerStartingX = 200, playerStartingY = 200;
    squirrel player;
    Array<Platform> platforms; // stores the platforms for the main menu screen
    OrthographicCamera camera;
    Viewport viewport;
    float deltaTime;
    ShapeRenderer shapeRenderer; // is responsible for rendering the hitboxes for debugging purposes
    Rectangle mouse; // is responsible for containing the mouse's position and hitbox which is used for button pressing
    // mouse could be changed to an animated object in the future
    Vector3 mousePosition;

    public UserInterfaceScreen(final squirrel_game game){
        this.game = game;
        deltaTime = 0;
        player = new squirrel(playerStartingX, playerStartingY);
        platforms = new Array<Platform>();

        // create the camera and the viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, worldWidth, worldHeight);
        viewport = new StretchViewport(worldWidth, worldHeight, camera);
        shapeRenderer = new ShapeRenderer();

        // initialize the mouse objects
        mousePosition = new Vector3();
        mouse = new Rectangle();
        mouse.width = 30;
        mouse.height = 30;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 1, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();
        if (mouse.overlaps(player.bounds) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
            deltaTime += 2f;
        else if (player.idle_animation_time <= 2f){
            deltaTime = 0;
        }

        // tell the camera to update its matrices.
        camera.update();

        // tell the SpriteBatch to render in the coordinate system specified by the camera.
        game.batch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

        // update the mouse objects
        updateMouse();

        // show hitboxes for debugging purposes
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.CYAN);
            shapeRenderer.rect(mouse.x, mouse.y, mouse.width, mouse.height);
            shapeRenderer.rect(player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);

            for (Platform platform : platforms) {
                shapeRenderer.rect(platform.bounds.x, platform.bounds.y, platform.bounds.width, platform.bounds.height);
                if (platform.hasDirt) shapeRenderer.rect(platform.getDirt().bounds.x, platform.getDirt().bounds.y,
                        platform.getDirt().bounds.width, platform.getDirt().bounds.height);
            }

            shapeRenderer.end();
        }

        // Render objects for background
        game.batch.begin();

        game.batch.draw(player.render(deltaTime), player.x, player.y, player.width, player.height);

        for (Platform platform : platforms) {
            game.batch.draw(platform.getPlatformTexture(), platform.x, platform.y, platform.width, platform.height);
            if (platform.hasDirt) game.batch.draw(platform.getDirt().getDirtTexture(), platform.getDirt().x,
                    platform.getDirt().y, platform.getDirt().width, platform.getDirt().height);
        }

        game.batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.Q)){
            dispose();
            Gdx.app.exit();
        }
    }

    // updates the mouse's position according to the computers mouse
    public void updateMouse() {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);
        mouse.x = mousePosition.x - mouse.width / 2;
        mouse.y = mousePosition.y - mouse.height / 2;
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
    }
}
