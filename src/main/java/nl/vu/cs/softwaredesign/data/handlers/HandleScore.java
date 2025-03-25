package nl.vu.cs.softwaredesign.data.handlers;

import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import javafx.beans.property.IntegerProperty;

import java.util.List;

public class HandleScore {
    private final List<Pillar> pillars = List.of(Pillar.PRIESTS, Pillar.FARMERS, Pillar.NOBLES, Pillar.MILITARY);

    private boolean isBalanced(Pillar pillar) {
        IntegerProperty pillarProgress = FXGL.getip(pillar.getName().toUpperCase());
        int value = pillarProgress.get();
        return value >= 25 && value <= 75;
    }

    public void updateScore(GameConfiguration gameConfiguration,  ScoreSettings scoreSettings) {
        int yearCount = gameConfiguration.getYearCount();
        int currentScore = gameConfiguration.getScoreCount();

        int scoreIncrease = scoreSettings.getBaseScoreIncrease();

        List<Integer> thresholds = scoreSettings.getThresholds();
        List<Integer> bonusScores = scoreSettings.getBonusScores();

        // Adjust score based on thresholds
        for (int i = 0; i < thresholds.size(); i++) {
            if (yearCount > thresholds.get(i)) {
                scoreIncrease += bonusScores.get(i);
            }
        }

        // Check if all pillars are balanced
        boolean allBalanced = pillars.stream().allMatch(this::isBalanced);
        if (allBalanced) {
            scoreIncrease += scoreSettings.getBalancedBonus();
        }

        gameConfiguration.setScoreCount(currentScore + scoreIncrease);
    }
}