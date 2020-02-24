package game.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:31
 * Purpose: Helper to load images.
 **/
public final class BufferedImageLoader {
    private static final BufferedImageLoader INSTANCE = new BufferedImageLoader();

    private BufferedImageLoader() {
    }

    public static BufferedImageLoader getInstance() {
        return INSTANCE;
    }

    public BufferedImage loadImage(String path) throws IOException {
        URL url = BufferedImageLoader.class.getResource(path);
        if (url == null) {
            throw new RuntimeException(String.format("Image not found:%s", path));
        }
        return ImageIO.read(url);
    }

}
