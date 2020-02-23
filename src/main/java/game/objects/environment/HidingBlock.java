package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;


/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:03
 * Purpose: A block which disappears if player set his foot on it.
 **/
public class HidingBlock extends GameObject {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final int DEFAULT_HIDING_TIME_IN_SEC = 2;
    private long lastHiddenTime;
    private boolean hidden;
    private int hidingTimeInSec;

    public HidingBlock(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.HIDING_BLOCK);
        hidingTimeInSec = DEFAULT_HIDING_TIME_IN_SEC;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(185, 122, 86));
        if (hidden) {
            g.fillRect((int) centre.getX(), (int) centre.getY(), 0, 0);
        } else {
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {
        if (getBounds().intersects(model.getPlayer1().getBounds())) {
            hidden = true;
            lastHiddenTime = System.currentTimeMillis();
        } else if (hidden && (System.currentTimeMillis() - lastHiddenTime > hidingTimeInSec * 1000)) {
            hidden = false;
        }
    }

    @Override
    public Rectangle getBounds() {
        if (hidden) {
            return new Rectangle((int) centre.getX(), (int) centre.getY(), 0, 0);
        } else {
            return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }
}
