package game.objects.enemies;

import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class EnemyPortal extends GameObject {
    public EnemyPortal(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.ENEMY_PORTAL);
    }

    @Override
    public void update(List<GameObject> objects) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(196, 255, 14));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
