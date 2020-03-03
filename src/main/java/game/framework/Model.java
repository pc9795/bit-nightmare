package game.framework;

import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.framework.controllers.MouseController;
import game.framework.levelloader.Level;
import game.framework.levelloader.LevelLoader;
import game.framework.levelloader.LevelObject;
import game.objects.*;
import game.objects.weapons.Weapon;
import game.physics.Boundary;
import game.physics.Point2f;
import game.physics.QuadTree;
import game.utils.Constants;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private Boundary levelBoundary;
    private int currLevelIndex;
    private boolean pause, started, completed;
    private Point2f lastCheckPointSaved;
    private Difficulty difficulty;
    private Descriptor descriptor;

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
        this.descriptor = new Descriptor(GameObject.GameObjectType.DESCRIPTOR);
    }

    public Difficulty getDifficulty() {
        return difficulty;
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

    public Descriptor getDescriptor() {
        return descriptor;
    }

    public String getCurrentLevel() {
        return levels.get(currLevelIndex);
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void nextLevel() {
        if (currLevelIndex == levels.size() - 1) {
            System.out.println("!!!WARNING!!!Next level requested at the last level.");
            return;
        }
        try {
            //Can look for a better way to do it. Currently changing levels can't be saved.
            //Caching previous state
            boolean bitBotFound = player1.isBitBotFound();
            List<Weapon> weapons = player1.getWeapons();
            int currWeaponIndex = player1.getCurrentWeaponIndex();
            //Loading next level
            loadLevel(currLevelIndex + 1, difficulty);
            //Setting the cached state.
            player1.setBitBotFound(bitBotFound);
            player1.setWeapons(weapons);
            player1.setCurrentWeaponIndex(currWeaponIndex);
            //Saving checkpoint on next level.
            saveCheckpoint(player1.getCentre().copy());

        } catch (IOException e) {
            System.out.println(String.format("Unable to load level at index:%s from levels:%s", currLevelIndex + 1, levels));
            e.printStackTrace();
        }
    }

    public void newGame(Difficulty difficulty) throws IOException {
        loadLevel(0, difficulty);
        saveCheckpoint(getPlayer1().getCentre().copy());
    }

    /**
     * Load a level
     *
     * @param index index of the level
     * @throws IOException not able to load level files
     */
    private void loadLevel(int index, Difficulty difficulty) throws IOException {
        clean();
        Level level = LevelLoader.getInstance().loadLevel(levels.get(index));
        loadLevelUtil(level, difficulty);
        this.difficulty = difficulty;
        this.currLevelIndex = index;
        this.started = true;
        this.pause = false;
    }

    /**
     * Convert level object into game objects
     *
     * @param level level object
     */
    private void loadLevelUtil(Level level, Difficulty difficulty) {
        this.levelBoundary = new Boundary(level.getMaxX() * Constants.Level.PIXEL_TO_WIDTH_RATIO,
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
            GameObject gameObject = GameObjectFactory.getGameObject(type, center, difficulty);
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

    private Path savePath() throws IOException {
        Path saveDirectory = Paths.get(Paths.get(System.getProperty("user.home")).toString(), Constants.SAVE_DIRECTORY_NAME);
        if (!Files.isDirectory(saveDirectory)) {
            Files.createDirectory(saveDirectory);
        }
        return Paths.get(saveDirectory.toString(), Constants.SAVE_FILE_NAME);
    }

    public void saveCheckpoint(Point2f checkPointLocation) {
        //If player is standing on the same checkpoint.
        if (lastCheckPointSaved != null && (lastCheckPointSaved.getX() == checkPointLocation.getX() && lastCheckPointSaved.getY() == checkPointLocation.getY())) {
            return;
        }
        try {
            Path savePath = savePath();
            //Auto closing
            try (FileOutputStream fos = new FileOutputStream(savePath.toFile()); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(toCheckpoint());
            }
            //Expecting that I am getting a copy so not calling copy().
            lastCheckPointSaved = checkPointLocation;
        } catch (Exception exc) {
            System.out.println("Not able to save checkpoint");
            exc.printStackTrace();
        }
    }

    public boolean isLastCheckpointAvailable() {
        Path savePath;
        try {
            savePath = savePath();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return Files.exists(savePath);
    }

    /**
     * Load a last checkpoint
     */
    public void loadLastCheckPoint() {
        Checkpoint checkpoint;
        try {
            //If no check point then load the current level.
            if (!isLastCheckpointAvailable()) {
                assert difficulty != null : "Difficulty can't be null at this point";
                loadLevel(currLevelIndex, difficulty);
                return;
            }

            Path savePath = savePath();
            //Auto closing
            try (FileInputStream fis = new FileInputStream(savePath.toFile()); ObjectInputStream ois = new ObjectInputStream(fis)) {
                checkpoint = (Checkpoint) ois.readObject();
            }

            loadLevel(checkpoint.currLevelIndex, checkpoint.difficulty);
            //Configuring player attributes.
            player1.setCentre(checkpoint.player1.getCentre());
            player1.setBitBotFound(checkpoint.player1.isBitBotFound());
            player1.setWeapons(checkpoint.player1.getWeapons());
            player1.setCurrentWeaponIndex(checkpoint.player1.getCurrentWeaponIndex());

            //Removing collectibles which wre already found. This code looks like mess but right now I think it is
            //the best way to do it may be can clean in future.
            //Checking which weapon types are found.
            Set<GameObject.GameObjectType> weaponTypes = new HashSet<>();
            checkpoint.player1.getWeapons().forEach(weapon -> weaponTypes.add(((GameObject) weapon).getType()));
            for (int i = 0; i < collectibles.size(); ) {
                GameObject object = collectibles.get(i);
                //Bit bot is already found then removing from collectibles.
                if (object.getType() == GameObject.GameObjectType.BIT_BOT && checkpoint.player1.isBitBotFound()) {
                    collectibles.remove(i);
                    continue;
                }
                //Remove weapons which are previously found.
                if (weaponTypes.contains(object.getType())) {
                    collectibles.remove(i);
                    continue;
                }
                i++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while loading checkpoint", e);
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
        //Descriptor
        descriptor.update();
        descriptor.perceiveEnv(this);
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
        MouseController.getInstance().poll();

        //Left and right
        if (keyboardController.isAPressed() || gamepadController.isHatSwitchLeftPressed()) {
            player1.moveLeft();
        } else if (keyboardController.isDPressed() || gamepadController.isHatSwitchRightPressed()) {
            player1.moveRight();
        } else {
            player1.makeIdle();
        }
        //Jump
        if ((keyboardController.isWPressedOnce() || gamepadController.isHatSwitchUpPressedOnce())) {
            player1.jump();
        }
        //Duck
        if (keyboardController.isSPressedOnce() || gamepadController.isHatSwitchDownPressedOnce()) {
            player1.toggleDuck();
        }
        //Cycle weapon
        if (keyboardController.isQPressedOnce() || gamepadController.isXPressedOnce()) {
            player1.cycleWeapon();
        }
        //Fire weapon
        if (keyboardController.isSpacePressedOnce() || gamepadController.isAPressedOnce()) {
            GameObject bullet = player1.fireWeapon();
            if (bullet != null) {
                bullets.add(bullet);
            }
        }
        if (keyboardController.isEscPressedOnce() || gamepadController.isStartPressedOnce()) {
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
            case KEY:
                collectibles.add(object);
                break;
            default:
                System.out.println(String.format("Object type is not supported: %s by the Model", object.getType()));
        }
    }

    private Checkpoint toCheckpoint() {
        return new Checkpoint(player1, currLevelIndex, difficulty);
    }

    private static class Checkpoint implements Serializable {
        private Player player1;
        private int currLevelIndex;
        private Difficulty difficulty;

        public Checkpoint(Player player1, int currLevelIndex, Difficulty difficulty) {
            this.player1 = player1;
            this.currLevelIndex = currLevelIndex;
            this.difficulty = difficulty;
        }
    }
}
