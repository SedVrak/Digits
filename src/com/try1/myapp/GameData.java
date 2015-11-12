package com.try1.myapp;

import com.try1.myapp.scenes.LevelType;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontUtils;
import org.andengine.util.color.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by SeniorJD.
 */
public class GameData {
    public static final String HIGHSCORE_DB_NAME = "Highscores";
    public static final String HIGHSCORE_LABEL = "score";

    public static final String GAME_TYPE_STRING = "gameType";
    public static final String LEVEL = "level";
    public static final String FIRST_LAUNCH = "firstLaunch";

    static final String SCORE = "score";
    static final String BACKGROUND = "background";
    static final String COLOR = "color";
    static final String SELECTION_COLOR = "selectionColor";
    static final String INACTIVE_COLOR = "inactiveColor";
    static final String CAMERA_WIDTH = "cameraWidth";
    static final String CAMERA_HEIGHT = "cameraHeight";
    static final String IS_CAMERA_SIZE_INIT = "isCameraSizeInit";
    static final String FONT_100 = "font100";
    static final String FONT_150 = "font150";
    static final String FONT_100_GREEN = "font100green";
    static final String FONT_100_RED = "font100red";


    private static Map<String, Object> map = new HashMap<>();

    static {
        map.put(BACKGROUND, new Background(0.9412f, 0.9412f, 0.9412f));
        map.put(COLOR, new Color(0.9412f, 0.9412f, 0.9412f));
        map.put(SELECTION_COLOR, new Color(0.75f, 0.75f, 0.75f));
        map.put(INACTIVE_COLOR, new Color(0.85f, 0.85f, 0.85f));
        map.put(IS_CAMERA_SIZE_INIT, false);
        map.put(IS_CAMERA_SIZE_INIT, true);
        map.put(CAMERA_WIDTH, 1080);
        map.put(CAMERA_HEIGHT, 1920);
    }

    public static void setScore(int score) {
        map.put(SCORE, score);
    }

    public static int getScore() {
        return (int) map.get(SCORE);
    }

    public static void setScore(LevelType levelType, int levelIndex, int score) {
        map.put(levelType.getKey() + " " + levelIndex, score);
    }

    public static int getScore(LevelType levelType, int levelIndex) {
        return (int) map.get(levelType.getKey() + " " + levelIndex);
    }

    public static Background getBackground() {
        return (Background) map.get(BACKGROUND);
    }

    public static Color getColor() {
        return (Color) map.get(COLOR);
    }

    public static Color getSelectionColor() {
        return (Color) map.get(SELECTION_COLOR);
    }

    public static Color getInactiveColor() {
        return (Color) map.get(INACTIVE_COLOR);
    }

    public static int getTextLength(Font font, String text) {
        return (int) FontUtils.measureText(font, text);
    }

    static void setCameraWidth(int width) {
        map.put(CAMERA_WIDTH, width);
        map.put(IS_CAMERA_SIZE_INIT, true);
    }

    public static int getCameraWidth() {
        return (int) map.get(CAMERA_WIDTH);
    }

    public static void setCameraHeight(int height) {
        map.put(CAMERA_HEIGHT, height);
        map.put(IS_CAMERA_SIZE_INIT, true);
    }

    public static int getCameraHeight() {
        return (int) map.get(CAMERA_HEIGHT);
    }

    public static boolean isCameraSizeInit() {
        return (boolean) map.get(IS_CAMERA_SIZE_INIT);
    }

    public static int getStarsCount(LevelType levelType, int levelIndex, int timerValue) {
        int optimalTime = 15 * ((levelType.getIndex() + 2) / 2) * (levelIndex + 2)/2;
        if (timerValue <= optimalTime) {
            return 3;
        } else if (timerValue <= optimalTime * 1.5) {
            return 2;
        } else if (timerValue <= optimalTime * 2) {
            return 1;
        } else {
            return 0;
        }
    }

    public static Font getFont100() {
        return (Font) map.get(FONT_100);
    }

    public static void setFont100(Font font100) {
        map.put(FONT_100, font100);
    }

    public static Font getFont100Green() {
        return (Font) map.get(FONT_100_GREEN);
    }

    public static void setFont100Green(Font font100green) {
        map.put(FONT_100_GREEN, font100green);
    }

    public static Font getFont100Red() {
        return (Font) map.get(FONT_100_RED);
    }

    public static void setFont100Red(Font font100red) {
        map.put(FONT_100_RED, font100red);
    }

    public static Font getFont150() {
        return (Font) map.get(FONT_150);
    }

    public static void setFont150(Font font150) {
        map.put(FONT_150, font150);
    }

    private static Map<String, String> extra = new HashMap<>();

    public static String LEVEL_TYPE_STRING = "levelType";
    public static String LEVEL_NUMBER_STRING = "levelNumber";

    public static void putExtra(String key, String value) {
        extra.put(key, value);
    }

    public static String getExtra(String key) {
        return extra.get(key);
    }

    public static void clearExtra() {
        extra.clear();
    }
}
