package game.framework;

import game.objects.GameObject;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 19-02-2020 00:48
 * Purpose: TODO:
 **/
public class Camera {
    private Point point;

    public Camera(float x, float y) {
        this.point = new Point((int) x, (int) y);
    }

    public float getX() {
        return point.x;
    }

    public float getY() {
        return point.y;
    }

    public void update(GameObject object) {
        //todo make configurable
        point.x = (int) -(object.getCentre().getX() - 400);
    }
}
