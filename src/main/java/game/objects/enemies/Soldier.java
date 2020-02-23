package game.objects.enemies;

import game.colliders.EnemyCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.objects.weapons.bullets.BitRevolverBullet;
import game.physics.Point2f;
import game.properties.Healthy;

import java.awt.*;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: Attack player using bit revolver
 **/
public class Soldier extends GameObject implements EnemyCollider, Healthy, Enemy {
    //Constants
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 64;
    private static final int DEFAULT_LOS = 600;
    private static final float DEFAULT_BULLET_FREQ_IN_SEC = 1.5f;
    private static final int DEFAULT_HEALTH = 100;
    private static final float DEFAULT_DUCKING_PROB = 0.002f;
    //Variables
    private int health;
    private boolean ducking;
    private Random random = new Random();
    private long lastFiredBullet = System.currentTimeMillis();
    private int los;
    private float bulletFreqInSec;
    private int maxHealth;
    private float duckingProb;

    public Soldier(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.SOLDIER);
        gravity = DEFAULT_GRAVITY;
        falling = true;
        los = DEFAULT_LOS;
        bulletFreqInSec = DEFAULT_BULLET_FREQ_IN_SEC;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
        duckingProb = DEFAULT_DUCKING_PROB;
    }

    @Override
    public void update() {
        if (random.nextInt((int) (1 / duckingProb)) == 1) {
            toggleDuck();
        }
        //Movement
        centre.setY(centre.getY() + velocity.getY());
        //Gravity
        if (falling) {
            velocity.setY(velocity.getY() + gravity);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(236, 28, 36));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, maxHealth, g);
    }

    @Override
    public void perceiveEnv(Model model) {
        if (health <= 0) {
            model.getEnemies().remove(this);
        }
        //Detect player and attack
        attackPlayer(model);

        //Check collision
        falling = enemyCollision(this, model);
    }

    @Override
    public void attackPlayer(Model model) {
        int playerX = (int) model.getPlayer1().getCentre().getX();
        //Player is out of line of sight.
        if (Math.abs(centre.getX() - playerX) > los) {
            return;
        }
        //Turn toward the player
        if (playerX < centre.getX()) {
            facingDirection = FacingDirection.LEFT;
        } else {
            facingDirection = FacingDirection.RIGHT;
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
        BitRevolverBullet bullet = new BitRevolverBullet(bulletPos, false, facingDirection);
        model.getBullets().add(bullet);
        lastFiredBullet = now;
    }

    /**
     * toogle ducking state
     */
    private void toggleDuck() {
        if (ducking) {
            centre.setY(centre.getY() - (height / 2));
            height *= 2;
            ducking = false;
        } else {
            centre.setY(centre.getY() + (height / 2));
            height /= 2;
            ducking = true;
        }
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
