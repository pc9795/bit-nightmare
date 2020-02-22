package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;


/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:03
 * Purpose: TODO:
 **/
public class HidingBlock extends GameObject {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private static final int DEFAULT_HIDING_TIME_IN_SEC = 2;
    private boolean touchingPlayer;
    private long lastHiddenTime;
    private boolean hidden;
    private int hidingTimeInSec;

    public HidingBlock(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.HIDING_BLOCK);
        hidingTimeInSec = DEFAULT_HIDING_TIME_IN_SEC;
    }

    public void setTouchingPlayer(boolean touchingPlayer) {
        this.touchingPlayer = touchingPlayer;
    }

    @Override
    public void update() {
        if (touchingPlayer) {
            hidden = true;
            lastHiddenTime = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        if (hidden && (now - lastHiddenTime > hidingTimeInSec * 1000)) {
            hidden = false;
        }
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
    public void collision(Model model) {

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
