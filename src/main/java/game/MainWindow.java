package game;

import game.framework.Game;
import game.utils.Constants;

import javax.swing.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 17-02-2020 21:24
 * Purpose: Entry point of the application
 **/
public class MainWindow {
    public static void main(String[] args) {
        Game game = new Game(Constants.GAME_NAME, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        // Because the Swing library is not thread-safe you should always create and show a JFrame on the Swing event
        // thread. However, the program's main() method is not invoked on the event thread, so it is necessary to use the
        // SwingUtilities class to launch the game window.
        //REF: https://books.google.ie/books/about/Fundamental_2D_Game_Programming_with_Jav.html?id=iRFvCgAAQBAJ&redir_esc=y
        //Use the chapter 1 of the mentioned book for the motivation to run game in swing even thread.
        SwingUtilities.invokeLater(game::start);
    }
}
