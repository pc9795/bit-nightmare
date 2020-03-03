package game.framework.narrator;

/**
 * Created By: Prashant Chaubey
 * Created On: 25-02-2020 13:41
 * Purpose: Configuration for stories.
 **/
//We need public getter methods for jackson to work that's why suppressing the warnings.
@SuppressWarnings({"WeakerAccess", "unused"})
public class StoryConfig {
    private String[] images;
    private Sequence[] sequences;

    public String[] getImages() {
        return images;
    }

    public Sequence[] getSequences() {
        return sequences;
    }
}
