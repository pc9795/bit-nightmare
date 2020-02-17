package game;

import game.framework.Game;
import game.utils.Constants;

import javax.swing.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 21:24
 * Purpose: TODO:
 **/
public class MainWindow {
    public static void main(String[] args) {
        Game game = new Game(Constants.GAME_NAME, Constants.WIDTH, Constants.HEIGHT);
        // Because the Swing library is not thread-safe you should always create and show a JFrame on the Swing event
        // thread. However, the program's main() method is not invoked on the event thread, so it is necessary to use the
        // SwingUtilities class to launch the game window.
        SwingUtilities.invokeLater(game::start);
    }
}
