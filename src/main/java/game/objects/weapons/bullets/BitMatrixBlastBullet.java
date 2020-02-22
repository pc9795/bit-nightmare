package game.objects.weapons.bullets;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.BulletCollider;
import game.objects.properties.Healthy;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:07
 * Purpose: TODO:
 **/
public class BitMatrixBlastBullet extends GameObject implements BulletCollider {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 64;
    private boolean isFiredByPlayer;

    public BitMatrixBlastBullet(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_MATRIX_BLAST_BULLET);
        this.isFiredByPlayer = true;
    }

    public BitMatrixBlastBullet(int width, int height, Point3f centre, boolean isFiredByPlayer) {
        super(width, height, centre, GameObjectType.BIT_REVOLVER_BULLET);
        this.isFiredByPlayer = isFiredByPlayer;
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + (facingDirection == FacingDirection.RIGHT ? 1 : -1) * Constants.Bullet.BIT_MATRIX_BLAST_VELOCITY);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {
        if (bulletCollision(model, this)) {
            return;
        }
        if (!isFiredByPlayer && model.getPlayer1().getBounds().intersects(getBounds())) {
            //todo make it configurable
            //todo remove 100 damage for testing only.
            model.getPlayer1().damageHealth(40);
            model.getBullets().remove(this);
            return;
        }
        for (GameObject obj : model.getEnemies()) {
            switch (obj.getType()) {
                case ENEMY1:
                case ENEMY2:
                case ENEMY3:
                case BOSS1:
                    //todo make it configurable
                    if (isFiredByPlayer && obj.getBounds().intersects(getBounds())) {
                        ((Healthy) obj).damageHealth(100);
                        model.getBullets().remove(this);
                    }
            }
        }
    }
}
