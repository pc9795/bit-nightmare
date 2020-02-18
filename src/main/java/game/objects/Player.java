package game.objects;

import game.framework.Model;
import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 23:07
 * Purpose: TODO:
 **/
public class Player extends GameObject {
    public Player(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.PLAYER);
        gravity = .098f;
        falling = true;
    }

    @Override
    public void update() {
        //Moving
        centre.setX(centre.getX() + velocity.getX());
        centre.setY(centre.getY() + velocity.getY());

        //Setting direction
        if (velocity.getX() < 0) {
            facingDirection = FacingDirection.LEFT;
        } else if (velocity.getX() > 0) {
            facingDirection = FacingDirection.RIGHT;
        }

        //Gravity
        if (falling || jumping) {
            velocity.setY(velocity.getY() + gravity);
        }
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(0, 0, 255));
        g.fill3DRect((int) centre.getX(), (int) centre.getY(), width, height, true);
    }

    @Override
    public void collision(Model model) {

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
