package nl.vu.cs.softwaredesign.pillars;

@FunctionalInterface
public interface PillarListener {

    void changed(Integer newValue);

}