package game.framework.controllers;

import game.utils.Constants;
import net.java.games.input.*;

import java.util.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 17-02-2020 21:00
 * Purpose: TODO:
 **/
public class GamepadController implements Runnable {
    private static final GamepadController INSTANCE = new GamepadController();
    private Map<String, Boolean> keys = new HashMap<>();
    private Map<String, Integer> pollCount = new HashMap<>();
    private float xAxis;
    private Controller gamepad;
    private boolean detecting;

    public static class GamepadEvent {
        public static final String BUTTON_0 = "Button 0";
        public static final String BUTTON_1 = "Button 1";
        public static final String BUTTON_2 = "Button 2";
        public static final String BUTTON_3 = "Button 3";
        public static final String BUTTON_7 = "Button 7";
        public static final String X_AXIS = "X Axis";

    }

    private GamepadController() {
        init();
    }

    private void init() {
        // All the keys supported by the class must be entered in the map for one time. Else it can result in
        // NullPointerException if we trying to access a non-existing key.
        List<String> configuredButtons = Arrays.asList(GamepadEvent.BUTTON_0, GamepadEvent.BUTTON_1, GamepadEvent.BUTTON_2,
                GamepadEvent.BUTTON_3, GamepadEvent.BUTTON_7);
        for (String button : configuredButtons) {
            keys.put(button, false);
            pollCount.put(button, 0);
        }
    }

    public static GamepadController getInstance() {
        return INSTANCE;
    }

    public void poll() {
        for (String key : pollCount.keySet()) {
            if (keys.get(key)) {
                pollCount.put(key, pollCount.get(key) + 1);
            } else {
                pollCount.put(key, 0);
            }
        }
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
                if (comp.getName().equals(GamepadEvent.X_AXIS)) {
                    xAxis = event.getValue();
                    continue;
                }
                keys.put(comp.getName(), event.getValue() == 1.0f);
            }
            //Hoping the event queue mechanism of the queue will not let events to miss out.
            sleep();
        }
    }

    private void sleep() {
        try {
            Thread.sleep(Constants.GAMEPAD_POLLING_WAIT_TIME_IN_SEC);

        } catch (InterruptedException e) {
            e.printStackTrace();
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
        return pollCount.get(GamepadEvent.BUTTON_0) > 0;
    }

    public boolean isXPressed() {
        return pollCount.get(GamepadEvent.BUTTON_1) > 0;
    }

    public boolean isYPressed() {
        return pollCount.get(GamepadEvent.BUTTON_2) > 0;
    }

    public boolean isBPressed() {
        return pollCount.get(GamepadEvent.BUTTON_3) > 0;
    }

    public boolean isAPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_0) == 1;
    }

    public boolean isXPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_1) == 1;
    }

    public boolean isYPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_2) == 1;
    }

    public boolean isBPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_3) == 1;
    }


    public boolean isStartPressed() {
        return keys.get(GamepadEvent.BUTTON_7);
    }

    public boolean isRightPressed() {
        return (int) xAxis == 1;
    }

    public boolean isLeftPressed() {
        return (int) xAxis == -1;
    }
}
