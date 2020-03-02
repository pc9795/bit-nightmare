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
 * Purpose: TODO:
 **/
public class StoryLoader {
    private static final StoryLoader INSTANCE = new StoryLoader();
    //For having locations in sorted order.
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

    private void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StoryConfig config;
        try (InputStream in = StoryLoader.class.getResourceAsStream(Constants.STORY_CONFIG_FILE_LOC)) {
            //Loading data from json
            config = mapper.readValue(in, StoryConfig.class);
        }
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        BufferedImage[] images = new BufferedImage[config.getImages().length];
        for (int i = 0; i < images.length; i++) {
            images[i] = imgLoader.loadImage(config.getImages()[i]);
        }
        for (Sequence sequence : config.getSequences()) {
            if (!sequenceMap.containsKey(sequence.getLevel())) {
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

    public boolean hasStories(String level) {
        return sequenceMap.getOrDefault(level, new HashMap<>()).size() != 0;
    }

    public int getNearestStoryPos(String level, int pos) {
        Map<Integer, List<Sequence>> levelSequenceMap = sequenceMap.getOrDefault(level, new HashMap<>());
        int nearest = -1;
        for (Integer storyPos : levelSequenceMap.keySet()) {
            //todo make configurable
            if (pos < storyPos - 50 || pos > storyPos + 50) {
                continue;
            }
            nearest = storyPos;
            break;
        }
        return nearest;
    }

    public List<Sequence> getSequences(String level, int pos) {
        Map<Integer, List<Sequence>> levelSequenceMap = sequenceMap.getOrDefault(level, new HashMap<>());
        return levelSequenceMap.getOrDefault(pos, new ArrayList<>());
    }
}
