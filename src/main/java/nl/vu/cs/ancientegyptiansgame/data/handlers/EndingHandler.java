package nl.vu.cs.ancientegyptiansgame.data.handlers;

import nl.vu.cs.ancientegyptiansgame.data.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Ending;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillar;
import nl.vu.cs.ancientegyptiansgame.pillars.PillarListener;
import nl.vu.cs.ancientegyptiansgame.ui.views.GameView;

public class EndingHandler implements PillarListener {

    private final GameConfiguration gameConfiguration;
    private final ScoreSettings scoreSettings;
    private final GameView gameView;

    public EndingHandler(ScoreSettings scoreSettings) {
        this.gameConfiguration = GameConfiguration.getInstance();
        this.scoreSettings = scoreSettings;
        this.gameView = GameView.getInstance();
    }

    @Override
    public void changed(Pillar pillar, Integer newValue) {
        int yearCount = gameConfiguration.getYearCount();
        int threshold = scoreSettings.getScoreConfig().getYearThreshold();

        boolean gameOverTriggered = false;
        boolean winTriggered = false;

        if (newValue == 0) {
            gameOverTriggered = true;
        } else if (newValue == 100 && yearCount >= threshold) {
            winTriggered = true;
        }

        if (winTriggered || gameOverTriggered) {
            Ending ending = winTriggered
                    ? ConfigurationLoader.getInstance().getGoldenAgeEnding()
                    : pillar.getEnding();

            if (ending != null) {
                gameView.showEndScreen(ending);
            }
        }
    }
}