package game.objects.weapons.bullets;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.BulletCollider;
import game.objects.properties.Healthy;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:06
 * Purpose: TODO:
 **/
public class BitRevolverBullet extends GameObject implements BulletCollider {
    boolean isFiredByPlayer;

    public BitRevolverBullet(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_REVOLVER_BULLET);
        isFiredByPlayer = true;
    }

    public BitRevolverBullet(int width, int height, Point3f centre, boolean isFiredByPlayer) {
        super(width, height, centre, GameObjectType.BIT_REVOLVER_BULLET);
        this.isFiredByPlayer = isFiredByPlayer;
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + (facingDirection == FacingDirection.RIGHT ? 1 : -1) * Constants.Bullet.BIT_REVOLVER_VELOCITY);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {
        if (bulletCollision(model, this)) {
            return;
        }
        if (!isFiredByPlayer && model.getPlayer1().getBounds().intersects(getBounds())) {
            //todo make it configurable
            model.getPlayer1().damageHealth(10);
            model.getBullets().remove(this);
            return;
        }
        for (GameObject obj : model.getEnemies()) {
            switch (obj.getType()) {
                case ENEMY1:
                case ENEMY2:
                    //todo make it configurable
                    if (isFiredByPlayer && obj.getBounds().intersects(getBounds())) {
                        ((Healthy) obj).damageHealth(10);
                        model.getBullets().remove(this);
                    }
            }
        }
    }
}
