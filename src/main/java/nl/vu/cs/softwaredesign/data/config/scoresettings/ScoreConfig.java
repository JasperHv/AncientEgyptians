package nl.vu.cs.softwaredesign.data.config.scoresettings;

public class ScoreConfig {
    private int initialScore;
    private int initialYearCount;
    private int yearThreshold;
    private int maximumYearCount;

    public ScoreConfig() {}
    public ScoreConfig(int initialScore, int initialYearCount, int yearThreshold, int maximumYearCount) {
        this.initialScore = initialScore;
        this.initialYearCount = initialYearCount;
        this.yearThreshold = yearThreshold;
        this.maximumYearCount = maximumYearCount;
    }

    public int getInitialScore() { return initialScore; }
    public int getInitialYearCount() { return initialYearCount; }
    public int getYearThreshold() { return yearThreshold; }
    public int getMaximumYearCount() { return maximumYearCount; }
}