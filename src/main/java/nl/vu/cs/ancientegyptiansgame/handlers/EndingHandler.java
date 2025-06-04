package nl.vu.cs.ancientegyptiansgame.handlers;

import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Ending;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.listeners.EndingListener;
import nl.vu.cs.ancientegyptiansgame.listeners.PillarListener;
import nl.vu.cs.ancientegyptiansgame.observer.YearsInPowerObserver;

public class EndingHandler implements PillarListener {

    private final GameConfiguration gameConfiguration;
    private final ScoreSettings scoreSettings;
    private final EndingListener endingListener;

    /****
     * Constructs an EndingHandler with the specified score settings and ending listener.
     *
     * @param scoreSettings the score settings used to determine ending conditions
     * @param endingListener the listener to notify when an ending is triggered
     */
    public EndingHandler(ScoreSettings scoreSettings, EndingListener endingListener) {
        this.gameConfiguration = GameConfiguration.getInstance();
        this.scoreSettings = scoreSettings;
        this.endingListener = endingListener;
    }

    /**
     * Evaluates pillar value changes to determine if a game ending condition is met and triggers the appropriate ending event.
     *
     * If a pillar value reaches 0, a game over is triggered. If a pillar value reaches 100 and the years in power meet or exceed the configured threshold, a win is triggered. In either case, notifies the registered ending listener with the corresponding ending.
     *
     * @param pillars the pillars whose value has changed
     * @param newValue the updated value of the pillar
     */
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