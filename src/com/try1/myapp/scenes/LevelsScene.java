package com.try1.myapp.scenes;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.KeyEvent;

import com.try1.myapp.GameData;
import com.try1.myapp.GameType;
import com.try1.myapp.MainMenuActivity;
import com.try1.myapp.component.RecordTextButton;
import com.try1.myapp.component.TextButtonActionListener;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

/**
 * Created by SeniorJD.
 */
public class LevelsScene extends Scene implements IScene {

    private MainMenuActivity mainMenuActivity;

    private RecordTextButton[] levelsTextButtons = new RecordTextButton[15];

    private int[] records;

    private LevelType levelType;
    private int lastOpenedLevel;

    public LevelsScene(MainMenuActivity mainMenuActivity) {
        this.mainMenuActivity = mainMenuActivity;

        loadLevelType();
        loadLastOpenedLevel();

        init();
    }

    private void init() {
        setBackground(GameData.getBackground());

        float px = 0;
        float py = MainMenuActivity.CAMERA_HEIGHT/6;

        for (int i = 0; i < levelsTextButtons.length; i++) {
            final int levelIndex = i;
            String s = String.valueOf(levelIndex + 1);

            int starsCount = (i <= lastOpenedLevel) ? GameData.getStarsCount(levelType, i, records[i]) : -1;

            RecordTextButton recordTextButton = new RecordTextButton(s, GameData.getFont150(), mainMenuActivity, starsCount);
            recordTextButton.setEnabled(i <= lastOpenedLevel);

            recordTextButton.adjustPosition(px, py);

            attachChild(recordTextButton);
            registerTouchArea(recordTextButton);

            recordTextButton.setTextButtonActionListener(new TextButtonActionListener() {
                @Override
                public void onClick() {
                    GameData.clearExtra();
                    GameData.putExtra(GameData.GAME_TYPE_STRING, GameType.LEVELS.toString());
                    GameData.putExtra(GameData.LEVEL_TYPE_STRING, levelType.toString());
                    GameData.putExtra(GameData.LEVEL_NUMBER_STRING, String.valueOf(levelIndex));

                    mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
                }
            });

            levelsTextButtons[i] = recordTextButton;

            if ((i + 1) % 3 == 0) {
                py += MainMenuActivity.CAMERA_HEIGHT/6;
                px = 0;
            } else {
                px += MainMenuActivity.CAMERA_WIDTH/3;
            }
        }

        Text level = new Text(0, MainMenuActivity.CAMERA_HEIGHT/12, GameData.getFont100(), levelType.getKey(), mainMenuActivity.getVertexBufferObjectManager());
        setText(level, levelType.getKey());
        attachChild(level);
    }

    private void setText(Text text, String s) {
        int length = GameData.getTextLength(GameData.getFont100(), s);

        float x = MainMenuActivity.CAMERA_WIDTH/2 - length/2 - s.length();
        float y = text.getY();

        text.setPosition(x, y);
    }

    private void loadLevelType() {
        levelType = LevelType.valueOf(GameData.getExtra(GameData.LEVEL_TYPE_STRING));
    }

    private void loadLastOpenedLevel() {
        SharedPreferences mScoreDb = mainMenuActivity.getSharedPreferences(GameData.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
        lastOpenedLevel = mScoreDb.getInt(levelType.getKey(), 0);

        records = new int[lastOpenedLevel + 1];

        for (int i = 0; i < lastOpenedLevel + 1; i++) {
            records[i] = mScoreDb.getInt(levelType.getKey() + " " + i, Integer.MAX_VALUE);
        }
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent event) {
        if (event.isActionDown()) {
            for (RecordTextButton levelsTextButton : levelsTextButtons) {
                levelsTextButton.setPressed(false);
            }
        } else if (event.isActionMove()) {
            for (RecordTextButton levelsTextButton : levelsTextButtons) {
                if (!levelsTextButton.contains(event.getX(), event.getY())) {
                    if (levelsTextButton.isPressed()) {
                        levelsTextButton.setPressed(false);
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
            mainMenuActivity.setNewScene(new LevelTypeScene(mainMenuActivity));
        }
        return false;
    }
}
