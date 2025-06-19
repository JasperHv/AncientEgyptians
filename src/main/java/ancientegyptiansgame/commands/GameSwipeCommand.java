package ancientegyptiansgame.commands;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.data.enums.SwipeSide;
import ancientegyptiansgame.data.model.Ending;
import ancientegyptiansgame.handlers.HandleInfluencePillars;
import ancientegyptiansgame.data.model.Card;
import ancientegyptiansgame.handlers.HandleScore;
import ancientegyptiansgame.listeners.EndingListener;
import ancientegyptiansgame.observer.ScoreObserver;
import ancientegyptiansgame.observer.YearsInPowerObserver;
import ancientegyptiansgame.controller.GameStateController;
import ancientegyptiansgame.logging.CommandLogger;
import ancientegyptiansgame.logging.GameCommandLogEntry;

public class GameSwipeCommand implements Command {

    private final SwipeSide side;
    private final Card card;
    private final ScoreSettings scoreSettings;
    private final GameStateController gameStateController;
    private final HandleInfluencePillars handleInfluencePillars;
    private final GameConfiguration gameConfiguration;
    private final EndingListener endingListener;

    public GameSwipeCommand(SwipeSide side, GameStateController gameStateController, EndingListener listener) {
        this.side = side;
        this.card = gameStateController.getCurrentGameCard();
        this.gameStateController = gameStateController;
        this.scoreSettings = gameStateController.getScoreSettings();
        this.handleInfluencePillars = new HandleInfluencePillars();
        this.gameConfiguration = GameConfiguration.getInstance();
        this.endingListener = listener;
    }

    @Override
    public void execute() {
        YearsInPowerObserver yearsObserver = gameConfiguration.getYearsInPowerObserver();
        ScoreObserver scoreObserver = gameConfiguration.getScoreObserver();
        int currentYear = yearsObserver.getYearsInPower();
        int newYear = currentYear + scoreSettings.getYearCountIncrease();
        yearsObserver.setYearsInPower(newYear);

        if (newYear >= scoreSettings.getScoreConfig().getMaximumYearCount()) {
            if (endingListener != null) {
                endingListener.onEndingTriggered(ConfigurationLoader.getInstance().getGoldenAgeEnding());
            }
        }

        HandleScore handleScore = new HandleScore();
        handleScore.updateScore(scoreSettings);
        handleInfluencePillars.applyInfluence(side, card.getInfluence());

        if (gameStateController.getNextCard() == null) {
            Ending badEnding = ConfigurationLoader.getInstance().getBadEnding();
            if (endingListener != null && badEnding != null) {
                endingListener.onEndingTriggered(badEnding);
            }
        }

        gameStateController.updateLegacyState(card.getPillar(), side);

        GameCommandLogEntry entry = new GameCommandLogEntry(
                card.getTitle(),
                side.toString(),
                card.getInfluence(),
                System.currentTimeMillis(),
                scoreObserver.getScore(),
                yearsObserver.getYearsInPower()
        );
        CommandLogger.logCommand(entry);
    }
}