package nl.vu.cs.softwaredesign.pillars;

import nl.vu.cs.softwaredesign.data.model.Pillar;

@FunctionalInterface
public interface PillarListener {

    void changed(Pillar pillar, Integer newValue);

}