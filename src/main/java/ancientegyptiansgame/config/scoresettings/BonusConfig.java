package ancientegyptiansgame.config.scoresettings;

import java.util.List;

public class BonusConfig {
    private List<Integer> thresholds;
    private List<Integer> bonusScores;
    private int balancedBonus;

    public BonusConfig() {}
    public BonusConfig(List<Integer> thresholds, List<Integer> bonusScores, int balancedBonus) {
        this.thresholds = thresholds;
        this.bonusScores = bonusScores;
        this.balancedBonus = balancedBonus;
    }

    public List<Integer> getThresholds() { return thresholds; }
    public List<Integer> getBonusScores() { return bonusScores; }
    public int getBalancedBonus() { return balancedBonus; }
}