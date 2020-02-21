package game.objects.weapons.bullets;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:07
 * Purpose: TODO:
 **/
public class BitMatrixBlastBullet extends GameObject {
    public BitMatrixBlastBullet(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_MATRIX_BLAST_BULLET);
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + velocity.getX());
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        for (GameObject env : willCollide) {
            if (env.getType() != GameObjectType.BLOCK || !env.getBounds().intersects(getBounds())) {
                continue;
            }
            model.getBullets().remove(this);
            return;
        }
        for (GameObject obj : model.getMovableEnvironment()) {
            if (!obj.getBounds().intersects(getBounds())) {
                continue;
            }
            model.getBullets().remove(this);
            return;
        }
    }
}
