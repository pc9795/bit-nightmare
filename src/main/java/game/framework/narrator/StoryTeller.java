package game.framework.narrator;

import com.fasterxml.jackson.databind.ObjectMapper;
import game.utils.BufferedImageLoader;
import game.utils.Constants;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-02-2020 13:41
 * Purpose: TODO:
 **/
public class StoryTeller {
    private static final StoryTeller INSTANCE = new StoryTeller();
    private Map<Integer, List<Sequence>> sequenceMap = new HashMap<>();

    private StoryTeller() {
        try {
            init();
        } catch (IOException e) {
            System.out.println("Error in loading stories");
            e.printStackTrace();
        }
    }

    public static StoryTeller getInstance() {
        return INSTANCE;
    }

    private void init() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        StoryConfig config;
        try (InputStream in = StoryTeller.class.getResourceAsStream(Constants.STORY_CONFIG_FILE_LOC)) {
            //Loading data from json
            config = mapper.readValue(in, StoryConfig.class);
        }
        BufferedImageLoader imgLoader = BufferedImageLoader.getInstance();
        BufferedImage[] images = new BufferedImage[config.getImages().length];
        for (int i = 0; i < images.length; i++) {
            images[i] = imgLoader.loadImage(config.getImages()[i]);
        }
        for (Sequence sequence : config.getSequences()) {
            if (!sequenceMap.containsKey(sequence.getPos())) {
                sequenceMap.put(sequence.getPos(), new LinkedList<>());
            }
            for (Story story : sequence.getStories()) {
                story.setImage(images[story.getImageLink()]);
            }
            sequenceMap.get(sequence.getPos()).add(sequence);
        }
    }

    public boolean isSequenceExists(int pos) {
        return sequenceMap.containsKey(pos);
    }

    public List<Sequence> getSequences(int pos) {
        return sequenceMap.get(pos);
    }
}
