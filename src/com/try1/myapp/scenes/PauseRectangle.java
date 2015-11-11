package com.try1.myapp.scenes;

import com.try1.myapp.GameData;
import com.try1.myapp.component.TextButton;
import com.try1.myapp.component.TextButtonActionListener;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;

import static com.try1.myapp.MainMenuActivity.CAMERA_HEIGHT;
import static com.try1.myapp.MainMenuActivity.CAMERA_WIDTH;

/**
 * Created by SeniorJD
 */
public class PauseRectangle extends Rectangle {
    static final int RESUME = 0;
    static final int RESTART = 1;
    static final int MENU = 2;

    protected GameScene gameScene;


    private TextButton pauseResumeButton;
    private TextButton pauseRestartButton;
    private TextButton pauseMenuButton;

    public PauseRectangle(GameScene gameScene) {
        super(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, gameScene.mainMenuActivity.getVertexBufferObjectManager());
        this.gameScene = gameScene;

        init();
    }

    private void init() {
        setColor(GameData.getColor());

        int i = 1;

        String pauseString = "Pause";
        Text pauseText = new Text(
                CAMERA_WIDTH/2 - GameData.getTextLength(GameData.getFont100(), pauseString)/2,
                CAMERA_HEIGHT * i / 6 - GameData.getFont100().getLineHeight()/2,
                GameData.getFont100(),
                pauseString,
                gameScene.mainMenuActivity.getVertexBufferObjectManager()
        );
        attachChild(pauseText);

        i++;
        pauseResumeButton = new TextButton("Resume", GameData.getFont100(), gameScene.mainMenuActivity.getVertexBufferObjectManager());
        pauseResumeButton.adjustPosition(0, CAMERA_HEIGHT * i / 6);
        gameScene.registerTouchArea(pauseResumeButton);
        attachChild(pauseResumeButton);
        pauseResumeButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                gameScene.resume(RESUME);
            }
        });

        i++;
        pauseRestartButton = new TextButton("Restart", GameData.getFont100(), gameScene.mainMenuActivity.getVertexBufferObjectManager());
        pauseRestartButton.adjustPosition(0, CAMERA_HEIGHT * i / 6);
        gameScene.registerTouchArea(pauseRestartButton);
        attachChild(pauseRestartButton);
        pauseRestartButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                gameScene.resume(RESTART);
            }
        });

        i++;
        pauseMenuButton = new TextButton("Menu", GameData.getFont100(), gameScene.mainMenuActivity.getVertexBufferObjectManager());
        pauseMenuButton.adjustPosition(0, CAMERA_HEIGHT * i / 6);
        gameScene.registerTouchArea(pauseMenuButton);
        attachChild(pauseMenuButton);
        pauseMenuButton.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                gameScene.resume(MENU);
            }
        });
    }

    @Override
    public boolean onAreaTouched(TouchEvent event, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (event.isActionDown()) {
            pauseResumeButton.setPressed(false);
            pauseRestartButton.setPressed(false);
            pauseMenuButton.setPressed(false);
        } else if (event.isActionMove()) {
            if (!pauseResumeButton.contains(event.getX(), event.getY())) {
                if (pauseResumeButton.isPressed()) {
                    pauseResumeButton.setPressed(false);
                }
            }

            if (!pauseRestartButton.contains(event.getX(), event.getY())) {
                if (pauseRestartButton.isPressed()) {
                    pauseRestartButton.setPressed(false);
                }
            }

            if (!pauseMenuButton.contains(event.getX(), event.getY())) {
                if (pauseMenuButton.isPressed()) {
                    pauseMenuButton.setPressed(false);
                }
            }
        }

        return super.onAreaTouched(event, pTouchAreaLocalX, pTouchAreaLocalY);
    }

    @Override
    public void setVisible(boolean pVisible) {
        boolean wasVisible = isVisible();

        super.setVisible(pVisible);

        if (!wasVisible && pVisible) {
            registerTouchArea();
        } else if (wasVisible && !pVisible) {
            unregisterTouchArea();
        }
    }

    private void registerTouchArea() {
        gameScene.registerTouchArea(pauseResumeButton);
        gameScene.registerTouchArea(pauseRestartButton);
        gameScene.registerTouchArea(pauseMenuButton);
    }

    private void unregisterTouchArea() {
        gameScene.unregisterTouchArea(pauseResumeButton);
        gameScene.unregisterTouchArea(pauseRestartButton);
        gameScene.unregisterTouchArea(pauseMenuButton);
    }
}
