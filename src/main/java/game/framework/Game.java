package game.framework;

import game.framework.controllers.KeyboardController;
import game.framework.controllers.MouseController;
import game.utils.Constants;

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


public class Game extends JFrame implements Runnable {
    private Model gameWorld;
    private View canvas;
    private int width, height;
    private boolean running;

    public Game(String title, int width, int height) {
        super(title);
        this.width = width;
        this.height = height;
    }

    public void start() {
        try {
            gameWorld = new Model();

        } catch (Exception e) {
            System.out.println("Not able to load the game world.");
            e.printStackTrace();
            System.exit(1);
        }
        canvas = new View(gameWorld);
        // It is important that the preferred size is set on the GamePanel and not on the JFrame. If the application
        // size is set on the JFrame, some of the drawing area will be taken up by the frame and the drawing area will
        // be little smaller.
        //todo setPreferredSize vs setSize
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.WHITE);
        // We are handling repaint on our own.
        canvas.setIgnoreRepaint(true);
        //Registering controllers
        canvas.addKeyListener(KeyboardController.getInstance());
        canvas.addMouseListener(MouseController.getInstance());
        canvas.addMouseMotionListener(MouseController.getInstance());
        canvas.addMouseWheelListener(MouseController.getInstance());
        //todo getContentPane().add(..) vs add(..)
        getContentPane().add(canvas);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // We are handling repaint on our own.
        setIgnoreRepaint(true);
        setResizable(false);
        pack();
        setVisible(true);
        // Starting game thread
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        if (running) {
            return;
        }
        running = true;
        requestFocus();
        // Can shift to nano seconds if need more juice with frames.
        long lastTime = System.currentTimeMillis();
        double timeBetweenFrames = 1000 / Constants.TARGET_FPS;
        double delta = 0;
        // It is safe to use int for updates and frames as it is reset every second.
        // Updates should be ideally equal to the Target fps.
        int updates = 0;
        // It is actual no of frames rendered
        int frames = 0;
        // This variable is used for logging frame rate.
        long timer = lastTime;
        while (running) {
            long now = System.currentTimeMillis();
            delta += (now - lastTime) / timeBetweenFrames;
            lastTime = now;
            while (delta >= 1) {
                gameWorld.gameLogic();
                delta--;
                updates++;
            }
            frames++;
            canvas.updateView();
            // It will print the no of updates and frames every second.
            if (now - timer > 1000) {
                timer += 1000;
                System.out.println(String.format("Frames: %s, Updates: %s", frames, updates));
                frames = 0;
                updates = 0;
            }
        }
    }
}

