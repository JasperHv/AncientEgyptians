package nl.vu.cs.softwaredesign.data.config.scoresettings;

public class ScoreConfig {
    private int initialScore;
    private int initialYearCount;
    private int yearThreshold;

    public ScoreConfig() {}
    public ScoreConfig(int initialScore, int initialYearCount, int yearThreshold) {
        this.initialScore = initialScore;
        this.initialYearCount = initialYearCount;
        this.yearThreshold = yearThreshold;
    }

    public int getInitialScore() { return initialScore; }
    public int getInitialYearCount() { return initialYearCount; }
    public int getYearThreshold() { return yearThreshold; }
}