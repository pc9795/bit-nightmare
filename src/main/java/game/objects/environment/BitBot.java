package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;

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
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(195, 195, 195));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {

    }
}
