package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import static com.mygdx.squirrel_game.game_screen.worldWidth;
import static com.mygdx.squirrel_game.game_screen.worldHeight;
// import com.badlogic.gdx.audio.Music; reserved for later use
// import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

public class UserInterfaceScreen implements Screen{
    final squirrel_game game;

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
        }

        game.batch.begin();

        game.batch.end();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) shapeRenderer.end();
    }

    // updates the mouse's position according to the computers mouse
    public void updateMouse(){
        mousePosition.set(mouse.getX(), mouse.getY(), 0);
        camera.unproject(mousePosition);
        mouse.x = mousePosition.x;
        mouse.y = mousePosition.y;
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
