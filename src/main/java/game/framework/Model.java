package game.framework;

import game.objects.GameObject;
import game.objects.Player;
import game.physics.Point3f;
import game.utils.Constants;
import game.utils.LevelLoader;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
   /
   (MIT LICENSE ) e.g do what you want with this :-) 
 */
public class Model {
    private Player player;
    private List<GameObject> objects = new CopyOnWriteArrayList<>();
    private List<String> levels = new ArrayList<>();

    public Model() throws IOException, URISyntaxException {
        init();
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameObject> getObjects() {
        return objects;
    }

    private void init() throws URISyntaxException, IOException {
        URI uri = Model.class.getResource(Constants.LEVEL_NAMES_FILE).toURI();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(uri))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                levels.add(line.trim());
            }
        }
        if (levels.size() == 0) {
            throw new RuntimeException("No levels to load");
        }
        switchLevel(levels.get(0));
    }

    private void switchLevel(String levelName) throws IOException {
        clean();
        LevelLoader.getInstance().loadLevel(levelName, this);
    }

    private void clean() {
        objects.clear();
        player = null;
    }

    /**
     * This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly.
     */
    void gameLogic() {
        playerLogic();
        enemyLogic();
        bulletLogic();
    }

    private void playerLogic() {

    }

    private void enemyLogic() {

    }

    private void bulletLogic() {

    }

    public void addGameObject(GameObject object) {
        switch (object.getType()) {
            case PLAYER:
                player = (Player) object;
            default:
                objects.add(object);
        }
    }

    public void removeGameObject(GameObject object) {

    }

}
