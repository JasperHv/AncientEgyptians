package nl.vu.cs.softwaredesign.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Card {
    private int id;
    private String title;
    private String scenario;
    private String pillar;
    private String type;
    private List<Influence> influence;

    public Card() {}

    public int getId() { return id; }

    public String getTitle() { return title; }

    public String getScenario() { return scenario; }

    public String getPillar() { return pillar; }

    public String getType() { return type; }

    public List<Influence> getInfluence() { return influence; }

}
