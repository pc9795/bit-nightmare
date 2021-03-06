package game.objects.weapons.bullets;

import game.colliders.BulletCollider;
import game.framework.Model;
import game.framework.visual.Animator;
import game.properties.Animated;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 21-02-2020 18:06
 * Purpose: Bit Array gun' bullet
 **/
public class BitArrayGunBullet extends GameObject implements BulletCollider, Animated {
    //Constants
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
    private static final int DEFAULT_COUNT = 5;
    private static final float DEFAULT_SPEED_X = 5f;
    private static final int DEFAULT_DAMAGE = 8;
    //Variables
    private boolean isFiredByPlayer;
    private int count, damage;
    private float speedX;
    private Animator idleRight, idleLeft;

    public BitArrayGunBullet(Point2f centre, boolean isFiredByPlayer, FacingDirection facingDirection) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_ARRAY_GUN_BULLET);
        //Shift when fired in left direction
        if (facingDirection == FacingDirection.LEFT) {
            this.centre.setX(this.centre.getX() - (2 * DEFAULT_COUNT - 1) * DEFAULT_WIDTH);
        }
        this.isFiredByPlayer = isFiredByPlayer;
        this.count = DEFAULT_COUNT;
        this.speedX = DEFAULT_SPEED_X;
        this.facingDirection = facingDirection;
        this.damage = DEFAULT_DAMAGE;
        setupAnimator();
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + (facingDirection == FacingDirection.RIGHT ? 1 : -1) * speedX);
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
        //2*width to leave some space after bullets
        if (texture != null && texture.getIdleRight().length != 0) {
            for (int i = 0, x = (int) centre.getX(); i < count; i++, x += 2 * width) {
                animator.draw(g, x, (int) centre.getY(), width, height);
            }
        }
    }

    /**
     * When there is no texture it will render a rectangle with a selected color for this object
     *
     * @param g graphics object
     */
    private void renderDefault(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        for (int i = 0, x = (int) centre.getX(); i < count; i++, x += 2 * width) {
            g.fillRect(x, (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {
        if (count == 0) {
            model.getBullets().remove(this);
            return;
        }

        //These bullets don't disappear immediately. Their count is decreased on collision.
        if (bulletEnvCollision(model, this) || bulletPlayerOrEnemyCollision(model, this, isFiredByPlayer, damage)) {
            if (facingDirection == FacingDirection.RIGHT) {
                centre.setX(centre.getX() + width);
            }
            count--;
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width * (2 * count - 1), height);
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
}
