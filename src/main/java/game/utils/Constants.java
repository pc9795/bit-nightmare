package game.utils;

/**
 * Created By: Prashant Chaubey
 * Created On: 16-02-2020 23:06
 * Purpose: Constants for the project
 **/
public final class Constants {
    private Constants() {

    }

    public static final String GAME_NAME = "Bit Nightmare";
    public static final int TARGET_FPS = 100;
    public static final int SCREEN_WIDTH = 1366;
    public static final int SCREEN_HEIGHT = 768;
    public static final int BUFFER_COUNT = 3;
    public static final int GAMEPAD_POLLING_WAIT_TIME_IN_SEC = 100;
    public static final String TEXTURE_CONFIG_FILE_LOC = "/texture_config.json";
    public static final String BACKGROUND_IMG_LOC = "/sprites/spooky.png";
    public static final String STORY_CONFIG_FILE_LOC = "/story.json";

    public static final class SoundPaths {
        private SoundPaths() {
        }

        public static final String MAIN_THEME = "/sound/theme.ogg";
    }

    public static final class Level {
        private Level() {
        }

        public static final int PIXEL_TO_WIDTH_RATIO = 32;
        public static final String INFO_FILE_LOC = "/level_names.txt";
        public static final String PROPERTIES_FILE_FORMAT = "/levels/%s.properties";
        public static final String PNG_FILE_FORMAT = "/levels/%s.png";
    }

    public static final class ErrorMessages {
        private ErrorMessages() {
        }

        public static final String CORRUPTED_LEVEL_PROPERTIES_FILE = "Corrupted property file for level:%s, corrupted row found:%s";
    }
}
