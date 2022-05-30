package com.mygdx.squirrel_game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.Texture;

import static com.mygdx.squirrel_game.game_screen.winScore;

// displays text at the end of a round showing the player if they won or lost and their score
public class endGameText {
    final squirrel_game game;
    // needs to account for offset created by player movement
    public final float maxDisplayCounterValue = 2.3f, yOffset = 200,
            xFromNextNumberIncrement = 100, startY = 700, xOffsetFromOffset = 600;
    public float displayCounter = 0, xOffset, xFromNextNumber = 100;
    // textures for the text
    Texture winMessage, scoreText;
    Array<Texture> score;
    Boolean isWinMessageEmpty = true;

    public endGameText(final squirrel_game game) {
        this.game = game;
        scoreText = new Texture(Gdx.files.internal("score.png"));
        score = new Array<Texture>();
    }

    public void initializeEndGameText(int score, float x) {
        if(score >= winScore) winMessage = new Texture(Gdx.files.internal("you_win.png"));
        else winMessage = new Texture(Gdx.files.internal("game_over.png"));
        isWinMessageEmpty = false;

        for (int i = score; i > 0; i /= 10)
            this.score.add(new Texture(Gdx.files.internal("num" + (i % 10) + ".png")));

        xOffset = x;
    }

    // render the text
    public void render() {
        game.batch.draw(winMessage, xOffset + xOffsetFromOffset, startY);
        game.batch.draw(scoreText, xOffset + xOffsetFromOffset, startY - yOffset);

        for (Texture num : score) {
            game.batch.draw(num, xOffset + xOffsetFromOffset + xFromNextNumber, startY - yOffset);
            xFromNextNumber += xFromNextNumberIncrement;
        }

        xFromNextNumber = xFromNextNumberIncrement;
    }

    public Boolean isDisplayTimeOver(float delta) {
        displayCounter += delta;

        if(displayCounter >= maxDisplayCounterValue) return true;
        else return false;
    }

    public void dispose() {
        if(!isWinMessageEmpty) winMessage.dispose();
        scoreText.dispose();

        for (Texture num : score) num.dispose();
    }
}
