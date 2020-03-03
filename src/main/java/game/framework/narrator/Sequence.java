package game.framework.narrator;

/**
 * Created By: Prashant Chaubey
 * Student No: 18200540
 * Created On: 25-02-2020 13:54
 * Purpose: Represent a sequence of stories at a particular position in a level.
 **/
//We need public getter methods for jackson to work that's why suppressing the warnings.
@SuppressWarnings({"WeakerAccess", "unused"})
public class Sequence {
    private String id;
    private int pos;
    private String level;
    private Story[] stories;

    public String getId() {
        return id;
    }

    public int getPos() {
        return pos;
    }

    public Story[] getStories() {
        return stories;
    }

    public String getLevel() {
        return level;
    }
}
