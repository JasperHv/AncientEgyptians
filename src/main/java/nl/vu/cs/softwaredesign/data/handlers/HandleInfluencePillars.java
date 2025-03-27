package nl.vu.cs.softwaredesign.data.handlers;

import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.model.Influence;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.pillars.PillarData;
import nl.vu.cs.softwaredesign.ui.views.GameView;

import java.util.List;
import java.util.stream.Collectors;

public class HandleInfluencePillars {
    private final ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
    private final GameConfiguration gameConfiguration = ConfigurationLoader.getInstance().getGameConfiguration();
    private final ModeConfiguration modeConfiguration = ModeConfiguration.getInstance();
    private final GameView gameView;

    public HandleInfluencePillars(GameView gameView) {
        this.gameView = gameView;
    }

    public void applyInfluence(SwipeSide side, List<Influence> influences) {
        if (influences == null || influences.isEmpty()) return;
        boolean isSwipeLeft = side == SwipeSide.LEFT;

        // Adjust influences based on the swipe direction
        List<Influence> adjustedInfluences = influences.stream()
                .map(influence -> new Influence(
                        influence.getPillar(),
                        isSwipeLeft ? -influence.getValue() : influence.getValue()
                ))
                .collect(Collectors.toList());

        for (Influence influence : adjustedInfluences) {
            Pillar pillarEnum = Pillar.fromName(influence.getPillar());
            int valueChange = influence.getValue();

            PillarData pillarData = modeConfiguration.getPillarData(pillarEnum);
            int currentValue = pillarData.getValue();
            int newValue = Math.min(Math.max(currentValue + valueChange, 0), 100);

            pillarData.setValue(newValue);

            pillarData.setValue(newValue);

        }
    }
}
