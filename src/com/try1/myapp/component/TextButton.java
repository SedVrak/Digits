package com.try1.myapp.component;

import android.util.Log;

import com.try1.myapp.GameData;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import static com.try1.myapp.MainMenuActivity.CAMERA_HEIGHT;
import static com.try1.myapp.MainMenuActivity.CAMERA_WIDTH;

/**
 * Created by SeniorJD
 */
public class TextButton extends Rectangle {
    private static float RECT_HEIGHT = 120;

    protected Text text;

    protected boolean pressed;
    protected boolean enabled;

    protected String textValue;
    protected TextButtonActionListener textButtonActionListener;
    protected Font font;

    public TextButton(String textValue, Font font, VertexBufferObjectManager vertexBufferObjectManager) {
        super(0f, 0f, 0f, RECT_HEIGHT, vertexBufferObjectManager);

        this.font = font;
        this.textValue = textValue;
        this.text = new Text(0, 0, font, textValue, vertexBufferObjectManager);

        init();
    }

    private void init() {
        attachChild(text);

        setPressed(false);
        setEnabled(true);
        adjustBackground();
    }

    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (!isEnabled()) {
            return false;
        }

        if (pSceneTouchEvent.isActionDown()) {
            setPressed(true);
            return true;
        } else if (pSceneTouchEvent.isActionCancel()) {
            setPressed(false);
            return true;
        } else if (pSceneTouchEvent.isActionUp()) {
            if (pressed) {
                setPressed(false);

                fireOnClick();

                return true;
            }
        }

        return false;
    }

    private void fireOnClick() {
        long start = System.currentTimeMillis();
        if (textButtonActionListener == null) {
            return;
        }

        textButtonActionListener.onClick();
        setPressed(false);

        Log.d("buttonPressure", "" + (System.currentTimeMillis() - start));
    }

    public void setTextButtonActionListener(TextButtonActionListener textButtonActionListener) {
        this.textButtonActionListener = textButtonActionListener;
    }

    public void setPressed(boolean pressed) {
        if (!isEnabled()) {
            return;
        }

        this.pressed = pressed;

        adjustBackground();
    }

    public boolean isPressed() {
        return pressed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        adjustBackground();
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;

        adjustPosition(getX(), getY());
    }

    public String getTextValue() {
        return textValue;
    }

    public void adjustPosition(float px, float py) {
        int length = GameData.getTextLength(font, textValue);

        float x = CAMERA_WIDTH/2 - length/2 - textValue.length();
        float y = CAMERA_HEIGHT/12 - font.getLineHeight()/2;

        text.setPosition(x, y);
        text.setText(textValue);

        setPosition(px, py);
        setSize(CAMERA_WIDTH, CAMERA_HEIGHT / 6);
    }

    private void adjustBackground() {
        if (isEnabled()) {
            if (isPressed()) {
                setColor(GameData.getSelectionColor());
            } else {
                setColor(GameData.getColor());
            }
        } else {
            setColor(GameData.getInactiveColor());
        }
    }
}
