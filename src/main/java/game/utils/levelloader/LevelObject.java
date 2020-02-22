package game.utils.levelloader;

import game.physics.Point3f;

/**
 * Created By: Prashant Chaubey
 * Created On: 20-02-2020 14:42
 * Purpose: Represents an object inside a level
 **/
public class LevelObject {
    private Point3f centre;
    private String type;

    LevelObject(int x, int y, String type) {
        this.centre = new Point3f(x, y);
        this.type = type;
    }

    public Point3f getCentre() {
        return centre;
    }

    public String getType() {
        return type;
    }
}
