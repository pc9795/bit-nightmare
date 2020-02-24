package game.framework.audio;

import game.utils.Constants;
import org.newdawn.slick.Music;

import java.io.InputStream;

/**
 * Created By: Prashant Chaubey
 * Created On: 23-02-2020 21:18
 * Purpose: TODO:
 **/
public class JukeBox {
    private static final JukeBox INSTANCE = new JukeBox();
    private Music themeMusic;

    private JukeBox() {
        init();
    }

    public static JukeBox getInstance() {
        return INSTANCE;
    }

    private void init() {
        try (InputStream in = JukeBox.class.getResourceAsStream(Constants.SoundPaths.MAIN_THEME)) {
            themeMusic = new Music(in, "theme.ogg");

        } catch (Exception e) {
            System.out.println("Unable to initialize Jukebox");
            e.printStackTrace();
        }
    }

    public void playTheme() {
        if (themeMusic == null) {
            return;
        }
        themeMusic.loop();
    }
}
