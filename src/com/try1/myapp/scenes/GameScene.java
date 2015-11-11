package com.try1.myapp.scenes;

import android.content.Context;
import android.os.Vibrator;
import android.view.KeyEvent;
import com.try1.myapp.GameData;
import com.try1.myapp.GameType;
import com.try1.myapp.KeyboardAction;
import com.try1.myapp.LevelGenerator;
import com.try1.myapp.MainMenuActivity;
import com.try1.myapp.component.GameButton;
import com.try1.myapp.component.TextButton;
import com.try1.myapp.component.TextButtonActionListener;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.ButtonSprite;
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
import java.util.HashSet;
import java.util.Set;

import static com.try1.myapp.MainMenuActivity.CAMERA_HEIGHT;
import static com.try1.myapp.MainMenuActivity.CAMERA_WIDTH;

/**
 * Created by SeniorJD.
 */
public class GameScene extends Scene implements IScene {
    private static final Color GREEN = new Color(0.77f, 0.99f, 0.77f);
    private static final Color YELLOW = new Color(0.99f, 0.99f, 0.76f);
    private static final Color RED = new Color(0.99f, 0.77f, 0.77f);

    private static final String COMMA = ",";
    private static final String MINUS = "-";

    private static final int MAX_ENTER = 10;

    protected MainMenuActivity mainMenuActivity;

    private int scoreTimerY = 50;
    private int taskY = 220;
    private int enterY = 360;

    protected TextButton[] buttons = new TextButton[12];

    protected Text taskField;

    private Rectangle enterRect;
    private Text enterField;

    private ITexture deleteButtonTexture;
    private ITextureRegion[] deleteButtonTextureRegions = new ITextureRegion[2];
    protected ButtonSprite deleteButton;

    protected TimerHandler timer;
    private int timerValue;
    protected Text timerText;

    protected int scoreValue;
    protected Text scoreText;

    protected GameType gameType;
    private LevelType levelType;
    private int levelIndex = -1;

    protected Rectangle pressToStartR;
    protected Text pressToStartText;

    private Rectangle pauseR;
    private Text pauseText;

    private Rectangle pauseResumeR;
    private Text pauseResumeText;

    private Rectangle pauseRestartR;
    private Text pauseRestartText;

    private Rectangle pauseMenuR;
    private Text pauseMenuText;

    private LevelGenerator levelGenerator;

    protected boolean ignoreKeyboard = false;

    private TimerHandler generatorTimer;
    private TimerHandler wrongEnteredDataTimer;

    private String enterValue;
    private String expectedResult;

    private Set<String> tasksSet = new HashSet<>();
    private Set<String> resultsSet = new HashSet<>();

    public GameScene(MainMenuActivity mainMenuActivity) {
        this.mainMenuActivity = mainMenuActivity;

        init();
    }

    protected void init() {
        loadTexture();
        loadBundle();

        build();
        startGame();
    }

    protected void build() {
        // task field

        Rectangle r = createTaskFieldBackground();
        attachChild(r);

        taskField = createTaskField();
        attachChild(taskField);

        // enter field
        enterRect = createEnterFieldBackground();
        attachChild(enterRect);

        enterField = createEnterField();
        attachChild(enterField);

        deleteButton = new ButtonSprite(CAMERA_WIDTH - 140, enterY, deleteButtonTextureRegions[0], deleteButtonTextureRegions[1], null);
        deleteButton.setSize(140, 140);
        attachChild(deleteButton);
        registerTouchArea(deleteButton);

        deleteButton.setOnClickListener(new ButtonSprite.OnClickListener() {
            @Override
            public void onClick(ButtonSprite button, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (button != deleteButton) {
                    return;
                }

                String text = enterField.getText().toString();

                if (text.length() == 0) {
                    return;
                }

                text = text.substring(0, text.length() - 1);

                setEnterValue(text);


            }
        });

        // �������� ��������� ����� ������
        float color = 0.9412f;
        setBackground(new Background(color, color, color));
        // ������� ������ � ������ ��� � �����

        pressToStartR = new Rectangle(0, enterY + 140, CAMERA_WIDTH, CAMERA_HEIGHT - 240 * 4 - enterY - 140, mainMenuActivity.getVertexBufferObjectManager());
        pressToStartR.setColor(new Color(0.9f, 0.9f, 0.9f));
        pressToStartText = new Text(
                CAMERA_WIDTH/2 - GameData.getTextLength(GameData.getFont100(),
                "Press here to start")/2,
                enterY + 160 + pressToStartR.getHeight()/2 - GameData.getFont100().getLineHeight(),
                GameData.getFont100(),
                "Press here to start",
                mainMenuActivity.getVertexBufferObjectManager()
        );
        pressToStartText.setAutoWrap(AutoWrap.NONE);
        pressToStartText.setSize(CAMERA_WIDTH, CAMERA_HEIGHT - 240 * 4 - enterY - 180);
        attachChild(pressToStartR);
        attachChild(pressToStartText);

        float px = 0;
        float py = CAMERA_HEIGHT - 240 * 4;

        // button 1
        buttons[0] = createButton(1, px, py);

        // button 2
        px += 360;
        buttons[1] = createButton(2, px, py);

        // button 3
        px += 360;
        buttons[2] = createButton(3, px, py);

        // button 4
        px = 0;
        py += 240;
        buttons[3] = createButton(4, px, py);

        // button 5
        px += 360;
        buttons[4] = createButton(5, px, py);

        // button 6
        px += 360;
        buttons[5] = createButton(6, px, py);

        // button 7
        px = 0;
        py += 240;
        buttons[6] = createButton(7, px, py);

        // button 8
        px += 360;
        buttons[7] = createButton(8, px, py);

        // button 9
        px += 360;
        buttons[8] = createButton(9, px, py);

        // button +-
        px = 0;
        py += 240;
        buttons[9] = createButton(11, px, py);

        // button 0
        px += 360;
        buttons[10] = createButton(0, px, py);

        // button comma
        px += 360;
        buttons[11] = createButton(10, px, py);

        for (int i = 0; i < buttons.length; i++) {
            attachChild(buttons[i]);
            registerTouchArea(buttons[i]);
        }

        // timer
        timerText = new Text(50, scoreTimerY, GameData.getFont100(), "", 3, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(timerText);

        // score
        scoreText = new Text(CAMERA_WIDTH - 50, scoreTimerY, GameData.getFont100(), "0", 5, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(scoreText);

        createPauseMenu();
    }

    protected void startGame() {
        if (gameType.equals(GameType.LEVELS)) {
            setTimerValue(0);
            setScoreValue(10);
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            setTimerValue(60);
            setScoreValue(0);
        } else {
            throw new IllegalStateException("gameType: " + gameType);
        }

        ignoreKeyboard = true;
    }

    private void setTimerValue(int value) {
        timerValue = value;
        timerText.setText(String.valueOf(value));
    }

    private void setScoreValue(int value) {
        scoreValue = value;

        String newValue = String.valueOf(value);

        int length = 0;
        for (int i = 0; i < newValue.length(); i++) {
            char c = newValue.charAt(i);

            length += GameData.getFont100().getLetter(c).mWidth;
        }

        scoreText.setText(newValue);
        scoreText.setSize(length, 120);
        scoreText.setPosition(CAMERA_WIDTH - length - 100, scoreTimerY);
    }

    private void startTimer() {
        timer = new TimerHandler(1f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                if (!timer.isAutoReset()) {
                    return;
                }

                if (gameType.equals(GameType.LEVELS)) {
                    setTimerValue(timerValue + 1);
                } else if (gameType.equals(GameType.TIME_ATTACK)) {
                    if (timerValue <= 0) {
                        levelDone();
                        return;
                    }

                    setTimerValue(timerValue - 1);
                } else {
                    throw new IllegalStateException("gameType: " + gameType);
                }

            }
        });
        ignoreKeyboard = false;

        pressToStartR.setVisible(false);
        pressToStartText.setVisible(false);
        generateNewTask();

        mainMenuActivity.getEngine().registerUpdateHandler(timer);
    }

    protected void levelDone() {
        if (gameType.equals(GameType.LEVELS)) {
            timer.setAutoReset(false);

            int optimalTime = 30 + (levelIndex + 1);
            if (timerValue <= optimalTime) {
                // TODO
            }

            GameData.setScore(levelType, levelIndex, timerValue);

            GameData.clearExtra();
            GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
            GameData.putExtra(GameData.LEVEL_TYPE_STRING, levelType == null ? "" : levelType.toString());
            GameData.putExtra(GameData.LEVEL_NUMBER_STRING, String.valueOf(levelIndex));

            mainMenuActivity.setNewScene(new ScoreScene(mainMenuActivity));
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            timer.setAutoReset(false);
            GameData.setScore(scoreValue);

            GameData.clearExtra();
            GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());

            mainMenuActivity.setNewScene(new ScoreScene(mainMenuActivity));
        } else {
            throw new IllegalStateException("gameType: " + gameType);
        }
    }

    private void createPauseMenu() {
        pauseR = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT, mainMenuActivity.getVertexBufferObjectManager());
        pauseR.setColor(GameData.getColor());
        String pauseString = "Pause";
        pauseText = new Text(CAMERA_WIDTH/2 - GameData.getTextLength(GameData.getFont100(), pauseString)/2, CAMERA_HEIGHT * 1 / 6 - GameData.getFont100().getLineHeight()/2, GameData.getFont100(), pauseString, mainMenuActivity.getVertexBufferObjectManager());

        pauseR.attachChild(pauseText);
        attachChild(pauseR);

        pauseResumeR = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT / 6, mainMenuActivity.getVertexBufferObjectManager());
        pauseResumeR.setColor(GameData.getColor());
        pauseResumeText = new Text(0, 0, GameData.getFont100(), "Resume", mainMenuActivity.getVertexBufferObjectManager());
        pauseResumeR.attachChild(pauseResumeText);

        pauseRestartR = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT / 6, mainMenuActivity.getVertexBufferObjectManager());
        pauseRestartR.setColor(GameData.getColor());
        pauseRestartText = new Text(0, 0, GameData.getFont100(), "Restart", mainMenuActivity.getVertexBufferObjectManager());
        pauseRestartR.attachChild(pauseRestartText);

        pauseMenuR = new Rectangle(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT / 6, mainMenuActivity.getVertexBufferObjectManager());
        pauseMenuR.setColor(GameData.getColor());
        pauseMenuText = new Text(0, 0, GameData.getFont100(), "Menu", mainMenuActivity.getVertexBufferObjectManager());
        pauseMenuR.attachChild(pauseMenuText);

        int i = 2;
        setText(pauseResumeText, pauseResumeR, pauseResumeText.getText().toString(), 0, CAMERA_HEIGHT * i / 6);
        i++;
        setText(pauseRestartText, pauseRestartR, pauseRestartText.getText().toString(), 0, CAMERA_HEIGHT * i / 6);
        i++;
        setText(pauseMenuText, pauseMenuR, pauseMenuText.getText().toString(), 0, CAMERA_HEIGHT * i / 6);

        pauseR.attachChild(pauseResumeR);
        pauseR.attachChild(pauseRestartR);
        pauseR.attachChild(pauseMenuR);
        pauseR.setVisible(false);
    }

    private void setText(String text) {
        int length = 40;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            length += GameData.getFont100().getLetter(c).mWidth;

            if (c == ' ') {
                length += 40;
            }
        }

        taskField.setText(text);
        taskField.setSize(length, 120);
        float x = (CAMERA_WIDTH - length) / 2 - text.length();
        taskField.setPosition(x, taskY + 20);

        enterRect.setColor(YELLOW);
        setEnterValue("");
    }

    protected void setText(Text text, Rectangle rectangle, String s, float px, float py) {
        int length = GameData.getTextLength(GameData.getFont100(), s);

        float x = CAMERA_WIDTH/2 - length/2 - s.length();
        float y = CAMERA_HEIGHT/12 - GameData.getFont100().getLineHeight()/2;

        text.setPosition(x, y);
        text.setText(s);

        rectangle.setPosition(px, py);
        rectangle.setSize(CAMERA_WIDTH, CAMERA_HEIGHT / 6);
    }

    private KeyboardAction getAction(int index) {
        KeyboardAction action;

        switch (index) {
            case 1:
                action = KeyboardAction.KEY_1;
                break;
            case 2:
                action = KeyboardAction.KEY_2;
                break;
            case 3:
                action = KeyboardAction.KEY_3;
                break;
            case 4:
                action = KeyboardAction.KEY_4;
                break;
            case 5:
                action = KeyboardAction.KEY_5;
                break;
            case 6:
                action = KeyboardAction.KEY_6;
                break;
            case 7:
                action = KeyboardAction.KEY_7;
                break;
            case 8:
                action = KeyboardAction.KEY_8;
                break;
            case 9:
                action = KeyboardAction.KEY_9;
                break;
            case 0:
                action = KeyboardAction.KEY_0;
                break;
            case 11:
                action = KeyboardAction.KEY_PM;
                break;
            case 10:
                action = KeyboardAction.KEY_COMMA;
                break;

            default:
                throw new IllegalArgumentException("Unknown index: " + index);
        }

        return action;
    }

    private TextButton createButton(final int index, float px, float py) {
        KeyboardAction action = getAction(index);

        final GameButton button = new GameButton(action, GameData.getFont150(), mainMenuActivity.getVertexBufferObjectManager());
        button.setTextButtonActionListener(new TextButtonActionListener() {
            @Override
            public void onClick() {
                if (ignoreKeyboard) {
                    return;
                }

                doAction(button.getKeyboardAction());
            }
        });

        button.adjustPosition(px, py);

        return button;
    }

    private void doAction(KeyboardAction action) {
        int plus = 0;
        switch (action) {
            case KEY_0:
                plus = 0;
                break;
            case KEY_1:
                plus = 1;
                break;
            case KEY_2:
                plus = 2;
                break;
            case KEY_3:
                plus = 3;
                break;
            case KEY_4:
                plus = 4;
                break;
            case KEY_5:
                plus = 5;
                break;
            case KEY_6:
                plus = 6;
                break;
            case KEY_7:
                plus = 7;
                break;
            case KEY_8:
                plus = 8;
                break;
            case KEY_9:
                plus = 9;
                break;
            case KEY_PM:
                String text = getEnterValue();

                if (text.startsWith(MINUS)) {
                    text = text.substring(1);
                } else if (text.length() < 10) {
                    text = MINUS + text;
                }

                setEnterValue(text);
                return;
            case KEY_COMMA:
                text = getEnterValue();


                if (!text.contains(COMMA)) {
                    text = text + COMMA;
                }

                setEnterValue(text);

                return;
        }

        String text = getEnterValue();

        text = text + String.valueOf(plus);

        setEnterValue(text);
    }

    private String getEnterValue() {
        return enterValue;
    }

    private Rectangle createTaskFieldBackground() {
        Rectangle r = new Rectangle(0, taskY, CAMERA_WIDTH, 140, mainMenuActivity.getVertexBufferObjectManager());

        r.setColor(new Color(0.75f, 0.75f, 0.75f));

        return r;
    }

    private Text createTaskField() {
        Text text = new Text(0, taskY + 20, GameData.getFont100(), "", CAMERA_WIDTH/50, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());

        return text;
    }

    private Text createEnterField() {
        Text text = new Text(0, enterY + 20, GameData.getFont100(), "", MAX_ENTER, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());

        return text;
    }

    private Rectangle createEnterFieldBackground() {
        Rectangle r = new Rectangle(0, enterY, CAMERA_WIDTH, 140, mainMenuActivity.getVertexBufferObjectManager());

        r.setColor(YELLOW);

        return r;
    }

    protected void loadTexture() {
        try {

            deleteButtonTexture = new BitmapTexture(mainMenuActivity.getTextureManager(), new IInputStreamOpener() {
                //@Override
                public InputStream open() throws IOException {
                    return mainMenuActivity.getAssets().open("gfx/delete.png");
                }
            });

            deleteButtonTexture.load();
            deleteButtonTextureRegions[0] = TextureRegionFactory.extractFromTexture(deleteButtonTexture, 0, 0, 140, 140);
            deleteButtonTextureRegions[1] = TextureRegionFactory.extractFromTexture(deleteButtonTexture, 140, 0, 140, 140);
        }
        catch (IOException e) {
            Debug.e(e);
        }
    }

    protected void loadBundle() {

        gameType = GameType.valueOf(GameData.getExtra(GameData.GAME_TYPE_STRING));

        if (gameType.equals(GameType.LEVELS)) {
            levelType = LevelType.valueOf(GameData.getExtra(GameData.LEVEL_TYPE_STRING));
            levelIndex = Integer.valueOf(GameData.getExtra(GameData.LEVEL_NUMBER_STRING));
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            // ok
        } else {
            throw new IllegalArgumentException("Illegal GameType: " + gameType);
        }
    }

    @Override
    public boolean onSceneTouchEvent(TouchEvent event) {
        if (pauseR.isVisible()) {
            if (event.getAction() == TouchEvent.ACTION_UP) {
                if (pauseResumeR.contains(event.getX(), event.getY())) {
                    pauseR.setVisible(false);
                    timer.setAutoReset(true);
                    generateNewTask();
                    timer.reset();

                    pauseResumeR.setColor(GameData.getColor());
                    return false;
                } else if (pauseRestartR.contains(event.getX(), event.getY())) {
                    pauseRestartR.setColor(GameData.getColor());
                    // restart
                    GameData.clearExtra();

                    if (GameType.LEVELS.equals(gameType)) {
                        GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
                        GameData.putExtra(GameData.LEVEL_TYPE_STRING, levelType.toString());
                        GameData.putExtra(GameData.LEVEL_NUMBER_STRING, String.valueOf(levelIndex));

                        mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
                    } else {
                        GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
                        mainMenuActivity.setNewScene(new GameScene(mainMenuActivity));
                    }
                } else if (pauseMenuR.contains(event.getX(), event.getY())) {
                    pauseMenuR.setColor(GameData.getColor());

                    GameData.clearExtra();
                    mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));
                } else {
                    pauseR.setVisible(false);
                    timer.setAutoReset(true);
                    generateNewTask();
                    timer.reset();

                }

            } else if (event.getAction() == TouchEvent.ACTION_DOWN || event.getAction() == TouchEvent.ACTION_MOVE) {
                if (pauseResumeR.contains(event.getX(), event.getY())) {
                    pauseResumeR.setColor(GameData.getSelectionColor());
                    pauseRestartR.setColor(GameData.getColor());
                    pauseMenuR.setColor(GameData.getColor());
                } else if (pauseRestartR.contains(event.getX(), event.getY())) {
                    pauseResumeR.setColor(GameData.getColor());
                    pauseRestartR.setColor(GameData.getSelectionColor());
                    pauseMenuR.setColor(GameData.getColor());
                } else if (pauseMenuR.contains(event.getX(), event.getY())) {
                    pauseResumeR.setColor(GameData.getColor());
                    pauseRestartR.setColor(GameData.getColor());
                    pauseMenuR.setColor(GameData.getSelectionColor());
                } else {
                    pauseResumeR.setColor(GameData.getColor());
                    pauseRestartR.setColor(GameData.getColor());
                    pauseMenuR.setColor(GameData.getColor());
                }
            }

            return false;
        }

        if (event.isActionDown()) {
            if (ignoreKeyboard) {
                return false;
            }
            for (TextButton button : buttons) {
                button.setPressed(false);
            }
        } else if (event.isActionMove()) {
            if (ignoreKeyboard) {
                return false;
            }
            for (TextButton button : buttons) {
                if (!button.contains(event.getX(), event.getY()) && button.isPressed()) {
                    button.setPressed(false);
                }
            }
        }
        if (event.getAction() == TouchEvent.ACTION_MOVE) {
            if (ignoreKeyboard) {
                return false;
            }

            float x = event.getX();
            float y = event.getY();

            boolean contains = deleteButton.contains(x, y);
            TouchEvent newEvent = TouchEvent.obtain(x, y, contains ? TouchEvent.ACTION_DOWN : TouchEvent.ACTION_UP, -1, null);
            deleteButton.onAreaTouched(newEvent, x, y);

        } else if (event.getAction() == TouchEvent.ACTION_UP) {
            if (timer == null && pressToStartR.contains(event.getX(), event.getY())) {
                startTimer();
            }
        } else if (event.getAction() == TouchEvent.ACTION_DOWN) {
        }

        if (ignoreKeyboard) {
            return false;
        }

        return super.onSceneTouchEvent(event);
    }

    protected void generateNewTask() {
        if (levelGenerator == null) {
            levelGenerator = LevelGenerator.getGenerator(gameType, levelType, levelIndex);
        }

        LevelGenerator.Task task = generateNewTaskImpl();

        tasksSet.add(task.getTask());
        resultsSet.add(task.getExpectedResult());

        expectedResult = task.getExpectedResult();
        setText(task.getTask());
    }

    private LevelGenerator.Task generateNewTaskImpl() {
        LevelGenerator.Task task;
        if (gameType.equals(GameType.TIME_ATTACK)) {
            task = levelGenerator.generateTask(scoreValue);
        } else {
            task = levelGenerator.generateTask();
        }

        while (tasksSet.contains(task.getTask()) || resultsSet.contains(task.getExpectedResult())) {
            if (gameType.equals(GameType.TIME_ATTACK)) {
                task = levelGenerator.generateTask(scoreValue);
            } else {
                task = levelGenerator.generateTask();
            }
        }

        return task;
    }

    private void setEnterValue(String enterValue) {
        this.enterValue = enterValue;
        valueChanged();
    }

    private void valueChanged() {
        setEnterFieldText(getEnterValue());

        checkEntered();
    }

    private void checkEntered() {
        String text = getEnterValue();

        if (text.equals(expectedResult)) {
            enterRect.setColor(GREEN);

            if (gameType.equals(GameType.LEVELS)) {
                setScoreValue(scoreValue - 1);

                if (scoreValue == 0) {
                    levelDone();
                    return;
                }
            } else if (gameType.equals(GameType.TIME_ATTACK)) {
                setScoreValue(scoreValue + 1);
                setTimerValue(timerValue + 2); // TODO show green
            } else {
                throw new IllegalStateException("gameType: " + gameType);
            }

            waitAndGenerate();
        } else {
            if (expectedResult.contains(MINUS)) {
                if (text.contains(MINUS)) {
                    if (expectedResult.length() <= text.length()) {
                        enterRect.setColor(RED);

                        Vibrator v = (Vibrator) mainMenuActivity.getSystemService(Context.VIBRATOR_SERVICE);
                        v.vibrate(100);
                        if (gameType.equals(GameType.LEVELS)) {
                            setTimerValue(timerValue + 1);
                        } else if (gameType.equals(GameType.TIME_ATTACK)) {
                            setTimerValue(timerValue - 1);
                        } else {
                            throw new IllegalStateException("gameType: " + gameType);
                        }

                        waitAndSetGreen();
                    }
                }
            } else if (!text.contains(MINUS)) {
                if (expectedResult.length() <= text.length()) {
                    enterRect.setColor(RED);

                    Vibrator v = (Vibrator) mainMenuActivity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    if (gameType.equals(GameType.LEVELS)) {
                        setTimerValue(timerValue + 1);
                    } else if (gameType.equals(GameType.TIME_ATTACK)) {
                        setTimerValue(timerValue - 1);
                    } else {
                        throw new IllegalStateException("gameType: " + gameType);
                    }

                    waitAndSetGreen();
                }
            } else {
                if (expectedResult.length() + 1 <= text.length()) {
                    enterRect.setColor(RED);

                    Vibrator v = (Vibrator) mainMenuActivity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    if (gameType.equals(GameType.LEVELS)) {
                        setTimerValue(timerValue + 1);
                    } else if (gameType.equals(GameType.TIME_ATTACK)) {
                        setTimerValue(timerValue - 1);
                    } else {
                        throw new IllegalStateException("gameType: " + gameType);
                    }

                    waitAndSetGreen();
                }
            }
        }
    }

    private void setEnterFieldText(String text) {
        int length = 40;

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);

            length += GameData.getFont100().getLetter(c).mWidth;

            if (c == ' ') {
                length += 40;
            }
        }

        enterField.setText(text);
        enterField.setSize(length, 120);
        float x = (CAMERA_WIDTH - length) / 2 - text.length();
        enterField.setPosition(x, enterY + 20);
    }

    private void waitAndGenerate() {
        if (generatorTimer == null) {
            generatorTimer = new TimerHandler(0.3f, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    generateNewTask();
                    ignoreKeyboard = false;
                }
            });

            mainMenuActivity.getEngine().registerUpdateHandler(generatorTimer);
        } else {
            ignoreKeyboard = true;
            generatorTimer.reset();
        }
    }

    private void waitAndSetGreen() {
        if (wrongEnteredDataTimer == null) {
            wrongEnteredDataTimer = new TimerHandler(0.2f, new ITimerCallback() {
                @Override
                public void onTimePassed(TimerHandler pTimerHandler) {
                    Vibrator v = (Vibrator) mainMenuActivity.getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    setEnterValue("");
                    enterRect.setColor(YELLOW);
                    ignoreKeyboard = false;
                }
            });

            mainMenuActivity.getEngine().registerUpdateHandler(wrongEnteredDataTimer);
        } else {
            ignoreKeyboard = true;
            wrongEnteredDataTimer.reset();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (timer == null) {
                GameData.clearExtra();
                mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));
                return false;
            }

            if (pauseR.isVisible()) {
                pauseR.setVisible(false);
                timer.setAutoReset(true);
                generateNewTask();
                timer.reset();
            } else {
                pauseR.setVisible(true);
                timer.setAutoReset(false);
            }

        }
        return false;
    }
}
