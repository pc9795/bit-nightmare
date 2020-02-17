import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

//Singeton pattern
public class Controller implements KeyListener {
    private static boolean keyAPressed = false;
    private static boolean keySPressed = false;
    private static boolean keyDPressed = false;
    private static boolean keyWPressed = false;
    private static boolean keySpacePressed = false;

    private static final Controller instance = new Controller();

    private Controller() {
    }

    static Controller getInstance() {
        return instance;
    }

    @Override
    // Key pressed , will keep triggering
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                setKeyAPressed(true);
                break;
            case 's':
                setKeySPressed(true);
                break;
            case 'w':
                setKeyWPressed(true);
                break;
            case 'd':
                setKeyDPressed(true);
                break;
            case ' ':
                setKeySpacePressed(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'a':
                setKeyAPressed(false);
                break;
            case 's':
                setKeySPressed(false);
                break;
            case 'w':
                setKeyWPressed(false);
                break;
            case 'd':
                setKeyDPressed(false);
                break;
            case ' ':
                setKeySpacePressed(false);
                break;
            default:
                break;
        }
    }

    boolean isKeyAPressed() {
        return keyAPressed;
    }

    private void setKeyAPressed(boolean keyAPressed) {
        Controller.keyAPressed = keyAPressed;
    }

    boolean isKeySPressed() {
        return keySPressed;
    }

    private void setKeySPressed(boolean keySPressed) {
        Controller.keySPressed = keySPressed;
    }

    boolean isKeyDPressed() {
        return keyDPressed;
    }

    protected void setKeyDPressed(boolean keyDPressed) {
        Controller.keyDPressed = keyDPressed;
    }

    boolean isKeyWPressed() {
        return keyWPressed;
    }

    protected void setKeyWPressed(boolean keyWPressed) {
        Controller.keyWPressed = keyWPressed;
    }

    boolean isKeySpacePressed() {
        return keySpacePressed;
    }

    protected void setKeySpacePressed(boolean keySpacePressed) {
        Controller.keySpacePressed = keySpacePressed;
    }


}
