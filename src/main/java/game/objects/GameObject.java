package game.objects;

import game.framework.Model;
import game.framework.visual.Texture;
import game.framework.visual.TextureLoader;
import game.physics.Point2f;
import game.physics.Vector2f;

import java.awt.*;
import java.io.Serializable;

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
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */

/**
 * A class which is the building block for other game objects.
 * <p>
 * REF: https://www.youtube.com/watch?v=LLTCdv2KFkc&list=PLWms45O3n--54U-22GDqKMRGlXROOZtMx&index=3
 * Took inspiration from the mentioned video to create `render`, `update` and `perceiveEnv` methods. A game object is
 * responsible for its own rendering, its own behavior updation, and how it reacts with the world around it. This helps
 * in pin-pointing things as any bug with a particular object can be easily traced back to its class. It de-clutter the
 * model to handle so many objects. It delegates that responsibility.
 */
public abstract class GameObject implements Serializable {
    //Type of game objects.
    public enum GameObjectType {
        BLOCK, LAVA, PLAYER, BIT_BOT, CHARGER, SOLDIER, SUPER_SOLDIER, ENEMY_PORTAL, BIT_REVOLVER, BIT_ARRAY_GUN,
        GATE, MOVABLE_BLOCK, GUARDIAN, BIT_MATRIX_BLAST, HIDING_BLOCK, END_GAME, CHECKPOINT, CHANGE_LEVEL,
        BIT_REVOLVER_BULLET, BIT_ARRAY_GUN_BULLET, BIT_MATRIX_BLAST_BULLET, KEY, DESCRIPTOR
    }

    //Facing direction
    public enum FacingDirection {
        RIGHT, LEFT
    }

    protected static final float DEFAULT_GRAVITY = 0.1f;
    protected static final float CORPSE_REMOVAL_PERIOD_IN_SEC = 2f;
    //Centre of object, using 3D as objects may be scaled
    protected Point2f centre;
    protected int width, height;
    protected Vector2f velocity;
    protected GameObjectType type;
    boolean jumping;
    protected boolean falling;
    protected FacingDirection facingDirection;
    protected float gravity;
    protected transient Texture texture;

    public GameObject(int width, int height, Point2f centre, GameObjectType type) {
        this.width = width;
        this.height = height;
        this.centre = centre;
        this.type = type;
        this.velocity = new Vector2f(0, 0);
        this.gravity = 0f;
        this.facingDirection = FacingDirection.RIGHT;
        this.texture = TextureLoader.getInstance().getTexture(type);
    }

    public Point2f getCentre() {
        return centre;
    }

    public void setCentre(Point2f centre) {
        this.centre = centre;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public GameObjectType getType() {
        return type;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    /**
     * Updates it self
     */
    public abstract void update();

    /**
     * It renders itself
     *
     * @param g graphics object
     */
    public abstract void render(Graphics g);

    /**
     * Game object perceive the game world to take action accordingly.
     *
     * @param model game world
     */
    public abstract void perceiveEnv(Model model);

    /**
     * Get bounds for collision detection
     *
     * @return bounds for this object
     */
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
    }

    public boolean isTextured() {
        return texture != null;
    }

    @Override
    public String toString() {
        return "GameObject{" +
                "centre=" + centre +
                ", width=" + width +
                ", height=" + height +
                ", type=" + type +
                '}';
    }
}
