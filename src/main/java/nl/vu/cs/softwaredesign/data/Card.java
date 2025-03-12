package nl.vu.cs.softwaredesign.data;

import java.util.List;
import java.util.ArrayList;

public class Card {

    private String image;

    private String name;

    private List<Effect> effects = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Effect> getEffects() {
        return effects;
    }

    public void setEffects(List<Effect> effects) {
        this.effects = effects;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
