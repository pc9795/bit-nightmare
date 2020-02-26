package game.framework;

import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.framework.controllers.MouseController;
import game.framework.narrator.Sequence;
import game.framework.narrator.Story;
import game.framework.narrator.StoryLoader;
import game.framework.visual.TextureLoader;
import game.utils.BufferedImageLoader;
import game.utils.Constants;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;


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
class View extends Canvas {
    private Model gameWorld;
    private BufferStrategy bs;
    private Camera camera;
    private BufferedImage bg;
    private StoryTeller storyTeller = new StoryTeller();
    private HashSet<Integer> storyLocationsProcessed = new HashSet<>();
    private float gameScreenAlpha = 1f;
    private Screen currScreen = Screen.TITLE, prevScreen = null;
    private String levelSelected;


    View(Model world) {
        this.gameWorld = world;
        this.camera = new Camera(0, 0);
        //This will start loading of the textures. As it gives a massive lag to the system. So doing it early on.
        TextureLoader.getInstance();
        try {
            this.bg = BufferedImageLoader.getInstance().loadImage(Constants.BACKGROUND_IMG_LOC);

        } catch (IOException e) {
            System.out.println(String.format("Unable to load background image:%s", Constants.BACKGROUND_IMG_LOC));
            e.printStackTrace();
        }
    }

    void updateView() {
        //Creating buffers for one time. We cannot create early as it throws an exception
        if (bs == null) {
            createBufferStrategy(Constants.BUFFER_COUNT);
            bs = getBufferStrategy();
        }
        //Update camera
        Graphics g = bs.getDrawGraphics();

        //ALL CONTROLLERS `poll` CALLS MUST BE INSIDE SPECIFIC CASES. Any call outside can produce unexpected results.
        //If menu items are a DAG then it is easy to navigate. Have to check the possibility of cycles.
        switch (currScreen) {
            case TITLE:
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setColor(Color.ORANGE);
                g.setFont(new Font("Game Music Love", Font.BOLD, 150));
                String text = "BIT-NIGHTMARE";
                FontMetrics fm = g.getFontMetrics();
                int fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                int textWidth = fm.stringWidth(text);
                int margin = 10;
                g.drawString(text, getWidth() / 2 - textWidth / 2, margin + fontHeight);
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
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.TITLE;
                }
                break;
            case MAIN:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                List<String> menuItems;
                boolean checkpointAvailable = gameWorld.isLastCheckpointAvailable();
                if (checkpointAvailable) {
                    menuItems = Arrays.asList("Continue", "New Game", "Select Level", "Settings", "Quit");
                } else {
                    menuItems = Arrays.asList("New Game", "Select Level", "Settings", "Quit");
                }
                Rectangle[] rectangles = new Rectangle[menuItems.size()];
                int y = margin;
                int option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                //Shifting as we now don't have a continue option
                if (!checkpointAvailable && option != -1) {
                    option++;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                            gameWorld.loadLastCheckPoint();
                            storyLocationsProcessed.clear();
                            currScreen = Screen.IN_GAME;
                            prevScreen = Screen.MAIN;
                            break;
                        case 1:
                            //Load first level.
                            //The game world should have at least one level else it will throw exception early.
                            levelSelected = gameWorld.getLevels().get(0);
                            if (checkpointAvailable) {
                                currScreen = Screen.NEW_GAME;
                            } else {
                                currScreen = Screen.DIFFICULTY_SELECT;
                            }
                            prevScreen = Screen.MAIN;
                            break;
                        case 2:
                            currScreen = Screen.LEVEL_SELECT;
                            prevScreen = Screen.MAIN;
                            break;
                        case 3:
                            currScreen = Screen.SETTINGS;
                            prevScreen = Screen.MAIN;
                            break;
                        case 4:
                            currScreen = Screen.QUIT;
                            prevScreen = Screen.MAIN;
                            break;
                    }
                }
                break;
            case NEW_GAME:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = Arrays.asList("Yes", "No");
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                g.drawString("You will lose your previous data!", margin, y + fontHeight);
                y += 2 * fontHeight;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                            try {
                                gameWorld.loadLevel(levelSelected);
                                storyLocationsProcessed.clear();
                                currScreen = Screen.IN_GAME;
                                prevScreen = Screen.NEW_GAME;
                                //Clean the name. This option always come after some previous screen
                                levelSelected = null;

                            } catch (IOException e) {
                                System.out.println(String.format("Unable to load level: %s", levelSelected));
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            currScreen = Screen.MAIN;
                            prevScreen = Screen.NEW_GAME;
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.NEW_GAME;
                }
                break;
            case DIFFICULTY_SELECT:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = Arrays.asList("- I am not a programmer", "- I can code", "- I can reprogram you");
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                g.drawString("Select a difficulty", margin, y + fontHeight);
                y += fontHeight;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                        case 1:
                        case 2:
                            try {
                                gameWorld.loadLevel(levelSelected);
                                storyLocationsProcessed.clear();
                                currScreen = Screen.IN_GAME;
                                prevScreen = Screen.DIFFICULTY_SELECT;
                                //Clean the name. This option always come after some previous screen
                                levelSelected = null;

                            } catch (IOException e) {
                                System.out.println(String.format("Unable to load level: %s", levelSelected));
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.DIFFICULTY_SELECT;
                }
                break;
            case LEVEL_SELECT:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = gameWorld.getLevels();
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case -1:
                            break;
                        default:
                            levelSelected = menuItems.get(option);
                            if (gameWorld.isLastCheckpointAvailable()) {
                                currScreen = Screen.NEW_GAME;
                            } else {
                                currScreen = Screen.DIFFICULTY_SELECT;
                            }
                            prevScreen = Screen.LEVEL_SELECT;
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.LEVEL_SELECT;
                    return;
                }
                break;
            case SETTINGS:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = Collections.singletonList("Show Controller Layout");
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                            currScreen = Screen.CONTROL_LAYOUT;
                            prevScreen = Screen.SETTINGS;
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.MAIN;
                    prevScreen = Screen.SETTINGS;
                    return;
                }
                break;
            case CONTROL_LAYOUT:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.SETTINGS;
                    prevScreen = Screen.CONTROL_LAYOUT;
                    return;
                }
                break;
            case PAUSE:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = Arrays.asList("- Resume", "- Quit to main menu", "- Quit");
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                g.drawString("Paused", margin, y + fontHeight);
                y += fontHeight;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                            currScreen = Screen.IN_GAME;
                            prevScreen = Screen.PAUSE;
                            gameWorld.resume();
                            break;
                        case 1:
                            currScreen = Screen.QUIT_TO_MENU;
                            prevScreen = Screen.PAUSE;
                            break;
                        case 2:
                            currScreen = Screen.QUIT;
                            prevScreen = Screen.PAUSE;
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.IN_GAME;
                    prevScreen = Screen.PAUSE;
                    gameWorld.resume();
                }
                break;
            case QUIT_TO_MENU:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = Arrays.asList("- Yes", "- No");
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                g.drawString("Quit to main menu", margin, y + fontHeight);
                y += 2 * fontHeight;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                            currScreen = Screen.MAIN;
                            prevScreen = Screen.QUIT_TO_MENU;
                            break;
                        case 1:
                            currScreen = Screen.PAUSE;
                            prevScreen = Screen.QUIT_TO_MENU;
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = Screen.PAUSE;
                    prevScreen = Screen.QUIT_TO_MENU;
                }
                break;
            case QUIT:
                g.setColor(Color.ORANGE);
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setFont(new Font("Game Music Love", Font.BOLD, 60));
                fm = g.getFontMetrics();
                margin = 10;
                fontHeight = fm.getHeight() + fm.getDescent() + fm.getAscent();
                menuItems = Arrays.asList("- Yes", "- No");
                rectangles = new Rectangle[menuItems.size()];
                y = margin;
                g.drawString("Quit the game", margin, y + fontHeight);
                y += 2 * fontHeight;
                option = -1;
                for (int i = 0; i < menuItems.size(); i++) {
                    rectangles[i] = new Rectangle(margin, y, fm.stringWidth(menuItems.get(i)), fontHeight);
                    if (rectangles[i].contains(MouseController.getInstance().getCurrentPos())) {
                        g.setColor(Color.GRAY);
                        option = i;
                    } else {
                        g.setColor(Color.ORANGE);
                    }
                    g.drawString(menuItems.get(i), margin, y + fontHeight);
                    y += fontHeight;
                }
                MouseController.getInstance().poll();
                if (MouseController.getInstance().isLeftClickedOnce()) {
                    switch (option) {
                        case 0:
                            System.exit(0);
                            break;
                        case 1:
                            currScreen = prevScreen;
                            break;
                    }
                }
                KeyboardController.getInstance().poll();
                if (KeyboardController.getInstance().isEscPressedOnce()) {
                    currScreen = prevScreen;
                }
                break;
            case IN_GAME:
                if (gameWorld.isPaused()) {
                    gameWorld.pause();
                    currScreen = Screen.PAUSE;
                    prevScreen = Screen.IN_GAME;
                }
                //When the View is in IN_GAME mode then controllers are expected to be processed by Model. So any `poll`
                //call to the controller will produce unexpected results.
                checkStories();
                renderGame(g);
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
                g.clearRect(0, 0, getWidth(), getHeight());
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);
                g.setColor(Color.ORANGE);
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
                break;

        }

        //todo remove; only for debug
        Point mousePos = MouseController.getInstance().getCurrentPos();
        g.setColor(Color.RED);
        g.setFont(new Font("Time New Roman", Font.BOLD, 20));
        g.drawString(String.format("X=%s,Y=%s", mousePos.getX(), mousePos.getY()), mousePos.x, mousePos.y);

        g.dispose();
        bs.show();
    }

    //todo improve
    private void renderStory(Graphics g) {
        Story story = storyTeller.nextFragment();
        if (story == null) {
            return;
        }
        KeyboardController.getInstance().poll();
        if (KeyboardController.getInstance().isSpacePressedOnce() || GamepadController.getInstance().isAPressedOnce()) {
            storyTeller.skip();
        }
        int dialogWidth = 800;
        int dialogHeight = 400;
        int margin = 10;
        int imageWidth = 100;
        int imageHeight = 100;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        int x = getWidth() / 2 - dialogWidth / 2;
        int y = getHeight() / 2 - dialogHeight / 2;
        g.setColor(new Color(111, 66, 150));
        g.fillRect(x, y, dialogWidth, dialogHeight);
        g.drawImage(story.getImage(), x + margin, y + margin, imageWidth, imageHeight, null);
        g.setFont(new Font("Arcade Classic", Font.BOLD, 25));
        g.setColor(Color.WHITE);
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getHeight() + fm.getDescent() + fm.getDescent();

        StringTokenizer tokenizer = new StringTokenizer(story.getText(), " ");
        StringBuilder sb = new StringBuilder();
        int yy = y + margin + imageHeight + fontHeight;
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (fm.stringWidth(sb.toString()) + fm.stringWidth(token) + fm.stringWidth(" ") <= 780) {
                sb.append(token).append(" ");
                continue;
            }
            g.drawString(sb.toString(), x + margin, yy);
            yy += fontHeight;
            sb = new StringBuilder();
            sb.append(token).append(" ");
        }
        g.drawString(sb.toString(), x + margin, yy);
        g.drawString("Press [Space] to skip", x + margin, y + dialogHeight - margin - fontHeight);
    }

    private void checkStories() {
        StoryLoader loader = StoryLoader.getInstance();
        if (!loader.hasStories()) {
            return;
        }
        int playerLoc = (int) gameWorld.getPlayer1().getCentre().getX();
        int nearestLoc = loader.getNearestStoryPos(playerLoc);
        if (storyLocationsProcessed.contains(nearestLoc)) {
            return;
        }
        storyTeller.load(loader.getSequences(nearestLoc));
        storyLocationsProcessed.add(nearestLoc);
    }

    private void renderGame(Graphics g) {
        camera.update(gameWorld.getPlayer1());
        Graphics2D g2d = (Graphics2D) g;
        //Clear scenery
        g.clearRect(0, 0, getWidth(), getHeight());
        //Background
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, gameScreenAlpha));
        g.drawImage(bg, 0, 0, getWidth(), getHeight(), null);

        g2d.translate(camera.getX(), camera.getY());
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        //Drawing stuff
        gameWorld.getEnvironment().forEach(object -> object.render(g));
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, gameScreenAlpha));
        gameWorld.getMovableEnvironment().forEach(object -> object.render(g));
        gameWorld.getCollectibles().forEach(object -> object.render(g));
        gameWorld.getEnemies().forEach(object -> object.render(g));
        gameWorld.getBullets().forEach(object -> object.render(g));
        gameWorld.getPlayer1().render(g);
        g2d.translate(-camera.getX(), -camera.getY());
    }

    private enum Screen {
        TITLE, LEVEL_SELECT, MAIN, QUIT_TO_MENU, PAUSE, QUIT, SETTINGS, CONTROL_LAYOUT, IN_GAME,
        NEW_GAME, DIFFICULTY_SELECT, END
    }

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

        void skip() {
            storyIndex++;
            wordIndex = 0;
            checkIndexes();
        }

        private void checkIndexes() {
            if (storyIndex == sequences.get(seqIndex).getStories().length) {
                seqIndex++;
                storyIndex = 0;
                wordIndex = 0;
            }
            if (seqIndex == sequences.size()) {
                on = false;
            }
        }

        Story nextFragment() {
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
}

