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
    private Pair<Integer, Integer> boundary;

    public Point3f(float x, float y, float z, Pair<Integer, Integer> boundary) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.boundary = boundary;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public Pair<Integer, Integer> getBoundary() {
        return new Pair<>(boundary.getKey(), boundary.getValue());
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
     *
     * @param vector vector object
     */
    //todo check what this method is doing
    public void applyVector(Vector3f vector) {
        x = checkBoundary(this.getX() + vector.getX());
        y = checkBoundary(this.getY() - vector.getY());
        z = checkBoundary(this.getZ() - vector.getZ());
    }

    private float checkBoundary(float f) {
        //Unbounded point
        if (boundary == null) {
            return f;
        }
        //Less than lower limit then set lower limit.
        if (f < boundary.getKey()) f = boundary.getKey();
        //Greater than higher limit set higher limit.
        if (f > boundary.getValue()) f = boundary.getValue();
        return f;
    }

    @Override
    public String toString() {
        return ("(" + getX() + "," + getY() + "," + getZ() + ")");
    }
}
