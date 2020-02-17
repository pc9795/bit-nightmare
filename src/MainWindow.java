import utils.Constants;
import utils.UnitTests;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
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


public class MainWindow {
    private static JFrame frame = new JFrame(Constants.GAME_NAME);
    //model
    private static Model gameWorld = new Model();
    //view
    private static Viewer canvas = new Viewer(gameWorld);
    //controller
    private static KeyListener controller = Controller.getInstance();
    private static boolean startGame = false;
    private JLabel backgroundImageForStartMenu;

    public MainWindow() {
        // todo you can customise this later and adapt it to change on size.
        // todo in the example i am following we are setting width and height in the canvas
        frame.setSize(1000, 1000);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.add(canvas);
        canvas.setBounds(0, 0, 1000, 1000);
        //white background  replaced by Space background but if you remove the background method this will draw a white screen
        canvas.setBackground(new Color(255, 255, 255));
        // this will become visible after you press the key.
        canvas.setVisible(false);

        // start button
        JButton startMenuButton = new JButton("Start Game");
        startMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startMenuButton.setVisible(false);
                backgroundImageForStartMenu.setVisible(false);
                canvas.setVisible(true);
                //adding the controller to the Canvas
                canvas.addKeyListener(controller);
                // making sure that the Canvas is in focus so keyboard input will be taking in .
                canvas.requestFocusInWindow();
                startGame = true;
            }
        });
        startMenuButton.setBounds(400, 500, 200, 40);

        //loading background image
        //should work okay on OSX and Linux but check if you have issues depending your eclipse install or if your running this without an IDE
        File backroundToLoad = new File(Constants.Sprite.INITIAL);
        try {
            BufferedImage myPicture = ImageIO.read(backroundToLoad);
            backgroundImageForStartMenu = new JLabel(new ImageIcon(myPicture));
            backgroundImageForStartMenu.setBounds(0, 0, 1000, 1000);
            frame.add(backgroundImageForStartMenu);

        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.add(startMenuButton);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //sets up environment
        new MainWindow();
        //not nice but remember we do just want to keep looping till the end.
        // this could be replaced by a thread but again we want to keep things simple
        while (true) {
            //swing has timer class to help us time this but I'm writing my own, you can of course use the timer, but I want to set FPS and display it
            int timeBetweenFrames = 1000 / Constants.TARGET_FPS;
            long frameCheck = System.currentTimeMillis() + (long) timeBetweenFrames;

            //wait till next time step
            while (frameCheck > System.currentTimeMillis()) {
            }

            if (startGame) {
                gameloop();
            }

            //UNIT test to see if framerate matches
            UnitTests.checkFrameRate(System.currentTimeMillis(), frameCheck, Constants.TARGET_FPS);

        }
    }

    //Basic Model-View-Controller pattern
    private static void gameloop() {
        // GAMELOOP

        // controller input  will happen on its own thread
        // So no need to call it explicitly

        // model update
        gameWorld.gameLogic();
        // view update

        canvas.updateview();

        // Both these calls could be setup as  a thread but we want to simplify the game logic for you.
        //score update
        frame.setTitle("Score =  " + gameWorld.getScore());


    }

}


