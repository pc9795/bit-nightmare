package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 02-03-2020 00:10
 * Purpose: Key for a gate
 **/
public class Key extends GameObject {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;

    public Key(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.KEY);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        if (texture != null && texture.getIdleRight().length != 0) {
            //THIS PORTION IS TOO MUCH DEPENDENT ON IMAGES USED.
            g.drawImage(texture.getIdleRight()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
        } else {
            g.setColor(new Color(0, 255, 0));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {

    }
}
