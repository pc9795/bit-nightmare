import game.framework.controllers.GamepadController;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 18:07
 * Purpose: TODO:
 **/
public class GamepadControllerTest {
    @Test
    public void testKeyboardControllerWorking() {
        JFrame testFrame = new JFrame("Gamepad Test");
        Canvas testCanvas = new Canvas();
        testCanvas.setPreferredSize(new Dimension(1000, 500));
        testCanvas.setBackground(Color.WHITE);
        GamepadController.getInstance().setDetecting(true);
        //Gamepad thread
        new Thread(GamepadController.getInstance()).start();
        testCanvas.setIgnoreRepaint(true);
        testFrame.getContentPane().add(testCanvas);
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testFrame.setResizable(false);
        testFrame.pack();
        testFrame.setVisible(true);
        testFrame.setIgnoreRepaint(true);

        new Thread(new Runnable() {
            boolean running;

            @Override
            public void run() {
                testCanvas.createBufferStrategy(3);
                running = true;
                printGamepadControllerStatus();
            }

            private void printGamepadControllerStatus() {
                testCanvas.requestFocus();
                BufferStrategy bs = testCanvas.getBufferStrategy();
                GamepadController controller = GamepadController.getInstance();
                while (running) {
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
        }).start();

        while (true) {
            //Stops frame from closing
        }
    }
}
