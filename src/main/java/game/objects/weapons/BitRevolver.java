package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitRevolverBullet;
import game.physics.Point3f;
import game.physics.Vector3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: TODO:
 **/
public class BitRevolver extends GameObject implements Weapon {
    public BitRevolver(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_REVOLVER);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(140, 255, 251));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {

    }

    @Override
    public GameObject fire(Point3f centre, FacingDirection direction) {
        BitRevolverBullet bullet = new BitRevolverBullet(Constants.Bullet.BIT_REVOLVER_WIDTH,
                Constants.Bullet.BIT_REVOLVER_HEIGHT, centre.copy());
        if (facingDirection == FacingDirection.RIGHT) {
            bullet.setVelocity(new Vector3f(Constants.Bullet.BIT_REVOLVER_VELOCITY, 0, 0));
        } else {
            bullet.setVelocity(new Vector3f(-Constants.Bullet.BIT_REVOLVER_VELOCITY, 0, 0));
        }
        return bullet;
    }
}
