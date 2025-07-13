package nl.vu.cs.ancientegyptiansgame.logging;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.vu.cs.ancientegyptiansgame.data.model.Mode;

import java.util.Map;

public class GameStateEntry {
    @JsonProperty("gameMode")
    private final Mode gameMode;

    @JsonProperty("pillarValues")
    private final Map<String, Integer> pillars;

    @JsonProperty("year")
    private final int year;

    @JsonProperty("score")
    private final int score;

    @JsonProperty("timestamp")
    private final long timestamp;

    @JsonCreator
    public GameStateEntry(
            @JsonProperty("gameMode") Mode gameMode,
            @JsonProperty("pillarValues") Map<String, Integer> pillars,
            @JsonProperty("year") int year,
            @JsonProperty("score") int score,
            @JsonProperty("timestamp") long timestamp) {
        this.gameMode = gameMode;
        this.pillars = pillars;
        this.year = year;
        this.score = score;
        this.timestamp = timestamp;
    }

    public Mode getGameMode() { return gameMode; }

    public Map<String, Integer> getPillars() { return pillars; }

    public int getYear() { return year; }

    public int getScore() { return score; }

    public long getTimestamp() {
        return timestamp;
    }
}
