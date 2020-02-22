package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitMatrixBlastBullet;
import game.physics.Point2f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class BitMatrixBlast extends GameObject implements Weapon {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    public BitMatrixBlast(int width, int height, Point2f centre) {
        super(width, height, centre, GameObjectType.BIT_MATRIX_BLAST);
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(14, 209, 69));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {

    }

    @Override
    public GameObject fire(Point2f centre, FacingDirection direction) {
        BitMatrixBlastBullet bullet = new BitMatrixBlastBullet(Constants.Bullet.BIT_MATRIX_BLAST_WIDTH, Constants.Bullet.BIT_MATRIX_BLAST_HEIGHT, centre.copy());
        bullet.setFacingDirection(direction);
        return bullet;
    }
}
