package nl.vu.cs.softwaredesign.data.controller;

import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;

import java.util.List;

public class GameStateController {

    private boolean isIntroPhase;
    private int introCardIndex = 0;
    private int gameCardIndex = 0;

    private final List<Card> gameCards;
    private final List<String> introCards;
    private final ScoreSettings scoreSettings;

    // Replaced IntegerProperty yearCount with a GameConfiguration
    private final GameConfiguration gameConfiguration;
    private final HandleInfluencePillars handleInfluencePillars;

    /**
     * Updated constructor: we now accept a GameConfiguration instead of an IntegerProperty.
     */
    public GameStateController(
            List<Card> gameCards,
            List<String> introCards,
            ScoreSettings scoreSettings,
            GameConfiguration gameConfiguration,
            HandleInfluencePillars handleInfluencePillars
    ) {
        this.isIntroPhase = true;
        this.gameCards = gameCards;
        this.introCards = introCards;
        this.scoreSettings = scoreSettings;
        this.gameConfiguration = gameConfiguration;
        this.handleInfluencePillars = handleInfluencePillars;
    }

    public boolean isIntroPhase() {
        return isIntroPhase;
    }

    public void setIntroPhase(boolean introPhase) {
        this.isIntroPhase = introPhase;
    }

    public List<String> getIntroCards() {
        return introCards;
    }

    public int getIntroCardIndex() {
        return introCardIndex;
    }

    public void setIntroCardIndex(int index) {
        this.introCardIndex = index;
    }

    public void advanceIntroCard() {
        if (introCardIndex < introCards.size() - 1) {
            introCardIndex++;
        } else {
            isIntroPhase = false;
        }
    }

    public Card getCurrentGameCard() {
        return gameCards.get(gameCardIndex);
    }

    public void advanceGameCard() {
        if (gameCardIndex < gameCards.size() - 1) {
            gameCardIndex++;
        } else {
            gameCardIndex = 0;
        }
    }

    public List<Card> getGameCards() {
        return gameCards;
    }

    public ScoreSettings getScoreSettings() {
        return scoreSettings;
    }

    public HandleInfluencePillars getInfluencePillars() {
        return handleInfluencePillars;
    }

    /**
     * Example convenience method: use gameConfiguration’s year count directly.
     */
    public int getYearCount() {
        return gameConfiguration.getYearCount();
    }

    public void setYearCount(int newYearCount) {
        gameConfiguration.setYearCount(newYearCount);
    }
}
