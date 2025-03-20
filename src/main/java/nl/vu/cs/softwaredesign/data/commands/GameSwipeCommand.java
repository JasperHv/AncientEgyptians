package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.GameStateManager;
import javafx.beans.property.IntegerProperty;

public class GameSwipeCommand implements Command {
    private final SwipeSide side;
    private final Card card;
    private final ScoreSettings scoreSettings;
    private final IntegerProperty yearCount;
    private final GameView gameView;
    private final GameStateManager gameStateManager;
    private final HandleInfluencePillars handleInfluencePillars;

    public GameSwipeCommand(SwipeSide side, Card card, ScoreSettings scoreSettings, IntegerProperty yearCount, GameView gameView, GameStateManager gameStateManager, HandleInfluencePillars handleInfluencePillars) {
        this.side = side;
        this.card = card;
        this.scoreSettings = scoreSettings;
        this.yearCount = yearCount;
        this.gameView = gameView;
        this.gameStateManager = gameStateManager;
        this.handleInfluencePillars = handleInfluencePillars;
    }

    @Override
    public void execute() {
        yearCount.set(yearCount.get() + scoreSettings.getYearCountIncrease());
        handleInfluencePillars.applyInfluence(side == SwipeSide.LEFT, card.getInfluence());

        gameView.updateScore();
        gameView.updateScoreAndYearBoxes();

        gameStateManager.advanceGameCard();
    }
}