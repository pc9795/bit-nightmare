package game.framework;

import game.framework.audio.JukeBox;
import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.framework.controllers.MouseController;
import game.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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


public class Game extends JFrame implements Runnable {
    private Model gameWorld;
    private View canvas;
    private int width, height;
    private boolean running;
    private Thread gameThread, gamepadControllerThread;

    public Game(String title, int width, int height) {
        super(title);
        this.width = width;
        this.height = height;
    }

    public void start() {
        try {
            gameWorld = new Model(width, height);
            registerFonts();
            canvas = new View(gameWorld);

        } catch (Exception e) {
            System.out.println("Not able to load the game.");
            e.printStackTrace();
            System.exit(1);
        }

        // Configuring view
        // It is important that the preferred size is set on the GamePanel and not on the JFrame. If the application
        // size is set on the JFrame, some of the drawing area will be taken up by the frame and the drawing area will
        // be little smaller.
        canvas.setPreferredSize(new Dimension(width, height));
        canvas.setBackground(Color.BLACK);
        // We are handling repaint on our own.
        canvas.setIgnoreRepaint(true);

        //Registering controllers
        canvas.addKeyListener(KeyboardController.getInstance());
        canvas.addMouseListener(MouseController.getInstance());
        canvas.addMouseMotionListener(MouseController.getInstance());
        canvas.addMouseWheelListener(MouseController.getInstance());
        GamepadController.getInstance().setDetecting(true);
        gamepadControllerThread = new Thread(GamepadController.getInstance());
        gamepadControllerThread.start();

        //Configuring game window
        getContentPane().add(canvas);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // We are handling repaint on our own.
        setIgnoreRepaint(true);
        setResizable(false);
        pack();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
        setVisible(true);

        running = true;
        //REF: https://books.google.ie/books/about/Fundamental_2D_Game_Programming_with_Jav.html?id=iRFvCgAAQBAJ&redir_esc=y
        //The idea of starting rendering in a separate thread is taken from the chapter 1 of this book.
        // Starting game thread
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * REF: https://www.youtube.com/watch?v=Zh7YiiEuJFw&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=2
     * I have used the mentioned video to design my game loop.
     */
    @Override
    public void run() {
        canvas.requestFocus();
        JukeBox.getInstance().playTheme();

        // Can shift to nano seconds if need more accuracy with frames.
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
                if (updates < Constants.TARGET_FPS - 10) {
                    System.out.println(String.format("!!!FPS Miss!!!: Frames: %s, Updates: %s", frames, updates));
                }
                frames = 0;
                updates = 0;
            }
        }
    }

    private void onWindowClosing() {
        try {
            //Waiting  for controller thread to finish.
            GamepadController.getInstance().setDetecting(false);
            if (gamepadControllerThread != null) {
                gamepadControllerThread.join();
            }
            //Waiting for the game thread to finish
            running = false;
            if (gameThread != null) {
                gameThread.join();
            }

        } catch (InterruptedException e) {
            System.out.println("Error while closing game.");
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Register fonts
     *
     * @throws IOException         fonts not found at the location
     * @throws FontFormatException fonts are not of correct format.
     */
    private void registerFonts() throws IOException, FontFormatException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //REF: https://www.1001fonts.com/game-music-love-font.html
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Game.class.getResourceAsStream("/fonts/gomarice_game_music_love.ttf")));
        //REF: https://www.1001fonts.com/arcadeclassic-font.html
        ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, Game.class.getResourceAsStream("/fonts/ARCADECLASSIC.TTF")));
    }
}
