package ancientegyptiansgame.listeners;

import ancientegyptiansgame.data.model.Pillars;

@FunctionalInterface
public interface PillarListener {

    void changed(Pillars pillars, Integer newValue);

}