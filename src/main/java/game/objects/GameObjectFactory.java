package game.objects;

import game.objects.enemies.*;
import game.objects.environment.*;
import game.objects.environment.movables.MovableBlock;
import game.objects.environment.movables.OscillatingBlock;
import game.objects.weapons.BitArrayGun;
import game.objects.weapons.BitMatrixBlast;
import game.objects.weapons.BitRevolver;
import game.physics.Point3f;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:20
 * Purpose: TODO:
 **/
public final class GameObjectFactory {
    public static GameObject getGameObject(GameObject.GameObjectType type, int width, int height, Point3f centre) {
        switch (type) {
            case BOSS1:
                return new Boss1(width, height, centre);
            case ENEMY1:
                return new Enemy1(width, height, centre);
            case ENEMY2:
                return new Enemy2(width, height, centre);
            case ENEMY3:
                return new Enemy3(width, height, centre);
            case ENEMY_PORTAL:
                return new EnemyPortal(width, height, centre);
            case BLOCK:
                return new Block(width, height, centre);
            case END_GAME:
                return new EndGame(width, height, centre);
            case CHANGE_LEVEL:
                return new ChangeLevel(width, height, centre);
            case CHECKPOINT:
                return new Checkpoint(width, height, centre);
            case GATE:
                return new Gate(width, height, centre);
            case LAVA:
                return new Lava(width, height, centre);
            case MOVABLE_BLOCK:
                return new MovableBlock(width, height, centre);
            case OSCILLATING_BLOCK:
                return new OscillatingBlock(width, height, centre);
            case BIT_ARRAY_GUN:
                return new BitArrayGun(width, height, centre);
            case BIT_MATRIX_BLAST:
                return new BitMatrixBlast(width, height, centre);
            case BIT_REVOLVER:
                return new BitRevolver(width, height, centre);
            case BIT_BOT:
                return new BitBot(width, height, centre);
            case PLAYER:
                return new Player(width, height, centre);
        }
        throw new RuntimeException(String.format("%s is not configured in factory", type));
    }
}
