package game.framework.levelloader;

import game.utils.BufferedImageLoader;
import game.utils.Constants;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 23:10
 * Purpose: Load levels from a png file format
 **/
public final class LevelLoader {
    private static final LevelLoader INSTANCE = new LevelLoader();

    private LevelLoader() {
    }

    public static LevelLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Give the properties file location for a given level
     *
     * @param levelName name of the level
     * @return properties file location
     */
    private String getLevelPropertiesFilename(String levelName) {
        return String.format(Constants.Level.PROPERTIES_FILE_FORMAT, levelName);
    }

    /**
     * Give the png file location for a given level
     *
     * @param levelName name of the level
     * @return png file location
     */
    private String getLevelDesignFilename(String levelName) {
        return String.format(Constants.Level.PNG_FILE_FORMAT, levelName);
    }

    /**
     * Load a level corresponding to a name
     *
     * @param levelName name of the level
     * @return an object containing information about level
     * @throws IOException If either png file or the properties file is not found for a given level
     */
    public Level loadLevel(String levelName) throws IOException {
        //Load properties file which contains the configuration for the given level
        Properties levelProperties = new Properties();
        try (InputStream in = LevelLoader.class.getResourceAsStream(getLevelPropertiesFilename(levelName))) {
            levelProperties.load(in);
        }

        //Load the data from properties file
        Map<Color, String> levelConfig = new HashMap<>();
        for (Object key : levelProperties.keySet()) {
            String type = String.valueOf(key);
            String row = levelProperties.getProperty(type);
            String[] items = row.split(",");
            if (items.length != 3) {
                throw new RuntimeException(String.format(Constants.ErrorMessages.CORRUPTED_LEVEL_PROPERTIES_FILE, levelName, row));
            }
            int red = Integer.valueOf(items[0].trim());
            int green = Integer.valueOf(items[1].trim());
            int blue = Integer.valueOf(items[2].trim());
            levelConfig.put(new Color(red, green, blue), type);
        }

        //Load the level png
        BufferedImage levelImg = BufferedImageLoader.getInstance().loadImage(getLevelDesignFilename(levelName));

        //REF: https://www.youtube.com/watch?v=1TFDOT1HiBo&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=11
        //Used the idea described in the mentioned video on how can we use pixels in an image for level design. I kept
        //the logic of level loading and creation of game object segregated to reduce coupling in my code.
        int imgWidth = levelImg.getWidth();
        int imgHeight = levelImg.getHeight();
        //maxX and maxY will hold the boundary of the level.
        int maxX = 0;
        int maxY = 0;
        List<LevelObject> levelObjects = new ArrayList<>();
        for (int x = 0; x < imgWidth; x++) {
            for (int y = 0; y < imgHeight; y++) {
                //Get color at a particular pixel
                Color color = new Color(levelImg.getRGB(x, y));
                if (!levelConfig.containsKey(color)) {
                    continue;
                }
                //Pixel's color found in the configuration
                maxX = Math.max(maxX, x);
                maxY = Math.max(maxY, y);
                levelObjects.add(new LevelObject(x, y, levelConfig.get(color)));
            }
        }
        return new Level(maxX, maxY, levelObjects);
    }
}
