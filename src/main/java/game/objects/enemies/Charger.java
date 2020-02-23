package game.objects.enemies;

import game.colliders.FineGrainedCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;
import game.properties.Healthy;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: Charges at player.
 **/
public class Charger extends GameObject implements FineGrainedCollider, Healthy, Enemy {
    //Constants
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final float DEFAULT_SPEED_X = 4f;
    private static final int DEFAULT_LOS = 300;
    private static final int DEFAULT_HEALTH = 100;
    //Variables
    private boolean playerDetected;
    private int health;
    private int los;
    private float speedX;
    private int maxHealth;

    public Charger(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.CHARGER);
        this.gravity = DEFAULT_GRAVITY;
        this.falling = true;
        this.speedX = DEFAULT_SPEED_X;
        this.los = DEFAULT_LOS;
        this.health = DEFAULT_HEALTH;
        this.maxHealth = DEFAULT_HEALTH;
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
        g.setColor(new Color(136, 9, 27));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, maxHealth, g);
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
                    collisions = fineGrainedCollision(this, env);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    } else if (collisions[FineGrainedCollider.LEFT]) {
                        facingDirection = FacingDirection.RIGHT;
                        velocity.setX(speedX);
                    } else if (collisions[FineGrainedCollider.RIGHT]) {
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
