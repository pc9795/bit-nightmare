package game.objects;

import game.framework.Model;
import game.physics.Point2f;
import game.physics.Vector2f;
import game.utils.Constants;

import java.awt.*;

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
public abstract class GameObject {
    public enum GameObjectType {
        BLOCK, LAVA, PLAYER, BIT_BOT, ENEMY1, ENEMY2, ENEMY3, ENEMY_PORTAL, BIT_REVOLVER, BIT_ARRAY_GUN,
        GATE, MOVABLE_BLOCK, BOSS1, BIT_MATRIX_BLAST, HIDING_BLOCK, END_GAME, CHECKPOINT, CHANGE_LEVEL,
        BIT_REVOLVER_BULLET, BIT_ARRAY_GUN_BULLET, BIT_MATRIX_BLAST_BULLET
    }

    public enum FacingDirection {
        RIGHT, LEFT
    }

    public static final float DEFAULT_GRAVITY = 0.1f;
    //Centre of object, using 3D as objects may be scaled
    protected Point2f centre;
    protected int width, height;
    protected boolean hasTextured;
    protected String textureLocation;
    protected Vector2f velocity = new Vector2f(0, 0, 0);
    protected GameObjectType type;
    protected boolean jumping, falling;
    protected FacingDirection facingDirection = FacingDirection.RIGHT;
    protected float gravity = 0f;

    public GameObject(int width, int height, Point2f centre, GameObjectType type) {
        this.width = width;
        this.height = height;
        this.centre = centre;
        this.type = type;
    }

    public GameObject(String textureLocation, int width, int height, Point2f centre, GameObjectType type) {
        this.hasTextured = true;
        this.textureLocation = textureLocation;
        this.width = width;
        this.height = height;
        this.centre = centre;
        this.type = type;
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

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public FacingDirection getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(FacingDirection facingDirection) {
        this.facingDirection = facingDirection;
    }

    public GameObjectType getType() {
        return type;
    }

    public String getTexture() {
        if (hasTextured) {
            return textureLocation;
        }
        return Constants.Sprite.BLANK;
    }

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public abstract void update();

    public abstract void render(Graphics g);

    //todo rename this method; like - sense()
    public abstract void collision(Model model);

    /**
     * Get bounds for collision detection
     *
     * @return bounds for this object
     */
    public Rectangle getBounds() {
        return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
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
