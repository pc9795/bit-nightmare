package game.objects.properties;

import game.physics.Point3f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 22-02-2020 02:56
 * Purpose: TODO:
 **/
public interface Healthy {
    default void showHealth(Point3f centre, int health, Graphics g) {
        g.setColor(Color.GREEN);
        //todo Make configurable
        g.fillRect((int) centre.getX(), (int) centre.getY() - 10, (int) (health * 0.3), 5);
        g.setColor(Color.RED);
        //todo Make configurable
        g.fillRect((int) (centre.getX() + (health * 0.3)), (int) centre.getY() - 10, (int) ((100 - health) * 0.3), 5);
    }

    void damageHealth(int damage);
}
