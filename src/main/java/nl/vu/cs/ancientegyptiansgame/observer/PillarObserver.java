package nl.vu.cs.ancientegyptiansgame.observer;

import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;

import java.util.EnumMap;
import java.util.Map;

public class PillarObserver {

    private final EnumMap<Pillars, PillarData> pillars;

    /**
     * Constructs a new PillarObserver with an empty mapping of pillars to their data.
     */
    public PillarObserver() {
        this.pillars = new EnumMap<>(Pillars.class);
    }

    /****
     * Adds a new pillar and its associated data to the observer.
     *
     * @param pillar the pillar to add
     * @param initialValue the initial value to associate with the pillar
     */
    public void addPillar(Pillars pillar, Integer initialValue) {
        pillars.put(pillar, new PillarData(pillar, initialValue));
    }

    /****
     * Removes the `PillarData` associated with the specified pillar from the collection.
     *
     * @param pillar the pillar whose data should be removed
     */
    public void removePillar(Pillars pillar) {
        pillars.remove(pillar);
    }

    /**
     * Retrieves the {@link PillarData} associated with the specified pillar.
     *
     * @param pillar the pillar whose data is to be retrieved
     * @return the {@code PillarData} for the given pillar, or {@code null} if not present
     */
    public PillarData getPillarData(Pillars pillar) {
        return pillars.get(pillar);
    }

    /**
     * Returns the map of all pillars and their associated data.
     *
     * @return a map containing each pillar and its corresponding PillarData
     */
    public Map<Pillars, PillarData> getAllPillars() {
        return pillars;
    }
}