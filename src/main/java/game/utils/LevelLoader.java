package game.utils;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.GameObjectFactory;
import game.physics.Point3f;
import javafx.util.Pair;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

    public void loadLevel(String levelName, Model model) throws IOException {
        Properties levelProperties = new Properties();
        try (InputStream in = LevelLoader.class.getResourceAsStream(getLevelPropertiesFilename(levelName))) {
            levelProperties.load(in);
        }
        Map<Color, Pair<String, Pair<Integer, Integer>>> objectInfoMap = new HashMap<>();
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
            objectInfoMap.put(new Color(red, green, blue), new Pair<>(objectType.trim(), new Pair<>(width, height)));
        }
        BufferedImage levelImg = BufferedImageLoader.getInstance().loadImage(getLevelDesignFilename(levelName));

        int w = levelImg.getWidth();
        int h = levelImg.getHeight();
        for (int row = 0; row < w; row++) {
            for (int col = 0; col < h; col++) {
                Color color = new Color(levelImg.getRGB(row, col));
                if (!objectInfoMap.containsKey(color)) {
                    continue;
                }
                Pair<String, Pair<Integer, Integer>> objectInfo = objectInfoMap.get(color);
                String objectTypeStr = objectInfo.getKey();
                GameObject.GameObjectType objectType = GameObject.GameObjectType.valueOf(objectTypeStr);

                int width = objectInfo.getValue().getKey();
                int height = objectInfo.getValue().getValue();
                model.addGameObject(GameObjectFactory.getGameObject(objectType, width, height,
                        new Point3f(row * Constants.LEVEL_PIXEL_TO_WIDTH_RATIO,
                                col * Constants.LEVEL_PIXEL_TO_WIDTH_RATIO, 0, new Pair<>(0, Constants.WIDTH))));
            }
        }
    }
}
