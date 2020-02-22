package game.utils.levelloader;

import java.util.Collections;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 20-02-2020 14:56
 * Purpose: An object containing the information about a level
 **/
public class Level {
    private int maxX, maxY;
    private List<LevelObject> levelObjects;

    Level(int maxX, int maxY, List<LevelObject> levelObjects) {
        this.maxX = maxX;
        this.maxY = maxY;
        //Immutable
        this.levelObjects = Collections.unmodifiableList(levelObjects);
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public List<LevelObject> getLevelObjects() {
        return levelObjects;
    }
}
