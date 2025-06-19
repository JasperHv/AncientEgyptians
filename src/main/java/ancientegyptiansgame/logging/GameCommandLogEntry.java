package ancientegyptiansgame.logging;

import ancientegyptiansgame.data.model.Influence;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GameCommandLogEntry extends CommandLogEntry {

    @JsonProperty("influence")
    private final List<Influence> influence;

    @JsonProperty("yearsInPower")
    private final int yearCount;

    @JsonProperty("scoreCount")
    private final int scoreCount;

    public GameCommandLogEntry(String cardTitle, String swipeDirection, List<Influence> influence, long timestamp, int yearCount, int scoreCount) {
        super("GameSwipeCommand", cardTitle, swipeDirection, timestamp);
        this.influence = influence;
        this.yearCount = yearCount;
        this.scoreCount = scoreCount;
    }

    public List<Influence> getInfluence() { return influence; }

    public int getYearCount() { return yearCount; }

    public int getScoreCount() { return scoreCount; }
}
