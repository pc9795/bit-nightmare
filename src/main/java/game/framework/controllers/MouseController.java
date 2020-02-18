package game.framework.controllers;

import java.awt.event.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 21:00
 * Purpose: TODO:
 **/
public final class MouseController implements MouseMotionListener, MouseListener, MouseWheelListener {
    private static final MouseController INSTANCE = new MouseController();

    private MouseController() {
    }

    public static MouseController getInstance() {
        return INSTANCE;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
