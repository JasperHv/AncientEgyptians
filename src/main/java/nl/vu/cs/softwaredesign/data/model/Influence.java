package nl.vu.cs.softwaredesign.data.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

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

    public void setPillar(String pillar) {
        this.pillar = pillar;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Influence{" +
                "pillar='" + pillar + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
