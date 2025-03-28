package nl.vu.cs.ancientegyptiansgame.listeners;

import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;

@FunctionalInterface
public interface PillarListener {

    void changed(Pillars pillars, Integer newValue);

}