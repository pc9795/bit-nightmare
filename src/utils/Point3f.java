package utils;
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


/**
 * A point in the 3D space
 * NOTE: Not implemented addition of two points as it is not defined
 */
public class Point3f {
    private float x;
    private float y;
    private float z;
    private int boundary = 900;

    public Point3f() {
    }

    public Point3f(float x, float y, float z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    private void setBoundary(int boundary) {
        this.boundary = boundary;

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
        return new Point3f(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }

    public Point3f subtract(Vector3f other) {
        return new Point3f(this.getX() - other.getX(), this.getY() - other.getY(), this.getZ() - other.getZ());
    }

    public Vector3f subtract(Point3f other) {
        return new Vector3f(this.getX() - other.getX(), this.getY() - other.getY(), this.getZ() - other.getZ());
    }

    /**
     * Use for direct application of a Vector
     *
     * @param vector vector object
     */
    public void applyVector(Vector3f vector) {
        setX(checkBoundary(this.getX() + vector.getX()));
        setY(checkBoundary(this.getY() - vector.getY()));
        setZ(checkBoundary(this.getZ() - vector.getZ()));
    }

    private float checkBoundary(float f) {
        if (f < 0) f = 0.0f;
        if (f > boundary) f = (float) boundary;
        return f;
    }

    public float getX() {
        return x;
    }

    private void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    private void setY(float y) {
        this.y = y;
    }

    float getZ() {
        return z;
    }

    private void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return ("(" + getX() + "," + getY() + "," + getZ() + ")");
    }
}
