package game.framework;

import game.utils.Constants;

import java.awt.*;
import java.awt.image.BufferStrategy;


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
public class View extends Canvas {
    private Model gameWorld;
    private BufferStrategy bs;

    public View(Model world) {
        this.gameWorld = world;
    }

    void updateView() {
        if (bs == null) {
            createBufferStrategy(Constants.BUFFER_COUNT);
            bs = getBufferStrategy();
        }
        Graphics g = bs.getDrawGraphics();
        g.clearRect(0, 0, getWidth(), getHeight());
        //Drawing stuff
        gameWorld.getEnvironment().forEach(object -> object.render(g));
        gameWorld.getCollectibles().forEach(object -> object.render(g));
        gameWorld.getEnemies().forEach(object -> object.render(g));
        gameWorld.getPlayer().render(g);
        g.dispose();
        bs.show();
    }

}

