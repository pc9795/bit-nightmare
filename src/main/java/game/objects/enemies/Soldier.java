package game.objects.enemies;

import game.framework.Model;
import game.framework.visual.Animator;
import game.objects.Difficulty;
import game.objects.enemies.adapters.ShootingAndDuckingEnemyAdapter;
import game.objects.weapons.bullets.BitRevolverBullet;
import game.physics.Point2f;

import java.awt.*;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 00:01
 * Purpose: Attack player using bit revolver
 **/
public class Soldier extends ShootingAndDuckingEnemyAdapter {
    //Constants
    //ADJUST DIFFICULTY WHEN YOU CHANGE THE DEFAULTS
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private static final int DEFAULT_LOS = 600;
    private static final float DEFAULT_BULLET_INTERVAL_IN_SEC = 1.5f;
    private static final int DEFAULT_HEALTH = 100;
    private static final float DEFAULT_DUCKING_PROB = 0.002f;
    //Variables
    private Random random = new Random();
    private long lastFiredBullet;
    private int los;
    private float bulletIntervalInSec, duckingProb;
    private Animator lastAnimator;

    private Soldier(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.SOLDIER);
        los = DEFAULT_LOS;
        bulletIntervalInSec = DEFAULT_BULLET_INTERVAL_IN_SEC;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
        duckingProb = DEFAULT_DUCKING_PROB;
        lastFiredBullet = System.currentTimeMillis();
    }

    public Soldier(Point2f centre, Difficulty difficulty) {
        this(centre);
        //ADJUST IF YOU HAVE CHANGED THE DEFAULTS
        switch (difficulty) {
            case EASY:
                bulletIntervalInSec += 0.5f;
                //They can die from BitArrayGun in 2 shots. Assuming attack of 8/bullet, total 40.
                health -= 20;
                duckingProb /= 2;
                break;
            case HARD:
                los += 100;
                bulletIntervalInSec -= 0.5f;
                //They can die from BitArrayGun in 4 shots. Assuming attack of 8/bullet, total 40.
                health += 30;
                duckingProb *= 2;
                break;
        }
        maxHealth = health;
    }

    @Override
    public void update() {
        if (dead) {
            return;
        }
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
        if (isTextured()) {
            renderTexture(g);
        } else {
            renderDefault(g);
        }
    }

    /**
     * Render the texture for the given object
     *
     * @param g grpahics object
     */
    private void renderTexture(Graphics g) {
        Animator animator = getAnimatorAccordingToState();
        if (lastAnimator != null && lastAnimator != animator) {
            //Reset the last animator
            lastAnimator.reset();
            lastAnimator = animator;
        }
        //THIS PORTION IS TOO MUCH DEPENDENT ON TEXTURE USED.
        if (ducking) {
            animator.draw(g, (int) centre.getX(), (int) centre.getY() - height, width, height * 2);
            showHealth((int) centre.getX(), (int) centre.getY() - height, health, maxHealth, g);
        } else if (attacking) {
            animator.draw(g, (int) (centre.getX() - .5 * width), (int) (centre.getY() - .3 * height), (int) (1.5 * width), (int) (1.3 * height));
            showHealth(centre, health, maxHealth, g);
        } else {
            animator.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);
            showHealth(centre, health, maxHealth, g);
        }
    }

    /**
     * When there is no texture it will render a rectangle with a selected color for this object
     *
     * @param g graphics object
     */
    private void renderDefault(Graphics g) {
        g.setColor(new Color(236, 28, 36));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, maxHealth, g);
    }

    @Override
    public void attackPlayer(Model model) {
        int playerX = (int) model.getPlayer1().getCentre().getX();
        //Player is out of line of sight.
        if (Math.abs(centre.getX() - playerX) > los) {
            attacking = false;
            return;
        }
        attacking = true;
        //Turn toward the player
        if (playerX < centre.getX()) {
            facingDirection = FacingDirection.LEFT;
        } else {
            facingDirection = FacingDirection.RIGHT;
        }
        long now = System.currentTimeMillis();
        long diff = now - lastFiredBullet;
        //Rate limiter
        if (diff <= bulletIntervalInSec * 1000) {
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
}
