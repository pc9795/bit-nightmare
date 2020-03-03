package game.framework.visual;

/**
 * Created By: Prashant Chaubey
 * Created On: 23-02-2020 18:25
 * Purpose: An object representing texture configuration
 **/
//We need public getter methods for jackson to work that's why suppressing the warnings.
@SuppressWarnings({"WeakerAccess", "unused"})
public class TextureConfig {
    private String type;
    private String[] spriteSheets;
    private ImageConfig[] idleRight;
    private ImageConfig[] idleLeft;
    private ImageConfig[] duckRight;
    private ImageConfig[] duckLeft;
    private ImageConfig[] deathLeft;
    private ImageConfig[] deathRight;
    private ImageConfig[] attackLeft;
    private ImageConfig[] attackRight;
    private ImageConfig[] runningLeft;
    private ImageConfig[] runningRight;
    private ImageConfig[] jumpLeft;
    private ImageConfig[] jumpRight;

    public String getType() {
        return type;
    }

    public String[] getSpriteSheets() {
        return spriteSheets;
    }

    public ImageConfig[] getIdleRight() {
        return idleRight;
    }

    public ImageConfig[] getIdleLeft() {
        return idleLeft;
    }

    public ImageConfig[] getDuckRight() {
        return duckRight;
    }

    public ImageConfig[] getDuckLeft() {
        return duckLeft;
    }

    public ImageConfig[] getDeathLeft() {
        return deathLeft;
    }

    public ImageConfig[] getDeathRight() {
        return deathRight;
    }

    public ImageConfig[] getAttackLeft() {
        return attackLeft;
    }

    public ImageConfig[] getAttackRight() {
        return attackRight;
    }

    public ImageConfig[] getRunningLeft() {
        return runningLeft;
    }

    public ImageConfig[] getRunningRight() {
        return runningRight;
    }

    public ImageConfig[] getJumpLeft() {
        return jumpLeft;
    }

    public ImageConfig[] getJumpRight() {
        return jumpRight;
    }

    public enum ImageType {
        SPRITE_SHEET, SINGLE
    }

    /**
     * Image configuration for a particular texture property such as `idealRight`.
     */
    public static class ImageConfig {
        //If the `ImageType` is SPRITE_SHEET then it will hold the index of the sprite sheet loaded in the main texture
        //configuration
        private int index;
        //Applicable for `ImageType` SPRITE_SHEET. Width of the sub image inside sprite sheet.
        private int width;
        //Applicable for `ImageType` SPRITE_SHEET. Height of the sub image inside sprite sheet.
        private int height;
        //Applicable for `ImageType` SPRITE_SHEET. x position of the sub image inside sprite sheet. 1-based index.
        private int x;
        //Applicable for `ImageType` SPRITE_SHEET. y position of the sub image inside sprite sheet. 1-based index.
        private int y;
        //Applicable for `ImageType` SINGLE. Location of the image.
        private String imgLoc;
        private ImageType imageType;

        public int getIndex() {
            return index;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public ImageType getImageType() {
            return imageType;
        }

        public String getImgLoc() {
            return imgLoc;
        }
    }
}
