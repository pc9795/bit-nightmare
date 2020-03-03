package game.objects.enemies;

import game.framework.Model;
import game.framework.visual.Animator;
import game.objects.Difficulty;
import game.objects.enemies.adapters.ShootingEnemyAdapter;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.objects.weapons.bullets.BitMatrixBlastBullet;
import game.physics.Point2f;

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
public class Guardian extends ShootingEnemyAdapter {
    //Constants
    //ADJUST DIFFICULTY WHEN YOU CHANGE THE DEFAULTS
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private static final float DEFAULT_SPEED_X = 5f;
    private static final float DEFAULT_CHARGE_DURATION_IN_SEC = 2f;
    private static final float DEFAULT_BULLET_INTERVAL_IN_SEC = 1.5f;
    private static final int DEFAULT_LOS = 900;
    private static final float DEFAULT_CHARGING_PROB = 0.002f;
    private static final int DEFAULT_HEALTH = 700;
    //Variables
    private Random random = new Random();
    private long lastFiredBullet, lastCharged;
    private boolean charging = true;
    private int los;
    private float chargeDurationInSec, bulletIntervalInSec, speedX, chargingProb;
    private Animator runningLeft, runningRight, lastAnimator;

    private Guardian(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.GUARDIAN);
        gravity = DEFAULT_GRAVITY;
        los = DEFAULT_LOS;
        chargeDurationInSec = DEFAULT_CHARGE_DURATION_IN_SEC;
        bulletIntervalInSec = DEFAULT_BULLET_INTERVAL_IN_SEC;
        speedX = DEFAULT_SPEED_X;
        falling = true;
        chargingProb = DEFAULT_CHARGING_PROB;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
        lastCharged = System.currentTimeMillis();
        lastFiredBullet = lastCharged;
    }

    public Guardian(Point2f centre, Difficulty difficulty) {
        this(centre);
        //ADJUST IF YOU HAVE CHANGED THE DEFAULTS
        switch (difficulty) {
            case EASY:
                speedX -= 1f;
                chargeDurationInSec -= 0.5f;
                bulletIntervalInSec += 0.5f;
                chargingProb /= 2;
                health -= 200;
                break;
            case HARD:
                los += 300;
                speedX += 1f;
                chargeDurationInSec += 0.5f;
                bulletIntervalInSec -= 0.5f;
                chargingProb *= 2;
                health += 200;
                break;
        }
        maxHealth = health;
    }

    @Override
    public void setupAnimator() {
        if (!isTextured()) {
            return;
        }
        //Setup animators in parent
        super.setupAnimator();
        //FRAME GAP IS DEPENDENT ON THE IMAGES USED
        runningLeft = new Animator(DEFAULT_FRAME_GAP, true, texture.getRunningLeft());
        runningRight = new Animator(DEFAULT_FRAME_GAP, true, texture.getRunningRight());
    }

    @Override
    public void update() {
        if (dead) {
            return;
        }
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
        if (isTextured()) {
            renderTexture(g);
        } else {
            renderDefault(g);
        }
        showHealth(centre, health, maxHealth, g);
    }

    private void renderTexture(Graphics g) {
        Animator animator = getAnimatorAccordingToState();
        if (lastAnimator != null && lastAnimator != animator) {
            //Reset the last animator
            lastAnimator.reset();
            lastAnimator = animator;
        }
        //THIS PORTION IS TOO MUCH DEPENDENT ON TEXTURE USED.
        if (charging) {
            animator.draw(g, (int) (centre.getX() - .1 * width), (int) (centre.getY() - .1 * height), (int) (1.1 * width), (int) (1.1 * height));
        } else if (attacking) {
            animator.draw(g, (int) (centre.getX() - .5 * width), (int) (centre.getY() - .3 * height), (int) (1.5 * width), (int) (1.3 * height));
        } else {
            animator.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @SuppressWarnings("Duplicates")
    @Override
    protected Animator getAnimatorAccordingToState() {
        Animator animator = super.getAnimatorAccordingToState();
        switch (facingDirection) {
            case LEFT:
                if (!dead && charging) {
                    animator = runningLeft;
                }
                break;
            case RIGHT:
                if (!dead && charging) {
                    animator = runningRight;
                }
                break;
        }
        return animator;
    }

    private void renderDefault(Graphics g) {
        g.setColor(new Color(0, 168, 243));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
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
        //If Boss is charging then it will not fire bullets.
        if (charging) {
            return;
        }
        attacking = true;
        //Turn in player's direction
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
        //50-50 chance to use one of the two bullets.
        if (random.nextBoolean()) {
            BitMatrixBlastBullet bullet = new BitMatrixBlastBullet(bulletPos, false, facingDirection);
            model.getBullets().add(bullet);
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
}
