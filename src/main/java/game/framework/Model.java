package game.framework;

import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.objects.GameObject;
import game.objects.GameObjectFactory;
import game.objects.Player;
import game.objects.weapons.Weapon;
import game.physics.Boundary;
import game.physics.Point2f;
import game.physics.QuadTree;
import game.utils.Constants;
import game.utils.levelloader.Level;
import game.utils.levelloader.LevelLoader;
import game.utils.levelloader.LevelObject;

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
    private Player player1;
    private List<GameObject> enemies;
    private QuadTree environmentQuadTree;
    //Immovable things
    private List<GameObject> environment;
    //Movable things
    private List<GameObject> movableEnvironment;
    private List<GameObject> collectibles;
    private List<GameObject> bullets;
    private List<String> levels;
    private Point2f lastCheckpoint;
    private Boundary levelBoundary;
    private String currentLevel;

    public Model(int width, int height) throws IOException, URISyntaxException {
        this.enemies = new CopyOnWriteArrayList<>();
        //It will not be modified therefore ArrayList
        this.environment = new ArrayList<>();
        this.collectibles = new CopyOnWriteArrayList<>();
        //It will not be modified therefore ArrayList
        this.levels = new ArrayList<>();
        this.environmentQuadTree = new QuadTree(new Rectangle(width, height));
        this.movableEnvironment = new ArrayList<>();
        this.bullets = new CopyOnWriteArrayList<>();
        this.levelBoundary = new Boundary(width, height);
        init();

    }

    private void init() throws URISyntaxException, IOException {
        URI uri = Model.class.getResource(Constants.Level.INFO_FILE_NAME).toURI();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(uri))) {
            String line;
            while ((line = br.readLine()) != null) {
                levels.add(line.trim());
            }
        }
        if (levels.size() == 0) {
            throw new RuntimeException("No levels to load");
        }
        loadLevel(levels.get(0));
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

    public Point2f getLastCheckpoint() {
        return lastCheckpoint;
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

    private void loadLevel(String levelName) throws IOException {
        clean();
        Level level = LevelLoader.getInstance().loadLevel(levelName);
        loadLevelUtil(level);
        currentLevel = levelName;
    }

    private void loadLevel(String levelName, Point2f lastCheckpoint) throws IOException {
        clean();
        Level level = LevelLoader.getInstance().loadLevel(levelName);
        loadLevelUtil(level);
        currentLevel = levelName;
        player1.setCentre(lastCheckpoint.copy());
    }

    private void loadLevelUtil(Level level) {
        levelBoundary = new Boundary(level.getMaxX() * Constants.Level.PIXEL_TO_WIDTH_RATIO,
                level.getMaxY() * Constants.Level.PIXEL_TO_WIDTH_RATIO);
        // QuadTree is to be initialized by the new boundaries.
        environmentQuadTree = new QuadTree(0, new Rectangle(0, 0, (int) levelBoundary.getxMax(), (int) levelBoundary.getyMax()));

        for (LevelObject object : level.getLevelObjects()) {
            GameObject.GameObjectType type = GameObject.GameObjectType.valueOf(object.getType());
            Point2f center = new Point2f(object.getCentre().getX() * Constants.Level.PIXEL_TO_WIDTH_RATIO,
                    object.getCentre().getY() * Constants.Level.PIXEL_TO_WIDTH_RATIO, levelBoundary);
            GameObject gameObject = GameObjectFactory.getGameObject(type, object.getWidth(), object.getHeight(), center);
            addGameObject(gameObject);
        }
        if (player1 == null) {
            throw new RuntimeException("No player found in the level.");
        }
        lastCheckpoint = player1.getCentre().copy();
    }

    private void clean() {
        enemies.clear();
        environmentQuadTree.clear();
        environment.clear();
        collectibles.clear();
        bullets.clear();
        movableEnvironment.clear();
        player1 = null;
    }

    /**
     * This is the heart of the game , where the model takes in all the inputs ,decides the outcomes and then changes the model accordingly.
     */
    public void gameLogic() {
        //Less than 0 for weird off by one error.
        if (player1.getHealth() <= 0) {
            //todo saving state; collectibles(ex - bit bot found, weapons)
            //todo remove
            boolean bitBotFound = player1.isBitBotFound();
            List<Weapon> weapons = player1.getWeapons();
            int currentWeaponIndex = player1.getCurrentWeaponIndex();
            try {
                loadLevel(currentLevel, lastCheckpoint);
                //todo remove
                player1.setBitBotFound(bitBotFound);
                player1.setWeapons(weapons);
                player1.setCurrentWeaponIndex(currentWeaponIndex);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Error while loading checkpoing: %s on level: %s", lastCheckpoint, currentLevel));
            }
        }
        processInput();
        //Player
        player1.update();
        player1.collision(this);
        //Bullets
        bullets.forEach(GameObject::update);
        bullets.forEach(object -> object.collision(this));
        //Environment
        environment.forEach(GameObject::update);
        environment.forEach(object -> object.collision(this));
        //Movable environment
        movableEnvironment.forEach(GameObject::update);
        movableEnvironment.forEach(object -> object.collision(this));
        //Enemies
        enemies.forEach(GameObject::update);
        enemies.forEach(object -> object.collision(this));
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
            player1.getVelocity().setX(-Constants.PLAYER_VELOCITY_X);
            player1.setFacingDirection(GameObject.FacingDirection.LEFT);
        } else if (keyboardController.isDPressed() || gamepadController.isRightPressed()) {
            player1.getVelocity().setX(Constants.PLAYER_VELOCITY_X);
            player1.setFacingDirection(GameObject.FacingDirection.RIGHT);
        } else {
            float velX = player1.getVelocity().getX();
            if (velX > 0) {
                player1.setFacingDirection(GameObject.FacingDirection.RIGHT);
            } else if (velX < 0) {
                player1.setFacingDirection(GameObject.FacingDirection.LEFT);
            }
            player1.getVelocity().setX(0);
        }
        //Jump
        if ((keyboardController.isWPressedOnce() || gamepadController.isAPressedOnce())
                && !player1.isJumping() && player1.isBitBotFound()) {
            player1.getVelocity().setY(-Constants.PLAYER_VELOCITY_Y);
            player1.setJumping(true);
        }
        //Duck
        if (keyboardController.isSPressedOnce() || gamepadController.isXPressedOnce()) {
            if (player1.isDucking()) {
                //todo check that here I am not resetting y-axis. I think code is working fine because of bottom collision.
                player1.setHeight(player1.getHeight() * 2);
                player1.setDucking(false);
            } else {
                player1.getCentre().setY(player1.getCentre().getY() + player1.getHeight() / 2);
                player1.setHeight(player1.getHeight() / 2);
                player1.setDucking(true);
            }
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
    }

    private void addGameObject(GameObject object) {
        switch (object.getType()) {
            case PLAYER:
                player1 = (Player) object;
                break;
            case BOSS1:
            case ENEMY1:
            case ENEMY2:
            case ENEMY3:
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
