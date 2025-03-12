package nl.vu.cs.softwaredesign.data;

import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.ScoreSettings;

import java.util.List;

public class HandleScore {
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

        if (isBalanced("priests") && isBalanced("farmers") && isBalanced("nobles") && isBalanced("military")) {
            scoreIncrease += scoreSettings.getBalancedBonus();
        }

        FXGL.inc("scoreCount", scoreIncrease);
    }
}