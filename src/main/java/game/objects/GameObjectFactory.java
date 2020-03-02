package game.objects;

import game.objects.enemies.Charger;
import game.objects.enemies.Guardian;
import game.objects.enemies.Soldier;
import game.objects.enemies.SuperSoldier;
import game.objects.environment.*;
import game.objects.environment.movables.MovableBlock;
import game.objects.weapons.BitArrayGun;
import game.objects.weapons.BitMatrixBlast;
import game.objects.weapons.BitRevolver;
import game.physics.Point2f;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 00:20
 * Purpose: Factory to generate game objects
 **/
public final class GameObjectFactory {
    public static GameObject getGameObject(GameObject.GameObjectType type, Point2f centre, Difficulty difficulty) {
        switch (type) {
            case GUARDIAN:
                return new Guardian(centre, difficulty);
            case CHARGER:
                return new Charger(centre, difficulty);
            case SOLDIER:
                return new Soldier(centre, difficulty);
            case SUPER_SOLDIER:
                return new SuperSoldier(centre, difficulty);
            case ENEMY_PORTAL:
                return new EnemyPortal(centre, difficulty);
            case BLOCK:
                return new Block(centre);
            case END_GAME:
                return new EndGame(centre);
            case CHANGE_LEVEL:
                return new ChangeLevel(centre);
            case CHECKPOINT:
                return new Checkpoint(centre);
            case GATE:
                return new Gate(centre);
            case LAVA:
                return new Lava(centre);
            case MOVABLE_BLOCK:
                return new MovableBlock(centre);
            case HIDING_BLOCK:
                return new HidingBlock(centre);
            case BIT_ARRAY_GUN:
                return new BitArrayGun(centre);
            case BIT_MATRIX_BLAST:
                return new BitMatrixBlast(centre);
            case BIT_REVOLVER:
                return new BitRevolver(centre);
            case BIT_BOT:
                return new BitBot(centre);
            case PLAYER:
                return new Player(centre);
            case KEY:
                return new Key(centre);
        }
        throw new RuntimeException(String.format("%s is not configured in factory", type));
    }
}
