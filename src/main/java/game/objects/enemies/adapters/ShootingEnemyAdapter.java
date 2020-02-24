package game.objects.enemies.adapters;

import game.colliders.EnemyCollider;
import game.framework.Model;
import game.framework.visual.Animator;
import game.objects.GameObject;
import game.objects.enemies.Enemy;
import game.physics.Point2f;
import game.properties.Healthy;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 24-02-2020 01:26
 * Purpose: TODO:
 **/
public class ShootingEnemyAdapter extends GameObject implements Enemy, EnemyCollider, Healthy {
    protected Animator leftIdle, rightIdle, lastAnimator, leftAttack, rightAttack, leftDeath, rightDeath;
    protected boolean attacking, dead;
    protected int health, maxHealth;
    long deathTime;

    public ShootingEnemyAdapter(int width, int height, Point2f centre, GameObjectType type) {
        super(width, height, centre, type);
        gravity = DEFAULT_GRAVITY;
        falling = true;
        setupAnimator();
    }

    protected void setupAnimator() {
        if (texture == null) {
            return;
        }
        //Value of frame gap depends on sprites so it will change according to images.
        if (texture.getIdleRight().length != 0) {
            rightIdle = new Animator(20, true, texture.getIdleRight());
        }
        if (texture.getIdleLeft().length != 0) {
            leftIdle = new Animator(20, true, texture.getIdleLeft());
        }
        if (texture.getAttackLeft().length != 0) {
            leftAttack = new Animator(20, true, texture.getAttackLeft());
        }
        if (texture.getAttackRight().length != 0) {
            rightAttack = new Animator(20, true, texture.getAttackRight());
        }
        if (texture.getDeathLeft().length != 0) {
            leftDeath = new Animator(20, false, texture.getDeathLeft());
        }
        if (texture.getDeathRight().length != 0) {
            rightDeath = new Animator(20, false, texture.getDeathRight());
        }
    }

    protected Animator getAnimatorAccordingToState() {
        Animator animator = null;
        switch (facingDirection) {
            case LEFT:
                if (dead) {
                    animator = leftDeath;
                } else if (attacking) {
                    animator = leftAttack;
                } else {
                    animator = leftIdle;
                }
                break;
            case RIGHT:
                if (dead) {
                    animator = rightDeath;
                } else if (attacking) {
                    animator = rightAttack;
                } else {
                    animator = rightIdle;
                }
                break;
        }
        return animator;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public void perceiveEnv(Model model) {
        if (health <= 0) {
            if (!dead) {
                //Death animation should start from standing position
                dead = true;
                deathTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - deathTime > CORPSE_REMOVAL_PERIOD_IN_SEC * 1000) {
                model.getEnemies().remove(this);
            }
            return;
        }
        //Detect player and attack
        attackPlayer(model);

        //Check collision
        falling = enemyCollision(this, model);
    }

    @Override
    public void attackPlayer(Model model) {

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
        health = Math.max(0, health - damage);
    }
}
