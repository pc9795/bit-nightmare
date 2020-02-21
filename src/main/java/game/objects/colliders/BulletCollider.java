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
    default void bulletCollision(Model model, GameObject bullet) {
        //Out of the frame
        if (bullet.getCentre().getX() < model.getLevelBoundary().getxMin()
                || bullet.getCentre().getX() > model.getLevelBoundary().getxMax()) {
            model.getBullets().remove(bullet);
        }
        //Currently Block, Movable Blocks and Oscillating Blocks will remove the bullet
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(bullet);
        for (GameObject env : willCollide) {
            if (env.getType() == GameObject.GameObjectType.BLOCK && env.getBounds().intersects(bullet.getBounds())) {
                model.getBullets().remove(bullet);
            }
        }
        for (GameObject obj : model.getMovableEnvironment()) {
            if (obj.getBounds().intersects(bullet.getBounds())) {
                model.getBullets().remove(bullet);
            }
        }
    }
}
