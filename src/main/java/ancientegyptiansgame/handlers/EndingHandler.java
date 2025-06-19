package ancientegyptiansgame.handlers;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.data.model.Ending;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.listeners.EndingListener;
import ancientegyptiansgame.listeners.PillarListener;
import ancientegyptiansgame.observer.YearsInPowerObserver;

public class EndingHandler implements PillarListener {

    private final GameConfiguration gameConfiguration;
    private final ScoreSettings scoreSettings;
    private final EndingListener endingListener;

    public EndingHandler(ScoreSettings scoreSettings, EndingListener endingListener) {
        this.gameConfiguration = GameConfiguration.getInstance();
        this.scoreSettings = scoreSettings;
        this.endingListener = endingListener;
    }

    @Override
    public void changed(Pillars pillars, Integer newValue) {
        YearsInPowerObserver yearsObserver = gameConfiguration.getYearsInPowerObserver();
        int yearCount = yearsObserver.getYearsInPower();
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

            if (ending != null && endingListener != null) {
                endingListener.onEndingTriggered(ending);
            }
        }
    }
}