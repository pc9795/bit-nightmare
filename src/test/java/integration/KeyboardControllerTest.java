package integration;

import game.framework.controllers.KeyboardController;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 15:01
 **/
public class KeyboardControllerTest {
    /**
     * It is more of an integration test to check keypad controller. I didn't wrote much documentation about how frame is
     * displayed here. For more info refer to the `Game` class.
     */
    @Test
    public void testKeyboardControllerWorking() throws InterruptedException {
        JFrame testFrame = new JFrame("Keyboard Test");
        Canvas testCanvas = new Canvas();
        testCanvas.setPreferredSize(new Dimension(1000, 500));
        testCanvas.setBackground(Color.WHITE);

        //Adding listeners
        testCanvas.addKeyListener(KeyboardController.getInstance());

        testCanvas.setIgnoreRepaint(true);
        testFrame.getContentPane().add(testCanvas);
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testFrame.setResizable(false);
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.setIgnoreRepaint(true);

        //Starting a new thread to render the screen.
        Thread thread = new Thread(new Runnable() {
            boolean running;

            @Override
            public void run() {
                testCanvas.createBufferStrategy(3);
                running = true;
                printKeyboardControllerStats();
            }

            /**
             * Method of interest
             */
            private void printKeyboardControllerStats() {
                testCanvas.requestFocus();
                BufferStrategy bs = testCanvas.getBufferStrategy();
                KeyboardController controller = KeyboardController.getInstance();
                while (running) {
                    controller.poll();
                    Graphics g = bs.getDrawGraphics();
                    g.clearRect(0, 0, testCanvas.getWidth(), testCanvas.getHeight());
                    g.setFont(new Font("Courier New", Font.BOLD, 20));
                    FontMetrics fm = g.getFontMetrics();
                    int fontHeight = fm.getHeight() + fm.getAscent() + fm.getDescent();
                    int initialTextPos = fontHeight;

                    g.drawString("PRESS keyboard keys and TEST", 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("W Pressed: %s", controller.isWPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("A Pressed: %s", controller.isAPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("D Pressed: %s", controller.isDPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("S Pressed: %s", controller.isSPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Q Pressed: %s", controller.isQPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Space Pressed: %s", controller.isSpacePressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Escape Pressed: %s", controller.isEscPressed()), 5, initialTextPos);

                    g.dispose();
                    bs.show();
                }
            }
        });

        thread.start();
        thread.join();
    }
}
