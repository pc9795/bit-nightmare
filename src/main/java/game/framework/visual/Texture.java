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
        //Initializing with default values for flexible configuration.
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

    private static BufferedImage[] getBufferedImagesFromImageConfig(TextureConfig.ImageConfig[] imgConfigArr,
                                                                    BufferedImage[] spriteSheets) throws IOException {
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        BufferedImage[] images = new BufferedImage[imgConfigArr.length];

        int i = 0;
        for (TextureConfig.ImageConfig imgConfig : imgConfigArr) {
            switch (imgConfig.getImageType()) {
                case SINGLE:
                    images[i++] = imgLoader.loadImage(imgConfig.getImgLoc());
                    break;
                case SPRITE_SHEET:
                    images[i++] = spriteSheets[imgConfig.getIndex()].
                            getSubimage((imgConfig.getX() - 1) * imgConfig.getWidth(), (imgConfig.getY() - 1) * imgConfig.getHeight(),
                                    imgConfig.getWidth(), imgConfig.getHeight());
                    break;
            }
        }
        return images;
    }
}
