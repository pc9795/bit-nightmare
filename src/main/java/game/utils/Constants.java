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
    public static final float GRAVITY = 0.1f;
    public static final float PLAYER_VELOCITY_X = 3f;
    public static final float PLAYER_VELOCITY_Y = 5f;
    public static final float MOVABLE_BLOCK_VELOCITY_X = 4f;
    public static final float MOVABLE_BLOCK_FRICTION = 0.1f;
    public static final int HIDING_BLOCK_HIDING_TIME_IN_SEC = 2;
    public static final int GAMEPAD_CONTROLLER_POLLING_WAIT_TIME_IN_MILLIS = 100;

    public static final class Enemies {
        private Enemies() {
        }

        public static final float ENEMY1_VELX = 4f;
        public static final int ENEMY1_LOS = 300;
        public static final int ENEMY2_LOS = 600;
        public static final float ENEMY2_BULLET_FREQ_IN_SEC = 1.5f;
        public static final float ENEMY3_BULLET_FREQ_IN_SEC = 1.5f;
        public static final float BOSS1_BULLET_FREQ_IN_SEC = 1.5f;
        ;
    }

    public static final class Bullet {
        private Bullet() {
        }

        //Bit Revolver
        public static final float BIT_REVOLVER_VELOCITY = 5f;
        public static final int BIT_REVOLVER_WIDTH = 10;
        public static final int BIT_REVOLVER_HEIGHT = 10;
        //Bit Array Gun
        public static final float BIT_ARRAY_GUN_VELOCITY = 5f;
        public static final int BIT_ARRAY_GUN_COUNT = 5;
        public static final int BIT_ARRAY_GUN_WIDTH = 10;
        public static final int BIT_ARRAY_GUN_HEIGHT = 10;
        //Bit Matrix Blast
        public static final float BIT_MATRIX_BLAST_VELOCITY = 4f;
        public static final int BIT_MATRIX_BLAST_WIDTH = 30;
        public static final int BIT_MATRIX_BLAST_HEIGHT = 30;

        public static final float PLAYER_BULLET_FREQ_IN_SEC = 1;
    }

    public static final class Sprite {
        private Sprite() {
        }

        public static final String BLANK = "blankSprite.png";

    }
}
