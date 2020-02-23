package game.colliders;

import game.framework.Model;
import game.objects.GameObject;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 22-02-2020 22:45
 * Purpose: A collider shared by some enemies
 **/
public interface EnemyCollider extends FineGrainedCollider {
    /**
     * Killed by lava. Stops at Blocks, Movable Blocks and Hiding Blocks.
     *
     * @param obj   object to check
     * @param model game world
     * @return true if object is falling.
     */
    default boolean enemyCollision(GameObject obj, Model model) {
        boolean[] collisions;
        boolean bottomCollision = false;
        List<GameObject> collisionCheck = model.getEnvironmentQuadTree().retrieve(obj);
        collisionCheck.addAll(model.getMovableEnvironment());
        for (GameObject env : collisionCheck) {
            Rectangle bounds = env.getBounds();
            switch (env.getType()) {
                case LAVA:
                    // If you touch it, you will burn.
                    if (bounds.intersects(obj.getBounds())) {
                        model.getEnemies().remove(obj);
                    }
                    break;
                case BLOCK:
                case HIDING_BLOCK:
                case MOVABLE_BLOCK:
                    collisions = fineGrainedCollision(obj, env);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    }
                    break;
            }
        }
        return !bottomCollision;
    }
}
