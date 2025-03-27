package nl.vu.cs.softwaredesign.pillars;

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