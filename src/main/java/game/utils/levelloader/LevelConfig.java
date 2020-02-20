package game.utils.levelloader;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 20-02-2020 14:42
 * Purpose: TODO:
 **/
public class LevelConfig {
    private Color color;
    private int width, height;
    private String type;

    public LevelConfig(int red, int green, int blue, int width, int height, String type) {
        this.color = new Color(red, green, blue);
        this.width = width;
        this.height = height;
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }
}
