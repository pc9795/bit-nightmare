package integration;

import game.framework.Model;
import game.objects.Player;
import game.physics.Point2f;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

/**
 * Created By: Prashant Chaubey
 * Created On: 01-03-2020 16:57
 **/
public class ModelTest {
    @Test
    public void testSaveCheckPoint() throws NoSuchFieldException, IllegalAccessException {
        try {
            Model model = new Model(0, 0);
            Player player = new Player(new Point2f(0, 0));
            Field playerField = model.getClass().getDeclaredField("player1");
            playerField.setAccessible(true);
            playerField.set(model, player);
            model.saveCheckpoint(new Point2f(0, 0));

        } catch (IOException | URISyntaxException e) {
            assert false : "Exception occured";
        }
    }
}
