package nl.vu.cs.softwaredesign.data.commands;

import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import javafx.beans.property.IntegerProperty;


public class GameSwipeCommand implements Command {
    private final SwipeSide side;
    private final Card card;
    private final int gameCardIndex;
    private final ScoreSettings scoreSettings;
    private final IntegerProperty yearCount;
    private final GameView gameView;
    private final HandleInfluencePillars handleInfluencePillars; // Store instance

    public GameSwipeCommand(SwipeSide side, Card card, int gameCardIndex, ScoreSettings scoreSettings, IntegerProperty yearCount, GameView gameView, HandleInfluencePillars handleInfluencePillars) {
        this.side = side;
        this.card = card;
        this.gameCardIndex = gameCardIndex;
        this.scoreSettings = scoreSettings;
        this.yearCount = yearCount;
        this.gameView = gameView;
        this.handleInfluencePillars = handleInfluencePillars; // Store it
    }

    @Override
    public void execute() {
        System.out.println("Year Count before update: " + yearCount.get());
        yearCount.set(yearCount.get() + scoreSettings.getYearCountIncrease());
        System.out.println("Year Count after update: " + yearCount.get());

        // Call applyInfluence using the instance of HandleInfluencePillars
        handleInfluencePillars.applyInfluence(side == SwipeSide.LEFT, card.getInfluence());

        // Update score and UI elements via gameView
        gameView.updateScore();
        gameView.updateScoreAndYearBoxes();

        // Move to the next card
        int newGameCardIndex = (gameCardIndex >= gameView.getGameCards().size() - 1) ? 0 : gameCardIndex + 1;
        gameView.setGameCardIndex(newGameCardIndex);
    }
}
