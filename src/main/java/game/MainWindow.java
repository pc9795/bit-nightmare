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
        SwingUtilities.invokeLater(game::start);
    }
}
