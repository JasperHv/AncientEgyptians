package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.controller.GameStateController;
import nl.vu.cs.softwaredesign.data.logging.CommandLogger;
import nl.vu.cs.softwaredesign.data.logging.GameCommandLogEntry;
import javafx.beans.property.IntegerProperty;

public class GameSwipeCommand implements Command {
    private final SwipeSide side;
    private final Card card;
    private final ScoreSettings scoreSettings;
    private final IntegerProperty yearCount;
    private final GameView gameView;
    private final GameStateController gameStateController;
    private final HandleInfluencePillars handleInfluencePillars;

    public GameSwipeCommand(SwipeSide side, Card card, ScoreSettings scoreSettings, IntegerProperty yearCount, GameView gameView, GameStateController gameStateController, HandleInfluencePillars handleInfluencePillars) {
        this.side = side;
        this.card = card;
        this.scoreSettings = scoreSettings;
        this.yearCount = yearCount;
        this.gameView = gameView;
        this.gameStateController = gameStateController;
        this.handleInfluencePillars = handleInfluencePillars;
    }

    @Override
    public void execute() {
        yearCount.set(yearCount.get() + scoreSettings.getYearCountIncrease());
        handleInfluencePillars.applyInfluence(side == SwipeSide.LEFT, card.getInfluence());

        gameView.updateScore();
        gameView.updateScoreAndYearBoxes();

        gameStateController.advanceGameCard();

        GameCommandLogEntry entry = new GameCommandLogEntry(
                card.getTitle(),
                side.toString(),
                card.getInfluence(),
                System.currentTimeMillis()
        );
        CommandLogger.logCommand(entry);
        gameStateController.getNextCard();
    }
}