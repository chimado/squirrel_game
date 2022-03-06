package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    public enum Action {
        nothing,
        start,
        options,
        exit
    }

    static final int playerStartingX = 200, playerStartingY = 100;
    squirrel player;
    Array<Platform> platforms; // stores the platforms for the main menu screen
    Array<TreeLeafsButton> leafButtons; // stores the buttons for the main menu screen
    OrthographicCamera camera;
    Viewport viewport;
    float deltaTime;
    ShapeRenderer shapeRenderer; // is responsible for rendering the hitboxes for debugging purposes
    Rectangle mouse; // is responsible for containing the mouse's position and hitbox which is used for button pressing
    // mouse could be changed to an animated object in the future
    Vector3 mousePosition;
    Action action;

    public UserInterfaceScreen(final squirrel_game game){
        this.game = game;
        deltaTime = 0;
        player = new squirrel(playerStartingX, playerStartingY);
        platforms = new Array<Platform>();
        leafButtons = new Array<TreeLeafsButton>();
        action = Action.nothing;

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

        generatePlatforms();
        generateLeafButtons();
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
            shapeRenderer.rect(player.bounds.x, player.bounds.y, player.bounds.width, player.bounds.height);

            for (Platform platform : platforms) {
                shapeRenderer.rect(platform.bounds.x, platform.bounds.y, platform.bounds.width, platform.bounds.height);
                if (platform.hasDirt) shapeRenderer.rect(platform.getDirt().bounds.x, platform.getDirt().bounds.y,
                        platform.getDirt().bounds.width, platform.getDirt().bounds.height);
            }

            for (TreeLeafsButton leafButton : leafButtons) {
                shapeRenderer.rect(leafButton.bounds.x, leafButton.bounds.y, leafButton.bounds.width, leafButton.bounds.height);
            }

            shapeRenderer.end();
        }

        // Render objects for background
        game.batch.begin();

        game.batch.draw(player.render(deltaTime +
                        (mouseClick(player.bounds) ? 2f : (player.idle_animation_time <= 2f ? -deltaTime : 0))
                        ), player.x, player.y, player.width, player.height);

        for (Platform platform : platforms) {
            game.batch.draw(platform.getPlatformTexture(), platform.x, platform.y, platform.width, platform.height);
            if (platform.hasDirt) game.batch.draw(platform.getDirt().getDirtTexture(), platform.getDirt().x,
                    platform.getDirt().y, platform.getDirt().width, platform.getDirt().height);
        }

        for (TreeLeafsButton leafButton : leafButtons) {
            // draws the leaf buttons and animates them accordingly
            game.batch.draw(leafButton.getLeafTexture(((deltaTime +
                    (leafButton.bounds.overlaps(mouse) ? (leafButton.Leafs.currentFrame == 0 ? 0.05f : 0) :
                    (leafButton.Leafs.currentFrame == 0 ? -deltaTime : 0))) *
                    (leafButton.canBeAnimated ? 1 : 0))
                    ), leafButton.x, leafButton.y, leafButton.width, leafButton.height);

            // makes sure the animation will only trigger once per mouse overlap
            if (leafButton.bounds.overlaps(mouse) && leafButton.Leafs.currentFrame == 0) leafButton.canBeAnimated = false;
            else leafButton.canBeAnimated = true;

        }

        game.batch.end();

        for (TreeLeafsButton leafButton : leafButtons) {
            // checks if the button was clicked
            if (mouseClick(leafButton.bounds)) {

                // checks which button was clicked and reacts accordingly
                switch (leafButton.text) {
                    case "start":
                        action = Action.start;
                        break;

                    case "options":
                        action = Action.options;
                        break;

                    case "exit":
                        action = Action.exit;
                        break;
                }
            }
        }

        // check which action to do
        switch (action) {
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

    // updates the mouse's position according to the computers mouse
    public void updateMouse() {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);
        mouse.x = mousePosition.x - mouse.width / 2;
        mouse.y = mousePosition.y - mouse.height / 2;
    }

    // checks if the mouse is clicking a Rectangle
    public boolean mouseClick(Rectangle Button){
        if (mouse.overlaps(Button) && Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            return true;
        }
        return false;
    }

    // generates the platforms for the main menu screen
    public void generatePlatforms(){
        for (int i = 0; i < 5; i++){
            platforms.add(new Platform(300, 30, i * 300, 127, true, false));
        }
    }

    // generates the leaf buttons
    public void generateLeafButtons(){
        leafButtons.add(new TreeLeafsButton(new String("start"), 500, 325, 300, 300));
        leafButtons.add(new TreeLeafsButton(new String("options"), 500, 175, 300, 300));
        leafButtons.add(new TreeLeafsButton(new String("exit"), 500, 25, 300, 300));
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

        for (TreeLeafsButton leafButton : leafButtons){
            leafButton.dispose();
        }

        player.dispose();
    }
}
