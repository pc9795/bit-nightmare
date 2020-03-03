package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 00:02
 * Purpose: Represent a gate
 **/
public class Gate extends GameObject {
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private boolean opened;

    public Gate(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.GATE);
    }

    @Override
    public void update() {

    }


    @Override
    public void render(Graphics g) {
        //We expect two textures. First will be for closed and second for opened
        if (texture != null && texture.getIdleRight().length >= 2) {
            //THIS PORTION IS TOO MUCH DEPENDENT ON IMAGES USED.
            if (opened) {
                g.drawImage(texture.getIdleRight()[1], (int) centre.getX(), (int) centre.getY(), width, height, null);
            } else {
                g.drawImage(texture.getIdleRight()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
            }
        } else {
            g.setColor(new Color(88, 88, 88));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {

    }

    @Override
    public Rectangle getBounds() {
        if (opened) {
            return new Rectangle((int) centre.getX(), (int) centre.getY(), 0, 0);
        }
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }

    public void open() {
        opened = true;
    }
}
