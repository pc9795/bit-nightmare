package game.properties;

import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 22-02-2020 02:56
 * Purpose: Methods relating to health
 **/
public interface Healthy {
    int HEALTH_BAR_OFFSET_ABOVE_HEAD = 10;
    float HEALTH_BAR_WIDTH_MULTIPLIER = 0.3f;
    int HEALTH_BAR_HEIGHT = 5;
    int HEALTH_BAR_RANGE = 100;

    /**
     * Show health bar over a object
     *
     * @param centre    centre of the object
     * @param health    health of the object
     * @param maxHealth maximum health to convert to a uniform scale
     * @param g         graphics object
     */
    default void showHealth(Point2f centre, int health, int maxHealth, Graphics g) {
        //Converting to 100 based health
        health = (int) (((float) health / maxHealth) * HEALTH_BAR_RANGE);
        g.setColor(Color.GREEN);
        int lifeLeft = (int) (health * HEALTH_BAR_WIDTH_MULTIPLIER);
        g.fillRect((int) centre.getX(), (int) centre.getY() - HEALTH_BAR_OFFSET_ABOVE_HEAD, lifeLeft, HEALTH_BAR_HEIGHT);
        g.setColor(Color.RED);
        g.fillRect((int) (centre.getX() + lifeLeft), (int) centre.getY() - HEALTH_BAR_OFFSET_ABOVE_HEAD,
                (int) ((HEALTH_BAR_RANGE - health) * HEALTH_BAR_WIDTH_MULTIPLIER), HEALTH_BAR_HEIGHT);
    }

    /**
     * Show health bar over a object
     *
     * @param x         x position
     * @param y         y position
     * @param health    health of the object
     * @param maxHealth maximum health to convert to a uniform scale
     * @param g         graphics object
     */
    default void showHealth(int x, int y, int health, int maxHealth, Graphics g) {
        //Converting to 100 based health
        health = (int) (((float) health / maxHealth) * HEALTH_BAR_RANGE);
        g.setColor(Color.GREEN);
        int lifeLeft = (int) (health * HEALTH_BAR_WIDTH_MULTIPLIER);
        g.fillRect(x, y - HEALTH_BAR_OFFSET_ABOVE_HEAD, lifeLeft, HEALTH_BAR_HEIGHT);
        g.setColor(Color.RED);
        g.fillRect(x + lifeLeft, y - HEALTH_BAR_OFFSET_ABOVE_HEAD,
                (int) ((HEALTH_BAR_RANGE - health) * HEALTH_BAR_WIDTH_MULTIPLIER), HEALTH_BAR_HEIGHT);
    }

    /**
     * Affect when a damage is received
     *
     * @param damage received damage
     */
    void damageHealth(int damage);
}
