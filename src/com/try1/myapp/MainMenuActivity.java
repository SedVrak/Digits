package com.try1.myapp;

import android.graphics.Color;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;

import com.try1.myapp.scenes.GameScene;
import com.try1.myapp.scenes.IScene;
import com.try1.myapp.scenes.MainMenuScene;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by SeniorJD.
 */
public class MainMenuActivity extends SimpleBaseGameActivity {

    public static int CAMERA_WIDTH = 1080;  // ширина экрана
    public static int CAMERA_HEIGHT = 1920; // высота экрана

    private Camera sapCamera;

    @Override
    protected void onCreateResources() {
        loadFont();

        loadCamera();
    }

    @Override
    protected void onSetContentView() {
        this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, this);
        if(android.os.Build.VERSION.SDK_INT >= 11){
            this.mRenderSurfaceView.setPreserveEGLContextOnPause(true);
        }
        this.setContentView(this.mRenderSurfaceView, BaseGameActivity.createSurfaceViewLayoutParams());
    }

    private void loadCamera() {
        if (GameData.isCameraSizeInit()) {
            CAMERA_WIDTH = GameData.getCameraWidth();
            CAMERA_HEIGHT = GameData.getCameraHeight();
        } else {
            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
            int honeyComb = android.os.Build.VERSION_CODES.HONEYCOMB_MR2;

            Display display = getWindowManager().getDefaultDisplay();
            if (currentApiVersion >= honeyComb) {
                Point size = new Point();
                display.getSize(size);
                CAMERA_WIDTH = size.x;
                CAMERA_HEIGHT = size.y;
            } else {
                CAMERA_WIDTH = display.getWidth();
                CAMERA_HEIGHT = display.getHeight();
            }

            GameData.setCameraWidth(CAMERA_WIDTH);
            GameData.setCameraHeight(CAMERA_HEIGHT);
        }
    }

    private void loadFont() {
        FontFactory.setAssetBasePath("font/");

        // 100
        ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);

        Font mFont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "roboto_pm.ttf", 100, true, android.graphics.Color.BLACK);
        mFont.load();

        GameData.setFont100(mFont);

        // 100 green
        fontTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);

        mFont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "roboto_pm.ttf", 100, true, Color.GREEN);
        mFont.load();

        GameData.setFont100Green(mFont);

        // 100 red
        fontTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);

        mFont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "roboto_pm.ttf", 100, true, Color.RED);
        mFont.load();

        GameData.setFont100Red(mFont);

        // 150
        fontTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);

        mFont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "roboto_pm.ttf", 150, true, android.graphics.Color.BLACK);
        mFont.load();

        GameData.setFont150(mFont);
    }

    @Override
    protected Scene onCreateScene() {
        return new MainMenuScene(this);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        sapCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), sapCamera);

        return engineOptions;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (getEngine().getScene() instanceof IScene) {
                ((IScene)getEngine().getScene()).onKeyDown(keyCode);
            }
        }
        return false;
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();

        if (getEngine().getScene() instanceof GameScene) {
            GameScene gameScene = (GameScene) getEngine().getScene();

            gameScene.onPause();
        }
    }

    public void setNewScene(Scene scene) {
        getEngine().setScene(scene);
    }

    @Override
    public void finish() {
        super.finish();
        System.exit(0);
    }
}
