package game.physics;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 19:21
 * No setters so effectively immutable. Have to check before adding setters as this object instance is shared between
 * multiple objects.
 **/
public class Boundary {
    private float xMin;
    private float xMax;
    private float yMin;
    private float yMax;
    private float zMin;
    private float zMax;

    public Boundary(float xMax, float yMax) {
        //Minimums are set to 0 by default.
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public Boundary(float xMax, float yMax, float zMax) {
        //Minimums are set to 0 by default.
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public Boundary(float xMin, float xMax, float yMin, float yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public Boundary(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public float getxMin() {
        return xMin;
    }

    public float getxMax() {
        return xMax;
    }

    public float getyMin() {
        return yMin;
    }

    public float getyMax() {
        return yMax;
    }

    public float getzMin() {
        return zMin;
    }

    public float getzMax() {
        return zMax;
    }

    @Override
    public String toString() {
        return "Boundary{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                ", zMin=" + zMin +
                ", zMax=" + zMax +
                '}';
    }
}
