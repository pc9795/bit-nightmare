package game.framework.narrator;

import java.awt.image.BufferedImage;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-02-2020 13:54
 * Purpose: TODO:
 **/
public class Story {
    private BufferedImage image;
    private String actor;
    private String text;
    private int imageLink;

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getActor() {
        return actor;
    }

    public String getText() {
        return text;
    }

    public int getImageLink() {
        return imageLink;
    }
}
