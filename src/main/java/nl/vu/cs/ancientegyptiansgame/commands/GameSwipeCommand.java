package nl.vu.cs.ancientegyptiansgame.commands;

import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.GameConfiguration;
import nl.vu.cs.ancientegyptiansgame.data.enums.SwipeSide;
import nl.vu.cs.ancientegyptiansgame.data.model.Ending;
import nl.vu.cs.ancientegyptiansgame.handlers.HandleInfluencePillars;
import nl.vu.cs.ancientegyptiansgame.data.model.Card;
import nl.vu.cs.ancientegyptiansgame.ui.views.GameView;
import nl.vu.cs.ancientegyptiansgame.controller.GameStateController;
import nl.vu.cs.ancientegyptiansgame.logging.CommandLogger;
import nl.vu.cs.ancientegyptiansgame.logging.GameCommandLogEntry;

public class GameSwipeCommand implements Command {

    private final SwipeSide side;
    private final Card card;
    private final ScoreSettings scoreSettings;
    private final GameView gameView;
    private final GameStateController gameStateController;
    private final HandleInfluencePillars handleInfluencePillars;
    private final GameConfiguration gameConfiguration;

    public GameSwipeCommand(SwipeSide side, GameStateController gameStateController, GameView gameView) {
        this.side = side;
        this.card = gameStateController.getCurrentGameCard();
        this.gameStateController = gameStateController;
        this.scoreSettings = gameStateController.getScoreSettings();
        this.handleInfluencePillars = new HandleInfluencePillars();
        this.gameView = gameView;
        this.gameConfiguration = GameConfiguration.getInstance();
    }

    @Override
    public void execute() {
        int currentYear = gameStateController.getYearCount();
        int newYear = currentYear + scoreSettings.getYearCountIncrease();
        gameStateController.setYearCount(newYear);

        if (newYear >= scoreSettings.getScoreConfig().getMaximumYearCount()) {
            gameView.showEndScreen(ConfigurationLoader.getInstance().getGoldenAgeEnding());
        }
        handleInfluencePillars.applyInfluence(side, card.getInfluence());

        gameView.updateScore();
        gameView.updateScoreAndYearBoxes();
        if (gameStateController.getNextCard() == null) {
            Ending badEnding = ConfigurationLoader.getInstance().getBadEnding();
            if (badEnding != null) {
                gameView.showEndScreen(badEnding);
            }
        }

        gameStateController.updateLegacyState(card.getPillar(), side);

        int currentScore = gameConfiguration.getScoreCount();
        int currentYearCount = gameConfiguration.getYearCount();

        GameCommandLogEntry entry = new GameCommandLogEntry(
                card.getTitle(),
                side.toString(),
                card.getInfluence(),
                System.currentTimeMillis(),
                currentScore,
                currentYearCount
        );
        CommandLogger.logCommand(entry);
    }
}