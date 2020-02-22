package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 14:26
 * Purpose: TODO:
 **/
public class Checkpoint extends GameObject {
    private static final int DEFAULT_WIDTH = 16;
    private static final int DEFAULT_HEIGHT = 64;

    public Checkpoint(int width, int height, Point2f centre) {
        super(width, height, centre, GameObjectType.CHECKPOINT);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(255, 236, 166));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {

    }
}
