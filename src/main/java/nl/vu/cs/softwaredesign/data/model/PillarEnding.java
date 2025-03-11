package nl.vu.cs.softwaredesign.data.model;

public class PillarEnding {
    private String description;
    private String image;

    public PillarEnding() {}
    public PillarEnding(String description, String image) {
        this.description = description;
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}

