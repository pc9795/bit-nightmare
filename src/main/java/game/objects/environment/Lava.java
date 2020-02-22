package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class Lava extends GameObject {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    public Lava(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.LAVA);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(184, 61, 186));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }


    @Override
    public void collision(Model model) {

    }
}
