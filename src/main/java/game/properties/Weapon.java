package game.properties;

import game.objects.GameObject;
import game.physics.Point2f;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 18-02-2020 22:23
 * Purpose: An interface for defining behavior of a weapon.
 **/
public interface Weapon {
    GameObject fire(Point2f position, GameObject.FacingDirection direction);
}
