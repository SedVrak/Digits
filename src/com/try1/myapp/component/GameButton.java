package com.try1.myapp.component;

import com.try1.myapp.GameData;
import com.try1.myapp.KeyboardAction;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by SeniorJD
 */
public class GameButton extends TextButton {

    private KeyboardAction keyboardAction;
    public GameButton(KeyboardAction keyboardAction, Font font, VertexBufferObjectManager vertexBufferObjectManager) {
        super(keyboardAction.getTextValue(), font, vertexBufferObjectManager);

        this.keyboardAction = keyboardAction;
    }

    public KeyboardAction getKeyboardAction() {
        return keyboardAction;
    }

    @Override
    public void adjustPosition(float px, float py) {
        int length = GameData.getTextLength(font, textValue);

        float x = 360/2 - length/2 - textValue.length();
        float y = 240/2 - font.getLineHeight()/2;

        text.setPosition(x, y);
        text.setText(textValue);

        setPosition(px, py);
        setSize(360, 240);
    }
}
