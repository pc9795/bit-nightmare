package game.utils;

import javax.rmi.CORBA.Util;

/*
 * Created by Abraham Campbell on 15/01/2020.
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
public final class Utils {
    private Utils() {
    }

    /**
     * to see if missed our target by more than a full frame.
     *
     * @param targetTime    the time at which the frame should be delivered
     * @param deliveredTime the time at which frame was originally delivered
     * @param targetFPS     target fps
     */
    public static void checkFrameRate(long targetTime, long deliveredTime, int targetFPS) {
        int timeBetweenFrames = 1000 / targetFPS;
        if ((targetTime - deliveredTime) > timeBetweenFrames) {
            //todo write out to log file.
            System.out.println("Frame was late by  " + (targetTime - deliveredTime) + " ms");
        }
    }

}
