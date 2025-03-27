package nl.vu.cs.ancientegyptiansgame.data.model;

import java.util.List;
import java.util.Objects;

public class Card implements Comparable<Card> {
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

    @Override
    public int compareTo(Card other) {
        return Integer.compare(other.frequency, this.frequency);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Card other = (Card) obj;
        return id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
