package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class BitArrayGun extends GameObject implements Weapon {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    public BitArrayGun(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_ARRAY_GUN);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(63, 72, 204));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {

    }

    @Override
    public GameObject fire(Point3f centre, FacingDirection direction) {
        BitArrayGunBullet bullet = new BitArrayGunBullet(Constants.Bullet.BIT_ARRAY_GUN_WIDTH, Constants.Bullet.BIT_ARRAY_GUN_HEIGHT, centre.copy());
        bullet.setFacingDirection(direction);
        return bullet;
    }
}
