package game.objects;

import game.framework.Model;
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

    public Player(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.PLAYER);
        gravity = Constants.PLAYER_GRAVITY;
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

    @Override
    public void update() {
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
    }

    private void collisionWithEnvironment(Model model) {
        boolean bottomIntersection = false;
        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        for (GameObject env : willCollide) {
            Rectangle bounds = env.getBounds();
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
        if (!bottomIntersection) {
            falling = true;
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }

    private Rectangle getBoundsLeft() {
        return new Rectangle((int) centre.getX(), (int) centre.getY() + 10, 5, height - 20);
    }

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
        currentWeaponIndex = (currentWeaponIndex + 1) / weapons.size();
    }

    public void fireWeapon() {
        if (currentWeaponIndex == -1) {
            return;
        }
        weapons.get(currentWeaponIndex).fire();
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }
}
