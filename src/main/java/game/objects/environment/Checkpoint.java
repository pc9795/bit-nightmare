package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 14:26
 * Purpose: Game object representing a point where player can save its progress
 **/
public class Checkpoint extends GameObject {
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;

    public Checkpoint(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.CHECKPOINT);
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
            g.setColor(new Color(255, 236, 166));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {

    }
}
