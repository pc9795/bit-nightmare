package game.objects.weapons;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

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
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void fire() {

    }
}
