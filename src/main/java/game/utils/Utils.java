package game.utils;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 02-03-2020 22:40
 * Purpose: Utility methods
 **/
public final class Utils {
    private Utils() {
    }

    /**
     * Render a dialog screen
     *
     * @param dialog      dialog message
     * @param g           graphics object
     * @param margin      margin
     * @param x           x position
     * @param y           y position
     * @param dialogWidth width of the dialog screen.
     */
    public static void renderDialog(String dialog, Graphics g, int margin, int x, int y, int dialogWidth) {
        FontMetrics fm = g.getFontMetrics();
        //Not using ascent and descent as things looking fine without this.
        int fontHeight = fm.getHeight();
        StringTokenizer tokenizer = new StringTokenizer(dialog, " ");
        StringBuilder sb = new StringBuilder();
        int yy = y + margin + fontHeight;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            //If adding current token is not overflowing the line
            if (fm.stringWidth(sb.toString()) + fm.stringWidth(token) + fm.stringWidth(" ") <= dialogWidth - 2 * margin) {
                sb.append(token).append(" ");
                continue;
            }
            //Adding current token is overflowing the line so draw tokens stored till this point..
            g.drawString(sb.toString(), x + margin, yy);
            yy += fontHeight;
            sb = new StringBuilder();
            //Save current token
            sb.append(token).append(" ");
        }
        g.drawString(sb.toString(), x + margin, yy);
    }
}
