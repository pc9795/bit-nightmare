package game.framework.visual;

import game.objects.GameObject;
import game.utils.BufferedImageLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Created On: 23-02-2020 18:21
 * Purpose: Represents texture for a game object
 **/
public class Texture {
    private GameObject.GameObjectType type;
    private BufferedImage[] idle;

    private Texture() {

    }

    public GameObject.GameObjectType getType() {
        return type;
    }

    public BufferedImage[] getIdle() {
        return idle;
    }

    static Texture fromTextureConfig(TextureConfig config) throws IOException {
        Texture texture = new Texture();
        //Load game object type
        texture.type = GameObject.GameObjectType.valueOf(config.getType());
        //Load sprite sheets
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        String[] spriteSheetPaths = config.getSpriteSheets();
        BufferedImage[] spriteSheets = new BufferedImage[spriteSheetPaths.length];
        for (int i = 0; i < spriteSheetPaths.length; i++) {
            spriteSheets[i] = imgLoader.loadImage(spriteSheetPaths[i]);
        }
        //Load idle images
        texture.idle = getBufferedImagesFromImageConfig(config.getIdle(), spriteSheets);
        return texture;
    }

    private static BufferedImage[] getBufferedImagesFromImageConfig(TextureConfig.ImageConfig[] imgConfigArr,
                                                                    BufferedImage[] spriteSheets) throws IOException {
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        BufferedImage[] images = new BufferedImage[imgConfigArr.length];

        int i = 0;
        for (TextureConfig.ImageConfig imgConfig : imgConfigArr) {
            switch (imgConfig.getImageType()) {
                case SINGLE:
                    images[i] = imgLoader.loadImage(imgConfig.getImgLoc());
                    break;
                case SPRITE_SHEET:
                    images[i] = spriteSheets[imgConfig.getIndex()].
                            getSubimage((imgConfig.getX() - 1) * imgConfig.getWidth(), (imgConfig.getY() - 1) * imgConfig.getHeight(),
                                    imgConfig.getWidth(), imgConfig.getHeight());
            }
        }
        return images;
    }
}
