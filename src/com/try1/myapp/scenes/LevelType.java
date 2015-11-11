package com.try1.myapp.scenes;

/**
 * Created by SeniorJD.
 */
public enum LevelType {
    PLUS("Plus Levels", 0),
    MINUS("Minus Levels", 1),
    MULTIPLY("Multiply Levels", 2);
//        DIVIDE("Divide Levels", 3);

    private String key;
    private int index;

    private LevelType(String s, int index) {
        this.key = s;
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public LevelType next() {
        switch (this) {
            case PLUS: return MINUS;
            case MINUS: return MULTIPLY;
            case MULTIPLY: return null;
//                case MULTIPLY: return DIVIDE;
//                case DIVIDE: return null;
        }

        return null;
    }

    public int getIndex() {
        return index;
    }
}
