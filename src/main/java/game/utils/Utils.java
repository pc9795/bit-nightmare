package game.utils;

import java.awt.*;
import java.util.StringTokenizer;

/**
 * Created By: Prashant Chaubey
 * Created On: 02-03-2020 22:40
 * Purpose: TODO:
 **/
public final class Utils {
    private Utils() {
    }

    public static void renderDialog(String dialog, Graphics g, int margin, int x, int y, int dialogWidth) {
        FontMetrics fm = g.getFontMetrics();
        //Not using ascent and descent as things looking fine without this.
        int fontHeight = fm.getHeight();
        StringTokenizer tokenizer = new StringTokenizer(dialog, " ");
        StringBuilder sb = new StringBuilder();
        int yy = y + margin + fontHeight;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (fm.stringWidth(sb.toString()) + fm.stringWidth(token) + fm.stringWidth(" ") <= dialogWidth - 2 * margin) {
                sb.append(token).append(" ");
                continue;
            }
            g.drawString(sb.toString(), x + margin, yy);
            yy += fontHeight;
            sb = new StringBuilder();
            sb.append(token).append(" ");
        }
        g.drawString(sb.toString(), x + margin, yy);
    }
}
