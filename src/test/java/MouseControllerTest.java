import game.framework.controllers.MouseController;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 16:23
 * Purpose: TODO:
 **/
public class MouseControllerTest {
    @Test
    public void testMouseControllerWorking() {
        JFrame testFrame = new JFrame("Mouse Test");
        Canvas testCanvas = new Canvas();
        testCanvas.setPreferredSize(new Dimension(1000, 500));
        testCanvas.setBackground(Color.WHITE);
        testCanvas.addMouseListener(MouseController.getInstance());
        testCanvas.addMouseMotionListener(MouseController.getInstance());
        testCanvas.addMouseWheelListener(MouseController.getInstance());
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
                printMouseControllerStatus();
            }

            private void printMouseControllerStatus() {
                testCanvas.requestFocus();
                BufferStrategy bs = testCanvas.getBufferStrategy();
                MouseController controller = MouseController.getInstance();
                while (running) {
                    Graphics g = bs.getDrawGraphics();
                    g.clearRect(0, 0, testCanvas.getWidth(), testCanvas.getHeight());
                    g.setFont(new Font("Courier New", Font.BOLD, 20));
                    FontMetrics fm = g.getFontMetrics();
                    int fontHeight = fm.getHeight() + fm.getAscent() + fm.getDescent();
                    int initialTextPos = fontHeight;

                    g.drawString("PRESS and MOVE mouse keys and TEST", 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Current positoin: %s", controller.getCurrentPos()), 5, initialTextPos);
                    initialTextPos += fontHeight;
                    g.drawString(String.format("Left cliked: %s", controller.isLeftClicked()), 5, initialTextPos);
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
