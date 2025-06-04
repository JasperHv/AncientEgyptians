package nl.vu.cs.ancientegyptiansgame.commands;

import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.data.enums.SwipeSide;
import nl.vu.cs.ancientegyptiansgame.data.model.Ending;
import nl.vu.cs.ancientegyptiansgame.handlers.HandleInfluencePillars;
import nl.vu.cs.ancientegyptiansgame.data.model.Card;
import nl.vu.cs.ancientegyptiansgame.handlers.HandleScore;
import nl.vu.cs.ancientegyptiansgame.listeners.EndingListener;
import nl.vu.cs.ancientegyptiansgame.observer.ScoreObserver;
import nl.vu.cs.ancientegyptiansgame.observer.YearsInPowerObserver;
import nl.vu.cs.ancientegyptiansgame.controller.GameStateController;
import nl.vu.cs.ancientegyptiansgame.logging.CommandLogger;
import nl.vu.cs.ancientegyptiansgame.logging.GameCommandLogEntry;

public class GameSwipeCommand implements Command {

    private final SwipeSide side;
    private final Card card;
    private final ScoreSettings scoreSettings;
    private final GameStateController gameStateController;
    private final HandleInfluencePillars handleInfluencePillars;
    private final GameConfiguration gameConfiguration;
    private final EndingListener endingListener;

    /**
     * Constructs a GameSwipeCommand to handle a player's swipe action in the game.
     *
     * @param side the direction of the swipe
     * @param gameStateController the controller managing the current game state
     * @param listener the listener to handle game ending events
     */
    public GameSwipeCommand(SwipeSide side, GameStateController gameStateController, EndingListener listener) {
        this.side = side;
        this.card = gameStateController.getCurrentGameCard();
        this.gameStateController = gameStateController;
        this.scoreSettings = gameStateController.getScoreSettings();
        this.handleInfluencePillars = new HandleInfluencePillars();
        this.gameConfiguration = GameConfiguration.getInstance();
        this.endingListener = listener;
    }

    /**
     * Executes the swipe command, updating the game's year, score, and pillar influences, and triggers game endings if conditions are met.
     *
     * Increments the years in power and updates the score based on the current card and swipe direction. Applies influence effects to pillars, checks for game-ending conditions (such as reaching the maximum year count or running out of cards), and notifies the ending listener if an ending is triggered. Logs the command execution details.
     */
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