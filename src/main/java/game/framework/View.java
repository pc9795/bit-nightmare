package game.framework;

import game.framework.controllers.GamepadController;
import game.framework.controllers.KeyboardController;
import game.framework.controllers.MouseController;
import game.framework.narrator.Sequence;
import game.framework.narrator.Story;
import game.framework.narrator.StoryLoader;
import game.utils.BufferedImageLoader;
import game.utils.Constants;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;


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

    View(Model world) {
        this.gameWorld = world;
        this.camera = new Camera(0, 0);
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

        checkStories();
        renderGame(g);

        if (storyTeller.isOn()) {
            gameWorld.pause();
            gameScreenAlpha = 0.5f;
            renderStory(g);
        } else {
            gameWorld.resume();
            gameScreenAlpha = 1f;
        }

        //todo remove; only for debug
        Point mousePos = MouseController.getInstance().getCurrentPos();
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

