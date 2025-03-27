package nl.vu.cs.ancientegyptiansgame.data.model;

public class Ending {
    private String description;
    private String image;

    public Ending() {}
    public Ending(String description, String image) {
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
