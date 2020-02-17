import utils.Constants;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */
public class Viewer extends JPanel {
    private long currentAnimationTime;
    private Model gameworld;

    public Viewer(Model world) {
        this.gameworld = world;
    }

    void updateview() {
        this.repaint();
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        currentAnimationTime++; // runs animation time step

        //Draw player Game Object
        int x = (int) gameworld.getPlayer().getCentre().getX();
        int y = (int) gameworld.getPlayer().getCentre().getY();
        int width = gameworld.getPlayer().getWidth();
        int height = gameworld.getPlayer().getHeight();
        String texture = gameworld.getPlayer().getTexture();

        //Draw background
        drawBackground(g);

        //Draw player
        drawPlayer(x, y, width, height, texture, g);

        //Draw Bullets
        // change back
        gameworld.getBullets().forEach((temp) ->
                drawBullet((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), temp.getWidth(), temp.getHeight(), temp.getTexture(), g));

        //Draw Enemies
        gameworld.getEnemies().forEach((temp) ->
                drawEnemies((int) temp.getCentre().getX(), (int) temp.getCentre().getY(), temp.getWidth(), temp.getHeight(), temp.getTexture(), g));
    }

    private void drawEnemies(int x, int y, int width, int height, String texture, Graphics g) {
        //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
        File TextureToLoad = new File(texture);
        try {
            Image myImage = ImageIO.read(TextureToLoad);
            //The spirte is 32x32 pixel wide and 4 of them are placed together so we need to grab a different one each time
            //remember your training :-) computer science everything starts at 0 so 32 pixels gets us to 31
            int currentPositionInAnimation = ((int) (currentAnimationTime % 4) * 32); //slows down animation so every 10 frames we get another frame so every 100ms
            g.drawImage(myImage, x, y, x + width, y + width, currentPositionInAnimation, 0, currentPositionInAnimation + 31, 32, null);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void drawBackground(Graphics g) {
        //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
        File textureToLoad = new File(Constants.Sprite.BACKGROUND);
        try {
            Image myImage = ImageIO.read(textureToLoad);
            g.drawImage(myImage, 0, 0, 1000, 1000, 0, 0, 1000, 1000, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawBullet(int x, int y, int width, int height, String texture, Graphics g) {
        //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
        File TextureToLoad = new File(texture);
        try {
            Image myImage = ImageIO.read(TextureToLoad);
            //64 by 128
            g.drawImage(myImage, x, y, x + width, y + width, 0, 0, 63, 127, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void drawPlayer(int x, int y, int width, int height, String texture, Graphics g) {
        //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
        File TextureToLoad = new File(texture);
        try {
            Image myImage = ImageIO.read(TextureToLoad);
            //The spirte is 32x32 pixel wide and 4 of them are placed together so we need to grab a different one each time
            //remember your training :-) computer science everything starts at 0 so 32 pixels gets us to 31
            int currentPositionInAnimation = ((int) ((currentAnimationTime % 40) / 10)) * 32; //slows down animation so every 10 frames we get another frame so every 100ms
            g.drawImage(myImage, x, y, x + width, y + width, currentPositionInAnimation, 0, currentPositionInAnimation + 31, 32, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //Lighnting Png from https://opengameart.org/content/animated-spaceships  its 32x32 thats why I know to increament by 32 each time
        // Bullets from https://opengameart.org/forumtopic/tatermands-art
        // background image from https://www.needpix.com/photo/download/677346/space-stars-nebula-background-galaxy-universe-free-pictures-free-photos-free-images

    }


}

