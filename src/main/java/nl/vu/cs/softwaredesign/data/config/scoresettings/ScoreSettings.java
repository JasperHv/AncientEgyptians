package nl.vu.cs.softwaredesign.data.config.scoresettings;

public class ScoreSettings {
    private final ScoreConfig scoreConfig;
    private final BonusConfig bonusConfig;
    private final int yearCountIncrease;
    private final int baseScoreIncrease;

    public ScoreSettings(ScoreConfig scoreConfig, BonusConfig bonusConfig, int yearCountIncrease, int baseScoreIncrease) {
        this.scoreConfig = scoreConfig;
        this.bonusConfig = bonusConfig;
        this.yearCountIncrease = yearCountIncrease;
        this.baseScoreIncrease = baseScoreIncrease;
    }

    public ScoreConfig getScoreConfig() { return scoreConfig; }
    public BonusConfig getBonusConfig() { return bonusConfig; }
    public int getYearCountIncrease() { return yearCountIncrease; }
    public int getBaseScoreIncrease() { return baseScoreIncrease; }

}