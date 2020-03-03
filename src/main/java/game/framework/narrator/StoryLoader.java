package game.framework.narrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.utils.BufferedImageLoader;
import game.utils.Constants;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-02-2020 13:41
 * Purpose: Load the stories from a story configuration.
 **/
public class StoryLoader {
    private static final int NEAREST_THRESHOLD = 50;
    private static final StoryLoader INSTANCE = new StoryLoader();
    private Map<String, Map<Integer, List<Sequence>>> sequenceMap = new HashMap<>();

    private StoryLoader() {
        try {
            init();

        } catch (IOException e) {
            System.out.println("Error in loading stories");
            e.printStackTrace();
        }
    }

    public static StoryLoader getInstance() {
        return INSTANCE;
    }

    /**
     * Initialization
     *
     * @throws IOException if there is error in reading the story config json file or any of the images mentioned in it.
     */
    private void init() throws IOException {
        StoryConfig config;

        //Loading data from json
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream in = StoryLoader.class.getResourceAsStream(Constants.STORY_CONFIG_FILE_LOC)) {
            config = mapper.readValue(in, StoryConfig.class);
        }

        //Loading all the images mentioned in config.
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        BufferedImage[] images = new BufferedImage[config.getImages().length];
        for (int i = 0; i < images.length; i++) {
            images[i] = imgLoader.loadImage(config.getImages()[i]);
        }

        //Loading all stories
        for (Sequence sequence : config.getSequences()) {
            if (!sequenceMap.containsKey(sequence.getLevel())) {
                //For having locations in sorted order.
                sequenceMap.put(sequence.getLevel(), new TreeMap<>());
            }
            Map<Integer, List<Sequence>> levelSequenceMap = sequenceMap.get(sequence.getLevel());
            if (!levelSequenceMap.containsKey(sequence.getPos())) {
                levelSequenceMap.put(sequence.getPos(), new LinkedList<>());
            }
            for (Story story : sequence.getStories()) {
                story.setImage(images[story.getImageLink()]);
            }
            levelSequenceMap.get(sequence.getPos()).add(sequence);
        }
    }

    /**
     * @param level name of the level
     * @return true if there are stories for a given level
     */
    public boolean hasStories(String level) {
        return sequenceMap.getOrDefault(level, new HashMap<>()).size() != 0;
    }

    /**
     * Get the nearest story for a given level and a position
     *
     * @param level name of the level
     * @param pos   position to which to find the nearest story.
     * @return position of the nearest story else -1.
     */
    public int getNearestStoryPos(String level, int pos) {
        Map<Integer, List<Sequence>> levelSequenceMap = sequenceMap.getOrDefault(level, new HashMap<>());
        int nearest = -1;
        //As the positions are stored in a tree map they will be sorted. So we traverse through all the positions
        //and stop at the first position within a threshold value.
        for (Integer storyPos : levelSequenceMap.keySet()) {
            if (Math.abs(pos - storyPos) > NEAREST_THRESHOLD) {
                continue;
            }
            nearest = storyPos;
            break;
        }
        return nearest;
    }

    /**
     * Get stories for a given level and position.
     *
     * @param level name of the level
     * @param pos   position of the sequence
     * @return sequence at the given level and position.
     */
    public List<Sequence> getSequences(String level, int pos) {
        Map<Integer, List<Sequence>> levelSequenceMap = sequenceMap.getOrDefault(level, new HashMap<>());
        return levelSequenceMap.getOrDefault(pos, new ArrayList<>());
    }
}
