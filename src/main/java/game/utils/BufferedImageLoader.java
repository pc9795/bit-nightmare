package game.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 00:31
 * Purpose: Helper to load images.
 * <p>
 * REF: https://www.youtube.com/watch?v=1TFDOT1HiBo&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=11
 * Used the mentioned video for the implementation.
 **/
public final class BufferedImageLoader {
    private static final BufferedImageLoader INSTANCE = new BufferedImageLoader();

    private BufferedImageLoader() {
    }

    public static BufferedImageLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Load an image from the classpath
     *
     * @param path path of the image.
     * @return loaded image
     * @throws IOException if image is not found.
     */
    public BufferedImage loadImage(String path) throws IOException {
        URL url = BufferedImageLoader.class.getResource(path);
        if (url == null) {
            throw new RuntimeException(String.format("Image not found:%s", path));
        }
        return ImageIO.read(url);
    }

}
