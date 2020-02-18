package game.framework;

import game.objects.GameObject;
import game.objects.Player;
import game.physics.QuadTree;
import game.utils.Constants;
import game.utils.LevelLoader;

import java.awt.*;
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
    private List<GameObject> enemies;
    private QuadTree environmentQuadTree;
    private List<GameObject> environment;
    private List<GameObject> collectibles;
    private List<String> levels;

    public Model(int width, int height) throws IOException, URISyntaxException {
        this.enemies = new CopyOnWriteArrayList<>();
        this.environmentQuadTree = new QuadTree(0, new Rectangle(0, 0, width, height));
        //It will not be modified.
        this.environment = new ArrayList<>();
        this.collectibles = new CopyOnWriteArrayList<>();
        this.levels = new ArrayList<>();
        init();

    }

    private void init() throws URISyntaxException, IOException {
        URI uri = Model.class.getResource(Constants.LEVEL_NAMES_FILE).toURI();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(uri))) {
            String line;
            while ((line = br.readLine()) != null) {
                levels.add(line.trim());
            }
        }
        if (levels.size() == 0) {
            throw new RuntimeException("No levels to load");
        }
        switchLevel(levels.get(0));
    }

    public Player getPlayer() {
        return player;
    }

    public List<GameObject> getEnemies() {
        return enemies;
    }

    public List<GameObject> getEnvironment() {
        return environment;
    }

    public QuadTree getEnvironmentQuadTree() {
        return environmentQuadTree;
    }

    public List<GameObject> getCollectibles() {
        return collectibles;
    }

    private void switchLevel(String levelName) throws IOException {
        clean();
        LevelLoader.getInstance().loadLevel(levelName, this);
    }

    private void clean() {
        enemies.clear();
        player = null;
    }

    /**
     * This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly.
     */
    public void gameLogic() {
        playerLogic();
    }

    private void playerLogic() {
        player.update();
        player.collision(this);
    }

    public void addGameObject(GameObject object) {
        switch (object.getType()) {
            case PLAYER:
                player = (Player) object;
                break;
            case BOSS1:
            case ENEMY1:
            case ENEMY2:
            case ENEMY3:
                enemies.add(object);
                break;
            case BLOCK:
            case END_GATE:
            case ENEMY_PORTAL:
            case GATE:
            case LAVA:
            case MOVABLE_BLOCK:
            case OSCILLATING_BLOCK:
                environmentQuadTree.insert(object);
                environment.add(object);
                break;
            case BIT_ARRAY_GUN:
            case BIT_MATRIX_BLAST:
            case BIT_REVOLVER:
                collectibles.add(object);
                break;
            default:
                System.out.println(String.format("Object type is not supported: %s by the Model", object.getType()));
        }
    }

    public void removeGameObject(GameObject object) {

    }
}
