package nl.vu.cs.ancientegyptiansgame.data.model;

import nl.vu.cs.ancientegyptiansgame.listeners.PillarListener;

import java.util.ArrayList;
import java.util.List;

public class PillarData {
    private Integer value;
    private final List<PillarListener> listeners;
    private final Pillars pillars;

    public PillarData(Pillars pillars, Integer initialValue) {
        this.pillars = pillars;
        this.listeners = new ArrayList<>();
        this.value = initialValue;
    }

    /****
     * Registers a listener to be notified when the pillar's value changes.
     *
     * @param listener the listener to add for value change notifications
     */
    public void addListener(PillarListener listener) {
        listeners.add(listener);
    }

    /****
     * Increases the current value by the specified increment, clamping the result between 0 and 100.
     *
     * @param increment the amount to add to the current value
     */
    public void increaseValue(Integer increment) {
        setValue(value + increment);
    }

    /**
     * Returns the current value of the pillar.
     *
     * @return the current integer value representing the pillar's state
     */
    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        if (value < 0) {
            this.value = 0;
        }
        if (value > 100) {
            this.value = 100;
        }
        for (PillarListener listener : listeners) {
            listener.changed(pillars, this.value);
        }
    }
}