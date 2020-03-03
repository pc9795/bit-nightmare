package game.objects;

import game.framework.Model;
import game.framework.controllers.MouseController;
import game.physics.Point2f;
import game.utils.Utils;

import java.awt.*;
import java.util.List;

import static game.utils.Constants.CAMERA_OFFSET;

/**
 * Created By: Prashant Chaubey
 * Created On: 02-03-2020 21:16
 * Purpose: TODO:
 **/
public class Descriptor extends GameObject {
    private static final int DEFAULT_HEIGHT = 16;
    private static final int DEFAULT_WIDTH = 16;
    private static final int DEFAULT_DIALOG_WIDTH = 200;
    private static final int DEFAULT_DIALOG_HEIGHT = 100;
    private String desc;
    private int dialogWidth, dialogHeight;
    private Font font;

    public Descriptor(GameObjectType type) {
        super(DEFAULT_WIDTH, DEFAULT_HEIGHT, new Point2f(0, 0), type);
        dialogHeight = DEFAULT_DIALOG_HEIGHT;
        dialogWidth = DEFAULT_DIALOG_WIDTH;
        font = new Font("Times New Roman", Font.BOLD, 16);
    }

    private String getDescription(GameObjectType type) {
        switch (type) {
            case CHARGER:
                return "Chargers are creatures that will run at you and corrupt your bits.";
            case GUARDIAN:
                return "Guardian of the prison. It charges towards enemies and fire array guns and matrix blasts.";
            case SOLDIER:
                return "Solider of the prison. They fire bit-revolvers.";
            case SUPER_SOLDIER:
                return "Super soldiers are tougher than soldiers and fire array guns.";
            case MOVABLE_BLOCK:
                return "An object that can be moved.";
            case BIT_BOT:
                return "A strange looking robot.";
            case BLOCK:
                return "Surface of the prison.";
            case CHANGE_LEVEL:
                return "A teleporter.";
            case CHECKPOINT:
                return "Save point.";
            case END_GAME:
                return "End Game.";
            case ENEMY_PORTAL:
                return "A portal which spawns enemies.";
            case GATE:
                return "A gate that need a key to be opened.";
            case HIDING_BLOCK:
                return "This surfaces disappear on the touch of intruders.";
            case KEY:
                return "A key that can open a nearby gate.";
            case LAVA:
                return "A plasma like substance that can destroy a bit sequence.";
            case BIT_ARRAY_GUN:
                return "A weapon that fires a stream of bits.";
            case BIT_MATRIX_BLAST:
                return "A weapon that fires a matrix of bits.";
            case BIT_REVOLVER:
                return "A weapon that fires bits as projectiles.";
        }
        return null;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics g) {
        if (desc == null) {
            return;
        }
        g.setColor(new Color(111, 66, 150));
        g.fillRect((int) centre.getX(), (int) centre.getY(), dialogWidth, dialogHeight);
        g.setColor(Color.WHITE);
        g.setFont(font);
        Utils.renderDialog(desc, g, 5, (int) centre.getX(), (int) centre.getY(), dialogWidth);
    }

    @Override
    public void perceiveEnv(Model model) {
        if (!MouseController.getInstance().isRightClicked()) {
            desc = null;
            return;
        }
        Point mousePtr = MouseController.getInstance().getCurrentPos();
        float x = (float) (model.getPlayer1().getCentre().getX() + (mousePtr.getX() - CAMERA_OFFSET));
        centre = new Point2f(x, (float) mousePtr.getY());

        List<GameObject> willCollide = model.getEnvironmentQuadTree().retrieve(this);
        willCollide.addAll(model.getMovableEnvironment());
        willCollide.addAll(model.getCollectibles());
        willCollide.addAll(model.getEnemies());
        boolean descFound = false;
        for (GameObject env : willCollide) {
            if (!env.getBounds().contains(centre.toAWTPoint())) {
                continue;
            }
            desc = getDescription(env.getType());
            descFound = true;
        }

        //So that old value is destroyed.
        if (!descFound) {
            desc = null;
        }
    }
}
