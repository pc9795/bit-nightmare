package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitRevolverBullet;
import game.physics.Point2f;
import game.properties.Weapon;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 00:01
 * Purpose: Bit revolver
 **/
public class BitRevolver extends GameObject implements Weapon {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    public BitRevolver(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_REVOLVER);
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
            g.setColor(new Color(140, 255, 251));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {

    }

    @Override
    public GameObject fire(Point2f centre, FacingDirection direction) {
        return new BitRevolverBullet(centre, true, direction);
    }
}
