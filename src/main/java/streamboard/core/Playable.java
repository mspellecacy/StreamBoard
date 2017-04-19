package streamboard.core;

/**
 * Created by mspellecacy on 4/18/2017.
 */
public class Playable {
    private String label;
    private String filePath;

    public Playable() {}

    public Playable(String label, String filePath) {
        this.label = label;
        this.filePath = filePath;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
