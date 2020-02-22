package game.objects.enemies;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.FineGrainedCollider;
import game.objects.properties.Healthy;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:01
 * Purpose: TODO:
 **/
public class Enemy1 extends GameObject implements FineGrainedCollider, Healthy {
    private boolean playerDetected;
    private int health = 100;

    public Enemy1(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.ENEMY1);
        gravity = Constants.GRAVITY;
        falling = true;
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + velocity.getX());
        //Gravity
        centre.setY(centre.getY() + velocity.getY());
        if (falling) {
            velocity.setY(velocity.getY() + gravity);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(136, 9, 27));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        showHealth(centre, health, g);
    }

    @Override
    public void collision(Model model) {
        if (health <= 0) {
            model.getEnemies().remove(this);
        }

        //Detect player and attack
        if (!playerDetected) {
            int playerX = (int) model.getPlayer1().getCentre().getX();
            if (Math.abs(centre.getX() - playerX) <= Constants.Enemies.ENEMY1_LOS) {
                if (playerX < centre.getX()) {
                    facingDirection = FacingDirection.LEFT;
                    velocity.setX(-Constants.Enemies.ENEMY1_VELX);
                } else {
                    facingDirection = FacingDirection.RIGHT;
                    velocity.setX(Constants.Enemies.ENEMY1_VELX);
                }
                playerDetected = true;
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
                    } else if (collisions[FineGrainedCollider.LEFT]) {
                        facingDirection = FacingDirection.RIGHT;
                        velocity.setX(Constants.Enemies.ENEMY1_VELX);
                    } else if (collisions[FineGrainedCollider.RIGHT]) {
                        facingDirection = FacingDirection.LEFT;
                        velocity.setX(-Constants.Enemies.ENEMY1_VELX);
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
        return new Rectangle((int) centre.getX(), (int) centre.getY() + 5, 5, height - 10);
    }

    //todo look for a way to make these hardcoded values configurable
    public Rectangle getBoundsRight() {
        return new Rectangle((int) (centre.getX() + width - 5), (int) (centre.getY() + 5), 5, height - 10);
    }

    public Rectangle getBoundsTop() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) centre.getY(), width / 2, height / 2);
    }

    public Rectangle getBoundsBottom() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) (centre.getY() + height / 2), width / 2, height / 2);
    }

    public void damageHealth(int damage) {
        health -= damage;
    }
}
