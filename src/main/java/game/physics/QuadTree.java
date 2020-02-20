package game.physics;

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
    private int MAX_OBJECTS = 10;
    //Deepest level of the sub-node.
    private int MAX_LEVELS = 0;

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
     * Clears the quad tree recursively.
     */
    public void clear() {
        objects.clear();
        for (int i = 0; i < children.length; i++) {
            if (children[i] == null) {
                continue;
            }
            children[i].clear();
            children[i] = null;
        }
    }

    public void split() {
        int width = (int) (bounds.getWidth() / 2);
        int height = (int) (bounds.getHeight() / 2);
        int x = (int) bounds.getX();
        int y = (int) bounds.getY();
        children[0] = new QuadTree(level + 1, new Rectangle(x + width, y, width, height));
        children[1] = new QuadTree(level + 1, new Rectangle(x, y, width, height));
        children[2] = new QuadTree(level + 1, new Rectangle(x, y + height, width, height));
        children[3] = new QuadTree(level + 1, new Rectangle(x + height, y + height, width, height));
    }

    public boolean isSplitted() {
        return children[0] != null;
    }

    public int getIndex(Rectangle boundsToCheck) {
        int index = -1;
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
        }
        willCollide.addAll(objects);
    }
}
