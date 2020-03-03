package game.objects.weapons.bullets;

import game.colliders.BulletCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 21-02-2020 18:07
 * Purpose: Bit Matrix blast's bullet
 **/
public class BitMatrixBlastBullet extends GameObject implements BulletCollider {
    //Constants
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 30;
    private static final float DEFAULT_SPEED_X = 4f;
    private static final int DEFAULT_DAMAGE = 50;
    //Variables
    private boolean isFiredByPlayer;
    private float speedX;
    private int damage;

    public BitMatrixBlastBullet(Point2f centre, boolean isFiredByPlayer, FacingDirection facingDirection) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_MATRIX_BLAST_BULLET);
        this.isFiredByPlayer = isFiredByPlayer;
        this.speedX = DEFAULT_SPEED_X;
        this.facingDirection = facingDirection;
        this.damage = DEFAULT_DAMAGE;
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + (facingDirection == FacingDirection.RIGHT ? 1 : -1) * speedX);
    }

    @Override
    public void render(Graphics g) {
        if (texture != null && texture.getIdleRight().length != 0) {
            //THIS PORTION IS TOO MUCH DEPENDENT ON IMAGES USED.
            g.drawImage(texture.getIdleRight()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
        } else {
            g.setColor(new Color(182, 3, 253));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {
        if (bulletEnvCollision(model, this) || bulletPlayerOrEnemyCollision(model, this, isFiredByPlayer, damage)) {
            model.getBullets().remove(this);
        }
    }
}
