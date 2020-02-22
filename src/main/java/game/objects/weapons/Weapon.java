package game.objects.weapons;

import game.objects.GameObject;
import game.physics.Point2f;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 22:23
 * Purpose: TODO:
 **/
public interface Weapon {
    GameObject fire(Point2f position, GameObject.FacingDirection direction);
}
