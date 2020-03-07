package game.physics;

import java.io.Serializable;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 19:21
 * No setters so effectively immutable. Have to check before adding setters as this object instance is shared between
 * multiple objects.
 **/
public class Boundary implements Serializable {
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;

    public Boundary(float xMax, float yMax) {
        //Minimums are set to 0 by default.
        this.xMin = 0;
        this.yMin = 0;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public float getxMin() {
        return xMin;
    }

    public float getxMax() {
        return xMax;
    }

    float getyMin() {
        return yMin;
    }

    public float getyMax() {
        return yMax;
    }

    @Override
    public String toString() {
        return "Boundary{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                '}';
    }
}
