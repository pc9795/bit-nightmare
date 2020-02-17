package game.framework;

import game.utils.Constants;
import game.utils.Utils;

import javax.swing.*;
import java.awt.*;

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


public class Game extends JFrame {
    private Model gameWorld;
    private View canvas;
    private int width, height;

    public Game(String title, int width, int height) {
        super(title);
        this.width = width;
        this.height = height;
    }

    private void init() {
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        gameWorld = new Model();
        canvas = new View(gameWorld);
        add(canvas);
        canvas.setBounds(0, 0, width, height);
        canvas.setBackground(new Color(255, 255, 255));
        canvas.setVisible(true);
        setVisible(true);
    }

    public void start() {
        init();
        while (true) {
            //swing has timer class to help us time this but I'm writing my own, you can of course use the timer, but I want to set FPS and display it
            int timeBetweenFrames = 1000 / Constants.TARGET_FPS;
            long frameCheck = System.currentTimeMillis() + (long) timeBetweenFrames;
            while (frameCheck > System.currentTimeMillis()) {
            }
            gameLoop();
            Utils.checkFrameRate(System.currentTimeMillis(), frameCheck, Constants.TARGET_FPS);
        }
    }

    private void gameLoop() {
        gameWorld.gameLogic();
        canvas.updateView();
    }
}


