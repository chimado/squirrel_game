package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

// is responsible for button and mouse management
public class ButtonManager {
    final squirrel_game game;

    public enum Action {
        nothing,
        start,
        options,
        exit,
        main_menu,
        resume
    }

    int ypos;
    float deltaTime;
    Array<ButtonManager.Action> chosenActions;
    Array<TreeLeafsButton> leafButtons; // stores the buttons for the main menu screen
    Rectangle mouse; // is responsible for containing the mouse's position and hitbox which is used for button pressing
    // mouse could be changed to an animated object in the future
    Vector3 mousePosition;
    ButtonManager.Action currentAction;
    OrthographicCamera camera;
    ShapeRenderer shapeRenderer;

    public ButtonManager(final squirrel_game game, OrthographicCamera camera, ShapeRenderer shapeRenderer, Array<ButtonManager.Action> chosenActions){
        this.game = game;
        this.chosenActions = chosenActions;
        this.camera = camera;
        this.shapeRenderer = shapeRenderer;
        currentAction = Action.nothing;
        leafButtons = new Array<TreeLeafsButton>();
        ypos = 325;

        // initialize the mouse objects
        mousePosition = new Vector3();
        mouse = new Rectangle();
        mouse.width = 30;
        mouse.height = 30;

        for (ButtonManager.Action action : chosenActions) {
            leafButtons.add(new TreeLeafsButton(action, 500, ypos, 300, 300));
            ypos -= 150;
        }
    }

    // should only be called mid-render (including shape render)
    public void renderButtons(){
        deltaTime = Gdx.graphics.getDeltaTime();
        updateMouse();

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            shapeRenderer.rect(mouse.x, mouse.y, mouse.width, mouse.height);

            for (TreeLeafsButton leafButton : leafButtons) {
                shapeRenderer.rect(leafButton.bounds.x, leafButton.bounds.y, leafButton.bounds.width, leafButton.bounds.height);
            }
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

        for (TreeLeafsButton leafButton : leafButtons) {
            // checks if the button was clicked
            if (mouseClick(leafButton.bounds)) {
                currentAction = leafButton.action;
            }
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

    public void dispose() {
        for (TreeLeafsButton leafButton : leafButtons) {
            leafButton.dispose();
        }
    }
}
