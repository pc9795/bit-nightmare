package game.objects.environment;

import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:00
 * Purpose: TODO:
 **/
public class Block extends GameObject {
    public Block(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BLOCK);
    }

    @Override
    public void update(List<GameObject> objects) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 0));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
