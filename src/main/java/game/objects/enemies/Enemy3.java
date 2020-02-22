package game.objects.enemies;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.FineGrainedCollider;
import game.objects.properties.Healthy;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.physics.Point2f;
import game.utils.Constants;

import java.awt.*;
import java.util.List;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: TODO:
 **/
public class Enemy3 extends GameObject implements FineGrainedCollider, Healthy {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 64;
    private static final int DEFAULT_LOS = 600;
    private static final float DEFAULT_BULLET_FREQ_IN_SEC = 1.5f;
    private int health = 100;
    private boolean ducking;
    private Random random = new Random();
    private long lastFiredBullet = System.currentTimeMillis();
    private int los;
    private float bulletFreqInSec;

    public Enemy3(int width, int height, Point2f centre) {
        super(width, height, centre, GameObjectType.ENEMY3);
        gravity = DEFAULT_GRAVITY;
        falling = true;
        los = DEFAULT_LOS;
        bulletFreqInSec = DEFAULT_BULLET_FREQ_IN_SEC;
    }

    @Override
    public void update() {
        // 1 in 250 chance to duck fire.
        //todo make configurable
        if (random.nextInt(250) == 1) {
            if (ducking) {
                height *= 2;
                ducking = false;
            } else {
                centre.setY(centre.getY() + (height / 2));
                height /= 2;
                ducking = true;
            }
        }
        //Gravity
        centre.setY(centre.getY() + velocity.getY());
        if (falling) {
            velocity.setY(velocity.getY() + gravity);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(255, 127, 39));
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
        if (Math.abs(centre.getX() - playerX) <= los) {
            if (playerX < centre.getX()) {
                facingDirection = FacingDirection.LEFT;
            } else {
                facingDirection = FacingDirection.RIGHT;
            }
            //Rate limiter; one bullet per second.
            long now = System.currentTimeMillis();
            long diff = now - lastFiredBullet;
            if (diff > bulletFreqInSec * 1000) {
                BitArrayGunBullet bullet = new BitArrayGunBullet(Constants.Bullet.BIT_ARRAY_GUN_WIDTH,
                        Constants.Bullet.BIT_ARRAY_GUN_HEIGHT, centre.copy(), false);
                bullet.setFacingDirection(facingDirection);
                model.getBullets().add(bullet);
                lastFiredBullet = now;
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
