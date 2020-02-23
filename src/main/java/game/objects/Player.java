package game.objects;

import game.colliders.FineGrainedCollider;
import game.framework.Model;
import game.objects.weapons.Weapon;
import game.physics.Point2f;
import game.properties.Healthy;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 23:07
 * Purpose: Player of the game
 **/
public class Player extends GameObject implements FineGrainedCollider, Healthy {
    //Constants
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 64;
    private static final float DEFAULT_SPEED_X = 3f;
    private static final float DEFAULT_SPEED_Y = 5f;
    private static final float DEFAULT_BULLET_FREQ_IN_SEC = 1f;
    private static final int DEFAULT_HEALTH = 200;
    //Variables
    private List<Weapon> weapons;
    private int currentWeaponIndex;
    private boolean ducking;
    private int health;
    private boolean bitBotFound;
    private long lastFiredBullet = System.currentTimeMillis();
    private float bulletFreqInSec;
    private int maxHealth;

    Player(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.PLAYER);
        gravity = DEFAULT_GRAVITY;
        falling = true;
        weapons = new ArrayList<>();
        currentWeaponIndex = -1;
        bulletFreqInSec = DEFAULT_BULLET_FREQ_IN_SEC;
        health = DEFAULT_HEALTH;
        maxHealth = DEFAULT_HEALTH;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isDucking() {
        return ducking;
    }

    public void setDucking(boolean ducking) {
        this.ducking = ducking;
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
        g.setColor(new Color(0, 0, 255));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);

        //todo remove
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.RED);
        g2d.draw(getBoundsBottom());
        g2d.draw(getBoundsLeft());
        g2d.draw(getBoundsRight());
        g2d.draw(getBoundsTop());
        g.drawString(String.format("X:%s,Y:%s", (int) centre.getX(), (int) centre.getY()), (int) centre.getX(), (int) centre.getY() - 20);

        showHealth(centre, health, maxHealth, g);
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
        for (GameObject env : willCollide) {
            Rectangle bounds = env.getBounds();
            switch (env.type) {
                case CHECKPOINT:
                    if (bounds.intersects(getBounds())) {
                        model.setLastCheckpoint(env.getCentre().copy());
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
            }
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

    public void cycleWeapon() {
        if (currentWeaponIndex == -1) {
            return;
        }
        currentWeaponIndex = (currentWeaponIndex + 1) % weapons.size();
    }

    public GameObject fireWeapon() {
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
        return weapons.get(currentWeaponIndex).fire(centre, facingDirection);
    }

    private void addWeapon(Weapon weapon) {
        weapons.add(weapon);
        currentWeaponIndex = weapons.size() - 1;
    }

    @Override
    public void damageHealth(int damage) {
        health -= damage;
    }
}
