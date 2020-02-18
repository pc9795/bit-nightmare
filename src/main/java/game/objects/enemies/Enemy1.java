package game.objects.enemies;

import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: TODO:
 **/
public class Enemy1 extends GameObject {
    public Enemy1(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.ENEMY1);
    }

    @Override
    public void update(List<GameObject> objects) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(136, 9, 27));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
