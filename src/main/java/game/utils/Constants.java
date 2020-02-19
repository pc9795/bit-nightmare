package game.utils;

/**
 * Created By: Prashant Chaubey
 * Created On: 16-02-2020 23:06
 * Purpose: TODO:
 **/
public final class Constants {
    private Constants() {

    }

    public static final String GAME_NAME = "Bit Nightmare";
    public static final int TARGET_FPS = 100;
    public static final int WIDTH = 1366;
    public static final int HEIGHT = 768;
    public static final int BUFFER_COUNT = 3;
    public static final int LEVEL_PIXEL_TO_WIDTH_RATIO = 32;
    public static final String LEVEL_NAMES_FILE = "/levelnames.txt";
    public static final String LEVEL_PROPERTIES_FORMAT = "/levels/%s.properties";
    public static final String LEVEL_PNG_FORMAT = "/levels/%s.png";
    public static final float PLAYER_GRAVITY = 0.1f;
    public static final float PLAYER_VELOCITY_X = 3f;
    public static final float PLAYER_VELOCITY_Y = 5f;


    public static final class Sprite {
        private Sprite() {

        }

        public static final String BLANK = "blankSprite.png";

    }
}
