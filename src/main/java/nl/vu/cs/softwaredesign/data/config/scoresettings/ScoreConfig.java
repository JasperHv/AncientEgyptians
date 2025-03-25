package nl.vu.cs.softwaredesign.data.config.scoresettings;

public class ScoreConfig {
    private final int initialScore;
    private final int initialYearCount;
    private final int yearThreshold;

    public ScoreConfig(int initialScore, int initialYearCount, int yearThreshold) {
        this.initialScore = initialScore;
        this.initialYearCount = initialYearCount;
        this.yearThreshold = yearThreshold;
    }

    public int getInitialScore() { return initialScore; }
    public int getInitialYearCount() { return initialYearCount; }
    public int getYearThreshold() { return yearThreshold; }
}