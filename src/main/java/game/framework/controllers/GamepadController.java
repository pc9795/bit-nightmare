package game.framework.controllers;

import net.java.games.input.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 21:00
 * Purpose: TODO:
 **/
public class GamepadController implements Runnable {
    private static final GamepadController INSTANCE = new GamepadController();
    private Map<String, Boolean> keys = new HashMap<>();
    private float xAxis;
    private Controller gamepad;
    private boolean detecting;

    private GamepadController() {
        init();
    }

    private void init() {
        keys.put("Button 0", false);
        keys.put("Button 1", false);
        keys.put("Button 2", false);
        keys.put("Button 3", false);
        keys.put("Button 7", false);
    }

    public static GamepadController getInstance() {
        return INSTANCE;
    }

    public boolean isDetecting() {
        return detecting;
    }

    public void setDetecting(boolean detecting) {
        this.detecting = detecting;
    }

    @Override
    public void run() {
        while (detecting) {
            boolean connected = gamepad != null && gamepad.poll();
            if (!connected && !connect()) {
                continue;
            }
            EventQueue queue = gamepad.getEventQueue();
            Event event = new Event();
            while (queue.getNextEvent(event)) {

                Component comp = event.getComponent();
                if (comp.getName().equals("X Axis")) {
                    xAxis = event.getValue();
                    continue;
                }
                keys.put(comp.getName(), event.getValue() == 1.0f);
            }
        }
    }

    private boolean connect() {
        Controller[] controllers;
        DirectAndRawInputEnvironmentPlugin directEnv = new DirectAndRawInputEnvironmentPlugin();
        //If it is not supported then you are not able to dynamically add or remove controller.
        if (directEnv.isSupported()) {
            controllers = directEnv.getControllers();
        } else {
            controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        }
        if (controllers.length == 0) {
            return false;
        }
        for (Controller controller : controllers) {
            if (controller.getType() != Controller.Type.GAMEPAD) {
                continue;
            }
            gamepad = controller;
            return true;
        }
        return false;
    }

    public boolean isAPressed() {
        return keys.get("Button 0");
    }

    public boolean isXPressed() {
        return keys.get("Button 1");
    }

    public boolean isYPressed() {
        return keys.get("Button 2");
    }

    public boolean isBPressed() {
        return keys.get("Button 3");
    }

    public boolean isStartPressed() {
        return keys.get("Button 7");
    }

    public boolean isRightPressed() {
        return (int) xAxis == 1;
    }

    public boolean isLeftPressed() {
        return (int) xAxis == -1;
    }
}
