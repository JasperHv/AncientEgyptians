package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.controller.GameStateController;
import nl.vu.cs.softwaredesign.data.logging.CommandLogger;
import nl.vu.cs.softwaredesign.data.logging.GameCommandLogEntry;

public class GameSwipeCommand implements Command {

    private final SwipeSide side;
    private final Card card;
    private final ScoreSettings scoreSettings;
    private final GameView gameView;
    private final GameStateController gameStateController;
    private final HandleInfluencePillars handleInfluencePillars;
    private final GameConfiguration gameConfiguration;

    public GameSwipeCommand(
            SwipeSide side,
            Card card,
            ScoreSettings scoreSettings,
            GameView gameView,
            GameStateController gameStateController,
            HandleInfluencePillars handleInfluencePillars,
            GameConfiguration gameConfiguration
    ) {
        this.side = side;
        this.card = card;
        this.scoreSettings = scoreSettings;
        this.gameView = gameView;
        this.gameStateController = gameStateController;
        this.handleInfluencePillars = handleInfluencePillars;
        this.gameConfiguration = gameConfiguration;
    }

    @Override
    public void execute() {
        int currentYear = gameStateController.getYearCount();
        int newYear = currentYear + scoreSettings.getYearCountIncrease();
        gameStateController.setYearCount(newYear);

        handleInfluencePillars.applyInfluence(side, card.getInfluence());

        gameView.updateScore();
        gameView.updateScoreAndYearBoxes();
        gameStateController.getNextCard();
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