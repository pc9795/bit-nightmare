package game.physics;
/*
 * Modified by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */
//Modified from Graphics 3033J course point class  by Abey Campbell 


import javafx.util.Pair;

/**
 * A point in the 3D space
 * NOTE: Not implemented addition of two points as it is not defined
 */
public class Point3f {
    private float x;
    private float y;
    private float z;
    private Boundary boundary;

    public Point3f(float x, float y, float z, Boundary boundary) {
        this.boundary = boundary;
        setX(x);
        setY(y);
        setZ(z);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        //Unbounded point
        if (boundary == null) {
            this.x = x;
            return;
        }
        //Less than lower limit then set lower limit.
        if (x < boundary.getxMin()) x = boundary.getxMin();
        //Greater than higher limit set higher limit.
        if (x > boundary.getxMax()) x = boundary.getxMax();
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        //Unbounded point
        if (boundary == null) {
            this.y = y;
            return;
        }
        //Less than lower limit then set lower limit.
        if (y < boundary.getyMin()) y = boundary.getyMin();
        //Greater than higher limit set higher limit.
        if (y > boundary.getyMax()) y = boundary.getyMax();
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        //Unbounded point
        if (boundary == null) {
            this.z = z;
            return;
        }
        //Less than lower limit then set lower limit.
        if (z < boundary.getzMin()) z = boundary.getzMin();
        //Greater than higher limit set higher limit.
        if (z > boundary.getzMax()) z = boundary.getzMax();
        this.z = z;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * sometimes for different algorithms we will need to address the point using positions 0 1 2
     *
     * @param position axis
     * @return the value of the axis
     */
    public float getPosition(int position) {
        switch (position) {
            case 0:
                return getX();
            case 1:
                return getY();
            case 2:
                return getZ();
            default:
                return Float.NaN;
        }
    }

    public Point3f add(Vector3f other) {
        return new Point3f(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ(), boundary);
    }

    public Point3f subtract(Vector3f other) {
        return new Point3f(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ(), boundary);
    }

    public Vector3f subtract(Point3f other) {
        return new Vector3f(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
    }

    /**
     * Use for direct application of a Vector
     * We are considering primary motion in x-axis as right and in y-axis as up. That's why a minus in y-direction and
     * plus in x-direction.
     *
     * @param vector vector object
     */
    public void applyVector(Vector3f vector) {
        setX(this.getX() + vector.getX());
        setY(this.getY() - vector.getY());
    }

    @Override
    public String toString() {
        return ("(" + getX() + "," + getY() + "," + getZ() + ")");
    }
}
