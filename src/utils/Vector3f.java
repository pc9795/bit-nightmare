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

/**
 * Modified from Graphics 3033J course point class  by Abey Campbell
 * NOTE: Subtraction of a Point is not implemented as it is not defined.
 */
public class Vector3f {
    private float x;
    private float y;
    private float z;

    public Vector3f() {
    }

    public Vector3f(float x, float y, float z) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
    }

    public Vector3f add(Vector3f additional) {
        return new Vector3f(this.getX() + additional.getX(), this.getY() + additional.getY(), this.getZ() + additional.getZ());
    }

    public Vector3f subtract(Vector3f other) {
        return new Vector3f(this.getX() - other.getX(), this.getY() - other.getY(), this.getZ() - other.getZ());
    }

    public Point3f add(Point3f other) {
        return new Point3f(this.getX() + other.getX(), this.getY() + other.getY(), this.getZ() + other.getZ());
    }


    private Vector3f scale(float scale) {
        return new Vector3f(this.getX() * scale, this.getY() * scale, this.getZ() * scale);
    }

    public Vector3f negate() {
        return new Vector3f(-this.getX(), -this.getY(), -this.getZ());
    }

    private float length() {
        return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
    }

    public Vector3f norm() {
        return this.scale(1.0f / length());
    }

    public float dotProduct(Vector3f v) {
        return (this.getX() * v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ());
    }

    public Vector3f crossProduct(Vector3f v) {
        float u0 = (this.getY() * v.getZ() - this.getZ() * v.getY());
        float u1 = (this.getZ() * v.getX() - this.getX() * v.getZ());
        float u2 = (this.getX() * v.getY() - this.getY() * v.getX());
        return new Vector3f(u0, u1, u2);
    }

    float getX() {
        return x;
    }

    private void setX(float x) {
        this.x = x;
    }

    float getY() {
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
}
