package game.framework.controllers;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 21:00
 * Purpose: Controller to access mouse
 **/
public final class MouseController implements MouseMotionListener, MouseListener, MouseWheelListener {
    private static final MouseController INSTANCE = new MouseController();
    private Point currentPos = new Point(0, 0);
    private Map<Integer, Boolean> keys = new HashMap<>();
    private Map<Integer, Integer> pollCount = new HashMap<>();

    private MouseController() {
        // All the keys supported by the class must be entered in the map for one time. Else it can result in
        // NullPointerException if we trying to access a non-existing key.
        keys.put(MouseEvent.BUTTON1, false);
        keys.put(MouseEvent.BUTTON3, false);
        pollCount.put(MouseEvent.BUTTON1, 0);
        pollCount.put(MouseEvent.BUTTON3, 0);
    }

    public static MouseController getInstance() {
        return INSTANCE;
    }

    /**
     * It must be called per frame. This method implements a functionality to check whether a button is pressed once
     * or not. As when button is pressed for multiple frames it can cause unexpected behavior
     */
    public void poll() {
        for (Integer key : pollCount.keySet()) {
            if (keys.get(key)) {
                pollCount.put(key, pollCount.get(key) + 1);
            } else {
                pollCount.put(key, 0);
            }
        }
    }

    public Point getCurrentPos() {
        return currentPos;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //Not needed
    }

    @Override
    public void mousePressed(MouseEvent e) {
        keys.put(e.getButton(), true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        keys.put(e.getButton(), false);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseMoved(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        currentPos = e.getPoint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        //Not needed
    }

    public boolean isLeftClicked() {
        return pollCount.get(MouseEvent.BUTTON1) > 0;
    }

    public boolean isLeftClickedOnce() {
        return pollCount.get(MouseEvent.BUTTON1) == 1;
    }

    public boolean isRightClicked() {
        return pollCount.get(MouseEvent.BUTTON3) > 0;
    }

    public boolean isRightClickedOnce() {
        return pollCount.get(MouseEvent.BUTTON3) == 1;
    }
}
