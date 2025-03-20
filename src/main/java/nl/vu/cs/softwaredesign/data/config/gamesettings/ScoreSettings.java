package nl.vu.cs.softwaredesign.data.config.gamesettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreSettings {
    private int initialScore;
    private int initialYearCount;
    private int yearThreshold;
    private int yearCountIncrease;
    private int baseScoreIncrease;
    private List<Integer> thresholds;
    private List<Integer> bonusScores;
    private int balancedBonus;
    private Map<String, Integer> legacyCardThresholds;

    public ScoreSettings() {
        legacyCardThresholds = new HashMap<>();
    }

    // Getters
    public int getInitialScore() {
        return initialScore;
    }

    public int getInitialYearCount() { return initialYearCount; }


    public void setInitialYearCount(int initialYearCount) { this.initialYearCount = initialYearCount; }

    public int getYearThreshold() { return yearThreshold; }

    public void setYearThreshold(int yearThreshold) { this.yearThreshold = yearThreshold; }

    public int getYearCountIncrease() { return yearCountIncrease; }

    public void setYearCountIncrease(int yearCountIncrease) { this.yearCountIncrease = yearCountIncrease; }
    public int getBaseScoreIncrease() { return baseScoreIncrease; }

    public void setBaseScoreIncrease(int baseScoreIncrease) { this.baseScoreIncrease = baseScoreIncrease; }

    public List<Integer> getThresholds() { return thresholds; }
    public void setThresholds(List<Integer> thresholds) { this.thresholds = thresholds; }

    public List<Integer> getBonusScores() { return bonusScores; }

    public void setBonusScores(List<Integer> bonusScores) { this.bonusScores = bonusScores; }

    public int getBalancedBonus() { return balancedBonus; }

    public void setBalancedBonus(int balancedBonus) { this.balancedBonus = balancedBonus; }

    public Map<String, Integer> getLegacyCardThresholds() { return legacyCardThresholds; }


    public Map<String, Integer> getInitialValues() {
        Map<String, Integer> values = new HashMap<>();
        values.put("scoreCount", initialScore);
        values.put("yearCount", initialYearCount);
        values.put("yearThreshold", yearThreshold);
        return values;
    }
}
