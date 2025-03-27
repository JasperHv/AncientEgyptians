package nl.vu.cs.ancientegyptiansgame.pillars;

import nl.vu.cs.ancientegyptiansgame.data.model.Pillar;

import java.util.HashMap;
import java.util.Map;

public class PillarObserver {

    Map<String, PillarData> pillars;

    public PillarObserver() {
        this.pillars = new HashMap<>();
    }

    public void addPillar(Pillar pillar, Integer initialValue) {
        pillars.put(pillar.getName(), new PillarData(pillar, initialValue));
    }

    public void removePillar(String name) {
        pillars.remove(name);
    }

    public PillarData getPillarData(String name) {
        return pillars.get(name);
    }
}