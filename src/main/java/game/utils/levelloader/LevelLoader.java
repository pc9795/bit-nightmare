package game.utils.levelloader;

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
 * Purpose: TODO:
 **/
public final class LevelLoader {
    private static final LevelLoader INSTANCE = new LevelLoader();

    private LevelLoader() {
    }

    public static LevelLoader getInstance() {
        return INSTANCE;
    }

    private String getLevelPropertiesFilename(String levelName) {
        return String.format(Constants.LEVEL_PROPERTIES_FORMAT, levelName);
    }

    private String getLevelDesignFilename(String levelName) {
        return String.format(Constants.LEVEL_PNG_FORMAT, levelName);
    }

    public Level loadLevel(String levelName) throws IOException {
        Properties levelProperties = new Properties();
        try (InputStream in = LevelLoader.class.getResourceAsStream(getLevelPropertiesFilename(levelName))) {
            levelProperties.load(in);
        }
        Map<Color, LevelConfig> objectInfo = new HashMap<>();
        for (Object key : levelProperties.keySet()) {
            String objectType = String.valueOf(key);
            String row = levelProperties.getProperty(objectType);
            String[] items = row.split(",");
            if (items.length != 5) {
                throw new RuntimeException(String.format("Corrupted property file for level:%s", levelName));
            }
            int red = Integer.valueOf(items[0].trim());
            int green = Integer.valueOf(items[1].trim());
            int blue = Integer.valueOf(items[2].trim());
            int width = Integer.valueOf(items[3].trim());
            int height = Integer.valueOf(items[4].trim());
            LevelConfig config = new LevelConfig(red, green, blue, width, height, objectType);
            objectInfo.put(config.getColor(), config);
        }
        BufferedImage levelImg = BufferedImageLoader.getInstance().loadImage(getLevelDesignFilename(levelName));

        int w = levelImg.getWidth();
        int h = levelImg.getHeight();
        int maxX = 0;
        int maxY = 0;
        List<LevelObject> levelObjects = new ArrayList<>();
        for (int row = 0; row < w; row++) {
            for (int col = 0; col < h; col++) {
                Color color = new Color(levelImg.getRGB(row, col));
                if (!objectInfo.containsKey(color)) {
                    continue;
                }
                maxX = Math.max(maxX, row);
                maxY = Math.max(maxY, col);
                LevelConfig config = objectInfo.get(color);
                levelObjects.add(new LevelObject(row, col, config.getType(), config.getWidth(), config.getHeight()));
            }
        }
        return new Level(maxX, maxY, levelObjects);
    }
}
