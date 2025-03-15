package nl.vu.cs.softwaredesign.data.config.gamesettings;

import java.util.List;

public class ScoreSettings {
    private int initialScore;
    private int initialYearCount;
    private int yearThreshold;
    private int yearCountIncrease;
    private int baseScoreIncrease;
    private List<Integer> thresholds;
    private List<Integer> bonusScores;
    private int balancedBonus;

    public int getInitialScore() {
        return initialScore;
    }

    public int getInitialYearCount() { return initialYearCount; }

    public int getYearThreshold() { return yearThreshold; }

    public int getYearCountIncrease() { return yearCountIncrease; }

    public int getBaseScoreIncrease() {
        return baseScoreIncrease;
    }

    public void setBaseScoreIncrease(int baseScoreIncrease) {
        this.baseScoreIncrease = baseScoreIncrease;
    }

    public List<Integer> getThresholds() {
        return thresholds;
    }

    public List<Integer> getBonusScores() {
        return bonusScores;
    }

    public int getBalancedBonus() {
        return balancedBonus;
    }
}
