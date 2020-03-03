package game.colliders;

import game.objects.GameObject;

import java.awt.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 22:17
 * Purpose: Detect collision in specific direction.
 * <p>
 * REF: https://www.youtube.com/watch?v=mdJNtXP1QiY&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=7
 * Used the mentioned video as a reference for creating 4 boundaries to finely detect collision. In the video this thing
 * was hardcoded for a 'Player' object. I have implemented it as a behavior by extracting it in an interface so that any
 * game object can use this. I have adjusted what happens at what boundary according to my needs and little bit
 * experimentation.
 **/
public interface FineGrainedCollider {
    int BOTTOM = 1;
    int LEFT = 2;
    int RIGHT = 3;

    /**
     * This method will do a fine grained collision for a specific boundary.
     *
     * @param o1 the object that is to be checked
     * @param o2 the object to which `o1` is going to be collided.
     * @return collisions a boolean array of length 4 which can be used to identify which boundary is colliding.
     * The boolean array indexes signify top, bottom, left, or right collision in the order.
     */
    default boolean[] fineGrainedCollision(GameObject o1, GameObject o2) {
        boolean[] collisions = new boolean[4];
        Rectangle bounds = o2.getBounds();

        if (bounds.intersects(getBoundsBottom())) {
            //This value of offset(1) is added to prevent oscillations.
            o1.getCentre().setY(o2.getCentre().getY() - o1.getHeight() + 1);
            o1.getVelocity().setY(0);
            o1.setFalling(false);
            o1.setJumping(false);
            collisions[1] = true;
        } else if (bounds.intersects(getBoundsTop())) {
            //This value of offset(2) is added to give a pushing effect from the object at the top.
            o1.getCentre().setY(o2.getCentre().getY() + o2.getWidth() + 2);
            o1.getVelocity().setY(0);
            collisions[0] = true;
        } else if (bounds.intersects(getBoundsLeft())) {
            o1.getCentre().setX(o2.getCentre().getX() + o2.getWidth());
            collisions[2] = true;
        } else if (bounds.intersects(getBoundsRight())) {
            o1.getCentre().setX(o2.getCentre().getX() - o1.getWidth());
            collisions[3] = true;
        }
        return collisions;
    }

    Rectangle getBoundsLeft();

    Rectangle getBoundsRight();

    Rectangle getBoundsTop();

    Rectangle getBoundsBottom();
}
