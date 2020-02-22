package game.objects.colliders;

import game.framework.Model;
import game.objects.GameObject;

import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 21:50
 * Purpose: TODO:
 **/
public interface BulletCollider {
    default boolean bulletCollision(Model model, GameObject bullet) {
        //Out of the frame
        if (bullet.getCentre().getX() < model.getLevelBoundary().getxMin()
                || bullet.getCentre().getX() > model.getLevelBoundary().getxMax()) {
            model.getBullets().remove(bullet);
            return true;
        }
        //Currently Block, Movable Blocks and Oscillating Blocks will remove the bullet
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(bullet);
        //Merging lists as same behavior
        willCollide.addAll(model.getMovableEnvironment());
        for (GameObject env : willCollide) {
            switch (env.getType()) {
                case BLOCK:
                case GATE:
                case LAVA:
                case HIDING_BLOCK:
                case MOVABLE_BLOCK:
                    if (env.getBounds().intersects(bullet.getBounds())) {
                        model.getBullets().remove(bullet);
                        return true;
                    }
            }
        }
        return false;
    }
}
