package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;

import java.util.EnumMap;
import java.util.Map;

public class PillarObserver {

    private final EnumMap<Pillars, PillarData> pillars;

    public PillarObserver() {
        this.pillars = new EnumMap<>(Pillars.class);
    }

    public void addPillar(Pillars pillar, Integer initialValue) {
        pillars.put(pillar, new PillarData(pillar, initialValue));
    }

    public void removePillar(Pillars pillar) {
        pillars.remove(pillar);
    }

    public PillarData getPillarData(Pillars pillar) {
        return pillars.get(pillar);
    }

    public Map<Pillars, PillarData> getAllPillars() {
        return pillars;
    }
}