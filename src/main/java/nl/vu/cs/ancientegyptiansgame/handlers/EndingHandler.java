package nl.vu.cs.ancientegyptiansgame.handlers;

import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Ending;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.listeners.PillarListener;
import nl.vu.cs.ancientegyptiansgame.ui.views.GameView;

public class EndingHandler implements PillarListener {

    private final GameConfiguration gameConfiguration;
    private final ScoreSettings scoreSettings;
    private GameView gameView;

    public EndingHandler(ScoreSettings scoreSettings, GameView gameView) {
        this.gameConfiguration = GameConfiguration.getInstance();
        this.scoreSettings = scoreSettings;
        this.gameView = gameView;
    }

    public void setGameView(GameView view) {
        this.gameView = view;
    }

    @Override
    public void changed(Pillars pillars, Integer newValue) {
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
                    : pillars.getEnding();

            if (ending != null) {
                gameView.showEndScreen(ending);
            }
        }
    }
}