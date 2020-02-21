package game.objects.weapons;

import game.framework.Game;
import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.objects.weapons.bullets.BitMatrixBlastBullet;
import game.physics.Point3f;
import game.physics.Vector3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class BitMatrixBlast extends GameObject implements Weapon {
    public BitMatrixBlast(int width, int height, Point3f centre) {
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
    public GameObject fire(Point3f centre, FacingDirection direction) {
        BitMatrixBlastBullet bullet = new BitMatrixBlastBullet(Constants.Bullet.BIT_MATRIX_BLAST_WIDTH,
                Constants.Bullet.BIT_MATRIX_BLAST_HEIGHT, centre.copy());
        if (facingDirection == FacingDirection.RIGHT) {
            bullet.setVelocity(new Vector3f(Constants.Bullet.BIT_MATRIX_BLAST_VELOCITY, 0, 0));
        } else {
            bullet.setVelocity(new Vector3f(-Constants.Bullet.BIT_MATRIX_BLAST_VELOCITY, 0, 0));
        }
        return bullet;
    }
}
