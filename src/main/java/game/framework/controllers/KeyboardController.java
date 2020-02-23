package game.framework.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * Controller to access keyboard
 */
public final class KeyboardController implements KeyListener {
    private static final KeyboardController instance = new KeyboardController();
    //Right now implemented keys as map. In future can move to array based implementation
    private Map<Integer, Boolean> keys = new HashMap<>();
    private Map<Integer, Integer> pollCount = new HashMap<>();

    private KeyboardController() {
        init();
    }


    /**
     * Initialization
     */
    private void init() {
        // All the keys supported by the class must be entered in the map for one time. Else it can result in
        // NullPointerException if we trying to access a non-existing key.
        List<Integer> configuredKeys = Arrays.asList(KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_S,
                KeyEvent.VK_Q, KeyEvent.VK_SPACE, KeyEvent.VK_ESCAPE);
        for (Integer key : configuredKeys) {
            keys.put(key, false);
            pollCount.put(key, 0);
        }
    }

    public static KeyboardController getInstance() {
        return instance;
    }

    /**
     * It must be called per frame. This method implements a functionality to check whether a button is pressed once
     * or not. As when button is pressed for multiple frames it can cause unexpected behavior
     */
    public void poll() {
        for (Integer key : pollCount.keySet()) {
            if (keys.get(key)) {
                pollCount.put(key, pollCount.get(key) + 1);
            } else {
                pollCount.put(key, 0);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //Not needed
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.put(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.put(e.getKeyCode(), false);
    }

    public boolean isWPressed() {
        return pollCount.get(KeyEvent.VK_W) > 0;
    }

    public boolean isWPressedOnce() {
        return pollCount.get(KeyEvent.VK_W) == 1;
    }

    public boolean isAPressed() {
        return pollCount.get(KeyEvent.VK_A) > 0;
    }

    public boolean isDPressed() {
        return pollCount.get(KeyEvent.VK_D) > 0;
    }

    public boolean isSPressedOnce() {
        return pollCount.get(KeyEvent.VK_S) == 1;
    }

    public boolean isSPressed() {
        return pollCount.get(KeyEvent.VK_S) > 0;
    }

    public boolean isQPressedOnce() {
        return pollCount.get(KeyEvent.VK_Q) == 1;
    }

    public boolean isQPressed() {
        return pollCount.get(KeyEvent.VK_Q) > 0;
    }

    public boolean isSpacePressedOnce() {
        return pollCount.get(KeyEvent.VK_SPACE) == 1;
    }

    public boolean isSpacePressed() {
        return pollCount.get(KeyEvent.VK_SPACE) > 0;
    }

    public boolean isEscPressed() {
        return pollCount.get(KeyEvent.VK_ESCAPE) > 0;
    }
}
