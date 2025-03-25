package nl.vu.cs.softwaredesign.data.config.scoresettings;

import java.util.List;

public class BonusConfig {
    private final List<Integer> thresholds;
    private final List<Integer> bonusScores;
    private final int balancedBonus;

    public BonusConfig(List<Integer> thresholds, List<Integer> bonusScores, int balancedBonus) {
        this.thresholds = thresholds;
        this.bonusScores = bonusScores;
        this.balancedBonus = balancedBonus;
    }

    public List<Integer> getThresholds() { return thresholds; }
    public List<Integer> getBonusScores() { return bonusScores; }
    public int getBalancedBonus() { return balancedBonus; }
}