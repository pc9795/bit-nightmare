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


import java.io.Serializable;

/**
 * A point in the 2D space
 */
public class Point2f implements Serializable {
    private float x;
    private float y;
    private Boundary boundary;

    public Point2f(float x, float y) {
        setX(x);
        setY(y);
    }

    public Point2f(float x, float y, Boundary boundary) {
        this.boundary = boundary;
        setX(x);
        setY(y);
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
        if (x > boundary.getxMax()) y = boundary.getxMax();
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

    public Point2f copy() {
        return new Point2f(x, y, boundary);
    }

    @Override
    public String toString() {
        return ("(" + getX() + "," + getY() + ")");
    }
}
