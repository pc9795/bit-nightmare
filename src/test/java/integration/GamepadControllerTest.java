package integration;

import game.framework.controllers.GamepadController;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 18:07
 **/
public class GamepadControllerTest {
    /**
     * It is more of an integration test to check game-pad controller. I didn't wrote much documentation about how frame is
     * displayed here. For more info refer to the `Game` class.
     */
    @Test
    public void testKeyboardControllerWorking() throws InterruptedException {
        JFrame testFrame = new JFrame("Gamepad Test");
        Canvas testCanvas = new Canvas();
        testCanvas.setPreferredSize(new Dimension(1000, 500));
        testCanvas.setBackground(Color.WHITE);

        //Game-pad thread
        GamepadController.getInstance().setDetecting(true);
        new Thread(GamepadController.getInstance()).start();

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
                printGamepadControllerStatus();
            }

            /**
             * Method of interest
             */
            private void printGamepadControllerStatus() {
                testCanvas.requestFocus();
                BufferStrategy bs = testCanvas.getBufferStrategy();
                GamepadController controller = GamepadController.getInstance();
                while (running) {
                    controller.poll();
                    Graphics g = bs.getDrawGraphics();
                    g.clearRect(0, 0, testCanvas.getWidth(), testCanvas.getHeight());
                    g.setFont(new Font("Courier New", Font.BOLD, 20));
                    FontMetrics fm = g.getFontMetrics();
                    int fontHeight = fm.getHeight() + fm.getAscent() + fm.getDescent();
                    int initialTextPos = fontHeight;

                    g.drawString("PRESS Gamepad keys and TEST", 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("A Pressed: %s", controller.isAPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("X Pressed: %s", controller.isXPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Y Pressed: %s", controller.isYPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("B Pressed: %s", controller.isBPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Right Pressed: %s", controller.isRightPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Left Pressed: %s", controller.isLeftPressed()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Start Pressed: %s", controller.isStartPressed()), 5, initialTextPos);

                    g.dispose();
                    bs.show();
                }
            }
        });

        thread.start();
        thread.join();
    }
}
