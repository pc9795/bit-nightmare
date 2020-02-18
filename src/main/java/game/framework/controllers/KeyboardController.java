package game.framework.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
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
 * Singleton class
 */
public final class KeyboardController implements KeyListener {
    private static final KeyboardController instance = new KeyboardController();
    //Right now implemented keys as map. In future can move to array based implementation
    private Map<Integer, Boolean> keys = new HashMap<>();

    private KeyboardController() {
        init();
    }


    private void init() {
        // All the keys supported by the class must be entered in the map for one time. Else it can result in
        // NullPointerException if we trying to access a non-existing key.
        keys.put(KeyEvent.VK_W, false);
        keys.put(KeyEvent.VK_A, false);
        keys.put(KeyEvent.VK_D, false);
        keys.put(KeyEvent.VK_S, false);
        keys.put(KeyEvent.VK_Q, false);
        keys.put(KeyEvent.VK_SPACE, false);
        keys.put(KeyEvent.VK_ESCAPE, false);
    }

    public static KeyboardController getInstance() {
        return instance;
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
        return keys.get(KeyEvent.VK_W);
    }

    public boolean isAPressed() {
        return keys.get(KeyEvent.VK_A);
    }

    public boolean isDPressed() {
        return keys.get(KeyEvent.VK_D);
    }

    public boolean isSPressed() {
        return keys.get(KeyEvent.VK_S);
    }

    public boolean isQPressed() {
        return keys.get(KeyEvent.VK_Q);
    }

    public boolean isSpacePressed() {
        return keys.get(KeyEvent.VK_SPACE);
    }

    public boolean isEscPressed() {
        return keys.get(KeyEvent.VK_ESCAPE);
    }
}
