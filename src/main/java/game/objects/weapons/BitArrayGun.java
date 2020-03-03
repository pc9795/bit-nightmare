package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.physics.Point2f;
import game.properties.Weapon;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: Bit array gun
 **/
public class BitArrayGun extends GameObject implements Weapon {
    private static final int DEFAULT_WIDTH = 48;
    private static final int DEFAULT_HEIGHT = 48;

    public BitArrayGun(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_ARRAY_GUN);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        if (texture != null && texture.getIdleRight().length != 0) {
            //THIS PORTION IS TOO MUCH DEPENDENT ON IMAGES USED.
            g.drawImage(texture.getIdleRight()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
        } else {
            g.setColor(new Color(63, 72, 204));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {

    }

    @Override
    public GameObject fire(Point2f centre, FacingDirection direction) {
        return new BitArrayGunBullet(centre, true, direction);
    }
}
