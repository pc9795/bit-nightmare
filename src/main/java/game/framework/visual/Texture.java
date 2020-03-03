package game.framework.visual;

import game.objects.GameObject;
import game.utils.BufferedImageLoader;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 23-02-2020 18:21
 * Purpose: Represents texture for a game object
 * <p>
 * REF: https://www.youtube.com/watch?v=DnsKYv39VfI&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=12
 * Used mentioned video to see the basics of using Sprite Sheets. My implementation took this basic information to the
 * next level by using a configuration based approach where a texture is unique for a game object. I can just drop in
 * any sprite sheet or image in the configuration and things will reflect here because of parsing logic I designed.
 * The video was very limiting and depended on hard-coded values. I have created different properties such as
 * `idleRight`, `runningLeft` etc. So that now inside code instead of deciding from a list of images what to do with
 * each image, we have a list of images grouped for a specific purpose.
 **/
public class Texture {
    private GameObject.GameObjectType type;
    private BufferedImage[] idleRight;
    private BufferedImage[] idleLeft;
    private BufferedImage[] duckRight;
    private BufferedImage[] duckLeft;
    private BufferedImage[] deathLeft;
    private BufferedImage[] deathRight;
    private BufferedImage[] attackLeft;
    private BufferedImage[] attackRight;
    private BufferedImage[] runningLeft;
    private BufferedImage[] runningRight;
    private BufferedImage[] jumpLeft;
    private BufferedImage[] jumpRight;


    //Only Texture loader will create it.
    private Texture() {
        //Initializing with default values for flexible configuration. Else we have to null handle everywhere.
        idleLeft = new BufferedImage[0];
        idleRight = new BufferedImage[0];
        duckLeft = new BufferedImage[0];
        duckRight = new BufferedImage[0];
        deathLeft = new BufferedImage[0];
        deathRight = new BufferedImage[0];
        attackLeft = new BufferedImage[0];
        attackRight = new BufferedImage[0];
        runningLeft = new BufferedImage[0];
        runningRight = new BufferedImage[0];
        jumpLeft = new BufferedImage[0];
        jumpRight = new BufferedImage[0];
    }

    public GameObject.GameObjectType getType() {
        return type;
    }

    public BufferedImage[] getIdleRight() {
        return idleRight;
    }

    public BufferedImage[] getIdleLeft() {
        return idleLeft;
    }

    public BufferedImage[] getDuckRight() {
        return duckRight;
    }

    public BufferedImage[] getDuckLeft() {
        return duckLeft;
    }

    public BufferedImage[] getDeathLeft() {
        return deathLeft;
    }

    public BufferedImage[] getDeathRight() {
        return deathRight;
    }

    public BufferedImage[] getAttackLeft() {
        return attackLeft;
    }

    public BufferedImage[] getAttackRight() {
        return attackRight;
    }

    public BufferedImage[] getRunningLeft() {
        return runningLeft;
    }

    public BufferedImage[] getRunningRight() {
        return runningRight;
    }

    public BufferedImage[] getJumpLeft() {
        return jumpLeft;
    }

    public BufferedImage[] getJumpRight() {
        return jumpRight;
    }

    /**
     * Create a texture based on a configuration.
     *
     * @param config texture config
     * @return texture
     * @throws IOException if the images mentioned in the configuration are not present.
     */
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
        if (config.getIdleRight() != null) {
            texture.idleRight = getBufferedImagesFromImageConfig(config.getIdleRight(), spriteSheets);
        }
        if (config.getIdleLeft() != null) {
            texture.idleLeft = getBufferedImagesFromImageConfig(config.getIdleLeft(), spriteSheets);
        }

        //Load ducking images
        if (config.getDuckLeft() != null) {
            texture.duckLeft = getBufferedImagesFromImageConfig(config.getDuckLeft(), spriteSheets);
        }
        if (config.getDuckRight() != null) {
            texture.duckRight = getBufferedImagesFromImageConfig(config.getDuckRight(), spriteSheets);
        }

        //Load death images
        if (config.getDeathLeft() != null) {
            texture.deathLeft = getBufferedImagesFromImageConfig(config.getDeathLeft(), spriteSheets);
        }
        if (config.getDeathRight() != null) {
            texture.deathRight = getBufferedImagesFromImageConfig(config.getDeathRight(), spriteSheets);
        }

        //Load attack images
        if (config.getAttackRight() != null) {
            texture.attackRight = getBufferedImagesFromImageConfig(config.getAttackRight(), spriteSheets);
        }
        if (config.getAttackLeft() != null) {
            texture.attackLeft = getBufferedImagesFromImageConfig(config.getAttackLeft(), spriteSheets);
        }

        //Load running images
        if (config.getRunningLeft() != null) {
            texture.runningLeft = getBufferedImagesFromImageConfig(config.getRunningLeft(), spriteSheets);
        }
        if (config.getRunningRight() != null) {
            texture.runningRight = getBufferedImagesFromImageConfig(config.getRunningRight(), spriteSheets);
        }

        //Load jumping images
        if (config.getJumpLeft() != null) {
            texture.jumpLeft = getBufferedImagesFromImageConfig(config.getJumpLeft(), spriteSheets);
        }
        if (config.getJumpRight() != null) {
            texture.jumpRight = getBufferedImagesFromImageConfig(config.getJumpRight(), spriteSheets);
        }

        return texture;
    }

    /**
     * Get images from an ImageConfig
     *
     * @param imgConfigArr array of ImageConfig objects
     * @param spriteSheets array of sprite sheets mentioned in the configuration.
     * @return images in the given configuration.
     * @throws IOException if the mentioned images are not present.
     */
    private static BufferedImage[] getBufferedImagesFromImageConfig(TextureConfig.ImageConfig[] imgConfigArr,
                                                                    BufferedImage[] spriteSheets) throws IOException {
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        BufferedImage[] images = new BufferedImage[imgConfigArr.length];

        int i = 0;
        for (TextureConfig.ImageConfig imgConfig : imgConfigArr) {
            switch (imgConfig.getImageType()) {
                //Single images will be loaded
                case SINGLE:
                    images[i++] = imgLoader.loadImage(imgConfig.getImgLoc());
                    break;
                //In case of sprite sheets we get specific part from the sprite sheet.
                //Index is 1 based.
                case SPRITE_SHEET:
                    int x = (imgConfig.getX() - 1) * imgConfig.getWidth();
                    int y = (imgConfig.getY() - 1) * imgConfig.getHeight();
                    images[i++] = spriteSheets[imgConfig.getIndex()].getSubimage(x, y, imgConfig.getWidth(), imgConfig.getHeight());
                    break;
            }
        }
        return images;
    }
}
