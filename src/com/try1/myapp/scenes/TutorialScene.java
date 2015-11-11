package com.try1.myapp.scenes;

import android.content.Context;
import android.content.SharedPreferences;
import com.try1.myapp.GameData;
import com.try1.myapp.GameType;
import com.try1.myapp.MainMenuActivity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import java.io.IOException;
import java.io.InputStream;

import static com.try1.myapp.MainMenuActivity.CAMERA_HEIGHT;
import static com.try1.myapp.MainMenuActivity.CAMERA_WIDTH;

/**
 * Created by SeniorJD.
 */
public class TutorialScene extends GameScene {
    private static final int MAX_TIP_COUNT = 5;

    private ITexture arrowTexture;
    private ITextureRegion[] arrowTextureRegions; // up, left, right, bottom
    private Sprite[] arrowSprites;

    private Sprite shownTipSprite;
    private Rectangle shownTipRect;
    private Text shownTipText;

    private int tutorialIndex = 0;
    private boolean isTipShown = false;

    public TutorialScene(MainMenuActivity mainMenuActivity) {
        super(mainMenuActivity);

        gameType = GameType.TIME_ATTACK;
    }

    @Override
    protected void init() {
        loadTexture();
        loadBundle();

        build();
        startGame();
    }

    @Override
    protected void loadTexture() {
        super.loadTexture();

        try {
            arrowTexture = new BitmapTexture(mainMenuActivity.getTextureManager(), new IInputStreamOpener() {
                //@Override
                public InputStream open() throws IOException {
                    return mainMenuActivity.getAssets().open("gfx/arrows.png");
                }
            });

            arrowTexture.load();

            arrowTextureRegions = new ITextureRegion[4];

            arrowTextureRegions[0] = TextureRegionFactory.extractFromTexture(arrowTexture, 0, 0, 256, 256);
            arrowTextureRegions[1] = TextureRegionFactory.extractFromTexture(arrowTexture, 256, 0, 256, 256);
            arrowTextureRegions[2] = TextureRegionFactory.extractFromTexture(arrowTexture, 0, 256, 256, 256);
            arrowTextureRegions[3] = TextureRegionFactory.extractFromTexture(arrowTexture, 256, 256, 256, 256);

            arrowSprites = new Sprite[4];
            for (int i = 0; i < arrowSprites.length; i++) {
                arrowSprites[i] = new Sprite(0, 0, arrowTextureRegions[i], mainMenuActivity.getVertexBufferObjectManager());
            }

        } catch (IOException e) {
            Debug.e(e);
        }
    }

    @Override
    protected void build() {
        super.build();


    }

    @Override
    protected void generateNewTask() {
        super.generateNewTask();

        if (tutorialIndex == 0) {
            showTip();
        }
    }

    private void showTip() {
        switch (tutorialIndex) {
            case 0:
                showTip0();
                break;
            case 1:
                showTip1();
                break;
            case 2:
                showTip2();
                break;
            case 3:
                showTip3();
                break;
            case 4:
                showTip4();
                break;
        }
    }

    private void showTip0() {
        isTipShown = true;
        timer.setAutoReset(false);

        shownTipSprite = arrowSprites[0];
        shownTipSprite.setPosition(GameData.getCameraWidth()/6, taskField.getY() + taskField.getHeight() - shownTipSprite.getHeight() / 3);

        shownTipRect = new Rectangle(
                25,
                shownTipSprite.getY() + shownTipSprite.getHeight() - 50,
                GameData.getCameraWidth() - 50,
                150,
                mainMenuActivity.getVertexBufferObjectManager()
        );
        shownTipRect.setColor(Color.BLACK);
        Rectangle r1 = new Rectangle(5, 5, GameData.getCameraWidth() - 60, 140, mainMenuActivity.getVertexBufferObjectManager());

        String text = "This is the task field";

        shownTipText = new Text(
                10,
                10,
                GameData.getFont100(),
                text,
                text.length(),
                new TextOptions(AutoWrap.WORDS, GameData.getCameraWidth() - 90, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
                mainMenuActivity.getVertexBufferObjectManager()
        );

        r1.attachChild(shownTipText);
        shownTipRect.attachChild(r1);

        attachChild(shownTipRect);
        attachChild(shownTipSprite);
    }

    private void showTip1() {
        isTipShown = true;
        timer.setAutoReset(false);

        shownTipSprite = arrowSprites[1];
        shownTipSprite.setPosition(timerText.getWidth() + 20, 50);

        shownTipRect = new Rectangle(
                shownTipSprite.getX() + shownTipSprite.getWidth() - 10,
                20,
                GameData.getCameraWidth() - (shownTipSprite.getX() + shownTipSprite.getWidth()),
                150,
                mainMenuActivity.getVertexBufferObjectManager()
        );
        shownTipRect.setColor(Color.BLACK);
        Rectangle r1 = new Rectangle(5, 5, shownTipRect.getWidth() - 10, shownTipRect.getHeight() - 10, mainMenuActivity.getVertexBufferObjectManager());

        String text = "Time left";

        shownTipText = new Text(
                10,
                10,
                GameData.getFont100(),
                text,
                text.length(),
                new TextOptions(AutoWrap.WORDS, GameData.getCameraWidth() - (shownTipSprite.getX() + shownTipSprite.getWidth() - 30), HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
                mainMenuActivity.getVertexBufferObjectManager()
        );

        r1.attachChild(shownTipText);
        shownTipRect.attachChild(r1);

        attachChild(shownTipRect);
        attachChild(shownTipSprite);
    }

    private void showTip2() {
        isTipShown = true;
        timer.setAutoReset(false);

        shownTipSprite = arrowSprites[2];
        shownTipSprite.setPosition(scoreText.getX() - shownTipSprite.getWidth(), -50);

        shownTipRect = new Rectangle(
                50,
                20,
                shownTipSprite.getX() - 20,
                150,
                mainMenuActivity.getVertexBufferObjectManager()
        );
        shownTipRect.setColor(Color.BLACK);
        Rectangle r1 = new Rectangle(5, 5, shownTipRect.getWidth() - 10, shownTipRect.getHeight() - 10, mainMenuActivity.getVertexBufferObjectManager());

        String text = "Tasks solved";

        shownTipText = new Text(
                10,
                10,
                GameData.getFont100(),
                text,
                text.length(),
                new TextOptions(AutoWrap.WORDS, shownTipRect.getWidth() - 30, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
                mainMenuActivity.getVertexBufferObjectManager()
        );

        r1.attachChild(shownTipText);
        shownTipRect.attachChild(r1);

        attachChild(shownTipRect);
        attachChild(shownTipSprite);
    }

    private void showTip3() {
        isTipShown = true;
        timer.setAutoReset(false);

        shownTipSprite = arrowSprites[3];
        shownTipSprite.setPosition(deleteButton.getX() - shownTipSprite.getWidth() / 5, deleteButton.getY() - shownTipSprite.getHeight());

        shownTipRect = new Rectangle(
                10,
                50,
                shownTipSprite.getX() + 20,
                300,
                mainMenuActivity.getVertexBufferObjectManager()
        );
        shownTipRect.setColor(Color.BLACK);
        Rectangle r1 = new Rectangle(5, 5, shownTipRect.getWidth() - 10, shownTipRect.getHeight() - 10, mainMenuActivity.getVertexBufferObjectManager());

        String text = "Remove one entered symbol";

        shownTipText = new Text(
                10,
                10,
                GameData.getFont100(),
                text,
                text.length(),
                new TextOptions(AutoWrap.WORDS, shownTipRect.getWidth() - 30, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
                mainMenuActivity.getVertexBufferObjectManager()
        );

        r1.attachChild(shownTipText);
        shownTipRect.attachChild(r1);

        attachChild(shownTipRect);
        attachChild(shownTipSprite);
    }

    private void showTip4() {
        isTipShown = true;
        timer.setAutoReset(false);

        shownTipSprite = arrowSprites[3];
        shownTipSprite.setPosition((GameData.getCameraWidth() - shownTipSprite.getWidth()) / 2, buttons[0].getY() - shownTipSprite.getHeight());

        shownTipRect = new Rectangle(
                20,
                shownTipSprite.getY() - 250,
                GameData.getCameraWidth() - 40,
                300,
                mainMenuActivity.getVertexBufferObjectManager()
        );
        shownTipRect.setColor(Color.BLACK);
        Rectangle r1 = new Rectangle(5, 5, shownTipRect.getWidth() - 10, shownTipRect.getHeight() - 10, mainMenuActivity.getVertexBufferObjectManager());

        String text = "Enter symbols by keyboard";

        shownTipText = new Text(
                10,
                10,
                GameData.getFont100(),
                text,
                text.length(),
                new TextOptions(AutoWrap.WORDS, shownTipRect.getWidth() - 30, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
                mainMenuActivity.getVertexBufferObjectManager()
        );

        r1.attachChild(shownTipText);
        shownTipRect.attachChild(r1);

        attachChild(shownTipRect);
        attachChild(shownTipSprite);
    }

    @Override
    protected void setText(Text text, Rectangle rectangle, String s, float px, float py) {
        int length = GameData.getTextLength(GameData.getFont100(), s);

        float x = CAMERA_WIDTH/2 - length/2 - s.length();
        float y = CAMERA_HEIGHT/12 - GameData.getFont100().getLineHeight()/2;

        text.setPosition(x, y);
        text.setText(s);

        rectangle.setPosition(px, py);
        rectangle.setSize(CAMERA_WIDTH / 2, CAMERA_HEIGHT / 6);
    }

    @Override
    protected void levelDone() {
        if (scoreValue > 0) {
            SharedPreferences mScoreDb = mainMenuActivity.getSharedPreferences(GameData.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mScoreDb.edit();

            editor.putBoolean(GameData.FIRST_LAUNCH, false);

            editor.apply();
        }

        super.levelDone();
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent event) {
        if (!isTipShown) {
            return super.onSceneTouchEvent(event);
        }

        if (event.getAction() != TouchEvent.ACTION_UP) {
            return false;
        }

        tutorialIndex++;
        detachChild(shownTipRect);
        detachChild(shownTipSprite);

        if (tutorialIndex >= MAX_TIP_COUNT) {
            isTipShown = false;
            timer = null;
            ignoreKeyboard = true;
            pressToStartR.setVisible(true);
            pressToStartText.setVisible(true);
            return false;
        }

        showTip();
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        if (!isTipShown) {
            return super.onKeyDown(keyCode);
        }

        GameData.clearExtra();
        mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));

        return false;
    }
}
