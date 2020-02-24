package game.objects.enemies;

import game.framework.Model;
import game.framework.visual.Animator;
import game.objects.enemies.adapters.ShootingAndDuckingEnemyAdapter;
import game.objects.weapons.bullets.BitArrayGunBullet;
import game.physics.Point2f;

import java.awt.*;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: Attack player using bit array gun
 **/
public class SuperSoldier extends ShootingAndDuckingEnemyAdapter {
    //Constants
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private static final int DEFAULT_LOS = 600;
    private static final float DEFAULT_BULLET_FREQ_IN_SEC = 1.5f;
    private static final int DEFAULT_HEALTH = 100;
    private static final float DEFAULT_DUCKING_PROB = 0.002f;
    //Variables
    private Random random = new Random();
    private long lastFiredBullet = System.currentTimeMillis();
    private int los;
    private float bulletFreqInSec;
    private float duckingProb;

    public SuperSoldier(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.SUPER_SOLDIER);
        los = DEFAULT_LOS;
        bulletFreqInSec = DEFAULT_BULLET_FREQ_IN_SEC;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
        duckingProb = DEFAULT_DUCKING_PROB;
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
        if (!renderTexture(g)) {
            renderDefault(g);
        }
    }

    private boolean renderTexture(Graphics g) {
        Animator animator = getAnimatorAccordingToState();
        if (animator == null) {
            return false;
        }
        if (lastAnimator != null && lastAnimator != animator) {
            //Reset the last animator
            lastAnimator.reset();
            lastAnimator = animator;
        }
        //THIS PORTION IS TOO MUCH DEPENDENT ON TEXTURE USED.
        if (ducking) {
            animator.draw(g, (int) centre.getX(), (int) centre.getY() - height, width, height * 2);
            showHealth((int) centre.getX(), (int) centre.getY() - height, health, maxHealth, g);
        } else if (attacking || dead) {
            animator.draw(g, (int) (centre.getX() - .5 * width), (int) (centre.getY() - .3 * height), (int) (1.5 * width), (int) (1.3 * height));
            showHealth(centre, health, maxHealth, g);
        } else {
            animator.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);
            showHealth(centre, health, maxHealth, g);
        }
        return true;
    }

    private void renderDefault(Graphics g) {
        g.setColor(new Color(255, 127, 39));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, maxHealth, g);
    }

    public void attackPlayer(Model model) {
        int playerX = (int) model.getPlayer1().getCentre().getX();
        //Player is out of line of sight
        if (Math.abs(centre.getX() - playerX) > los) {
            return;
        }
        //Turn toward player
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
        BitArrayGunBullet bullet = new BitArrayGunBullet(bulletPos, false, facingDirection);
        model.getBullets().add(bullet);
        lastFiredBullet = now;
    }
}
