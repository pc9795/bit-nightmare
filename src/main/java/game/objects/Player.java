package game.objects;

import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 23:07
 * Purpose: TODO:
 **/
public class Player extends GameObject {
    public Player(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.PLAYER);
    }

    @Override
    public void update(List<GameObject> objects) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 255));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
