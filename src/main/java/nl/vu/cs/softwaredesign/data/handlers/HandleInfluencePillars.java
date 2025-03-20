package nl.vu.cs.softwaredesign.data.handlers;

import com.almasb.fxgl.dsl.FXGL;
import javafx.beans.property.IntegerProperty;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.model.Influence;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.PillarEnding;
import nl.vu.cs.softwaredesign.ui.views.GameView;

import java.util.List;
import java.util.stream.Collectors;

public class HandleInfluencePillars {
    private final ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
    private final GameView gameView;

    // Constructor to inject GameView
    public HandleInfluencePillars(GameView gameView) {
        this.gameView = gameView;
    }

    public void applyInfluence(boolean isSwipeLeft, List<Influence> influences) {
        if (influences == null || influences.isEmpty()) return;

        List<Influence> adjustedInfluences = influences.stream()
                .map(influence -> new Influence(
                        influence.getPillar(),
                        isSwipeLeft ? -influence.getValue() : influence.getValue()
                ))
                .collect(Collectors.toList());

        IntegerProperty yearCount = FXGL.getip("yearCount");
        int threshold = scoreSettings.getYearThreshold();

        boolean gameOverTriggered = false;
        boolean winTriggered = false;
        Pillar triggeredPillar = null;
        List<Pillar> pillars = ConfigurationLoader.getInstance().getPillars();

        for (Influence influence : adjustedInfluences) {
            String pillarName = influence.getPillar();
            int valueChange = influence.getValue();

            IntegerProperty pillarProgress = FXGL.getip(pillarName);
            int currentValue = pillarProgress.get();
            int newValue = Math.min(Math.max(currentValue + valueChange, 0), 100);

            pillarProgress.set(newValue);

            if (newValue == 0) {
                gameOverTriggered = true;
                triggeredPillar = pillars.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(pillarName))
                        .findFirst()
                        .orElse(null);
            } else if (newValue == 100 && yearCount.get() >= threshold) {
                winTriggered = true;
                triggeredPillar = pillars.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(pillarName))
                        .findFirst()
                        .orElse(null);
            }
        }

        if (winTriggered || gameOverTriggered) {
            PillarEnding ending = winTriggered
                    ? ConfigurationLoader.getInstance().getGoldenAgeEnding()
                    : triggeredPillar != null ? triggeredPillar.getEnding() : null;

            if (ending != null) {
                gameView.showEndScreen(ending);
            }
        }
    }
}
