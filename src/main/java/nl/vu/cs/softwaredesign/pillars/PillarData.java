package nl.vu.cs.softwaredesign.pillars;

import java.util.ArrayList;
import java.util.List;

public class PillarData {
    private Integer value;
    private final List<PillarListener> listeners;

    public PillarData(Integer initialValue) {
        this.listeners = new ArrayList<>();
        this.value = initialValue;
    }


    public void addListener(PillarListener listener) {
        listeners.add(listener);
    }

    public void increaseValue(Integer increment) {
        setValue(value + increment);
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
        if(value < 0) {
            this.value = 0;
        }
        if(value > 100) {
            this.value = 100;
        }
        for (PillarListener listener : listeners) {
            listener.changed(this.value);
        }
    }

}