package nl.vu.cs.ancientegyptiansgame.pillars;

import nl.vu.cs.ancientegyptiansgame.data.model.Pillar;

@FunctionalInterface
public interface PillarListener {

    void changed(Pillar pillar, Integer newValue);

}