package game.framework.controllers;

import game.utils.Constants;
import net.java.games.input.*;

import java.util.*;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 17-02-2020 21:00
 * Purpose: Controller to access game pad.
 * <p>
 * REF: https://mvnrepository.com/artifact/net.java.jinput/jinput
 * Used the mentioned library to implement Gamepad.
 **/
public class GamepadController implements Runnable {
    private static final GamepadController INSTANCE = new GamepadController();
    private static final float THRESHOLD = 0.001f;
    private Map<String, Boolean> keys = new HashMap<>();
    private Map<String, Integer> pollCount = new HashMap<>();
    private Controller gamepad;
    private boolean detecting;

    /**
     * Class to consolidate different type of events generated by Gamepad. Could use Enums.
     */
    private static class GamepadEvent {
        static final String BUTTON_0 = "Button 0";
        static final String BUTTON_1 = "Button 1";
        static final String BUTTON_2 = "Button 2";
        static final String BUTTON_3 = "Button 3";
        static final String BUTTON_7 = "Button 7";
        static final String HAT_SWITCH = "Hat Switch";
        //Custom ones
        static final String HAT_SWITCH_UP = "Hat Switch-Up";
        static final String HAT_SWITCH_RIGHT = "Hat Switch-Right";
        static final String HAT_SWITCH_DOWN = "Hat Switch-Down";
        static final String HAT_SWITCH_LEFT = "Hat Switch-Left";

    }

    private GamepadController() {
        init();
    }

    /**
     * Initialization
     */
    private void init() {
        // All the keys supported by the class must be entered in the map for one time. Else it can result in
        // NullPointerException if we trying to access a non-existing key.
        List<String> configuredButtons = Arrays.asList(GamepadEvent.BUTTON_0, GamepadEvent.BUTTON_1, GamepadEvent.BUTTON_2,
                GamepadEvent.BUTTON_3, GamepadEvent.BUTTON_7, GamepadEvent.HAT_SWITCH_DOWN, GamepadEvent.HAT_SWITCH_LEFT,
                GamepadEvent.HAT_SWITCH_RIGHT, GamepadEvent.HAT_SWITCH_UP);
        for (String button : configuredButtons) {
            keys.put(button, false);
            pollCount.put(button, 0);
        }
    }

    public static GamepadController getInstance() {
        return INSTANCE;
    }

    /**
     * It must be called per frame. This method implements a functionality to check whether a button is pressed once
     * or not. As when button is pressed for multiple frames it can cause unexpected behavior
     * <p>
     * REF: https://books.google.ie/books/about/Fundamental_2D_Game_Programming_with_Jav.html?id=iRFvCgAAQBAJ&redir_esc=y
     * The idea to use a poll count to detect whether a key is pressed once or not is taken by the Chapter 2 of this book.
     */
    public void poll() {
        for (String key : pollCount.keySet()) {
            if (keys.get(key)) {
                pollCount.put(key, pollCount.get(key) + 1);
            } else {
                pollCount.put(key, 0);
            }
        }
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
            //REF: https://github.com/jinput/jinput/wiki/Code-Example---Event-Queues
            //Used the mentioned code to process the events. I have implemented handling HatSwitch on my own. As the
            //given example only prints what events are happening, I saved those generated events into my maps.
            EventQueue queue = gamepad.getEventQueue();
            Event event = new Event();
            while (queue.getNextEvent(event)) {
                Component comp = event.getComponent();
                //If not hat switch just save the event. All keys when pressed will give a value of 1
                if (!comp.getName().equals(GamepadEvent.HAT_SWITCH)) {
                    keys.put(comp.getName(), Math.abs(event.getValue() - 1.0f) < THRESHOLD);
                    continue;
                }
                //Handling Hat Switch
                //Using threshold based approach for float comparison
                if (Math.abs(event.getValue() - 0.125f) < THRESHOLD) {
                    //North-West
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, true);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, true);
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, false);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, false);
                } else if (Math.abs(event.getValue() - 0.25f) < THRESHOLD) {
                    //North
                    keys.put(GamepadEvent.HAT_SWITCH_UP, true);
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, false);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, false);
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, false);
                } else if (Math.abs(event.getValue() - 0.375f) < THRESHOLD) {
                    //North-East
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, true);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, true);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, false);
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, false);
                } else if (Math.abs(event.getValue() - 0.5f) < THRESHOLD) {
                    //East
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, true);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, false);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, false);
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, false);
                } else if (Math.abs(event.getValue() - 0.625f) < THRESHOLD) {
                    //South-East
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, true);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, true);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, false);
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, false);
                } else if (Math.abs(event.getValue() - 0.75f) < THRESHOLD) {
                    //South
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, true);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, false);
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, false);
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, false);
                } else if (Math.abs(event.getValue() - 0.875f) < THRESHOLD) {
                    //South-West
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, true);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, true);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, false);
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, false);
                } else if (Math.abs(event.getValue() - 1f) < THRESHOLD) {
                    //West
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, true);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, false);
                    keys.put(GamepadEvent.HAT_SWITCH_UP, false);
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, false);
                } else {
                    //All keys are lifted
                    keys.put(GamepadEvent.HAT_SWITCH_UP, false);
                    keys.put(GamepadEvent.HAT_SWITCH_RIGHT, false);
                    keys.put(GamepadEvent.HAT_SWITCH_DOWN, false);
                    keys.put(GamepadEvent.HAT_SWITCH_LEFT, false);
                }
            }
            //Hoping the event queue mechanism of the library will not let events to miss out.
            //Without sleeping for some time it is a lot of work by this thread.
            try {
                Thread.sleep(Constants.GAMEPAD_POLLING_WAIT_TIME_IN_SEC);

            } catch (InterruptedException e) {
                System.out.println("Error while sleeping inside Gamepad Controller polling thread.");
                e.printStackTrace();
            }
        }
    }

    /**
     * Try to identify a controller and connect.
     * <p>
     * REF: https://github.com/jinput/jinput/wiki/Code-Example---Event-Queues
     * I have added the functionality of using `DirectAndRawInputEnvironmentPlugin` so that if the controller is removed
     * it can be connected again.
     *
     * @return true if able to find a controller and connect to it.
     */
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

    public boolean isAPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_0) == 1;
    }

    public boolean isAPressed() {
        return pollCount.get(GamepadEvent.BUTTON_0) > 0;
    }

    public boolean isXPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_2) == 1;
    }

    public boolean isXPressed() {
        return pollCount.get(GamepadEvent.BUTTON_2) > 0;
    }

    public boolean isStartPressedOnce() {
        return pollCount.get(GamepadEvent.BUTTON_7) == 1;
    }

    public boolean isStartPressed() {
        return pollCount.get(GamepadEvent.BUTTON_7) > 0;
    }

    public boolean isHatSwitchUpPressedOnce() {
        return pollCount.get(GamepadEvent.HAT_SWITCH_UP) == 1;
    }

    public boolean isHatSwitchUpPressed() {
        return pollCount.get(GamepadEvent.HAT_SWITCH_UP) > 0;
    }

    public boolean isHatSwitchDownPressedOnce() {
        return pollCount.get(GamepadEvent.HAT_SWITCH_DOWN) == 1;
    }

    public boolean isHatSwitchDownPressed() {
        return pollCount.get(GamepadEvent.HAT_SWITCH_DOWN) > 0;
    }

    public boolean isHatSwitchRightPressed() {
        return pollCount.get(GamepadEvent.HAT_SWITCH_RIGHT) > 0;
    }

    public boolean isHatSwitchLeftPressed() {
        return pollCount.get(GamepadEvent.HAT_SWITCH_LEFT) > 0;
    }
}
