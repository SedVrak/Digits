package com.try1.myapp;

/**
 * Created by SeniorJD.
 */
public class LevelsActivity /*extends SimpleBaseGameActivity*/ {
//    public enum LevelType {
//        PLUS("Plus Levels", 0),
//        MINUS("Minus Levels", 1),
//        MULTIPLY("Multiply Levels", 2);
////        DIVIDE("Divide Levels", 3);
//
//        private String key;
//        private int index;
//
//        private LevelType(String s, int index) {
//            this.key = s;
//            this.index = index;
//        }
//
//        public String getKey() {
//            return key;
//        }
//
//        public LevelType next() {
//            switch (this) {
//                case PLUS: return MINUS;
//                case MINUS: return MULTIPLY;
//                case MULTIPLY: return null;
////                case MULTIPLY: return DIVIDE;
////                case DIVIDE: return null;
//            }
//
//            return null;
//        }
//
//        public int getIndex() {
//            return index;
//        }
//    }
//
//    public static String LEVEL_TYPE_STRING = "levelType";
//    public static String LEVEL_NUMBER_STRING = "levelNumber";
//
//    private static int CAMERA_WIDTH = 1080;  // ширина экрана
//    private static int CAMERA_HEIGHT = 1920; // высота экрана
//
//    private Camera sapCamera;
//
//    private Font mFont150;
//    private Font mFont100;
//
//    private Rectangle[] levelsR = new Rectangle[15];
//    private Text[] levelsText = new Text[15];
//
//    private int[] records;
//
//    private ITexture mTexture;
//    private ITextureRegion textureRegion;
//
//    @Override
//    protected void onCreateResources() {
//        loadFont();
//        loadCamera();
//        loadBundle();
//        loadLastOpenedLevel();
//        loadStarTexture();
//    }
//
//    private void loadStarTexture() {
//        try {
//            mTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
//                //@Override
//                public InputStream open() throws IOException {
//                    return getAssets().open("gfx/star.png");
//                }
//            });
//
//            mTexture.load();
//
//            textureRegion = TextureRegionFactory.extractFromTexture(mTexture, 0, 0, mTexture.getWidth(), mTexture.getHeight());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private void loadCamera() {
//        if (GameData.isCameraSizeInit()) {
//            CAMERA_WIDTH = GameData.getCameraWidth();
//            CAMERA_HEIGHT = GameData.getCameraHeight();
//        } else {
//            int currentApiVersion = android.os.Build.VERSION.SDK_INT;
//            int honeyComb = android.os.Build.VERSION_CODES.HONEYCOMB_MR2;
//
//            Display display = getWindowManager().getDefaultDisplay();
//            if (currentApiVersion >= honeyComb) {
//                Point size = new Point();
//                display.getSize(size);
//                CAMERA_WIDTH = size.x;
//                CAMERA_HEIGHT = size.y;
//            } else {
//                CAMERA_WIDTH = display.getWidth();
//                CAMERA_HEIGHT = display.getHeight();
//            }
//
//            GameData.setCameraWidth(CAMERA_WIDTH);
//            GameData.setCameraHeight(CAMERA_HEIGHT);
//        }
//    }
//
//    private void loadFont() {
//        FontFactory.setAssetBasePath("font/");
//        ITexture fontTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
//
//        mFont150 = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "roboto.ttf", 150, true, android.graphics.Color.BLACK);
//        mFont150.load();
//
//        fontTexture = new BitmapTextureAtlas(getTextureManager(), 1024, 1024, TextureOptions.DEFAULT);
//        mFont100 = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "roboto.ttf", 100, true, android.graphics.Color.BLACK);
//        mFont100.load();
//    }
//
//    @Override
//    protected Scene onCreateScene() {
//        Scene scene = new Scene() {
//            @Override
//            public boolean onSceneTouchEvent(TouchEvent event) {
//                if (event.getAction() == TouchEvent.ACTION_UP) {
//                    for (int i = 0; i < levelsText.length; i++) {
//                        Rectangle r = levelsR[i];
//
//                        if (r.contains(event.getX(), event.getY())) {
//                            r.setColor((i <= lastOpenedLevel) ? GameData.getColor() : GameData.getInactiveColor());
//
//                            if (i <= lastOpenedLevel) {
//                                Intent intent = new Intent(LevelsActivity.this, GameActivity.class);
//                                intent.putExtra(GameData.GAME_TYPE_STRING, GameType.LEVELS);
//                                intent.putExtra(LEVEL_TYPE_STRING, levelType);
//                                intent.putExtra(LEVEL_NUMBER_STRING, i);
//                                startActivity(intent);
//                                finish();
//                            }
//                            break;
//                        }
//                    }
//                } else if (event.getAction() == TouchEvent.ACTION_DOWN || event.getAction() == TouchEvent.ACTION_MOVE) {
//                    for (int i = 0; i < levelsText.length; i++) {
//                        Rectangle r = levelsR[i];
//
//                        if (r.contains(event.getX(), event.getY())) {
//                            r.setColor((i <= lastOpenedLevel) ? GameData.getSelectionColor() : GameData.getInactiveColor());
//                        } else {
//                            r.setColor((i <= lastOpenedLevel) ? GameData.getColor() : GameData.getInactiveColor());
//                        }
//                    }
//                }
//
//                return super.onSceneTouchEvent(event);
//            }
//        };
//
//        scene.setBackground(GameData.getBackground());
//
//        float px = 0;
//        float py = CAMERA_HEIGHT/6;
//
//        for (int i = 0; i < levelsText.length; i++) {
//            String s = String.valueOf(i + 1);
//
//            Text text = new Text(px, py, mFont150, s, getVertexBufferObjectManager());
//            Rectangle r = new Rectangle(0, 0, 0, CAMERA_HEIGHT/6, getVertexBufferObjectManager());
//            r.setColor((i <= lastOpenedLevel) ? GameData.getColor() : GameData.getInactiveColor());
//
//            setText(text, r, s, px, py);
//
//            r.attachChild(text);
//
//            if (i < lastOpenedLevel + 1) {
//                int starsCount = GameData.getStarsCount(levelType, i, records[i]);
//
//                int x = CAMERA_WIDTH/6 - starsCount * CAMERA_WIDTH/36;
//
//                for (int j = 0; j < starsCount; j++) {
//                    ButtonSprite button;
//
//
//                    button = new ButtonSprite(x, CAMERA_HEIGHT / 6 - CAMERA_WIDTH/18 - CAMERA_WIDTH/36, textureRegion, getVertexBufferObjectManager());
//                    button.setSize(CAMERA_WIDTH/18, CAMERA_WIDTH/18);
//
//                    r.attachChild(button);
//
//                    x += CAMERA_WIDTH/18;
//                }
//            }
//
//            scene.attachChild(r);
////            scene.attachChild(text);
//            scene.registerTouchArea(r);
//
//            levelsText[i] = text;
//            levelsR[i] = r;
//
//            if ((i + 1) % 3 == 0) {
//                py += CAMERA_HEIGHT/6;
//                px = 0;
//            } else {
//                px += CAMERA_WIDTH/3;
//            }
//        }
//
//        Text level = new Text(0, CAMERA_HEIGHT/12, mFont100, levelType.getKey(), getVertexBufferObjectManager());
//        setText(level, levelType.getKey());
//        scene.attachChild(level);
//
//        return scene;
//    }
//
//    private LevelType levelType;
//    private int lastOpenedLevel;
//
//    private void loadBundle() {
//        Intent intent = getIntent();
//
//        Bundle extras = intent.getExtras();
//
//        levelType = (LevelType) extras.get(LEVEL_TYPE_STRING);
//    }
//
//    private void loadLastOpenedLevel() {
//        SharedPreferences mScoreDb = getSharedPreferences(ScoreActivity.HIGHSCORE_DB_NAME, Context.MODE_PRIVATE);
//        lastOpenedLevel = mScoreDb.getInt(levelType.getKey(), 0);
//
//        records = new int[lastOpenedLevel + 1];
//
//        for (int i = 0; i < lastOpenedLevel + 1; i++) {
//            records[i] = mScoreDb.getInt(levelType.getKey() + " " + i, Integer.MAX_VALUE);
//        }
//    }
//
//    @Override
//    public EngineOptions onCreateEngineOptions() {
//        sapCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
//
//        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), sapCamera);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            LevelsActivity.this.startActivity(new Intent(LevelsActivity.this, LevelTypeActivity.class));
//            LevelsActivity.this.finish();
//        }
//        return false;
//    }
//
//    private void setText(Text text, Rectangle rectangle, String s, float px, float py) {
//        int length = GameData.getTextLength(mFont150, s);
////        int length = s.length() * 60;
//
//        float x = CAMERA_WIDTH/6 - length/2;
//        float y = CAMERA_HEIGHT/12 - mFont150.getLineHeight()/2;
//
//        text.setPosition(x, y);
//        text.setText(s);
//
//        text.setSize(length, 150);
//
//        rectangle.setPosition(px, py);
//        rectangle.setSize(CAMERA_WIDTH/3, CAMERA_HEIGHT/6);
//    }
//
//    private void setText(Text text, String s) {
//        int length = GameData.getTextLength(mFont100, s);
//
//        float x = CAMERA_WIDTH/2 - length/2 - s.length();
//        float y = text.getY();
//
//        text.setPosition(x, y);
//    }
}
