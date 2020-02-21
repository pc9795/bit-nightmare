package game.objects.weapons.bullets;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.BulletCollider;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:06
 * Purpose: TODO:
 **/
public class BitArrayGunBullet extends GameObject implements BulletCollider {
    public BitArrayGunBullet(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_ARRAY_GUN_BULLET);
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + (facingDirection == FacingDirection.RIGHT ? 1 : -1) * Constants.Bullet.BIT_ARRAY_GUN_VELOCITY);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        //2*width to leave some space after bullets
        for (int i = 0, x = (int) centre.getX(); i < Constants.Bullet.BIT_ARRAY_GUN_COUNT; i++, x += 2 * width) {
            g.fillRect(x, (int) centre.getY(), width, height);
        }
    }

    @Override
    public void collision(Model model) {
        bulletCollision(model, this);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width * (2 * Constants.Bullet.BIT_ARRAY_GUN_COUNT - 1), height);
    }
}
