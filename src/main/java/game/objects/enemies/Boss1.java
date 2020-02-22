package game.objects.enemies;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.FineGrainedCollider;
import game.objects.properties.Healthy;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.objects.weapons.bullets.BitMatrixBlastBullet;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:03
 * Purpose: TODO:
 **/
public class Boss1 extends GameObject implements Healthy, FineGrainedCollider {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 64;
    private int health = 100;
    private Random random = new Random();
    private long lastFiredBullet = System.currentTimeMillis();
    private long lastCharged = System.currentTimeMillis();
    private boolean charging = true;

    public Boss1(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BOSS1);
        gravity = Constants.GRAVITY;
        falling = true;
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        // 1 in 500 chance to charge
        //todo make configurable
        if (!charging && random.nextInt(500) == 1) {
            if (facingDirection == FacingDirection.RIGHT) {
                velocity.setX(Constants.Enemies.BOSS1_VELX);
            } else {
                velocity.setX(-Constants.Enemies.BOSS1_VELX);
            }
            charging = true;
            lastCharged = now;
        }
        if (charging && now - lastCharged > Constants.Enemies.BOSS1_CHARGE_DURATION_IN_SEC * 1000) {
            charging = false;
        }
        //Movement
        centre.setX(centre.getX() + velocity.getX());
        //Gravity
        centre.setY(centre.getY() + velocity.getY());
        if (falling) {
            velocity.setY(velocity.getY() + gravity);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 168, 243));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, g);
    }

    @Override
    public void collision(Model model) {

        if (health <= 0) {
            model.getEnemies().remove(this);
        }
        //Detect player and attack
        int playerX = (int) model.getPlayer1().getCentre().getX();
        if (Math.abs(centre.getX() - playerX) <= Constants.Enemies.BOSS1_LOS) {
            if (playerX < centre.getX()) {
                facingDirection = FacingDirection.LEFT;
            } else {
                facingDirection = FacingDirection.RIGHT;
            }
            if (!charging) {
                //Rate limiter; one bullet per second.
                long now = System.currentTimeMillis();
                long diff = now - lastFiredBullet;
                if (diff > Constants.Enemies.BOSS1_BULLET_FREQ_IN_SEC * 1000) {
                    if (random.nextBoolean()) {
                        BitMatrixBlastBullet bullet = new BitMatrixBlastBullet(Constants.Bullet.BIT_MATRIX_BLAST_WIDTH,
                                Constants.Bullet.BIT_MATRIX_BLAST_HEIGHT, centre.copy(), false);
                        bullet.setFacingDirection(facingDirection);
                        model.getBullets().add(bullet);
                    } else {
                        BitArrayGunBullet bullet = new BitArrayGunBullet(Constants.Bullet.BIT_ARRAY_GUN_WIDTH,
                                Constants.Bullet.BIT_ARRAY_GUN_HEIGHT,
                                new Point3f(centre.getX(), centre.getY(), centre.getBoundary()), false);
                        bullet.setFacingDirection(facingDirection);
                        model.getBullets().add(bullet);

                        bullet = new BitArrayGunBullet(Constants.Bullet.BIT_ARRAY_GUN_WIDTH, Constants.Bullet.BIT_ARRAY_GUN_HEIGHT,
                                new Point3f(centre.getX(), centre.getY() + (height / 2), centre.getBoundary()), false);
                        bullet.setFacingDirection(facingDirection);
                        model.getBullets().add(bullet);
                    }
                    lastFiredBullet = now;
                }
            }
        }

        boolean[] collisions;
        boolean bottomCollision = false;
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        //Combining all the environments as behavior is same
        willCollide.addAll(model.getMovableEnvironment());
        for (GameObject env : willCollide) {
            Rectangle bounds = env.getBounds();
            switch (env.getType()) {
                case LAVA:
                    // If you touch it, you will burn.
                    if (bounds.intersects(getBounds())) {
                        model.getEnemies().remove(this);
                    }
                    break;
                case BLOCK:
                case HIDING_BLOCK:
                    collisions = fineGrainedCollision(this, env);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    }
                    if (collisions[FineGrainedCollider.LEFT] || collisions[FineGrainedCollider.RIGHT]) {
                        velocity.setX(0);
                    }
                    break;
            }
        }
        if (!bottomCollision) {
            falling = true;
        }
    }

    //todo look for a way to make these hardcoded values configurable
    public Rectangle getBoundsLeft() {
        return new Rectangle((int) centre.getX(), (int) centre.getY() + 10, 5, height - 20);
    }

    //todo look for a way to make these hardcoded values configurable
    public Rectangle getBoundsRight() {
        return new Rectangle((int) (centre.getX() + width - 5), (int) (centre.getY() + 10), 5, height - 20);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) centre.getY(), width / 2, height / 2);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) (centre.getY() + height / 2), width / 2, height / 2);
    }

    @Override
    public void damageHealth(int damage) {
        health -= damage;
    }
}
