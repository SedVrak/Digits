package com.try1.myapp.component;

import com.try1.myapp.GameData;
import com.try1.myapp.MainMenuActivity;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SeniorJD
 */
public class RecordTextButton extends TextButton {

    private ITexture mTexture;
    private ITextureRegion textureRegion;

    private int starsCount;

    public RecordTextButton(String textValue, Font font, MainMenuActivity mainMenuActivity, int starsCount) {
        super(textValue, font, mainMenuActivity.getVertexBufferObjectManager());
        this.starsCount = starsCount;

        loadStarTexture(mainMenuActivity);
        init();
    }

    private void loadStarTexture(final MainMenuActivity mainMenuActivity) {
        try {
            mTexture = new BitmapTexture(mainMenuActivity.getTextureManager(), new IInputStreamOpener() {
                //@Override
                public InputStream open() throws IOException {
                    return mainMenuActivity.getAssets().open("gfx/star.png");
                }
            });

            mTexture.load();

            textureRegion = TextureRegionFactory.extractFromTexture(mTexture, 0, 0, mTexture.getWidth(), mTexture.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        int x = MainMenuActivity.CAMERA_WIDTH/6 - starsCount * MainMenuActivity.CAMERA_WIDTH/36;

        for (int j = 0; j < starsCount; j++) {
            ButtonSprite button;

            button = new ButtonSprite(x, MainMenuActivity.CAMERA_HEIGHT / 6 - MainMenuActivity.CAMERA_WIDTH/18 - MainMenuActivity.CAMERA_WIDTH/36, textureRegion, getVertexBufferObjectManager());
            button.setSize(MainMenuActivity.CAMERA_WIDTH/18, MainMenuActivity.CAMERA_WIDTH/18);

            attachChild(button);

            x += MainMenuActivity.CAMERA_WIDTH/18;
        }
    }

    @Override
    public void adjustPosition(float px, float py) {
        int length = GameData.getTextLength(font, textValue);

        float x = MainMenuActivity.CAMERA_WIDTH/6 - length/2;
        float y = MainMenuActivity.CAMERA_HEIGHT/12 - GameData.getFont150().getLineHeight()/2;

        text.setPosition(x, y);
        text.setText(textValue);

        text.setSize(length, 150);

        setPosition(px, py);
        setSize(MainMenuActivity.CAMERA_WIDTH / 3, MainMenuActivity.CAMERA_HEIGHT / 6);
    }
}
