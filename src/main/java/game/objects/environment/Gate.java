package game.objects.environment;

import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class Gate extends GameObject {
    public Gate(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.GATE);
    }

    @Override
    public void update(List<GameObject> objects) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(88, 88, 88));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
