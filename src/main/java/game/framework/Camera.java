package game.framework;

import game.objects.GameObject;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 19-02-2020 00:48
 * Purpose: Camera object which follows a object
 **/
public class Camera {
    private static final int CAMERA_OFFSET = 400;
    private Point point;

    Camera(float x, float y) {
        this.point = new Point((int) x, (int) y);
    }

    public float getX() {
        return point.x;
    }

    public float getY() {
        return point.y;
    }

    public void update(GameObject object) {
        point.x = (int) -(object.getCentre().getX() - CAMERA_OFFSET);
    }
}
