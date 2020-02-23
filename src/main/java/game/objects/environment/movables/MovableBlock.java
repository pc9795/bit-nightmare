package game.objects.environment.movables;

import game.colliders.FineGrainedCollider;
import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: Block which can be moved by player
 **/
public class MovableBlock extends GameObject implements FineGrainedCollider {
    //Constants
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final float DEFAULT_SPEED_X = 4f;
    private static final float DEFAULT_FRICTION = 0.1f;
    //Variables
    private float friction;
    private float speedX;
    private boolean touchingPlayer;

    public MovableBlock(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.MOVABLE_BLOCK);
        gravity = DEFAULT_GRAVITY;
        friction = DEFAULT_FRICTION;
        speedX = DEFAULT_SPEED_X;
    }


    public void setTouchingPlayer(boolean touchingPlayer) {
        this.touchingPlayer = touchingPlayer;
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
        //Apply friction
        if (velocity.getX() > 0) {
            velocity.setX(Math.max(0, velocity.getX() - friction));
        } else if (velocity.getX() < 0) {
            velocity.setX(Math.min(0, velocity.getX() + friction));
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(255, 202, 24));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void perceiveEnv(Model model) {
        //If touching player then move according to the player
        if (touchingPlayer) {
            velocity.setX(velocity.getX() + model.getPlayer1().getVelocity().getX() > 0 ? speedX : -speedX);
        }

        //todo implement LOS because when player is around only then this object may be moving.

        boolean[] collisions;
        boolean bottomCollision = false;
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        willCollide.addAll(model.getCollectibles());
        willCollide.addAll(model.getMovableEnvironment());
        willCollide.addAll(model.getEnemies());
        //Not taking account that this block can fall on a player.
        for (GameObject object : willCollide) {
            if (object == this) {
                continue;
            }
            switch (object.getType()) {
                case CHARGER:
                case GUARDIAN:
                case SOLDIER:
                case SUPER_SOLDIER:
                case MOVABLE_BLOCK:
                case BIT_BOT:
                case BLOCK:
                case GATE:
                case HIDING_BLOCK:
                case LAVA:
                case BIT_ARRAY_GUN:
                case BIT_MATRIX_BLAST:
                case BIT_REVOLVER:
                    collisions = fineGrainedCollision(this, object);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    }
                    break;
            }
        }
        if (!bottomCollision) {
            falling = true;
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
}
