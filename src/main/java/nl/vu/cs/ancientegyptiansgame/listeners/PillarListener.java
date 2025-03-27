package nl.vu.cs.ancientegyptiansgame.listeners;

import nl.vu.cs.ancientegyptiansgame.data.model.Pillar;

@FunctionalInterface
public interface PillarListener {

    void changed(Pillar pillar, Integer newValue);

}