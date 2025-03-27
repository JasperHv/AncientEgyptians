package nl.vu.cs.ancientegyptiansgame.handlers;

import nl.vu.cs.ancientegyptiansgame.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.BonusConfig;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillar;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.ModeConfiguration;

import java.util.List;

public class HandleScore {
    private final List<Pillar> pillars = List.of(Pillar.PRIESTS, Pillar.FARMERS, Pillar.NOBLES, Pillar.MILITARY);

    private boolean isBalanced(Pillar pillar) {
        PillarData pillarData = ModeConfiguration.getInstance().getPillarData(pillar);
        int value = pillarData.getValue();
        return value >= 25 && value <= 75;
    }

    public void updateScore(ScoreSettings scoreSettings) {
        GameConfiguration gameConfiguration = GameConfiguration.getInstance();
        int yearCount = gameConfiguration.getYearCount();
        int currentScore = gameConfiguration.getScoreCount();

        int scoreIncrease = scoreSettings.getBaseScoreIncrease();

        BonusConfig bonusConfig = scoreSettings.getBonusConfig();
        List<Integer> thresholds = bonusConfig.getThresholds();
        List<Integer> bonusScores = bonusConfig.getBonusScores();

        // Adjust score based on thresholds
        for (int i = 0; i < thresholds.size(); i++) {
            if (yearCount > thresholds.get(i)) {
                scoreIncrease += bonusScores.get(i);
            }
        }

        // Check if all pillars are balanced
        boolean allBalanced = pillars.stream().allMatch(this::isBalanced);
        if (allBalanced) {
            scoreIncrease += bonusConfig.getBalancedBonus();
        }

        gameConfiguration.setScoreCount(currentScore + scoreIncrease);
    }
}