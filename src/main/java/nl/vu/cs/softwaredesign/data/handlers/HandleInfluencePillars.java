package nl.vu.cs.softwaredesign.data.handlers;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.property.IntegerProperty;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.model.Influence;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.Ending;
import nl.vu.cs.softwaredesign.ui.views.GameView;

import java.util.List;
import java.util.stream.Collectors;

public class HandleInfluencePillars {
    private final ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
    private final GameConfiguration gameConfiguration = ConfigurationLoader.getInstance().getGameConfiguration();
    private final GameView gameView;

    public HandleInfluencePillars(GameView gameView) {
        this.gameView = gameView;
    }

    public void applyInfluence(boolean isSwipeLeft, List<Influence> influences) {
        if (influences == null || influences.isEmpty()) return;

        // Adjust influences based on the swipe direction
        List<Influence> adjustedInfluences = influences.stream()
                .map(influence -> new Influence(
                        influence.getPillar(),
                        isSwipeLeft ? -influence.getValue() : influence.getValue()
                ))
                .collect(Collectors.toList());

        int yearCount = gameConfiguration.getYearCount();
        int threshold = scoreSettings.getYearThreshold();

        boolean gameOverTriggered = false;
        boolean winTriggered = false;
        Pillar triggeredPillar = null;

        for (Influence influence : adjustedInfluences) {
            Pillar pillarEnum = Pillar.fromName(influence.getPillar());
            int valueChange = influence.getValue();

            IntegerProperty pillarProgress = FXGL.getip(pillarEnum.getName().toUpperCase());
            int currentValue = pillarProgress.get();
            int newValue = Math.min(Math.max(currentValue + valueChange, 0), 100);

            pillarProgress.set(newValue);

            if (newValue == 0) {
                gameOverTriggered = true;
                triggeredPillar = pillarEnum;
            } else if (newValue == 100 && yearCount >= threshold) {
                winTriggered = true;
                triggeredPillar = pillarEnum;
            }
        }

        if (winTriggered || gameOverTriggered) {
            Ending ending = winTriggered
                    ? ConfigurationLoader.getInstance().getGoldenAgeEnding()
                    : triggeredPillar.getEnding();

            if (ending != null) {
                gameView.showEndScreen(ending);
            }
        }
    }
}
