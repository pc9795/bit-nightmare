package game.framework.controllers;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 21:00
 * Purpose: TODO:
 **/
public final class MouseController implements MouseMotionListener, MouseListener, MouseWheelListener {
    private static final MouseController INSTANCE = new MouseController();
    private Point currentPos = new Point(0, 0);
    private Map<Integer, Boolean> keys = new HashMap<>();

    private MouseController() {
        // All the keys supported by the class must be entered in the map for one time. Else it can result in
        // NullPointerException if we trying to access a non-existing key.
        keys.put(MouseEvent.BUTTON1, false);
    }

    public static MouseController getInstance() {
        return INSTANCE;
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
        return keys.get(MouseEvent.BUTTON1);
    }
}
