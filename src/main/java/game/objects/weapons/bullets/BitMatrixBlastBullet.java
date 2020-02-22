package game.objects.weapons.bullets;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.BulletCollider;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:07
 * Purpose: TODO:
 **/
public class BitMatrixBlastBullet extends GameObject implements BulletCollider {
    boolean isFiredByPlayer;

    public BitMatrixBlastBullet(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_MATRIX_BLAST_BULLET);
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
        bulletCollision(model, this);
    }
}
