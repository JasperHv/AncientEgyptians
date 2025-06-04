package nl.vu.cs.ancientegyptiansgame.handlers;

import nl.vu.cs.ancientegyptiansgame.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.BonusConfig;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
import nl.vu.cs.ancientegyptiansgame.observer.ScoreObserver;
import nl.vu.cs.ancientegyptiansgame.observer.YearsInPowerObserver;

import java.util.List;

public class HandleScore {
    private final List<Pillars> pillars = List.of(Pillars.PRIESTS, Pillars.FARMERS, Pillars.NOBLES, Pillars.MILITARY);

    private boolean isBalanced(Pillars pillars) {
        PillarData pillarData = ModeConfiguration.getInstance().getPillarData(pillars);
        int value = pillarData.getValue();
        return value >= 25 && value <= 75;
    }

    /**
     * Updates the game score based on the current years in power, configured thresholds, and pillar balance.
     *
     * Calculates the score increase using the base score, applies additional bonuses for surpassing configured year thresholds,
     * and adds a bonus if all pillars are balanced. The updated score is set via the ScoreObserver.
     *
     * @param scoreSettings the configuration containing base score increase, bonus thresholds, and pillar balance bonus
     */
    public void updateScore(ScoreSettings scoreSettings) {
        GameConfiguration gameConfiguration = GameConfiguration.getInstance();
        ScoreObserver scoreObserver = gameConfiguration.getScoreObserver();
        YearsInPowerObserver yearsObserver = gameConfiguration.getYearsInPowerObserver();

        int yearCount = yearsObserver.getYearsInPower();
        int currentScore = scoreObserver.getScore();

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

        int newScore = currentScore + scoreIncrease;
        scoreObserver.setScore(newScore);
    }
}