package game.framework.visual;

import game.objects.GameObject;

/**
 * Created By: Prashant Chaubey
 * Created On: 23-02-2020 18:25
 * Purpose: An object representing texture configuration
 **/
public class TextureConfig {
    private String type;
    private String[] spriteSheets;
    private ImageConfig[] idle;

    public String getType() {
        return type;
    }

    public String[] getSpriteSheets() {
        return spriteSheets;
    }

    public ImageConfig[] getIdle() {
        return idle;
    }

    public enum ImageType {
        SPRITE_SHEET, SINGLE
    }

    public static class ImageConfig {
        private int index;
        private int width;
        private int height;
        private int x;
        private int y;
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
