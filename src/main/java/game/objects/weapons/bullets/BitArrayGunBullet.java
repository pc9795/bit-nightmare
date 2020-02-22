package game.objects.weapons.bullets;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.properties.Healthy;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 18:06
 * Purpose: TODO:
 **/
public class BitArrayGunBullet extends GameObject {
    boolean isFiredByPlayer;
    int count;

    public BitArrayGunBullet(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_ARRAY_GUN_BULLET);
        isFiredByPlayer = true;
        count = Constants.Bullet.BIT_ARRAY_GUN_COUNT;
    }

    public BitArrayGunBullet(int width, int height, Point3f centre, boolean isFiredByPlayer) {
        super(width, height, centre, GameObjectType.BIT_REVOLVER_BULLET);
        this.isFiredByPlayer = isFiredByPlayer;
        count = Constants.Bullet.BIT_ARRAY_GUN_COUNT;
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + (facingDirection == FacingDirection.RIGHT ? 1 : -1) * Constants.Bullet.BIT_ARRAY_GUN_VELOCITY);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        //2*width to leave some space after bullets
        for (int i = 0, x = (int) centre.getX(); i < count; i++, x += 2 * width) {
            g.fillRect(x, (int) centre.getY(), width, height);
        }
    }

    @Override
    public void collision(Model model) {
        if (count == 0) {
            model.getBullets().remove(this);
        }
        //Out of the frame
        if (getCentre().getX() < model.getLevelBoundary().getxMin()) {
            count--;
        }
        if (getCentre().getX() > model.getLevelBoundary().getxMax()) {
            centre.setX(centre.getX() + width);
            count--;
        }
        //Currently Block, Movable Blocks and Oscillating Blocks will remove the bullet
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        //Merging lists as same behavior
        willCollide.addAll(model.getMovableEnvironment());
        for (GameObject env : willCollide) {
            switch (env.getType()) {
                case BLOCK:
                case GATE:
                case LAVA:
                case HIDING_BLOCK:
                case MOVABLE_BLOCK:
                    if (env.getBounds().intersects(getBounds())) {
                        if (facingDirection == FacingDirection.RIGHT) {
                            centre.setX(centre.getX() + width);
                        }
                        count--;
                    }
            }
        }
        if (!isFiredByPlayer && model.getPlayer1().getBounds().intersects(getBounds())) {
            //todo make it configurable
            //todo remove 100 damage for testing only.
            model.getPlayer1().damageHealth(10);
            if (facingDirection == FacingDirection.RIGHT) {
                centre.setX(centre.getX() + width);
            }
            count--;
            return;
        }
        for (GameObject obj : model.getEnemies()) {
            switch (obj.getType()) {
                case ENEMY1:
                case ENEMY2:
                case ENEMY3:
                    //todo make it configurable
                    if (isFiredByPlayer && obj.getBounds().intersects(getBounds())) {
                        ((Healthy) obj).damageHealth(100);
                        if (facingDirection == FacingDirection.RIGHT) {
                            centre.setX(centre.getX() + width);
                        }
                        count--;
                    }
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width * (2 * Constants.Bullet.BIT_ARRAY_GUN_COUNT - 1), height);
    }
}
