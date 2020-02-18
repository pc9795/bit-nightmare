package game.objects;

import game.physics.Point3f;

import java.awt.*;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:00
 * Purpose: TODO:
 **/
public class BitBot extends GameObject {
    public BitBot(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.BIT_BOT);
    }

    @Override
    public void update(List<GameObject> objects) {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(195, 195, 195));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }
}
