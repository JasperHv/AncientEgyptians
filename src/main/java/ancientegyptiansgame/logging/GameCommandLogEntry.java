package ancientegyptiansgame.logging;

import ancientegyptiansgame.data.model.Influence;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GameCommandLogEntry extends CommandLogEntry {

    private final List<Influence> influence;
    private final int yearCount;
    private final int scoreCount;

    @JsonCreator
    public GameCommandLogEntry(
            @JsonProperty("cardTitle") String cardTitle,
            @JsonProperty("swipeDirection") String swipeDirection,
            @JsonProperty("influence") List<Influence> influence,
            @JsonProperty("timestamp") long timestamp,
            @JsonProperty("yearsInPower") int yearCount,
            @JsonProperty("scoreCount") int scoreCount
    ) {
        super("GameSwipeCommand", cardTitle, swipeDirection, timestamp);
        this.influence = influence;
        this.yearCount = yearCount;
        this.scoreCount = scoreCount;
    }

    public List<Influence> getInfluence() { return influence; }
    public int getYearCount() { return yearCount; }
    public int getScoreCount() { return scoreCount; }
}