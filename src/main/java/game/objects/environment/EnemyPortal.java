package game.objects.environment;

import game.framework.Model;
import game.objects.GameObject;
import game.objects.GameObjectFactory;
import game.physics.Point2f;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:02
 * Purpose: TODO:
 **/
public class EnemyPortal extends GameObject {
    private static final int DEFAULT_WIDTH = 64;
    private static final int DEFAULT_HEIGHT = 64;
    private static final int DEFAULT_LOS = 400;
    private static final int DEFAULT_RANGE = 300;
    private static final float DEFAULT_SPAWN_FREQ_IN_SEC = 1f;
    private List<GameObjectType> enemyTypes;
    private long lastBeingSpawned;
    private Random random = new Random();
    private int enemyCount = 5;
    private int los;
    private int range;
    private float spawnFreqInSec;

    public EnemyPortal(int width, int height, Point2f centre) {
        super(width, height, centre, GameObjectType.ENEMY_PORTAL);
        enemyTypes = Arrays.asList(GameObjectType.ENEMY1, GameObjectType.ENEMY2, GameObjectType.ENEMY3);
        los = DEFAULT_LOS;
        range = DEFAULT_RANGE;
        spawnFreqInSec = DEFAULT_SPAWN_FREQ_IN_SEC;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(new Color(196, 255, 14));
        g.fillRect((int) centre.getX(), (int) centre.getY(), width, height);
    }

    @Override
    public void collision(Model model) {
        if (enemyCount == 0) {
            return;
        }
        //Detect player and attack
        int playerX = (int) model.getPlayer1().getCentre().getX();
        if (Math.abs(centre.getX() - playerX) <= los) {
            if (playerX < centre.getX()) {
                facingDirection = FacingDirection.LEFT;
            } else {
                facingDirection = FacingDirection.RIGHT;
            }
            //Rate limiter; one bullet per second.
            long now = System.currentTimeMillis();
            long diff = now - lastBeingSpawned;
            if (diff > spawnFreqInSec * 1000) {
                GameObjectType type = enemyTypes.get(random.nextInt(enemyTypes.size()));
                //todo fix this.
                int width = 32, height = 0;
                switch (type) {
                    case ENEMY1:
                        height = 32;
                        break;
                    case ENEMY2:
                    case ENEMY3:
                        height = 64;
                        break;
                }
                float x = centre.getX();
                int position = random.nextInt(range);
                x -= facingDirection == FacingDirection.LEFT ? position : -position;
                GameObject enemy = GameObjectFactory.getGameObject(type, width, height, new Point2f(x, centre.getY(), model.getLevelBoundary()));
                model.getEnemies().add(enemy);
                lastBeingSpawned = now;
                enemyCount--;
            }
        }
    }
}
