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
 * Purpose: Bit revolver's bullet
 **/
public class BitRevolverBullet extends GameObject implements BulletCollider, Animated {
    //Constants
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
    private static final float DEFAULT_SPEED_X = 5f;
    private static final int DEFAULT_DAMAGE = 20;
    //Variables
    private float speedX;
    private boolean isFiredByPlayer;
    private int damage;
    private Animator idleRight, idleLeft;

    public BitRevolverBullet(Point2f centre, boolean isFiredByPlayer, FacingDirection facingDirection) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.BIT_REVOLVER_BULLET);
        this.isFiredByPlayer = isFiredByPlayer;
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
        animator.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);
    }

    /**
     * When there is no texture it will render a rectangle with a selected color for this object
     *
     * @param g graphics object
     */
    private void renderDefault(Graphics g) {
        g.setColor(new Color(182, 3, 253));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }


    @Override
    public void perceiveEnv(Model model) {
        if (bulletEnvCollision(model, this) || bulletPlayerOrEnemyCollision(model, this, isFiredByPlayer, damage)) {
            model.getBullets().remove(this);
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
}
