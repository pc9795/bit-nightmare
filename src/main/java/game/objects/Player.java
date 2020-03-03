package game.objects;

import game.colliders.FineGrainedCollider;
import game.framework.Model;
import game.framework.visual.Animator;
import game.objects.environment.Gate;
import game.objects.environment.HidingBlock;
import game.objects.environment.movables.MovableBlock;
import game.properties.Weapon;
import game.physics.Point2f;
import game.properties.Animated;
import game.properties.Healthy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static game.utils.Constants.DEV_MODE;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 23:07
 * Purpose: Player of the game
 **/
public class Player extends GameObject implements FineGrainedCollider, Healthy, Animated {
    //Constants
    private static final int DEFAULT_WIDTH = 48;
    private static final int DEFAULT_HEIGHT = 64;
    private static final float DEFAULT_SPEED_X = 3f;
    private static final float DEFAULT_SPEED_Y = 5f;
    private static final float DEFAULT_BULLET_FREQ_IN_SEC = 1f;
    private static final int DEFAULT_HEALTH = 200;
    private static final int DEFAULT_LIVES = 10;
    //Variables
    private List<Weapon> weapons;
    private boolean ducking, attacking, bitBotFound, hasKey;
    private int health, maxHealth, currentWeaponIndex, lives;
    private transient long lastFiredBullet;
    private float speedX, speedY, bulletFreqInSec;
    private transient Animator idleLeft, idleRight, jumpLeft, jumpRight, runningLeft, runningRight, duckLeft, duckRight,
            attackLeft, attackRight;

    //Only for deserialization purpose
    public Player() {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, new Point2f(0, 0), GameObjectType.PLAYER);
    }

    public Player(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.PLAYER);
        gravity = DEFAULT_GRAVITY;
        falling = true;
        weapons = new ArrayList<>();
        currentWeaponIndex = -1;
        bulletFreqInSec = DEFAULT_BULLET_FREQ_IN_SEC;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
        speedX = DEFAULT_SPEED_X;
        speedY = DEFAULT_SPEED_Y;
        lastFiredBullet = System.currentTimeMillis();
        lives = DEFAULT_LIVES;
        setupAnimator();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isBitBotFound() {
        return bitBotFound;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<Weapon> weapons) {
        this.weapons = weapons;
    }

    public void setBitBotFound(boolean bitBotFound) {
        this.bitBotFound = bitBotFound;
    }

    public int getCurrentWeaponIndex() {
        return currentWeaponIndex;
    }

    public void setCurrentWeaponIndex(int currentWeaponIndex) {
        this.currentWeaponIndex = currentWeaponIndex;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseLives() {
        lives--;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + velocity.getX());
        centre.setY(centre.getY() + velocity.getY());
        //Gravity
        if (falling || jumping) {
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

        if (DEV_MODE) {
            //Debug for player position.
            g.setColor(Color.RED);
            g.setFont(new Font("Time New Roman", Font.BOLD, 20));
            g.drawString(String.format("X:%s,Y:%s", (int) centre.getX(), (int) centre.getY()), (int) centre.getX(), (int) centre.getY() - 20);
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
                if (ducking) {
                    animator = duckLeft;
                } else if (attacking) {
                    animator = attackLeft;
                } else if (velocity.getX() < 0) {
                    animator = runningLeft;
                } else if (jumping) {
                    animator = jumpLeft;
                } else {
                    animator = idleLeft;
                }
                break;
            case RIGHT:
                if (ducking) {
                    animator = duckRight;
                } else if (attacking) {
                    animator = attackRight;
                } else if (velocity.getX() > 0) {
                    animator = runningRight;
                } else if (jumping) {
                    animator = jumpRight;
                } else {
                    animator = idleRight;
                }
                break;
        }
        //THIS PORTION IS TOO MUCH DEPENDENT ON IMAGES USED.
        if (ducking) {
            //Image is according to full size even though we are dividing our height by 2.
            animator.draw(g, (int) centre.getX(), (int) centre.getY() - height, width, height * 2);
            showHealth((int) centre.getX(), (int) centre.getY() - height, health, maxHealth, g);
        } else {
            animator.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);
            showHealth(centre, health, maxHealth, g);
        }
    }

    /**
     * When there is no texture it will render a rectangle with a selected color for this object
     *
     * @param g graphics object
     */
    private void renderDefault(Graphics g) {
        g.setColor(new Color(0, 0, 255));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);
        g2d.draw(getBoundsBottom());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsTop());
    }

    @Override
    public void perceiveEnv(Model model) {
        collisionWithEnvironment(model);
        collisionWithCollectibles(model);
        collisionWithEnemies(model);
    }

    /**
     * Check collision with environment
     *
     * @param model game world
     */
    private void collisionWithEnvironment(Model model) {
        boolean[] collisions;
        boolean bottomCollision = false;

        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        willCollide.addAll(model.getMovableEnvironment());

        for (GameObject env : willCollide) {
            Rectangle bounds = env.getBounds();
            switch (env.getType()) {
                case GATE:
                    collisions = fineGrainedCollision(this, env);
                    if ((collisions[FineGrainedCollider.RIGHT] || collisions[FineGrainedCollider.LEFT]) && hasKey) {
                        ((Gate) env).open();
                    } else if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    }
                    break;
                case END_GAME:
                    if (bounds.intersects(getBounds())) {
                        model.setCompleted(true);
                    }
                    break;
                case CHANGE_LEVEL:
                    if (bounds.intersects(getBounds())) {
                        model.nextLevel();
                    }
                    break;
                case CHECKPOINT:
                    if (bounds.intersects(getBounds())) {
                        model.saveCheckpoint(env.getCentre().copy());
                    }
                    break;
                case LAVA:
                    // If you touch it, you will burn.
                    if (bounds.intersects(getBounds())) {
                        health = 0;
                    }
                    break;
                case BLOCK:
                case ENEMY_PORTAL:
                    collisions = fineGrainedCollision(this, env);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    }
                    break;
                case MOVABLE_BLOCK:
                    collisions = fineGrainedCollision(this, env);
                    if (collisions[FineGrainedCollider.LEFT] || collisions[FineGrainedCollider.RIGHT]) {
                        ((MovableBlock) env).setTouchingPlayer(true);
                    } else {
                        ((MovableBlock) env).setTouchingPlayer(false);
                    }
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                    }
                    break;
                case HIDING_BLOCK:
                    collisions = fineGrainedCollision(this, env);
                    if (collisions[FineGrainedCollider.BOTTOM]) {
                        bottomCollision = true;
                        ((HidingBlock) env).setTouchingPlayer(true);
                    } else {
                        ((HidingBlock) env).setTouchingPlayer(false);
                    }
                    break;
            }
        }
        if (!bottomCollision) {
            falling = true;
        }
    }

    /**
     * Collision with collectibles
     *
     * @param model game world
     */
    private void collisionWithCollectibles(Model model) {
        for (GameObject obj : model.getCollectibles()) {
            if (!obj.getBounds().intersects(getBounds())) {
                continue;
            }
            switch (obj.getType()) {
                case BIT_BOT:
                    bitBotFound = true;
                    break;
                case BIT_REVOLVER:
                case BIT_ARRAY_GUN:
                case BIT_MATRIX_BLAST:
                    addWeapon((Weapon) obj);
                    break;
                case KEY:
                    hasKey = true;
                    break;
            }
            //Collectibles are picked up so removed from the world.
            model.getCollectibles().remove(obj);
        }
    }

    /**
     * Collision with enemies
     *
     * @param model game world
     */
    private void collisionWithEnemies(Model model) {
        for (GameObject obj : model.getEnemies()) {
            switch (obj.type) {
                case GUARDIAN:
                case SOLDIER:
                case SUPER_SOLDIER:
                case CHARGER:
                    //Don't touch me
                    if (obj.getBounds().intersects(getBounds())) {
                        health = 0;
                    }
                    break;
            }
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

    /**
     * Cycle weapons.
     */
    public void cycleWeapon() {
        if (currentWeaponIndex == -1) {
            return;
        }
        currentWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
    }

    /**
     * Fire weapon
     *
     * @return a bullet as a result of firing weapon.
     */
    public GameObject fireWeapon() {
        attacking = true;
        if (currentWeaponIndex == -1) {
            return null;
        }
        //Rate limiter
        long now = System.currentTimeMillis();
        long diff = now - lastFiredBullet;
        if (diff <= bulletFreqInSec * 1000) {
            return null;
        }
        lastFiredBullet = now;
        Point2f bulletPos = centre.copy();
        if (facingDirection == FacingDirection.RIGHT) {
            bulletPos.setX(bulletPos.getX() + width);
        }
        return weapons.get(currentWeaponIndex).fire(bulletPos, facingDirection);
    }

    /**
     * Add a weapon
     *
     * @param weapon weapon
     */
    private void addWeapon(Weapon weapon) {
        weapons.add(weapon);
        currentWeaponIndex = weapons.size() - 1;
    }

    /**
     * Move right.
     */
    public void moveRight() {
        if (ducking) {
            //ADJUST ACCORDING TO DEFAULTS
            velocity.setX(speedX - 1);
        } else {
            velocity.setX(speedX);
        }
        facingDirection = FacingDirection.RIGHT;
        attacking = false;
    }

    /**
     * Move left
     */
    public void moveLeft() {
        if (ducking) {
            //ADJUST ACCORDING TO DEFAULTS
            velocity.setX(-speedX + 1f);
        } else {
            velocity.setX(-speedX);
        }

        facingDirection = FacingDirection.LEFT;
        attacking = false;
    }

    /**
     * Stand still.
     */
    public void makeIdle() {
        if (velocity.getX() > 0) {
            facingDirection = FacingDirection.RIGHT;
        } else if (velocity.getX() < 0) {
            facingDirection = FacingDirection.LEFT;
        }
        velocity.setX(0);
    }

    /**
     * Duck and Stand
     */
    public void toggleDuck() {
        if (ducking) {
            centre.setY(centre.getY() - height / 2);
            height *= 2;
            ducking = false;
        } else {
            centre.setY(centre.getY() + height / 2);
            height /= 2;
            ducking = true;
        }
    }

    /**
     * Jump
     */
    public void jump() {
        if (jumping || !isBitBotFound()) {
            return;
        }
        if (ducking) {
            //ADJUST ACCORDING TO DEFAULTS
            velocity.setY(-speedY + 1);
        } else {
            velocity.setY(-speedY);
        }
        jumping = true;
    }

    @Override
    public void damageHealth(int damage) {
        health -= damage;
    }

    @Override
    public void setupAnimator() {
        if (!isTextured()) {
            return;
        }
        //FRAME GAP IS DEPENDENT ON THE IMAGES USED
        idleRight = new Animator(10, true, texture.getIdleRight());
        idleLeft = new Animator(10, true, texture.getIdleLeft());
        jumpLeft = new Animator(10, true, texture.getJumpLeft());
        jumpRight = new Animator(10, true, texture.getJumpRight());
        runningLeft = new Animator(10, true, texture.getRunningLeft());
        runningRight = new Animator(10, true, texture.getRunningRight());
        duckLeft = new Animator(10, true, texture.getDuckLeft());
        duckRight = new Animator(10, true, texture.getDuckRight());
        attackLeft = new Animator(20, true, texture.getAttackLeft());
        attackRight = new Animator(20, true, texture.getAttackRight());
    }
}
