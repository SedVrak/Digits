package com.try1.myapp.scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.try1.myapp.GameData;
import com.try1.myapp.GameType;
import com.try1.myapp.MainMenuActivity;
import com.try1.myapp.component.TextButton;
import com.try1.myapp.component.TextButtonActionListener;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import java.io.IOException;
import java.io.InputStream;

import static com.try1.myapp.MainMenuActivity.CAMERA_HEIGHT;
import static com.try1.myapp.MainMenuActivity.CAMERA_WIDTH;

/**
 * Created by SeniorJD.
 */
public class ScoreScene extends Scene implements IScene {
    private MainMenuActivity mainMenuActivity;

    private ITextureRegion textureRegion;

    private GameType gameType;
    private LevelType levelType;
    private int levelIndex;

    private LevelType nextLevelType;
    private int nextLevelIndex;

    private int highScore;

    private TextButton againTextButton;
    private TextButton nextTextButton;
    private TextButton exitTextButton;

    public ScoreScene(MainMenuActivity mainMenuActivity) {
        this.mainMenuActivity = mainMenuActivity;

        loadBundle();
        loadScore();

        if (gameType.equals(GameType.LEVELS)) {
            loadStarTexture();
        }

        init();
    }

    private void loadBundle() {
        gameType = GameType.valueOf(GameData.getExtra(GameData.GAME_TYPE_STRING));

        if (gameType.equals(GameType.LEVELS)) {
            levelType = LevelType.valueOf(GameData.getExtra(GameData.LEVEL_TYPE_STRING));
            levelIndex = Integer.valueOf(GameData.getExtra(GameData.LEVEL_NUMBER_STRING));

            if (levelIndex == 14) {
                nextLevelIndex = 0;
                nextLevelType = levelType.next();
            } else {
                nextLevelIndex = levelIndex + 1;
                nextLevelType = levelType;
            }
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            // ok
        } else {
            throw new IllegalArgumentException("Illegal GameType: " + gameType);
        }
    }

    private void loadScore() {
        if (gameType.equals(GameType.LEVELS)) {
            loadLevelsScore();
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            loadTimeAttackScore();
        } else {
            throw new IllegalArgumentException("Illegal GameType: " + gameType);
        }
    }

    private void loadTimeAttackScore() {
        SharedPreferences mScoreDb = mainMenuActivity.getSharedPreferences(GameData.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
        highScore = mScoreDb.getInt(GameData.HIGHSCORE_LABEL, 0);

        if (GameData.getScore() > highScore) {
            highScore = GameData.getScore();

            SharedPreferences.Editor mScoreDbEditor = mScoreDb.edit();
            mScoreDbEditor.putInt(GameData.HIGHSCORE_LABEL, GameData.getScore());
            mScoreDbEditor.apply();
        }
    }

    private void loadLevelsScore() {
        SharedPreferences mScoreDb = mainMenuActivity.getSharedPreferences(GameData.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
        highScore = mScoreDb.getInt(levelType.getKey() + " " + levelIndex, Integer.MAX_VALUE);

        SharedPreferences.Editor mScoreDbEditor = mScoreDb.edit();

        if (GameData.getScore(levelType, levelIndex) < highScore) {
            highScore = GameData.getScore(levelType, levelIndex);

            mScoreDbEditor.putInt(levelType.getKey() + " " + levelIndex, highScore);
            mScoreDbEditor.apply();
        }

        int lastOpenedLevel = mScoreDb.getInt(levelType.getKey(), 0);

        if (nextLevelIndex > lastOpenedLevel) {
            mScoreDbEditor.putInt(nextLevelType.getKey(), nextLevelIndex);
        }

        String s = mScoreDb.getString(GameData.LEVEL, LevelType.PLUS.toString());
        LevelType lastOpenedLevelType = LevelType.valueOf(s);

        if (nextLevelType.getIndex() > lastOpenedLevelType.getIndex()) {
            mScoreDbEditor.putString(GameData.LEVEL, nextLevelType.toString());
            mScoreDbEditor.putInt(nextLevelType.getKey(), nextLevelIndex);
        }

        mScoreDbEditor.apply();
    }

    private void loadStarTexture() {
        try {
            ITexture mTexture = new BitmapTexture(mainMenuActivity.getTextureManager(), new IInputStreamOpener() {
                //@Override
                public InputStream open() throws IOException {
                    return mainMenuActivity.getAssets().open("gfx/star.png");
                }
            });

            mTexture.load();

            textureRegion = TextureRegionFactory.extractFromTexture(mTexture, 0, 0, mTexture.getWidth(), mTexture.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        setBackground(GameData.getBackground());

        String scoreString = (gameType.equals(GameType.LEVELS) ? ("Time " + GameData.getScore(levelType, levelIndex) + "s") : ("Score " + GameData.getScore()));
        Text scoreText = new Text(0, CAMERA_HEIGHT/9, GameData.getFont100(), scoreString, new TextOptions(HorizontalAlign.CENTER), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(scoreText);

        Text highScoreText = new Text(0, CAMERA_HEIGHT * 2 / 9, GameData.getFont100(), "Record " + highScore + (gameType.equals(GameType.LEVELS) ? "s" : ""), new TextOptions(HorizontalAlign.CENTER), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(highScoreText);

        int i = 3;

        againTextButton = new TextButton("Again", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
        againTextButton.adjustPosition(0, CAMERA_HEIGHT * i / 6);
        registerTouchArea(againTextButton);
        attachChild(againTextButton);
        againTextButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                if (gameType.equals(GameType.TIME_ATTACK)) {
                    GameData.clearExtra();
                    GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
                    mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
                } else {
                    GameData.clearExtra();
                    GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
                    GameData.putExtra(GameData.LEVEL_TYPE_STRING, levelType.toString());
                    GameData.putExtra(GameData.LEVEL_NUMBER_STRING, String.valueOf(levelIndex));
                    mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
                }
            }
        });

        if (gameType.equals(GameType.LEVELS) && nextLevelType != null) {
            i++;

            nextTextButton = new TextButton("Next", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
            nextTextButton.adjustPosition(0, CAMERA_HEIGHT * i / 6);
            registerTouchArea(nextTextButton);
            attachChild(nextTextButton);
            nextTextButton.setTextButtonActionListener(new TextButtonActionListener() {
                @Override
                public void onClick() {
                    if (nextLevelType != null) {
                        GameData.clearExtra();
                        GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
                        GameData.putExtra(GameData.LEVEL_TYPE_STRING, nextLevelType.toString());
                        GameData.putExtra(GameData.LEVEL_NUMBER_STRING, String.valueOf(nextLevelIndex));
                        mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
                    } else {
                        GameData.clearExtra();
                        mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));
                    }
                }
            });
        }

        i++;
        exitTextButton = new TextButton("Menu", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(exitTextButton);
        registerTouchArea(exitTextButton);
        exitTextButton.adjustPosition(0, CAMERA_HEIGHT * i / 6);
        exitTextButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                GameData.clearExtra();
                mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));
            }
        });

        setText(scoreText, scoreText.getText().toString());
        setText(highScoreText, highScoreText.getText().toString());

        if (gameType.equals(GameType.LEVELS)) {
            int starsCount = GameData.getStarsCount(levelType, levelIndex, GameData.getScore(levelType, levelIndex));

            int x = CAMERA_WIDTH/2 - (starsCount * CAMERA_WIDTH/12) / 2;

            for (int j = 0; j < starsCount; j++) {
                ButtonSprite button;


                button = new ButtonSprite(x, CAMERA_HEIGHT * 2 / 6, textureRegion, mainMenuActivity.getVertexBufferObjectManager());
                button.setSize(CAMERA_WIDTH/12, CAMERA_WIDTH/12);

                attachChild(button);

                x += CAMERA_WIDTH/12;
            }
        }
    }

    private void setText(Text text, String s) {
        int length = GameData.getTextLength(GameData.getFont100(), s);

        float x = CAMERA_WIDTH/2 - length/2 - s.length();
        float y = text.getY();

        text.setPosition(x, y);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent event) {
        if (event.isActionDown()) {
            againTextButton.setPressed(false);
            nextTextButton.setPressed(false);
            exitTextButton.setPressed(false);
        } else if (event.isActionMove()) {
            if (!againTextButton.contains(event.getX(), event.getY())) {
                if (againTextButton.isPressed()) {
                    againTextButton.setPressed(false);
                }
            }

            if (!nextTextButton.contains(event.getX(), event.getY())) {
                if (nextTextButton.isPressed()) {
                    nextTextButton.setPressed(false);
                }
            }

            if (!exitTextButton.contains(event.getX(), event.getY())) {
                if (exitTextButton.isPressed()) {
                    exitTextButton.setPressed(false);
                }
            }
        }

        return super.onSceneTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            GameData.clearExtra();
            mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));
        }
        return false;
    }
}
