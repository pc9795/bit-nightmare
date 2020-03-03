package game.colliders;

import game.framework.Model;
import game.objects.GameObject;
import game.properties.Healthy;

import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 21-02-2020 21:50
 * Purpose: Collisions for Bullets
 **/
public interface BulletCollider {
    /**
     * Check collision of bullets
     *
     * @param model  game world
     * @param bullet bullet object
     * @return true if bullet is collided.
     */
    default boolean bulletEnvCollision(Model model, GameObject bullet) {
        //Out of the frame. Collided with the boundary
        if (bullet.getCentre().getX() < model.getLevelBoundary().getxMin() || bullet.getCentre().getX() > model.getLevelBoundary().getxMax()) {
            return true;
        }
        //Collecting environment objects to which it can collide.
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(bullet);
        willCollide.addAll(model.getMovableEnvironment());

        for (GameObject env : willCollide) {
            //Skipping if no intersection
            if (!env.getBounds().intersects(bullet.getBounds())) {
                continue;
            }
            switch (env.getType()) {
                case BLOCK:
                case GATE:
                case LAVA:
                case HIDING_BLOCK:
                case MOVABLE_BLOCK:
                    return true;
            }
        }
        return false;
    }

    /**
     * Check collision of bullet with enemy or player
     *
     * @param model  game world
     * @param bullet bullet object
     * @return true if bullet is collided
     */
    default boolean bulletPlayerOrEnemyCollision(Model model, GameObject bullet, boolean isFiredByPlayer, int damage) {
        if (!isFiredByPlayer && model.getPlayer1().getBounds().intersects(bullet.getBounds())) {
            //Damage health.
            model.getPlayer1().damageHealth(damage);
            return true;
        }
        for (GameObject obj : model.getEnemies()) {
            //An enemies bullet can't damage an enemy.
            //Skipping if no intersection.
            if (!isFiredByPlayer || !obj.getBounds().intersects(bullet.getBounds())) {
                continue;
            }
            switch (obj.getType()) {
                case CHARGER:
                case SOLDIER:
                case SUPER_SOLDIER:
                case GUARDIAN:
                    //Damage health.
                    ((Healthy) obj).damageHealth(damage);
                    return true;
            }
        }
        return false;
    }
}
