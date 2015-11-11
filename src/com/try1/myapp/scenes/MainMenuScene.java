package com.try1.myapp.scenes;

import android.content.Context;
import android.content.SharedPreferences;

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
public class MainMenuScene extends Scene implements IScene {

    private TextButton startGameLevelsButton;
    private TextButton startGameTimeAttackButton;
    private TextButton tutorialButton;
    private TextButton exitGameButton;

    private MainMenuActivity mainMenuActivity;

    private boolean isFirstLaunch;

    public MainMenuScene(final MainMenuActivity mainMenuActivity) {
        this.mainMenuActivity = mainMenuActivity;

        loadIsFirstLaunch();

        setBackground(GameData.getBackground());

        startGameLevelsButton = new TextButton("Levels", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
        startGameLevelsButton.setEnabled(!isFirstLaunch);
        attachChild(startGameLevelsButton);
        registerTouchArea(startGameLevelsButton);
        startGameLevelsButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                GameData.clearExtra();
                mainMenuActivity.setNewScene(new LevelTypeScene(mainMenuActivity));
            }
        });

        startGameTimeAttackButton = new TextButton("Time attack", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
        startGameTimeAttackButton.setEnabled(!isFirstLaunch);
        attachChild(startGameTimeAttackButton);
        registerTouchArea(startGameTimeAttackButton);
        startGameTimeAttackButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                GameData.clearExtra();
                GameData.putExtra(GameData.GAME_TYPE_STRING, GameType.TIME_ATTACK.toString());

                mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
            }
        });

        if (isFirstLaunch) {
            tutorialButton = new TextButton("Tutorial", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
            attachChild(tutorialButton);
            registerTouchArea(tutorialButton);
            tutorialButton.setTextButtonActionListener(new TextButtonActionListener() {
                @Override
                public void onClick() {
                    GameData.clearExtra();
                    GameData.putExtra(GameData.GAME_TYPE_STRING, GameType.TIME_ATTACK.toString());

                    mainMenuActivity.setNewScene(new TutorialScene(mainMenuActivity));
                }
            });
        }

        exitGameButton = new TextButton("Exit", GameData.getFont100(), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(exitGameButton);
        registerTouchArea(exitGameButton);
        exitGameButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                mainMenuActivity.finish();
            }
        });

        int i = 1;
        startGameLevelsButton.adjustPosition(0, MainMenuActivity.CAMERA_HEIGHT * i / 6);
        i++;
        startGameTimeAttackButton.adjustPosition(0, MainMenuActivity.CAMERA_HEIGHT * i / 6);
        i++;
        if (isFirstLaunch) {
            tutorialButton.adjustPosition(0, MainMenuActivity.CAMERA_HEIGHT * i / 6);
        }
        i++;
        exitGameButton.adjustPosition(0, MainMenuActivity.CAMERA_HEIGHT * i / 6);
    }

    private void loadIsFirstLaunch() {
        SharedPreferences mScoreDb = mainMenuActivity.getSharedPreferences(GameData.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
        isFirstLaunch = mScoreDb.getBoolean(GameData.FIRST_LAUNCH, true);
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent event) {
        if (event.isActionDown()) {
            startGameLevelsButton.setPressed(false);
            startGameTimeAttackButton.setPressed(false);
            if (tutorialButton != null) {
                tutorialButton.setPressed(false);
            }
            exitGameButton.setPressed(false);

        } else if (event.isActionMove()) {
            if (!startGameLevelsButton.contains(event.getX(), event.getY())) {
                if (startGameLevelsButton.isPressed()) {
                    startGameLevelsButton.setPressed(false);
                }
            }

            if (!startGameTimeAttackButton.contains(event.getX(), event.getY())) {
                if (startGameTimeAttackButton.isPressed()) {
                    startGameTimeAttackButton.setPressed(false);
                }
            }

            if (tutorialButton != null && !tutorialButton.contains(event.getX(), event.getY())) {
                if (tutorialButton.isPressed()) {
                    tutorialButton.setPressed(false);
                }
            }

            if (!exitGameButton.contains(event.getX(), event.getY())) {
                if (exitGameButton.isPressed()) {
                    exitGameButton.setPressed(false);
                }
            }
        }

        return super.onSceneTouchEvent(event);
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        mainMenuActivity.finish();
        return false;
    }
}
