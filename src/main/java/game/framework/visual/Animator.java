package game.framework.visual;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created By: Prashant Chaubey
 * Created On: 23-02-2020 18:26
 * Purpose: TODO:
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

    private void run() {
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

    public void reset() {
        count = 0;
    }

    public void draw(Graphics g, float x, float y, int width, int height) {
        run();
        g.drawImage(images[count], (int) x, (int) y, width, height, null);
    }
}
