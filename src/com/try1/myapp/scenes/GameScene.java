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
import com.try1.myapp.component.ShowBonusTimePanel;
import com.try1.myapp.component.TextButton;
import com.try1.myapp.component.TextButtonActionListener;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

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

    protected IUpdateHandler timer;
    private double timerValueD;
    protected Text timerText;

    protected int scoreValue;
    protected Text scoreText;

    protected GameType gameType;
    private LevelType levelType;
    private int levelIndex = -1;

    protected Rectangle pressToStartR;
    protected Text pressToStartText;

    private Rectangle pauseR;

    private ShowBonusTimePanel bonusTimePanel;

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

        float color = 0.9412f;
        setBackground(new Background(color, color, color));

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

        for (TextButton button : buttons) {
            attachChild(button);
            registerTouchArea(button);
        }

        // timer
        timerText = new Text(50, scoreTimerY, GameData.getFont100(), "", 3, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(timerText);

        // score
        scoreText = new Text(CAMERA_WIDTH - 50, scoreTimerY, GameData.getFont100(), "0", 5, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());
        attachChild(scoreText);

        createPauseMenu();
        createBonusTimer();
    }

    protected void startGame() {
        if (gameType.equals(GameType.LEVELS)) {
            setTimerValue(0d);
            setScoreValue(10);
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            setTimerValue(60d);
            setScoreValue(0);
        } else {
            throw new IllegalStateException("gameType: " + gameType);
        }

        ignoreKeyboard = true;
    }

    public void setTimerValue(double value) {
        timerValueD = value;

        timerText.setText(String.valueOf((int)value));
    }

    public GameType getGameType() {
        return gameType;
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

    private void showBonus(int difference) {
        if (gameType == GameType.LEVELS) {
            if (difference > 0) {
                bonusTimePanel.showBonus("+" + difference, ShowBonusTimePanel.RED, timerText.getWidth() + timerText.getX() + 20, timerText.getY());
            } else if (difference < 0) {
                bonusTimePanel.showBonus(String.valueOf(difference), ShowBonusTimePanel.GREEN, timerText.getWidth() + timerText.getX() + 20, timerText.getY());
            }
        } else if (gameType == GameType.TIME_ATTACK) {
            if (difference > 0) {
                bonusTimePanel.showBonus("+" + difference, ShowBonusTimePanel.GREEN, timerText.getWidth() + timerText.getX() + 20, timerText.getY());
            } else if (difference < 0) {
                bonusTimePanel.showBonus(String.valueOf(difference), ShowBonusTimePanel.RED, timerText.getWidth() + timerText.getX() + 20, timerText.getY());
            }
        }
    }

    private void startTimer() {
        timer = new IUpdateHandler() {
            @Override
            public void onUpdate(float pSecondsElapsed) {
                if (gameType == GameType.LEVELS) {
                    timerValueD += pSecondsElapsed;
                    setTimerValue(timerValueD);
                } else if (gameType == GameType.TIME_ATTACK) {
                    timerValueD -= pSecondsElapsed;
                    if (timerValueD <= 0) {
                        levelDone();

                    } else {
                        setTimerValue(timerValueD);
                    }
                } else {
                    throw new IllegalStateException("Illegal game type: " + getGameType());
                }
            }

            @Override
            public void reset() {}
        };
        registerUpdateHandler(timer);

        ignoreKeyboard = false;

        pressToStartR.setVisible(false);
        pressToStartText.setVisible(false);
        generateNewTask();
    }

    public void levelDone() {
        if (gameType.equals(GameType.LEVELS)) {
            unregisterUpdateHandler(timer);

            GameData.setScore(levelType, levelIndex, (int)timerValueD);

            GameData.clearExtra();
            GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());
            GameData.putExtra(GameData.LEVEL_TYPE_STRING, levelType == null ? "" : levelType.toString());
            GameData.putExtra(GameData.LEVEL_NUMBER_STRING, String.valueOf(levelIndex));

            mainMenuActivity.setNewScene(new ScoreScene(mainMenuActivity));
        } else if (gameType.equals(GameType.TIME_ATTACK)) {
            unregisterUpdateHandler(timer);
            GameData.setScore(scoreValue);

            GameData.clearExtra();
            GameData.putExtra(GameData.GAME_TYPE_STRING, gameType.toString());

            mainMenuActivity.setNewScene(new ScoreScene(mainMenuActivity));
        } else {
            throw new IllegalStateException("gameType: " + gameType);
        }
    }

    private void createPauseMenu() {
        pauseR = new PauseRectangle(this);
        attachChild(pauseR);

        pauseR.setVisible(false);
    }

    private void createBonusTimer() {
        bonusTimePanel = new ShowBonusTimePanel(200, scoreTimerY, timerText.getWidth(), timerText.getHeight(), mainMenuActivity.getVertexBufferObjectManager());
        bonusTimePanel.setVisible(false);
        attachChild(bonusTimePanel);

        mainMenuActivity.getEngine().registerUpdateHandler(bonusTimePanel.getTimer());
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
                action = KeyboardAction.KEY_C;
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
            case KEY_C:
                text = getEnterValue();

                if (text.length() != 0) {
                    text = text.substring(0, text.length() - 1);
                    setEnterValue(text);
                }

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
        return new Text(0, taskY + 20, GameData.getFont100(), "", CAMERA_WIDTH/50, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());
    }

    private Text createEnterField() {
        return new Text(0, enterY + 20, GameData.getFont100(), "", MAX_ENTER, new TextOptions(HorizontalAlign.LEFT), mainMenuActivity.getVertexBufferObjectManager());
    }

    private Rectangle createEnterFieldBackground() {
        Rectangle r = new Rectangle(0, enterY, CAMERA_WIDTH, 140, mainMenuActivity.getVertexBufferObjectManager());

        r.setColor(YELLOW);

        return r;
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
            pauseR.onAreaTouched(event, event.getX(), event.getY());

            return super.onSceneTouchEvent(event);
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
                setTimerValue(timerValueD + 2);
                showBonus(2);
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
                            setTimerValue(timerValueD + 1);
                            showBonus(1);
                        } else if (gameType.equals(GameType.TIME_ATTACK)) {
                            setTimerValue(timerValueD - 1);
                            showBonus(-1);
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
                        setTimerValue(timerValueD + 1);
                        showBonus(1);
                    } else if (gameType.equals(GameType.TIME_ATTACK)) {
                        setTimerValue(timerValueD - 1);
                        showBonus(-1);
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
                        setTimerValue(timerValueD + 1);
                        showBonus(1);
                    } else if (gameType.equals(GameType.TIME_ATTACK)) {
                        setTimerValue(timerValueD - 1);
                        showBonus(-1);
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
                resume(PauseRectangle.RESUME);
            } else {
                onPause();
            }

        }
        return false;
    }

    void resume(int code) {
        switch (code) {
            case PauseRectangle.RESUME:
                resume();
                break;
            case PauseRectangle.RESTART:
                restart();
                break;
            case PauseRectangle.MENU:
                toMenu();
                break;
        }
    }

    private void resume() {
        pauseR.setVisible(false);
        registerMainControls();

        generateNewTask();
        registerUpdateHandler(timer);
    }

    private void restart() {
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
    }

    private void toMenu() {
        GameData.clearExtra();
        mainMenuActivity.setNewScene(new MainMenuScene(mainMenuActivity));
    }

    public void onPause() {
        unregisterMainControls();
        pauseR.setVisible(true);
        unregisterUpdateHandler(timer);
    }

    private void registerMainControls() {
        for (TextButton button : buttons) {
            registerTouchArea(button);
        }
    }

    private void unregisterMainControls() {
        for (TextButton button : buttons) {
            unregisterTouchArea(button);
        }
    }
}
