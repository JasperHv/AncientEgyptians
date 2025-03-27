package nl.vu.cs.softwaredesign.pillars;

import nl.vu.cs.softwaredesign.data.model.Pillar;

import java.util.ArrayList;
import java.util.List;

public class PillarData {
    private Integer value;
    private final List<PillarListener> listeners;
    private final Pillar pillar; // Assuming you have a reference to the Pillar

    public PillarData(Pillar pillar, Integer initialValue) {
        this.pillar = pillar;
        this.listeners = new ArrayList<>();
        this.value = initialValue;
        System.out.println("PillarData initialized with value: " + initialValue);
    }

    public void addListener(PillarListener listener) {
        listeners.add(listener);
        System.out.println("Listener added. Total listeners: " + listeners.size());
    }

    public void increaseValue(Integer increment) {
        System.out.println("Increasing value by: " + increment);
        setValue(value + increment);
    }

    public Integer getValue() {
        System.out.println("Current value retrieved: " + value);
        return value;
    }

    public void setValue(Integer value) {
        System.out.println("Setting value to: " + value);
        this.value = value;
        if (value < 0) {
            this.value = 0;
            System.out.println("Value adjusted to minimum: 0");
        }
        if (value > 100) {
            this.value = 100;
            System.out.println("Value adjusted to maximum: 100");
        }
        for (PillarListener listener : listeners) {
            listener.changed(pillar, this.value); // Pass the Pillar object
        }
        System.out.println("Listeners notified of value change: " + this.value);
    }
}