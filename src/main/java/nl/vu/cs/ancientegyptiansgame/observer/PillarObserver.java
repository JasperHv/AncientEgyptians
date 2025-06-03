package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;

import java.util.HashMap;
import java.util.Map;

public class PillarObserver {

    Map<String, PillarData> pillars;

    public PillarObserver() {
        this.pillars = new HashMap<>();
    }

    public void addPillar(String name, Integer initialValue) {
        pillars.put(name, new PillarData(initialValue));
    }

    public void removePillar(String name) {
        pillars.remove(name);
    }

    public PillarData getPillarData(String name) {
        return pillars.get(name);
    }
}