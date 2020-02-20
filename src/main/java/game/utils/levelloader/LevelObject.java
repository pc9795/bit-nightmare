package game.utils.levelloader;

import game.physics.Point3f;

/**
 * Created By: Prashant Chaubey
 * Created On: 20-02-2020 14:42
 * Purpose: TODO:
 **/
public class LevelObject {
    private Point3f centre;
    private String type;
    private int width, height;

    public LevelObject(int x, int y, String type, int width, int height) {
        this.centre = new Point3f(x, y);
        this.type = type;
        this.width = width;
        this.height = height;
    }

    public Point3f getCentre() {
        return centre;
    }

    public String getType() {
        return type;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
