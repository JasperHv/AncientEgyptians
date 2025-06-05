package nl.vu.cs.ancientegyptiansgame.logging;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class GameStateEntry {
    @JsonProperty("pillarValues")
    private final Map<String, Integer> pillars;

    @JsonProperty("year")
    private final int year;

    @JsonProperty("score")
    private final int score;
    @JsonProperty("timestamp")
    private final long timestamp;

    public GameStateEntry(Map<String, Integer> pillars, int year, int score, long timestamp) {
        this.pillars = pillars;
        this.year = year;
        this.score = score;
        this.timestamp = timestamp;
    }

    public Map<String, Integer> getPillars() { return pillars; }

    public int getYear() { return year; }

    public int getScore() { return score; }

    public long getTimestamp() {
        return timestamp;
    }
}
