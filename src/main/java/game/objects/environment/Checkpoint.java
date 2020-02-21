package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 14:26
 * Purpose: TODO:
 **/
public class Checkpoint extends GameObject {

    public Checkpoint(int width, int height, Point3f centre) {
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
