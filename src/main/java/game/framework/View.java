package game.framework;

import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.framework.controllers.MouseController;
import game.framework.narrator.Sequence;
import game.framework.narrator.Story;
import game.framework.narrator.StoryLoader;
import game.framework.visual.TextureLoader;
import game.objects.Difficulty;
import game.utils.BufferedImageLoader;
import game.utils.Constants;
import game.utils.Utils;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static game.utils.Constants.DEV_MODE;


/*
 * Created by Abraham Campbell on 15/01/2020.
 *   Copyright (c) 2020  Abraham Campbell

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
   
   (MIT LICENSE ) e.g do what you want with this :-) 
 */

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Class which is responsible for rendering everything on the screen.
 */
class View extends Canvas {
    private Model gameWorld;
    private BufferStrategy bs;
    private Camera camera;
    private BufferedImage bg, healthMarker;
    private StoryTeller storyTeller;
    private HashSet<Integer> storyLocationsProcessed;
    private float gameScreenAlpha;
    private Screen currScreen, prevScreen;


    View(Model world) throws IOException {
        this.gameWorld = world;
        this.camera = new Camera(0, 0);
        this.storyTeller = new StoryTeller();
        //todo it should be part of the model.
        this.storyLocationsProcessed = new HashSet<>();
        this.gameScreenAlpha = 1f;
        this.currScreen = Screen.TITLE;
        //This will start loading of the textures. As it gives a massive lag to the system. So doing it early on.
        //noinspection ResultOfMethodCallIgnored
        TextureLoader.getInstance();

        this.bg = BufferedImageLoader.getInstance().loadImage(Constants.BACKGROUND_IMG_LOC);
        this.healthMarker = BufferedImageLoader.getInstance().loadImage(Constants.HEALTH_MARKER_IMG_LOC);
    }

    private enum Screen {
        TITLE, MAIN, QUIT_TO_MENU, PAUSE, QUIT, SETTINGS, CONTROL_LAYOUT, IN_GAME,
        NEW_GAME, DIFFICULTY_SELECT, END, GAME_OVER
    }

    void updateView() {
        //REF: https://books.google.ie/books/about/Fundamental_2D_Game_Programming_with_Jav.html?id=iRFvCgAAQBAJ&redir_esc=y
        //Used the idea of buffering from the chapter 1 of the given book.

        //Creating buffers for one time. We cannot create early as it throws an exception
        if (bs == null) {
            createBufferStrategy(Constants.BUFFER_COUNT);
            bs = getBufferStrategy();
        }

        Graphics g = bs.getDrawGraphics();
        //Drawing back ground
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);

        g.setColor(Color.ORANGE);
        int margin = 10;

        //ALL CONTROLLERS `poll` CALLS MUST BE INSIDE SPECIFIC CASES. Any call outside can produce unexpected results.
        //If menu items are a DAG then it is easy to navigate. Have to check the possibility of cycles.
        switch (currScreen) {
            case TITLE:
                //Drawing the main title.
                g.setFont(new Font("Game Music Love", Font.BOLD, 150));
                String text = "BIT-NIGHTMARE";
                FontMetrics fm = g.getFontMetrics();
                int fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                int textWidth = fm.stringWidth(text);
                g.drawString(text, getWidth() / 2 - textWidth / 2, margin + fontHeight);

                //Drawing the "LEFT CLICK TO CONTINUE" at the bottom.
                g.setFont(new Font("Game Music Love", Font.BOLD, 30));
                text = "LEFT CLICK TO CONTINUE";
                fm = g.getFontMetrics();
                textWidth = fm.stringWidth(text);
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                Rectangle textArea = new Rectangle(getWidth() / 2 - textWidth / 2, getHeight() - fontHeight - margin, textWidth, fontHeight);
                if (textArea.contains(MouseController.getInstance().getCurrentPos())) {
                    g.setColor(Color.GRAY);
                }
                g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() - margin);

                //Navigation to main screen on any left click.
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.TITLE;
                }
                break;
            case MAIN:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                List<String> menuItems;
                boolean checkpointAvailable = gameWorld.isLastCheckpointAvailable();
                if (checkpointAvailable) {
                    menuItems = Arrays.asList("Continue", "New Game", "Settings", "Quit");
                } else {
                    menuItems = Arrays.asList("New Game", "Settings", "Quit");
                }
                int option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin);
                //Shifting when we don't have a continue option
                if (!checkpointAvailable && option != -1) {
                    option++;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicked on "Continue"
                        case 0:
                            gameWorld.loadLastCheckPoint();
                            storyLocationsProcessed.clear();
                            currScreen = Screen.IN_GAME;
                            prevScreen = Screen.MAIN;
                            break;
                        //Clicking on "New Game"
                        case 1:
                            if (checkpointAvailable) {
                                currScreen = Screen.NEW_GAME;
                            } else {
                                currScreen = Screen.DIFFICULTY_SELECT;
                            }
                            prevScreen = Screen.MAIN;
                            break;
                        //Clicking the "Settings"
                        case 2:
                            currScreen = Screen.SETTINGS;
                            prevScreen = Screen.MAIN;
                            break;
                        //Clicking the "Quit"
                        case 3:
                            currScreen = Screen.QUIT;
                            prevScreen = Screen.MAIN;
                            break;
                    }
                }
                break;
            case NEW_GAME:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                //Drawing a header
                g.drawString("You will lose your previous data!", margin, margin + fontHeight);
                //Drawing the menu
                menuItems = Arrays.asList("Yes", "No");
                option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin + 2 * fontHeight);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicking "Yes"
                        case 0:
                            currScreen = Screen.DIFFICULTY_SELECT;
                            prevScreen = Screen.NEW_GAME;
                            break;
                        //Clicking "No"
                        case 1:
                            currScreen = Screen.MAIN;
                            prevScreen = Screen.NEW_GAME;
                            break;
                    }
                }
                //"Escape functionality"
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.NEW_GAME;
                }
                break;
            case DIFFICULTY_SELECT:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                //Drawing a header
                g.drawString("Select a difficulty", margin, margin + fontHeight);
                //Drawing the menu
                menuItems = Arrays.asList("- I am not a programmer", "- I can code", "- I can reprogram you");
                option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin + fontHeight);
                Difficulty difficulty = null;
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicking "I am not a programmer"
                        case 0:
                            difficulty = Difficulty.EASY;
                            break;
                        //Clicking "I can code"
                        case 1:
                            difficulty = Difficulty.MEDIUM;
                            break;
                        //Clicking "I can reprogram you"
                        case 2:
                            difficulty = Difficulty.HARD;
                            break;
                    }
                }
                if (difficulty != null) {
                    try {
                        gameWorld.newGame(difficulty);
                        storyLocationsProcessed.clear();
                        currScreen = Screen.IN_GAME;
                        prevScreen = Screen.DIFFICULTY_SELECT;

                    } catch (IOException e) {
                        System.out.println("Unable to start a new game");
                        e.printStackTrace();
                    }
                }
                //"Escape functionality"
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.DIFFICULTY_SELECT;
                }
                break;
            case SETTINGS:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                menuItems = Collections.singletonList("Show Controller Layout");
                option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicking "Show Controller Layout"
                        case 0:
                            currScreen = Screen.CONTROL_LAYOUT;
                            prevScreen = Screen.SETTINGS;
                            break;
                    }
                }
                //"Escape functionality"
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.SETTINGS;
                    return;
                }
                break;
            case CONTROL_LAYOUT:
                //REF: https://opengameart.org/content/free-keyboard-and-controllers-prompts-pack
                //For images.
                try {
                    g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                    margin = 100;
                    BufferedImageLoader loader = BufferedImageLoader.getInstance();
                    //Keyboard button images
                    BufferedImage w = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_W.png");
                    BufferedImage a = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_A.png");
                    BufferedImage s = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_S.png");
                    BufferedImage d = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_D.png");
                    BufferedImage q = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_Q.png");
                    BufferedImage space = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_Space.png");
                    BufferedImage esc = loader.loadImage("/sprites/controllers/keyboard/Keyboard_White_Esc.png");
                    int x = margin;
                    int y = margin;
                    int imgHeight = 90;
                    int imgWidth = 90;
                    g.drawImage(w, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(a, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(s, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(d, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(q, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(space, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(esc, x, y, imgWidth, imgHeight, null);
                    //Controller button images
                    BufferedImage left = loader.loadImage("/sprites/controllers/gamepad/360_Dpad_Left.png");
                    BufferedImage right = loader.loadImage("/sprites/controllers/gamepad/360_Dpad_Right.png");
                    BufferedImage up = loader.loadImage("/sprites/controllers/gamepad/360_Dpad_Up.png");
                    BufferedImage down = loader.loadImage("/sprites/controllers/gamepad/360_Dpad_Down.png");
                    BufferedImage a_ = loader.loadImage("/sprites/controllers/gamepad/360_A.png");
                    BufferedImage x_ = loader.loadImage("/sprites/controllers/gamepad/360_X.png");
                    BufferedImage start = loader.loadImage("/sprites/controllers/gamepad/360_Start_Alt.png");
                    x = getWidth() - margin - imgWidth;
                    y = margin;
                    g.drawImage(up, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(left, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(down, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(right, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(x_, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(a_, x, y, imgWidth, imgHeight, null);
                    y += imgHeight;
                    g.drawImage(start, x, y, imgWidth, imgHeight, null);
                    //Action of the buttons.
                    fm = g.getFontMetrics();
                    y = margin;
                    List<String> words = Arrays.asList("Jump", "Move Left", "Duck", "Move Right", "Cycle Weapon", "Fire Weapon", "Pause");
                    for (String word : words) {
                        x = getWidth() / 2 - fm.stringWidth(word) / 2;
                        g.drawString(word, x, y + imgHeight);
                        y += imgHeight;
                    }
                    //"Escape functionality"
                    KeyboardController.getInstance().poll();
                    if (KeyboardController.getInstance().isEscPressedOnce()) {
                        currScreen = Screen.SETTINGS;
                        prevScreen = Screen.CONTROL_LAYOUT;
                        return;
                    }

                } catch (Exception e) {
                    System.out.println("Unable to show the controller layout screen.");
                    e.printStackTrace();
                }
                break;
            case PAUSE:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                //Draw header
                g.drawString("Paused", margin, margin + fontHeight);
                //Draw menu
                menuItems = Arrays.asList("- Resume", "- Quit to main menu", "- Quit");
                option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin + fontHeight);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicking "Resume"
                        case 0:
                            currScreen = Screen.IN_GAME;
                            prevScreen = Screen.PAUSE;
                            gameWorld.resume();
                            break;
                        //Clicking "Quit to main menu"
                        case 1:
                            currScreen = Screen.QUIT_TO_MENU;
                            prevScreen = Screen.PAUSE;
                            break;
                        //Clicking "Quit"
                        case 2:
                            currScreen = Screen.QUIT;
                            prevScreen = Screen.PAUSE;
                            break;
                    }
                }
                //Escape functionality
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.IN_GAME;
                    prevScreen = Screen.PAUSE;
                    gameWorld.resume();
                }
                break;
            case QUIT_TO_MENU:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                //Draw header
                g.drawString("Quit to main menu", margin, margin + fontHeight);
                //Draw menu
                menuItems = Arrays.asList("- Yes", "- No");
                option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin + 2 * fontHeight);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicking "Yes"
                        case 0:
                            currScreen = Screen.MAIN;
                            prevScreen = Screen.QUIT_TO_MENU;
                            break;
                        //Clicking "No"
                        case 1:
                            currScreen = Screen.PAUSE;
                            prevScreen = Screen.QUIT_TO_MENU;
                            break;
                    }
                }
                //Escape functionality
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.PAUSE;
                    prevScreen = Screen.QUIT_TO_MENU;
                }
                break;
            case QUIT:
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                //Draw header
                g.drawString("Quit the game", margin, margin + fontHeight);
                //Draw menu
                menuItems = Arrays.asList("- Yes", "- No");
                option = renderMenu(menuItems, MouseController.getInstance().getCurrentPos(), g, margin, margin + 2 * fontHeight);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        //Clicking "Yes"
                        case 0:
                            System.exit(0);
                            break;
                        //Clicking "No"
                        case 1:
                            currScreen = prevScreen;
                            break;
                    }
                }
                //Escape functionality
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = prevScreen;
                }
                break;
            case IN_GAME:
                if (gameWorld.isGameOver()) {
                    //Game over
                    currScreen = Screen.GAME_OVER;
                    prevScreen = Screen.IN_GAME;
                } else if (gameWorld.isCompleted()) {
                    //Game finished
                    currScreen = Screen.END;
                    prevScreen = Screen.IN_GAME;
                } else if (gameWorld.isPaused()) {
                    //Game paused
                    gameWorld.pause();
                    currScreen = Screen.PAUSE;
                    prevScreen = Screen.IN_GAME;
                }
                //When the View is in IN_GAME mode then controllers are expected to be processed by Model. So any `poll`
                //call to the controller will produce unexpected results.
                checkStories();
                renderGame(g);
                renderPlayerStats(g);
                if (storyTeller.isOn()) {
                    gameWorld.stop();
                    gameScreenAlpha = 0.5f;
                    renderStory(g);
                } else {
                    gameWorld.start();
                    gameScreenAlpha = 1f;
                }
                break;
            case END:
                g.setFont(new Font("Game Music Love", Font.BOLD, 100));
                text = "A Game by Prashant";
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                textWidth = fm.stringWidth(text);
                margin = 10;
                g.drawString(text, getWidth() / 2 - textWidth / 2, margin + fontHeight);

                margin += fontHeight;
                text = "Hope you enjoyed!";
                textWidth = fm.stringWidth(text);
                g.drawString(text, getWidth() / 2 - textWidth / 2, margin + fontHeight);

                g.setFont(new Font("Game Music Love", Font.BOLD, 30));
                text = "LEFT CLICK TO GO TO MAIN MENU";
                fm = g.getFontMetrics();
                textWidth = fm.stringWidth(text);
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                textArea = new Rectangle(getWidth() / 2 - textWidth / 2, getHeight() - fontHeight - margin, textWidth, fontHeight);
                if (textArea.contains(MouseController.getInstance().getCurrentPos())) {
                    g.setColor(Color.GRAY);
                }
                g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() - margin);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.END;
                }
                break;
            case GAME_OVER:
                g.setFont(new Font("Game Music Love", Font.BOLD, 150));
                text = "GAME OVER";
                fm = g.getFontMetrics();
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                textWidth = fm.stringWidth(text);
                g.drawString(text, getWidth() / 2 - textWidth / 2, margin + fontHeight);

                g.setFont(new Font("Game Music Love", Font.BOLD, 30));
                text = "LEFT CLICK TO GO TO MAIN MENU";
                fm = g.getFontMetrics();
                textWidth = fm.stringWidth(text);
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                textArea = new Rectangle(getWidth() / 2 - textWidth / 2, getHeight() - fontHeight - margin, textWidth, fontHeight);
                if (textArea.contains(MouseController.getInstance().getCurrentPos())) {
                    g.setColor(Color.GRAY);
                }
                g.drawString(text, getWidth() / 2 - textWidth / 2, getHeight() - margin);
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.END;
                }
                break;
        }

        if (DEV_MODE) {
            //Debug for mouse position
            Point mousePos = MouseController.getInstance().getCurrentPos();
            g.setColor(Color.RED);
            g.setFont(new Font("Time New Roman", Font.BOLD, 20));
            g.drawString(String.format("X=%s,Y=%s", mousePos.getX(), mousePos.getY()), mousePos.x, mousePos.y);
        }
        g.dispose();
        bs.show();
    }

    /**
     * Render a story
     *
     * @param g graphics object
     */
    private void renderStory(Graphics g) {
        Story story = storyTeller.nextFragment();
        if (story == null) {
            return;
        }
        //Skipping
        KeyboardController.getInstance().poll();
        GamepadController.getInstance().poll();
        if (KeyboardController.getInstance().isSpacePressedOnce() || GamepadController.getInstance().isAPressedOnce()) {
            storyTeller.skip();
        }
        //Dialog configuration
        int dialogWidth = 800;
        int dialogHeight = 400;
        int margin = 10;
        int imageWidth = 100;
        int imageHeight = 100;
        //Render story
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        int x = getWidth() / 2 - dialogWidth / 2;
        int y = getHeight() / 2 - dialogHeight / 2;
        g.setColor(new Color(111, 66, 150));
        g.fillRect(x, y, dialogWidth, dialogHeight);
        g.drawImage(story.getImage(), x + margin, y + margin, imageWidth, imageHeight, null);
        g.setFont(new Font("Times New Roman", Font.BOLD, 25));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight() + fm.getDescent() + fm.getDescent();
        Utils.renderDialog(story.getText(), g, margin, x, y + imageHeight, dialogWidth);
        g.drawString("Press [Space]/[A] to skip", x + margin, y + dialogHeight - margin - fontHeight);
    }

    /**
     * Check for stories
     */
    private void checkStories() {
        String level = gameWorld.getCurrentLevel();
        //Check for stories
        StoryLoader loader = StoryLoader.getInstance();
        if (!loader.hasStories(level)) {
            return;
        }
        //Get nearest story from the player's location
        int playerLoc = (int) gameWorld.getPlayer1().getCentre().getX();
        int nearestLoc = loader.getNearestStoryPos(level, playerLoc);
        //If story already played
        if (storyLocationsProcessed.contains(nearestLoc)) {
            return;
        }
        //Loading the found story into the story teller
        storyTeller.load(loader.getSequences(level, nearestLoc));
        //Caching so that not have to play every time.
        storyLocationsProcessed.add(nearestLoc);
    }

    /**
     * Render the game
     *
     * @param g grpahics object
     */
    private void renderGame(Graphics g) {
        //Updating camera with player location
        camera.update(gameWorld.getPlayer1());
        Graphics2D g2d = (Graphics2D) g;
        //Moving camera
        g2d.translate(camera.getX(), camera.getY());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        gameWorld.getEnvironment().forEach(object -> object.render(g));
        //Apart from environment other objects are dimmed in case of a story is going on.
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, gameScreenAlpha));
        gameWorld.getMovableEnvironment().forEach(object -> object.render(g));
        gameWorld.getCollectibles().forEach(object -> object.render(g));
        gameWorld.getEnemies().forEach(object -> object.render(g));
        gameWorld.getBullets().forEach(object -> object.render(g));
        gameWorld.getPlayer1().render(g);
        gameWorld.getDescriptor().render(g);
        g2d.translate(-camera.getX(), -camera.getY());
    }

    /**
     * It load a list of sequences and narrate the story.
     */
    private static class StoryTeller {
        private static final float DEFAULT_DELAY = 0.05f;
        private List<Sequence> sequences = new ArrayList<>();
        private int seqIndex, storyIndex, wordIndex;
        private long lastPrint = System.currentTimeMillis();
        private boolean on;

        void load(List<Sequence> sequences) {
            if (sequences.size() == 0) {
                return;
            }
            this.sequences = sequences;
            this.seqIndex = 0;
            this.storyIndex = 0;
            this.wordIndex = 0;
            this.on = true;
        }

        public boolean isOn() {
            return on;
        }

        /**
         * Skip a story.
         */
        void skip() {
            //Moving on to the next story.
            storyIndex++;
            wordIndex = 0;
            checkIndexes();
        }

        /**
         * Check that all sequences are finished.
         */
        private void checkIndexes() {
            //Check a story is finished. If yes moving on to next sequence.
            if (storyIndex == sequences.get(seqIndex).getStories().length) {
                seqIndex++;
                storyIndex = 0;
                wordIndex = 0;
            }
            //Check that all sequences are finshed.
            if (seqIndex == sequences.size()) {
                on = false;
            }
        }

        /**
         * It will create a fragment from current played story. Each fragment wil incrementally have a word from the story.
         *
         * @return fragment of the story.
         */
        Story nextFragment() {
            //Protection from a `skip` call.
            if (!on) {
                return null;
            }
            long now = System.currentTimeMillis();
            //This construct will hold the story from going
            if (wordIndex == sequences.get(seqIndex).getStories()[storyIndex].getText().length() + 1) {
                wordIndex--;
            }
            checkIndexes();
            if (!on) {
                return null;
            }
            //Create a fragment of the original story
            Story currStory = sequences.get(seqIndex).getStories()[storyIndex];
            Story fragment = new Story();
            fragment.setActor(currStory.getActor());
            fragment.setImage(currStory.getImage());
            fragment.setText(currStory.getText().substring(0, wordIndex));
            if (now - lastPrint > DEFAULT_DELAY * 1000) {
                wordIndex++;
                lastPrint = now;
            }
            return fragment;
        }
    }

    /**
     * Render a menu from a list of menu items
     *
     * @param menuItems    list of menu items
     * @param mousePointer position of the mouse. It will be used to identify if any of the menu item is currently selected or not.
     * @param g            graphics object
     * @param margin       margin of the menu.
     * @param y            y position of the menu.
     * @return index of the menu item if selected by mouse else -1.
     */
    private int renderMenu(List<String> menuItems, Point mousePointer, Graphics g, int margin, int y) {
        int option = -1;
        Rectangle[] rectangles = new Rectangle[menuItems.size()];
        //Get the font height
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();

        for (int i = 0; i < menuItems.size(); i++) {
            //Draw a bounding box.
            rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
            //Change the color of menu item in case of mouse pointer is inside it.
            if (rectangles[i].contains(mousePointer)) {
                g.setColor(Color.GRAY);
                option = i;
            } else {
                g.setColor(Color.ORANGE);
            }
            //Print the name of the menu.
            g.drawString(menuItems.get(i), margin, y + fontHeight);
            y += fontHeight;
        }
        return option;
    }

    /**
     * Create a player stats screen on the left of the main screen. It will have player related information. Right now
     * it tells the lives left for the player.
     *
     * @param g graphics object.
     */
    private void renderPlayerStats(Graphics g) {
        //Player stats box configuration
        int margin = 10;
        int imgHeight = 50;
        int imgWidth = 50;

        g.drawImage(healthMarker, margin, margin, imgWidth, imgHeight, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Times New Roman", Font.BOLD, 30));
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight();
        g.drawString(String.format("x %s", gameWorld.getPlayer1().getLives()), margin + imgWidth, margin + fontHeight);
    }
}
