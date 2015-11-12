package com.try1.myapp.component;

import com.try1.myapp.GameData;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by SeniorJD
 */
public class ShowBonusTimePanel extends Rectangle {
    public static final int GREEN = 0;
    public static final int RED = 1;

    private Text bonusTimeGreen;
    private Text bonusTimeRed;
    private TimerHandler timer;

    public ShowBonusTimePanel(float pX, float pY, float pWidth, float pHeight, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

        init();
    }

    private void init() {
        setColor(GameData.getColor());

        bonusTimeGreen = new Text(0, 0, GameData.getFont100Green(), "", 2, getVertexBufferObjectManager());
        attachChild(bonusTimeGreen);
        bonusTimeRed = new Text(0, 0, GameData.getFont100Red(), "", 2, getVertexBufferObjectManager());
        attachChild(bonusTimeRed);

        timer = new TimerHandler(1.5f, new ITimerCallback() {
            @Override
            public void onTimePassed(TimerHandler pTimerHandler) {
                bonusTimeGreen.setVisible(false);
                bonusTimeRed.setVisible(false);
                setVisible(false);
            }
        });
    }

    public void showBonus(String text, int color, float px, float py) {
        if (color == GREEN) {
            bonusTimeGreen.setVisible(true);
            bonusTimeRed.setVisible(false);
            bonusTimeGreen.setText(text);
        } else if (color == RED) {
            bonusTimeRed.setVisible(true);
            bonusTimeGreen.setVisible(false);
            bonusTimeRed.setText(text);
        }

        setX(px);
        setY(py);
        setVisible(true);

        timer.reset();
    }

    public TimerHandler getTimer() {
        return timer;
    }
}
