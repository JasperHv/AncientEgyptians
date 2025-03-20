package nl.vu.cs.softwaredesign.data.handlers;

import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import javafx.beans.property.IntegerProperty;
import java.util.List;

public class HandleScore {
    private final IntegerProperty scoreCount = FXGL.getip("scoreCount");
    private final List<String> pillars = List.of("priests", "farmers", "nobles", "military");

    private boolean isBalanced(String pillar) {
        int value = FXGL.getip(pillar).get();
        return value >= 25 && value <= 75;
    }

    public void updateScore() {
        int yearCount = FXGL.getip("yearCount").get();
        ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        int scoreIncrease = scoreSettings.getBaseScoreIncrease();

        List<Integer> thresholds = scoreSettings.getThresholds();
        List<Integer> bonusScores = scoreSettings.getBonusScores();

        for (int i = 0; i < thresholds.size(); i++) {
            if (yearCount > thresholds.get(i)) {
                scoreIncrease += bonusScores.get(i);
            }
        }

        boolean allBalanced = pillars.stream().allMatch(this::isBalanced);
        if (allBalanced) {
            scoreIncrease += scoreSettings.getBalancedBonus();
        }

        scoreCount.set(scoreCount.get() + scoreIncrease);
    }
}