package game.objects.environment;

import game.framework.Model;
import game.framework.visual.Animator;
import game.properties.Animated;
import game.objects.GameObject;
import game.physics.Point2f;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: Burn almost every thing.
 **/
public class Lava extends GameObject implements Animated {
    private static final int DEFAULT_WIDTH = 32;
    private static final int DEFAULT_HEIGHT = 32;
    private Animator idleRight;

    public Lava(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.LAVA);
        setupAnimator();
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        if (isTextured()) {
            renderTexture(g);
        } else {
            renderDefault(g);
        }
    }

    /**
     * Render the texture for the given object
     *
     * @param g grpahics object
     */
    private void renderTexture(Graphics g) {
        //THIS PORTION IS TOO MUCH DEPENDENT ON TEXTURE USED.
        idleRight.draw(g, (int) centre.getX(), (int) centre.getY(), width, height);

    }

    /**
     * When there is no texture it will render a rectangle with a selected color for this object
     *
     * @param g graphics object
     */
    private void renderDefault(Graphics g) {
        g.setColor(new Color(184, 61, 186));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }


    @Override
    public void perceiveEnv(Model model) {
    }

    @Override
    public void setupAnimator() {
        if (!isTextured()) {
            return;
        }
        //FRAME GAP IS DEPENDENT ON THE IMAGES USED
        idleRight = new Animator(10, true, texture.getIdleRight());
    }
}
