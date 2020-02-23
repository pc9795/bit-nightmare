package game.objects.enemies;

import game.colliders.EnemyCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.objects.weapons.bullets.BitMatrixBlastBullet;
import game.physics.Point2f;
import game.properties.Healthy;

import java.awt.*;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:03
 * Purpose: Boss enemy
 * Attacks:
 * 1. Charges at player
 * 2. Fires 2 kinds of bullets.
 **/
public class Guardian extends GameObject implements Healthy, EnemyCollider, Enemy {
    //Constants
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 64;
    private static final float DEFAULT_SPEED_X = 5f;
    private static final float DEFAULT_CHARGE_DURATION_IN_SEC = 2f;
    private static final float DEFAULT_BULLET_FREQ_IN_SEC = 1.5f;
    private static final int DEFAULT_LOS = 500;
    private static final float DEFAULT_CHARGING_PROB = 0.002f;
    private static final int DEFAULT_HEALTH = 100;
    //Variables
    private int health;
    private Random random = new Random();
    private long lastFiredBullet = System.currentTimeMillis();
    private long lastCharged = System.currentTimeMillis();
    private boolean charging = true;
    private int los;
    private float chargeDurationInSec;
    private float bulletFreqInSec;
    private float speedX;
    private float chargingProb;
    private int maxHealth;

    public Guardian(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.GUARDIAN);
        gravity = DEFAULT_GRAVITY;
        los = DEFAULT_LOS;
        chargeDurationInSec = DEFAULT_CHARGE_DURATION_IN_SEC;
        bulletFreqInSec = DEFAULT_BULLET_FREQ_IN_SEC;
        speedX = DEFAULT_SPEED_X;
        falling = true;
        chargingProb = DEFAULT_CHARGING_PROB;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
    }

    @Override
    public void update() {
        long now = System.currentTimeMillis();
        //Charging attack
        if (!charging && random.nextInt((int) (1 / chargingProb)) == 1) {
            if (facingDirection == FacingDirection.RIGHT) {
                velocity.setX(speedX);
            } else if (facingDirection == FacingDirection.LEFT) {
                velocity.setX(-speedX);
            }
            charging = true;
            lastCharged = now;
        }
        //Stop charging
        if (charging && now - lastCharged > chargeDurationInSec * 1000) {
            charging = false;
            velocity.setX(0);
        }
        //Movement
        centre.setX(centre.getX() + velocity.getX());
        centre.setY(centre.getY() + velocity.getY());
        //Gravity
        if (falling) {
            velocity.setY(velocity.getY() + gravity);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 168, 243));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, maxHealth, g);
    }

    @Override
    public void perceiveEnv(Model model) {
        //Die
        if (health <= 0) {
            model.getEnemies().remove(this);
            return;
        }
        //Detect player and attack
        attackPlayer(model);
        //Check collision
        falling = enemyCollision(this, model);
    }

    /**
     * Attack the player
     *
     * @param model game world
     */
    public void attackPlayer(Model model) {
        int playerX = (int) model.getPlayer1().getCentre().getX();
        //Player is not in line of sight.
        if (Math.abs(centre.getX() - playerX) > los) {
            return;
        }
        //Turn in player's direction
        if (playerX < centre.getX()) {
            facingDirection = FacingDirection.LEFT;
        } else {
            facingDirection = FacingDirection.RIGHT;
        }
        //If Boss is charging then it will not fire bullets.
        if (charging) {
            return;
        }
        long now = System.currentTimeMillis();
        long diff = now - lastFiredBullet;
        //Rate limiter
        if (diff <= bulletFreqInSec * 1000) {
            return;
        }
        //In case of right facing shift the bullet position by width
        Point2f bulletPos = centre.copy();
        if (facingDirection == FacingDirection.RIGHT) {
            bulletPos.setX(bulletPos.getX() + width);
        }
        //50-50 chance to use one of the two bullets.
        if (random.nextBoolean()) {
            BitMatrixBlastBullet bullet = new BitMatrixBlastBullet(bulletPos, false, facingDirection);
            model.getBullets().add(bullet);
            return;
        } else {
            //Assuming boss' height is same as player so need to change if height changes.
            BitArrayGunBullet bullet = new BitArrayGunBullet(bulletPos, false, facingDirection);
            model.getBullets().add(bullet);

            bulletPos = bulletPos.copy();
            bulletPos.setY(bulletPos.getY() + height / 2);
            bullet = new BitArrayGunBullet(bulletPos, false, facingDirection);
            model.getBullets().add(bullet);
        }
        lastFiredBullet = now;
    }

    @Override
    public Rectangle getBoundsLeft() {
        return new Rectangle((int) centre.getX(), (int) centre.getY() + height / 8, width / 4, 3 * (height / 4));
    }

    @Override
    public Rectangle getBoundsRight() {
        return new Rectangle((int) (centre.getX() + 3 * (width / 4)), (int) (centre.getY() + height / 8), width / 4, 3 * (height / 4));
    }

    @Override
    public Rectangle getBoundsTop() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) centre.getY(), width / 2, height / 2);
    }

    @Override
    public Rectangle getBoundsBottom() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) (centre.getY() + height / 2), width / 2, height / 2);
    }

    @Override
    public void damageHealth(int damage) {
        health -= damage;
    }
}
