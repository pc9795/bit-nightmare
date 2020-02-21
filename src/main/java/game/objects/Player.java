package game.objects;

import game.framework.Model;
import game.framework.controllers.KeyboardController;
import game.objects.weapons.Weapon;
import game.physics.Point3f;
import game.utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 23:07
 * Purpose: TODO:
 **/
public class Player extends GameObject {
    //The weapon at the front is the primary weapon.
    private List<Weapon> weapons;
    private int currentWeaponIndex;
    private boolean ducking;
    private int health = 100;
    private boolean bitBotFound;
    private long lastFiredBullet = System.currentTimeMillis();

    public Player(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.PLAYER);
        //todo remove
        //this.centre = new Point3f(8000, 577, centre.getBoundary());
        gravity = Constants.GRAVITY;
        falling = true;
        weapons = new ArrayList<>();
        currentWeaponIndex = -1;
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

    @Override
    public void update() {
        //Movement
        centre.setX(centre.getX() + velocity.getX());
        //Gravity
        centre.setY(centre.getY() + velocity.getY());
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
        g.drawString(String.format("X:%s,Y:%s", (int) centre.getX(), (int) centre.getY()), (int) centre.getX(), (int) centre.getY());
    }

    @Override
    public void collision(Model model) {
        collisionWithEnvironment(model);
        collisionWithCollectibles(model);
    }

    private void collisionWithEnvironment(Model model) {
        //This is set currently by a block object.
        boolean bottomIntersection = false;
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        for (GameObject env : willCollide) {
            Rectangle bounds = env.getBounds();
            if (env.type != GameObjectType.BLOCK && !bounds.intersects(getBounds())) {
                continue;
            }
            // Changing level
            if (env.type == GameObjectType.CHANGE_LEVEL) {
                //todo change level logic
                continue;
            }
            //Ending the game
            if (env.type == GameObjectType.END_GAME) {
                //todo end game logic
                continue;
            }
            // Saving last checkpoint
            //todo can add additional equality check that same check point is not saved every time. not important though
            if (env.type == GameObjectType.CHECKPOINT) {
                model.setLastCheckpoint(env.getCentre().copy());
                continue;
            }
            // If you touch it you will burn.
            if (env.type == GameObjectType.LAVA) {
                centre = model.getLastCheckpoint().copy();
                health = 100;
                continue;
            }
            if (env.type == GameObjectType.BLOCK) {
                if (bounds.intersects(getBoundsBottom())) {
                    //Bottom collision
                    centre.setY(env.getCentre().getY() - height + 1);
                    getVelocity().setY(0);
                    falling = false;
                    jumping = false;
                    bottomIntersection = true;
                }
                if (bounds.intersects(getBoundsTop())) {
                    //Top collision
                    //todo make hardcoded value configurable
                    centre.setY(env.getCentre().getY() + env.getWidth() + 5);
                    velocity.setY(0);
                }
                if (bounds.intersects(getBoundsLeft())) {
                    //Left collision
                    centre.setX(env.getCentre().getX() + env.getWidth());
                }
                if (bounds.intersects(getBoundsRight())) {
                    //Right collision
                    centre.setX(env.getCentre().getX() - width);
                }
            }
        }
        if (!bottomIntersection) {
            falling = true;
        }
    }

    private void collisionWithMovableEnvironment(Model model) {
    }

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
            //I am using object equality maybe can check this later.
            model.getCollectibles().remove(obj);
        }
    }

    //todo look for a way to make these hardcoded values configurable
    private Rectangle getBoundsLeft() {
        return new Rectangle((int) centre.getX(), (int) centre.getY() + 10, 5, height - 20);
    }

    //todo look for a way to make these hardcoded values configurable
    private Rectangle getBoundsRight() {
        return new Rectangle((int) (centre.getX() + width - 5), (int) (centre.getY() + 10), 5, height - 20);
    }

    private Rectangle getBoundsTop() {
        return new Rectangle((int) (centre.getX() + (width / 2) - (width / 4)), (int) centre.getY(), width / 2, height / 2);
    }

    private Rectangle getBoundsBottom() {
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
        //Rate limiter; one bullet per second.
        long now = System.currentTimeMillis();
        long diff = now - lastFiredBullet;
        if (diff <= Constants.Bullet.BULLET_FREQ_IN_SEC * 1000) {
            return null;
        }
        lastFiredBullet = now;
        return weapons.get(currentWeaponIndex).fire(centre, facingDirection);
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
        currentWeaponIndex = weapons.size() - 1;
    }
}
