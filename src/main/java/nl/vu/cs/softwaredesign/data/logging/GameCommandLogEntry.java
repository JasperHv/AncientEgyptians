package nl.vu.cs.softwaredesign.data.logging;

import nl.vu.cs.softwaredesign.data.model.Influence;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GameCommandLogEntry extends CommandLogEntry {
    @JsonProperty("influence")
    private final List<Influence> influence;

    public GameCommandLogEntry(String cardTitle, String swipeDirection, List<Influence> influence, long timestamp) {
        super("GameSwipeCommand", cardTitle, swipeDirection, timestamp);
        this.influence = influence;
    }

    public List<Influence> getInfluence() {
        return influence;
    }
}
