package game.physics;

import game.framework.Model;
import game.objects.GameObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 18-02-2020 20:15
 * Purpose: TODO:
 **/
public class QuadTree {
    //How many objects a node can hold
    private static int MAX_OBJECTS = 10;
    //Deepest level of the sub-node.
    private static int MAX_LEVELS = 5;

    //current node level; 0 is the root node
    private int level;
    private List<GameObject> objects;
    //2d-space occupied by the node
    private Rectangle bounds;
    private QuadTree[] children;

    public QuadTree(int level, Rectangle bounds) {
        this.level = level;
        this.objects = new ArrayList<>();
        this.bounds = bounds;
        this.children = new QuadTree[4];
    }

    /**
     * Clears the quad tree recursively. A point to notice is that neither we provide access to the child quad trees nor
     * we clear the quad tree partially. That's why this operation is expected to be performed from root only. As we are
     * assuming that either all children will be present or not.
     */
    public void clear() {
        objects.clear();
        if (!isSplitted()) {
            return;
        }
        for (int i = 0; i < children.length; i++) {
            children[i].clear();
            children[i] = null;
        }
    }

    private void split() {
        int width = (int) (bounds.getWidth() / 2);
        int height = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        //As rectangle constructor stores only integer values for x and y, a bound with odd values of width and height
        //will have a leftover layer of pixels which will not be stored inside any children. It will reside in the parent
        //node.
        children[0] = new QuadTree(level + 1, new Rectangle(x + width, y, width, height));
        children[1] = new QuadTree(level + 1, new Rectangle(x, y, width, height));
        children[2] = new QuadTree(level + 1, new Rectangle(x, y + height, width, height));
        children[3] = new QuadTree(level + 1, new Rectangle(x + height, y + height, width, height));
    }

    private boolean isSplitted() {
        return children[0] != null;
    }

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

    private void sizeCheck() {
        if (objects.size() <= MAX_OBJECTS || level >= MAX_LEVELS) {
            return;
        }
        if (!isSplitted()) {
            split();
        }
        for (int i = 0; i < objects.size(); ) {
            int index = getIndex(objects.get(i).getBounds());
            if (index == -1) {
                i++;
                continue;
            }
            children[index].insert(objects.remove(i));
        }
    }

    public void insert(GameObject object) {
        if (isSplitted()) {
            int index = getIndex(object.getBounds());
            if (index != -1) {
                children[index].insert(object);
                return;
            }
        }
        objects.add(object);
        sizeCheck();
    }

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
        if (x2 > x1 && y2 > y1) {
            parts[1] = new TempGameObject(x2 - x1, y2 - y1, new Point3f(x1, y1), null);
        }
        //Check for part in first quadrant
        x1 = xMid;
        x2 = (int) (objectBounds.getX() + objectBounds.getWidth());
        y1 = (int) objectBounds.getY();
        y2 = (int) Math.min(y1 + objectBounds.getHeight(), yMid);
        if (x2 > x1 && y2 > y1) {
            parts[0] = new TempGameObject(x2 - x1, y2 - y1, new Point3f(x1, y1), null);
        }
        //Check for part in third quadrant
        x1 = (int) objectBounds.getX();
        x2 = (int) Math.min(x1 + objectBounds.getWidth(), xMid);
        y1 = yMid;
        y2 = (int) (objectBounds.getY() + objectBounds.getHeight());
        if (x2 > x1 && y2 > y1) {
            parts[2] = new TempGameObject(x2 - x1, y2 - y1, new Point3f(x1, y1), null);
        }
        //Check for part in fourth quadrant
        x1 = xMid;
        x2 = (int) (objectBounds.getX() + objectBounds.getWidth());
        y1 = yMid;
        y2 = (int) (objectBounds.getY() + objectBounds.getHeight());
        if (x2 > x1 && y2 > y1) {
            parts[3] = new TempGameObject(x2 - x1, y2 - y1, new Point3f(x1, y1), null);
        }
        return parts;
    }

    private static class TempGameObject extends GameObject {

        TempGameObject(int width, int height, Point3f centre, GameObjectType type) {
            super(width, height, centre, type);
        }

        @Override
        public void update() {
        }

        @Override
        public void render(Graphics g) {
        }

        @Override
        public void collision(Model model) {
        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle((int) centre.getX(), (int) centre.getY(), width, height);
        }
    }
}
