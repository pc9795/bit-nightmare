package game.objects.weapons.bullets;

import game.colliders.BulletCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:06
 * Purpose: Bit revolver's bullet
 **/
public class BitRevolverBullet extends GameObject implements BulletCollider {
    //Constants
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
    private static final float DEFAULT_SPEED_X = 5f;
    private static final int DEFAULT_DAMAGE = 20;
    //Variables
    private float speedX;
    private boolean isFiredByPlayer;
    private int damage;

    public BitRevolverBullet(Point2f centre, boolean isFiredByPlayer, FacingDirection facingDirection) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_REVOLVER_BULLET);
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
        if (texture != null && texture.getIdle().length != 0) {
            g.drawImage(texture.getIdle()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
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
