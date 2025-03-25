package nl.vu.cs.softwaredesign.data.model;

import java.util.List;

public class Card {
    private int id;
    private String title;
    private String scenario;
    private String pillar;
    private String type;
    private List<Influence> influence;
    private int frequency;

    public Card() {}
    public Card(int id, String title, String scenario, String pillar, String type, List<Influence> influence, int frequency) {
        this.id = id;
        this.title = title;
        this.scenario = scenario;
        this.pillar = pillar;
        this.type = type;
        this.influence = influence;
        this.frequency = frequency;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getScenario() { return scenario; }
    public String getPillar() { return pillar; }
    public String getType() { return type; }
    public List<Influence> getInfluence() { return influence; }
    public int getFrequency() { return frequency; }

    public Card decrementFrequency() {
        return new Card(this.id, this.title, this.scenario, this.pillar, this.type, this.influence, this.frequency - 1);
    }
}
