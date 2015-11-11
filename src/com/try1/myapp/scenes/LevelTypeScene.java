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
import org.andengine.input.touch.TouchEvent;

/**
 * Created by SeniorJD.
 */
public class LevelTypeScene extends Scene implements IScene {

    private MainMenuActivity mainMenuActivity;

    private TextButton[] textButtons = new TextButton[LevelType.values().length];

    private LevelType lastOpenedLevelType;

    public LevelTypeScene(MainMenuActivity mainMenuActivity) {
        this.mainMenuActivity = mainMenuActivity;

        loadLastOpenedLevel();

        init();
    }

    private void init() {
        setBackground(GameData.getBackground());

        for (int i = 0; i < textButtons.length; i++) {
            final LevelType levelType = LevelType.values()[i];
            String s = levelType.getKey();

            TextButton textButton = new TextButton(s, GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
            textButton.setEnabled(i <= lastOpenedLevelType.getIndex());
            textButton.setTextButtonActionListener(new TextButtonActionListener() {
                @Override
                public void onClick() {
                    GameData.clearExtra();
                    GameData.putExtra(GameData.GAME_TYPE_STRING, GameType.LEVELS.toString());
                    GameData.putExtra(GameData.LEVEL_TYPE_STRING, levelType.toString());

                    mainMenuActivity.setNewScene(new LevelsScene(mainMenuActivity));
                }
            });

            attachChild(textButton);
            registerTouchArea(textButton);

            textButton.adjustPosition(0, MainMenuActivity.CAMERA_HEIGHT * (i + 1) / 6);

            textButtons[i] = textButton;
        }
    }

    private void loadLastOpenedLevel() {
        SharedPreferences mScoreDb = mainMenuActivity.getSharedPreferences(GameData.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
        String s = mScoreDb.getString(GameData.LEVEL, LevelType.PLUS.toString());

        lastOpenedLevelType = LevelType.valueOf(s);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent event) {
        if (event.isActionDown()) {
            for (TextButton textButton : textButtons) {
                textButton.setPressed(false);
            }
        } else if (event.getAction() == TouchEvent.ACTION_MOVE) {
            for (TextButton textButton : textButtons) {
                if (!textButton.contains(event.getX(), event.getY())) {
                    if (textButton.isPressed()) {
                        textButton.setPressed(false);
                    }
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
