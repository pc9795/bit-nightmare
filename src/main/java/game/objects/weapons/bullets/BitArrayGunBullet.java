package game.objects.weapons.bullets;

import game.colliders.BulletCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:06
 * Purpose: Bit Array gun' bullet
 **/
public class BitArrayGunBullet extends GameObject implements BulletCollider {
    //Constants
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
    private static final int DEFAULT_COUNT = 5;
    private static final float DEFAULT_SPEED_X = 5f;
    private static final int DEFAULT_DAMAGE = 8;
    //Variables
    private boolean isFiredByPlayer;
    private int count;
    private float speedX;
    private int damage;

    public BitArrayGunBullet(Point2f centre, boolean isFiredByPlayer, FacingDirection facingDirection) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_ARRAY_GUN_BULLET);
        //Shift when fired in left direction
        if (facingDirection == FacingDirection.LEFT) {
            this.centre.setX(this.centre.getX() - (2 * DEFAULT_COUNT - 1) * DEFAULT_WIDTH);
        }
        this.isFiredByPlayer = isFiredByPlayer;
        this.count = DEFAULT_COUNT;
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
        g.setColor(new Color(182, 3, 253));
        //2*width to leave some space after bullets
        for (int i = 0, x = (int) centre.getX(); i < count; i++, x += 2 * width) {
            g.fillRect(x, (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {
        if (count == 0) {
            model.getBullets().remove(this);
            return;
        }

        if (bulletEnvCollision(model, this) || bulletPlayerOrEnemyCollision(model, this, isFiredByPlayer, damage)) {
            if (facingDirection == FacingDirection.RIGHT) {
                centre.setX(centre.getX() + width);
            }
            count--;
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width * (2 * count - 1), height);
    }
}
