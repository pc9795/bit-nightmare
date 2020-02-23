package game.physics;

import game.framework.Model;
import game.objects.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 20:15
 * Purpose: Quad tree for game objects
 **/
public class QuadTree {
    //How many objects a node can hold
    private static final int MAX_OBJECTS = 10;
    //Deepest level of the sub-node.
    private static final int MAX_LEVELS = 5;

    //current node level; 0 is the root node
    private int level;
    private List<GameObject> objects;
    //2d-space occupied by the node
    private Rectangle bounds;
    //Child index represents the quadrant which it represents
    private QuadTree[] children;

    public QuadTree(Rectangle bounds) {
        this.level = 0;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.children = new QuadTree[4];
    }

    private QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.children = new QuadTree[4];
    }

    /**
     * Clears the quad tree recursively.
     * As we don't provide access to the child quad trees this operation is expected to be performed from root only.
     */
    public void clear() {
        objects.clear();
        //Either there will be all children or none of them because we can't clear quad tree partially.
        if (!isSplitted()) {
            return;
        }
        for (int i = 0; i < children.length; i++) {
            children[i].clear();
            children[i] = null;
        }
    }

    /**
     * Initialize child nodes
     */
    private void split() {
        int width = (int) (bounds.getWidth() / 2);
        int height = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        //As rectangle constructor stores only integer values for x and y, a bound with odd values of width and height
        //will have a leftover layer of pixels which will not be stored inside any children. Consequently, it will reside
        //in the parent node.
        children[0] = new QuadTree(level + 1, new Rectangle(x + width, y, width, height));
        children[1] = new QuadTree(level + 1, new Rectangle(x, y, width, height));
        children[2] = new QuadTree(level + 1, new Rectangle(x, y + height, width, height));
        children[3] = new QuadTree(level + 1, new Rectangle(x + height, y + height, width, height));
    }

    /**
     * Check whether children are initialized or not
     *
     * @return true if children are initialized
     */
    private boolean isSplitted() {
        //Either there will be all children or none of them because we can't clear quad tree partially.
        return children[0] != null;
    }

    /**
     * Get the index of the child where the given rectangle resides
     *
     * @param boundsToCheck rectangle to check
     * @return index of the child which in turn represents the quadrant where the given rectangle resides. Returns -1 if
     * the rectangle doesn't COMPLETELY fits in any of the children.
     */
    private int getIndex(Rectangle boundsToCheck) {
        double xMid = bounds.getX() + (bounds.getWidth() / 2);
        double yMid = bounds.getY() + (bounds.getHeight() / 2);
        boolean upperHalf = false;
        boolean lowerHalf = false;

        if (boundsToCheck.getY() < yMid && boundsToCheck.getY() + boundsToCheck.getHeight() < yMid) {
            upperHalf = true;
        }
        if (boundsToCheck.getY() > yMid) {
            lowerHalf = true;
        }
        if (boundsToCheck.getX() < xMid && boundsToCheck.getX() + boundsToCheck.getWidth() < xMid) {
            if (upperHalf) {
                return 1;
            }
            if (lowerHalf) {
                return 2;
            }
        }
        if (boundsToCheck.getX() > xMid) {
            if (upperHalf) {
                return 0;
            }
            if (lowerHalf) {
                return 3;
            }
        }
        return -1;
    }

    /**
     * Transfer some node to child. It will create children if not there already.
     */
    private void shred() {
        if (objects.size() <= MAX_OBJECTS || level >= MAX_LEVELS) {
            return;
        }
        if (!isSplitted()) {
            split();
        }
        for (int i = 0; i < objects.size(); ) {
            int index = getIndex(objects.get(i).getBounds());
            //Can't fit to any child.
            if (index == -1) {
                i++;
                continue;
            }
            children[index].insert(objects.remove(i));
        }
    }

    /**
     * Insert the given object in the tree.
     *
     * @param object object to insert in the quad tree.
     */
    public void insert(GameObject object) {
        //If splitted check the possibility of insertion in the children
        if (isSplitted()) {
            int index = getIndex(object.getBounds());
            if (index != -1) {
                children[index].insert(object);
                return;
            }
        }
        objects.add(object);
        shred();
    }

    /**
     * Retrieve the neighbors for a given object
     *
     * @param object object whose neighbors to be found
     * @return neighbors for the given object.
     */
    public List<GameObject> retrieve(GameObject object) {
        List<GameObject> willCollide = new ArrayList<>();
        retrieveUtil(willCollide, object);
        return willCollide;
    }

    private void retrieveUtil(List<GameObject> willCollide, GameObject object) {
        int index = getIndex(object.getBounds());
        if (index != -1 && isSplitted()) {
            children[index].retrieveUtil(willCollide, object);
        } else {
            //They don't belong to a single children then we break the object apart
            GameObject[] parts = chop(object);
            for (int i = 0; i < 4; i++) {
                if (parts[i] == null || children[i] == null) {
                    continue;
                }
                children[i].retrieveUtil(willCollide, parts[i]);
            }
        }
        willCollide.addAll(objects);
    }

    /**
     * Chop objects around the two centred axis.
     *
     * @param object object to be chopped
     * @return an array of length 4 where the index represents the part of the given object in the corresponding quadrant
     */
    private GameObject[] chop(GameObject object) {
        GameObject[] parts = new GameObject[4];
        Rectangle objectBounds = object.getBounds();
        int xMid = (int) (bounds.getX() + (bounds.getWidth() / 2));
        int yMid = (int) (bounds.getY() + (bounds.getHeight() / 2));

        //Check for part in second quadrant
        int x1 = (int) objectBounds.getX();
        int x2 = (int) Math.min(x1 + objectBounds.getWidth(), xMid);
        int y1 = (int) objectBounds.getY();
        int y2 = (int) Math.min(y1 + objectBounds.getHeight(), yMid);
        //If valid
        if (x2 > x1 && y2 > y1) {
            parts[1] = new GameObjectPart(x2 - x1, y2 - y1, new Point2f(x1, y1), null);
        }
        //Check for part in first quadrant
        x1 = xMid;
        x2 = (int) (objectBounds.getX() + objectBounds.getWidth());
        y1 = (int) objectBounds.getY();
        y2 = (int) Math.min(y1 + objectBounds.getHeight(), yMid);
        //If valid
        if (x2 > x1 && y2 > y1) {
            parts[0] = new GameObjectPart(x2 - x1, y2 - y1, new Point2f(x1, y1), null);
        }
        //Check for part in third quadrant
        x1 = (int) objectBounds.getX();
        x2 = (int) Math.min(x1 + objectBounds.getWidth(), xMid);
        y1 = yMid;
        y2 = (int) (objectBounds.getY() + objectBounds.getHeight());
        //If valid
        if (x2 > x1 && y2 > y1) {
            parts[2] = new GameObjectPart(x2 - x1, y2 - y1, new Point2f(x1, y1), null);
        }
        //Check for part in fourth quadrant
        x1 = xMid;
        x2 = (int) (objectBounds.getX() + objectBounds.getWidth());
        y1 = yMid;
        y2 = (int) (objectBounds.getY() + objectBounds.getHeight());
        //If valid
        if (x2 > x1 && y2 > y1) {
            parts[3] = new GameObjectPart(x2 - x1, y2 - y1, new Point2f(x1, y1), null);
        }
        return parts;
    }

    private static class GameObjectPart extends GameObject {

        GameObjectPart(int width, int height, Point2f centre, GameObjectType type) {
            super(width, height, centre, type);
        }

        @Override
        public void update() {
        }

        @Override
        public void render(Graphics g) {
        }

        @Override
        public void perceiveEnv(Model model) {
        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }
}
