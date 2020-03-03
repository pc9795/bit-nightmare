package game.objects.environment;

import game.framework.Model;
import game.objects.Difficulty;
import game.objects.GameObject;
import game.objects.GameObjectFactory;
import game.properties.Enemy;
import game.physics.Point2f;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 00:02
 * Purpose: A portal which can spawn different enemies
 **/
public class EnemyPortal extends GameObject implements Enemy {
    //Constants
    //ADJUST DIFFICULTY WHEN YOU CHANGE THE DEFAULTS
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private static final int DEFAULT_LOS = 500;
    private static final int DEFAULT_RANGE = 300;
    private static final float DEFAULT_SPAWN_INTERVAL_IN_SEC = 1f;
    private static final int DEFAULT_ENEMY_COUNT = 5;
    //Variables
    private List<GameObjectType> enemyTypes;
    private long lastBeingSpawned;
    private Random random = new Random();
    private int enemyCount;
    private int los;
    private int range;
    private float spawnIntervalInSec;

    private EnemyPortal(Point2f centre) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, centre, GameObjectType.ENEMY_PORTAL);
        enemyTypes = Arrays.asList(GameObjectType.CHARGER, GameObjectType.SOLDIER, GameObjectType.SUPER_SOLDIER);
        los = DEFAULT_LOS;
        range = DEFAULT_RANGE;
        spawnIntervalInSec = DEFAULT_SPAWN_INTERVAL_IN_SEC;
        enemyCount = DEFAULT_ENEMY_COUNT;
    }

    public EnemyPortal(Point2f centre, Difficulty difficulty) {
        this(centre);
        //ADJUST DIFFICULTY WHEN YOU CHANGE THE DEFAULTS
        switch (difficulty) {
            case EASY:
                enemyCount -= 1;
                spawnIntervalInSec += 0.5f;
                break;
            case HARD:
                los += 100;
                range += 100;
                enemyCount += 2;
                spawnIntervalInSec -= 0.5f;
                break;
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        if (texture != null && texture.getIdleRight().length != 0) {
            //THIS PORTION IS TOO MUCH DEPENDENT ON TEXTURE USED.
            g.drawImage(texture.getIdleRight()[0], (int) centre.getX(), (int) centre.getY(), width, height, null);
        } else {
            g.setColor(new Color(196, 255, 14));
            g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }

    @Override
    public void perceiveEnv(Model model) {
        //This portal can't spawn any more enemies
        if (enemyCount == 0) {
            return;
        }
        //Detect player and attack
        attackPlayer(model);
    }

    @Override
    public void attackPlayer(Model model) {
        int playerX = (int) model.getPlayer1().getCentre().getX();
        //Player is out of line of sight.
        if (Math.abs(centre.getX() - playerX) > los) {
            return;
        }
        //Turn to the player's direction
        if (playerX < centre.getX()) {
            facingDirection = FacingDirection.LEFT;
        } else {
            facingDirection = FacingDirection.RIGHT;
        }
        long now = System.currentTimeMillis();
        long diff = now - lastBeingSpawned;
        //Rate limiter
        if (diff <= spawnIntervalInSec * 1000) {
            return;
        }
        //Select an enemy to spawn
        GameObjectType type = enemyTypes.get(random.nextInt(enemyTypes.size()));
        //Decide a spawn location
        float x = centre.getX();
        x -= facingDirection == FacingDirection.LEFT ? random.nextInt(range) : -random.nextInt(range);
        //Create enemy
        GameObject enemy = GameObjectFactory.getGameObject(type, new Point2f(x, centre.getY(), model.getLevelBoundary()), model.getDifficulty());
        model.getEnemies().add(enemy);
        lastBeingSpawned = now;
        enemyCount--;
    }
}
