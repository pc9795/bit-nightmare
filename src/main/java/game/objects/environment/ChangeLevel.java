package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 14:47
 * Purpose: TODO:
 **/
public class ChangeLevel extends GameObject {
    private static final int DEFAULT_WIDTH = 16;
    private static final int DEFAULT_HEIGHT = 64;

    public ChangeLevel(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.CHANGE_LEVEL);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(255, 242, 0));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {

    }
}
