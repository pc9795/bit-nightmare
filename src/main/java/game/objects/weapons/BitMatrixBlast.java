package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitMatrixBlastBullet;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: Bit matrix blast
 **/
public class BitMatrixBlast extends GameObject implements Weapon {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    public BitMatrixBlast(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_MATRIX_BLAST);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        if (texture != null && texture.getIdle().length != 0) {
            g.drawImage(texture.getIdle()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
        } else {
            g.setColor(new Color(14, 209, 69));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {

    }

    @Override
    public GameObject fire(Point2f centre, FacingDirection direction) {
        return new BitMatrixBlastBullet(centre, true, direction);
    }
}
