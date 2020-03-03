package game.objects.enemies.adapters;

import game.colliders.EnemyCollider;
import game.framework.Model;
import game.framework.visual.Animator;
import game.properties.Animated;
import game.objects.GameObject;
import game.objects.GameObjectFactory;
import game.properties.Enemy;
import game.physics.Point2f;
import game.properties.Healthy;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 24-02-2020 01:26
 * Purpose: This object will shoot
 **/
public class ShootingEnemyAdapter extends GameObject implements Enemy, EnemyCollider, Healthy, Animated {
    private Animator idleLeft, idleRight, attackLeft, attackRight, deathLeft, deathRight;
    protected boolean attacking, dead;
    protected int health, maxHealth;
    long deathTime;

    public ShootingEnemyAdapter(int width, int height, Point2f centre, GameObjectType type) {
        super(width, height, centre, type);
        gravity = DEFAULT_GRAVITY;
        falling = true;
        setupAnimator();
    }

    @Override
    public void setupAnimator() {
        if (!isTextured()) {
            return;
        }
        //FRAME GAP IS DEPENDENT ON THE IMAGES USED
        idleRight = new Animator(20, true, texture.getIdleRight());
        idleLeft = new Animator(20, true, texture.getIdleLeft());
        attackLeft = new Animator(20, true, texture.getAttackLeft());
        attackRight = new Animator(20, true, texture.getAttackRight());
        deathLeft = new Animator(20, false, texture.getDeathLeft());
        deathRight = new Animator(20, false, texture.getDeathRight());
    }

    /**
     * @return animator according to object's state.
     */
    protected Animator getAnimatorAccordingToState() {
        Animator animator = null;
        switch (facingDirection) {
            case LEFT:
                if (dead) {
                    animator = deathLeft;
                } else if (attacking) {
                    animator = attackLeft;
                } else {
                    animator = idleLeft;
                }
                break;
            case RIGHT:
                if (dead) {
                    animator = deathRight;
                } else if (attacking) {
                    animator = attackRight;
                } else {
                    animator = idleRight;
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
                //Death of a guardian will generate a key.
                model.getCollectibles().add(GameObjectFactory.getGameObject(GameObjectType.KEY, centre.copy(), model.getDifficulty()));
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
