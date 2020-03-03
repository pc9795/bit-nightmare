package game.objects.enemies;

import game.colliders.FineGrainedCollider;
import game.framework.Model;
import game.framework.visual.Animator;
import game.properties.Animated;
import game.objects.Difficulty;
import game.objects.GameObject;
import game.physics.Point2f;
import game.properties.Enemy;
import game.properties.Healthy;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: Charges at player.
 **/
public class Charger extends GameObject implements FineGrainedCollider, Healthy, Enemy, Animated {
    //Constants
    //ADJUST DIFFICULTY WHEN YOU CHANGE THE DEFAULTS
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final float DEFAULT_SPEED_X = 4f;
    private static final int DEFAULT_LOS = 300;
    private static final int DEFAULT_HEALTH = 30;
    //Variables
    private boolean playerDetected;
    private int health, los, maxHealth;
    private float speedX;
    private Animator idleRight, idleLeft;

    private Charger(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.CHARGER);
        this.gravity = DEFAULT_GRAVITY;
        this.falling = true;
        this.speedX = DEFAULT_SPEED_X;
        this.los = DEFAULT_LOS;
        this.health = DEFAULT_HEALTH;
        this.maxHealth = DEFAULT_HEALTH;
        setupAnimator();
    }

    public Charger(Point2f centre, Difficulty difficulty) {
        this(centre);
        //ADJUST IF YOU HAVE CHANGED THE DEFAULTS
        switch (difficulty) {
            case EASY:
                this.speedX -= 1f;
                break;
            case HARD:
                this.los += 100;
                this.speedX += 1f;
                break;
        }
    }

    @Override
    public void setupAnimator() {
        if (!isTextured()) {
            return;
        }
        //FRAME GAP IS DEPENDENT ON THE IMAGES USED
        idleRight = new Animator(10, true, texture.getIdleRight());
        idleLeft = new Animator(10, true, texture.getIdleLeft());
    }

    @Override
    public void update() {
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

    /**
     * Render the texture for the given object
     *
     * @param g grpahics object
     */
    private void renderTexture(Graphics g) {
        Animator animator = null;
        switch (facingDirection) {
            case LEFT:
                animator = idleLeft;
                break;
            case RIGHT:
                animator = idleRight;
                break;
        }
        //THIS PORTION IS TOO MUCH DEPENDENT ON IMAGES USED.
        animator.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);
    }

    /**
     * When there is no texture it will render a rectangle with a selected color for this object
     *
     * @param g graphics object
     */
    private void renderDefault(Graphics g) {
        g.setColor(new Color(136, 9, 27));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void perceiveEnv(Model model) {
        //Cease to exist
        if (health <= 0) {
            model.getEnemies().remove(this);
            return;
        }

        //Detect player and attack
        attackPlayer(model);

        //Collisions
        boolean[] collisions;
        boolean bottomCollision = false;

        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
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
                case MOVABLE_BLOCK:
                case GATE:
                    collisions = fineGrainedCollision(this, env);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    } else if (collisions[FineGrainedCollider.LEFT]) {
                        //Turn the direction
                        facingDirection = FacingDirection.RIGHT;
                        velocity.setX(speedX);
                    } else if (collisions[FineGrainedCollider.RIGHT]) {
                        //Turn the direction
                        facingDirection = FacingDirection.LEFT;
                        velocity.setX(-speedX);
                    }
                    break;
            }
        }
        if (!bottomCollision) {
            falling = true;
        }
    }

    @Override
    public void attackPlayer(Model model) {
        //Player is already detected so it started charging.
        if (playerDetected) {
            return;
        }
        int playerX = (int) model.getPlayer1().getCentre().getX();
        //Player is out of the line of sight.
        if (Math.abs(centre.getX() - playerX) > los) {
            return;
        }
        //Turn toward the player and charge
        if (playerX < centre.getX()) {
            facingDirection = FacingDirection.LEFT;
            velocity.setX(-speedX);
        } else {
            facingDirection = FacingDirection.RIGHT;
            velocity.setX(speedX);
        }
        playerDetected = true;
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

    public void damageHealth(int damage) {
        health -= damage;
    }
}
