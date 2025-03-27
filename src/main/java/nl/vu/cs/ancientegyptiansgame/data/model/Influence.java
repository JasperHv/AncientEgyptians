package nl.vu.cs.ancientegyptiansgame.data.model;

public class Influence {
    private String pillar;
    private Integer value;

    public Influence() {}
    public Influence(String pillar, Integer value) {
        this.pillar = pillar;
        this.value = value;
    }

    public String getPillar() {
        return pillar;
    }

    public Integer getValue() { return value; }


}
