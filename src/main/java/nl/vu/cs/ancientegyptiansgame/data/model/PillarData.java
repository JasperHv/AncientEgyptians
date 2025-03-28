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

    public void addListener(PillarListener listener) {
        listeners.add(listener);
    }

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