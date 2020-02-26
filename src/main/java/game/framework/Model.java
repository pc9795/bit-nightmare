package game.framework;

import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.framework.levelloader.Level;
import game.framework.levelloader.LevelLoader;
import game.framework.levelloader.LevelObject;
import game.objects.GameObject;
import game.objects.GameObjectFactory;
import game.objects.Player;
import game.objects.weapons.Weapon;
import game.physics.Boundary;
import game.physics.Point2f;
import game.physics.QuadTree;
import game.utils.Constants;

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

/**
 * To start the model see `loadLastCheckpoint` and `loadLevel`
 */
public class Model {
    private Player player1;
    private List<GameObject> enemies, environment, movableEnvironment, collectibles, bullets;
    private QuadTree environmentQuadTree;
    private List<String> levels;
    private Point2f lastCheckpoint;
    private Boundary levelBoundary;
    private String currentLevel;
    private boolean pause;
    private boolean started;


    public Model(int width, int height) throws IOException, URISyntaxException {
        //Things which will be added and removed are stored in ArrayList.
        this.enemies = new CopyOnWriteArrayList<>();
        this.environment = new ArrayList<>();
        this.collectibles = new CopyOnWriteArrayList<>();
        this.levels = new ArrayList<>();
        this.environmentQuadTree = new QuadTree(new Rectangle(width, height));
        this.movableEnvironment = new ArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.levelBoundary = new Boundary(width, height);
        init();
    }

    /**
     * Initialization
     *
     * @throws URISyntaxException if the file location containing level info is not in a correct format
     * @throws IOException        not able to load level files
     */
    private void init() throws URISyntaxException, IOException {
        URI uri = Model.class.getResource(Constants.Level.INFO_FILE_LOC).toURI();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(uri))) {
            String line;
            while ((line = br.readLine()) != null) {
                levels.add(line.trim());
            }
        }
        if (levels.size() == 0) {
            throw new RuntimeException("No levels to load");
        }
    }

    public Player getPlayer1() {
        return player1;
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

    public void setLastCheckpoint(Point2f lastCheckpoint) {
        this.lastCheckpoint = lastCheckpoint;
    }

    public List<GameObject> getMovableEnvironment() {
        return movableEnvironment;
    }

    public List<GameObject> getBullets() {
        return bullets;
    }

    public Boundary getLevelBoundary() {
        return levelBoundary;
    }

    public List<String> getLevels() {
        return levels;
    }

    public void pause() {
        pause = true;
    }

    public void resume() {
        pause = false;
    }

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }

    public boolean isPaused() {
        return pause;
    }

    /**
     * Load a level
     *
     * @param levelName name of the level
     * @throws IOException not able to load level files
     */
    public void loadLevel(String levelName) throws IOException {
        clean();
        Level level = LevelLoader.getInstance().loadLevel(levelName);
        loadLevelUtil(level);
        currentLevel = levelName;
        started = true;
        pause = false;
    }

    /**
     * Convert level object into game objects
     *
     * @param level level object
     */
    private void loadLevelUtil(Level level) {
        levelBoundary = new Boundary(level.getMaxX() * Constants.Level.PIXEL_TO_WIDTH_RATIO,
                level.getMaxY() * Constants.Level.PIXEL_TO_WIDTH_RATIO);
        // QuadTree is to be initialized by the new boundaries.
        environmentQuadTree = new QuadTree(new Rectangle(0, 0, (int) levelBoundary.getxMax(), (int) levelBoundary.getyMax()));

        //Convert level objects to game object
        for (LevelObject object : level.getLevelObjects()) {
            GameObject.GameObjectType type;
            //Skipping in case of invalid configuration.
            try {
                type = GameObject.GameObjectType.valueOf(object.getType());

            } catch (Exception e) {
                System.out.println(String.format("Invalid configuration found while loading level with type: %s", object.getType()));
                continue;
            }
            Point2f center = new Point2f(object.getCentre().getX() * Constants.Level.PIXEL_TO_WIDTH_RATIO,
                    object.getCentre().getY() * Constants.Level.PIXEL_TO_WIDTH_RATIO, levelBoundary);
            GameObject gameObject = GameObjectFactory.getGameObject(type, center);
            addGameObject(gameObject);
        }
        if (player1 == null) {
            throw new RuntimeException("No player found in the level.");
        }
    }

    /**
     * Clean game world
     */
    private void clean() {
        enemies.clear();
        environmentQuadTree.clear();
        environment.clear();
        collectibles.clear();
        bullets.clear();
        movableEnvironment.clear();
        player1 = null;
    }

    public boolean isLastCheckpointAvailable() {
        return false;
    }

    /**
     * Load a last checkpoint
     */
    //todo implement it
    public void loadLastCheckPoint() {
        if (player1 == null) {
            return;
        }
        boolean bitBotFound = player1.isBitBotFound();
        List<Weapon> weapons = player1.getWeapons();
        int currentWeaponIndex = player1.getCurrentWeaponIndex();
        try {
            loadLevel(currentLevel);
            if (lastCheckpoint != null) {
                player1.setCentre(lastCheckpoint.copy());
            }
            player1.setBitBotFound(bitBotFound);
            player1.setWeapons(weapons);
            player1.setCurrentWeaponIndex(currentWeaponIndex);

        } catch (IOException e) {
            throw new RuntimeException(String.format("Error while loading checkpoint: %s on level: %s", lastCheckpoint, currentLevel));
        }
        started = true;
        pause = false;
    }

    /**
     * This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly.
     */
    void gameLogic() {
        if (!started) {
            return;
        }
        if (pause) {
            return;
        }
        if (player1.getHealth() <= 0) {
            loadLastCheckPoint();
        }
        processInput();
        //Player
        player1.update();
        player1.perceiveEnv(this);
        //Bullets
        bullets.forEach(GameObject::update);
        bullets.forEach(object -> object.perceiveEnv(this));
        //Environment
        environment.forEach(GameObject::update);
        environment.forEach(object -> object.perceiveEnv(this));
        //Movable environment
        movableEnvironment.forEach(GameObject::update);
        movableEnvironment.forEach(object -> object.perceiveEnv(this));
        //Enemies
        enemies.forEach(GameObject::update);
        enemies.forEach(object -> object.perceiveEnv(this));
    }

    /**
     * Process the input. My philosophy is that the controllers are affecting the state fo the player. So it can be
     * handled centrally in the model class. If another objects needs information then it can check the state of player
     * as it will have a method with `Model` object in it. Maybe this approach can change in future.
     */
    private void processInput() {
        KeyboardController keyboardController = KeyboardController.getInstance();
        GamepadController gamepadController = GamepadController.getInstance();
        keyboardController.poll();
        gamepadController.poll();

        //Left and right
        if (keyboardController.isAPressed() || gamepadController.isLeftPressed()) {
            player1.moveLeft();
        } else if (keyboardController.isDPressed() || gamepadController.isRightPressed()) {
            player1.moveRight();
        } else {
            player1.makeIdle();
        }
        //Jump
        if ((keyboardController.isWPressedOnce() || gamepadController.isAPressedOnce())) {
            player1.jump();
        }
        //Duck
        if (keyboardController.isSPressedOnce() || gamepadController.isXPressedOnce()) {
            player1.toggleDuck();
        }
        //Cycle weapon
        if (keyboardController.isQPressedOnce() || gamepadController.isYPressedOnce()) {
            player1.cycleWeapon();
        }
        //Fire weapon
        if (keyboardController.isSpacePressedOnce() || gamepadController.isBPressedOnce()) {
            GameObject bullet = player1.fireWeapon();
            if (bullet != null) {
                bullets.add(bullet);
            }
        }
        if (keyboardController.isEscPressedOnce()) {
            pause();
        }
    }

    private void addGameObject(GameObject object) {
        switch (object.getType()) {
            case PLAYER:
                player1 = (Player) object;
                break;
            case GUARDIAN:
            case CHARGER:
            case SOLDIER:
            case SUPER_SOLDIER:
                enemies.add(object);
                break;
            case BLOCK:
            case END_GAME:
            case ENEMY_PORTAL:
            case GATE:
            case LAVA:
            case CHANGE_LEVEL:
            case CHECKPOINT:
            case HIDING_BLOCK:
                environment.add(object);
                environmentQuadTree.insert(object);
                break;
            case MOVABLE_BLOCK:
                movableEnvironment.add(object);
                break;
            case BIT_ARRAY_GUN:
            case BIT_MATRIX_BLAST:
            case BIT_REVOLVER:
            case BIT_BOT:
                collectibles.add(object);
                break;
            default:
                System.out.println(String.format("Object type is not supported: %s by the Model", object.getType()));
        }
    }
}
