import game.framework.Model;
import game.objects.GameObject;
import game.physics.Point2f;
import game.physics.QuadTree;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created By: Prashant Chaubey
 * Created On: 21-02-2020 13:40
 **/
public class QuadTreeTest {
    @Test
    public void testChop_ObjectLiesIn2ndAnd3rdQuadrant() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QuadTree tree = new QuadTree( new Rectangle(100, 100));
        TestGameObject obj = new TestGameObject(10, 50, new Point2f(10, 10), null);
        Method method = tree.getClass().getDeclaredMethod("chop", GameObject.class);
        method.setAccessible(true);

        GameObject[] parts = (GameObject[]) method.invoke(tree, obj);

        //Right now in the actual game everything works on reference equality so there is no equals method implementation.
        //In future then these things can be cleaned up a little.
        assert parts[0] == null;
        assert parts[1] != null;
        assert parts[1].getCentre().getX() == 10;
        assert parts[1].getCentre().getY() == 10;
        assert parts[1].getWidth() == 10;
        assert parts[1].getHeight() == 40;

        assert parts[2] != null;
        assert parts[2].getCentre().getX() == 10;
        assert parts[2].getCentre().getY() == 50;
        assert parts[2].getWidth() == 10;
        assert parts[2].getHeight() == 10;
        assert parts[3] == null;
    }

    @Test
    public void testChop_ObjectInCentre() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        QuadTree tree = new QuadTree( new Rectangle(100, 100));
        TestGameObject obj = new TestGameObject(50, 50, new Point2f(10, 10), null);
        Method method = tree.getClass().getDeclaredMethod("chop", GameObject.class);
        method.setAccessible(true);

        GameObject[] parts = (GameObject[]) method.invoke(tree, obj);

        assert parts[0] != null;
        assert parts[0].getCentre().getX() == 50;
        assert parts[0].getCentre().getY() == 10;
        assert parts[0].getWidth() == 10;
        assert parts[0].getHeight() == 40;

        assert parts[1] != null;
        assert parts[1].getCentre().getX() == 10;
        assert parts[1].getCentre().getY() == 10;
        assert parts[1].getWidth() == 40;
        assert parts[1].getHeight() == 40;

        assert parts[2] != null;
        assert parts[2].getCentre().getX() == 10;
        assert parts[2].getCentre().getY() == 50;
        assert parts[2].getWidth() == 40;
        assert parts[2].getHeight() == 10;

        assert parts[3] != null;
        assert parts[3].getCentre().getX() == 50;
        assert parts[3].getCentre().getY() == 50;
        assert parts[3].getWidth() == 10;
        assert parts[3].getHeight() == 10;
    }

    /**
     * GameObject implementation for testing.
     */
    public static class TestGameObject extends GameObject {

        TestGameObject(int width, int height, Point2f centre, GameObjectType type) {
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
