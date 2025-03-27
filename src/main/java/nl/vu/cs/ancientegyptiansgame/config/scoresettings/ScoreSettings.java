package nl.vu.cs.ancientegyptiansgame.config.scoresettings;

public class ScoreSettings {
    private ScoreConfig scoreConfig;
    private BonusConfig bonusConfig;
    private int yearCountIncrease;
    private int baseScoreIncrease;
    public ScoreSettings() {}
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