package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point3f;

import java.awt.*;

import static game.utils.Constants.HIDING_BLOCK_HIDING_TIME_IN_SEC;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:03
 * Purpose: TODO:
 **/
public class HidingBlock extends GameObject {
    private boolean touchingPlayer;
    private long lastHiddenTime;
    private boolean hidden;

    public HidingBlock(int width, int height, Point3f centre) {
        super(width, height, centre, GameObjectType.HIDING_BLOCK);
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
        if (hidden && (now - lastHiddenTime > HIDING_BLOCK_HIDING_TIME_IN_SEC * 1000)) {
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