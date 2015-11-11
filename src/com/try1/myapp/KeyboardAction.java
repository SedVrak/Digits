package com.try1.myapp;

/**
 * Created by SeniorJD.
 */
public enum KeyboardAction {
    KEY_1 ("1"),
    KEY_2 ("2"),
    KEY_3 ("3"),
    KEY_4 ("4"),
    KEY_5 ("5"),
    KEY_6 ("6"),
    KEY_7 ("7"),
    KEY_8 ("8"),
    KEY_9 ("9"),
    KEY_0 ("0"),
    KEY_PM ("\u2213"),
    KEY_C("C");

    private String textValue;

    KeyboardAction(String textValue) {
        this.textValue = textValue;
    }

    public String getTextValue() {
        return textValue;
    }
}
