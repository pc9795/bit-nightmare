package game.framework.visual;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 23-02-2020 18:26
 * Purpose: Class which generate animation from a set of images.
 * <p>
 * REF: https://www.youtube.com/watch?v=kzgNCEWUqUs&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=13
 * Used the mentioned video for basic concepts. My implementation is much refactored and I added a looping and non-looping
 * variation. Also I delegated the drawing responsibilities to the animator itself.
 **/
public class Animator {
    private int frameGap;
    private int index;
    private int count;
    private BufferedImage[] images;
    private boolean loop;

    public Animator(int frameGap, boolean loop, BufferedImage... images) {
        this.frameGap = frameGap;
        this.images = images;
        this.loop = loop;
    }

    /**
     * Run the animation
     */
    private void run() {
        //Skip some frames according to frame gap.
        if (index != frameGap) {
            index++;
            return;
        }
        index = 0;
        if (loop) {
            count = (count + 1) % images.length;
            return;
        }
        //If it is not looping animator then at the end always last image is shown.
        if (count == images.length - 1) {
            return;
        }
        count++;
    }

    /**
     * Reset the animation so that it can run from start. Important for non looping animations.
     */
    public void reset() {
        count = 0;
    }

    /**
     * Draw the image.
     *
     * @param g      graphics object
     * @param x      x position
     * @param y      y position
     * @param width  width of the image.
     * @param height height of the image.
     */
    public void draw(Graphics g, float x, float y, int width, int height) {
        run();
        g.drawImage(images[count], (int) x, (int) y, width, height, null);
    }
}
