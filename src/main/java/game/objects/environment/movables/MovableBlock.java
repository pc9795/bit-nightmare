package game.objects.environment.movables;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.colliders.FineGrainedCollider;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class MovableBlock extends GameObject implements FineGrainedCollider {
    private boolean touchingPlayer;
    private Point3f lastPosition;

    public MovableBlock(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.MOVABLE_BLOCK);
        gravity = Constants.GRAVITY;
        lastPosition = centre.copy();
    }

    public void setTouchingPlayer(boolean touchingPlayer) {
        this.touchingPlayer = touchingPlayer;
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
        velocity.setX(Math.max(0, velocity.getX() - (velocity.getX() > 0 ? 1 : -1) * Constants.MOVABLE_BLOCK_FRICTION));
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(255, 202, 24));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        //todo remove
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);
        g2d.draw(getBoundsBottom());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsTop());
    }

    @Override
    public void collision(Model model) {
        boolean[] collisions;
        boolean bottomCollision = false;
        //We can optimise by only frame rendering or checking that last position changed or not.
        if (touchingPlayer) {
            velocity.setX(velocity.getX() + model.getPlayer1().getVelocity().getX() > 0 ? Constants.MOVABLE_BLOCK_VELOCITY_X : -Constants.MOVABLE_BLOCK_VELOCITY_X);
        }
        for (GameObject object : model.getCollectibles()) {
            collisions = fineGrainedCollision(this, object);
            if (collisions[FineGrainedCollider.BOTTOM]) {
                bottomCollision = true;
            }
        }
        for (GameObject object : model.getMovableEnvironment()) {
            if (object == this) {
                continue;
            }
            collisions = fineGrainedCollision(this, object);
            if (collisions[FineGrainedCollider.BOTTOM]) {
                bottomCollision = true;
            }
        }
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        for (GameObject object : willCollide) {
            if (object.getType() == GameObjectType.CHECKPOINT || object.getType() == GameObjectType.CHANGE_LEVEL
                    || object.getType() == GameObjectType.ENEMY_PORTAL) {
                //The block will pass through these objects.
                continue;
            }
            collisions = fineGrainedCollision(this, object);
            if (collisions[FineGrainedCollider.BOTTOM]) {
                bottomCollision = true;
            }
        }
        for (GameObject object : model.getEnemies()) {
            collisions = fineGrainedCollision(this, object);
            if (collisions[FineGrainedCollider.BOTTOM]) {
                bottomCollision = true;
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
}
