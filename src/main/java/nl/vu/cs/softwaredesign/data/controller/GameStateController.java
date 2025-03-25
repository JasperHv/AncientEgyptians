package nl.vu.cs.softwaredesign.data.controller;

import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.CardDeck;
import nl.vu.cs.softwaredesign.data.model.Ending;
import nl.vu.cs.softwaredesign.ui.views.GameView;

import java.util.*;

public class GameStateController {

    private boolean isIntroPhase;
    private int introCardIndex = 0;
    private Card currentCard;
    private final CardDeck cardDeck;
    private final List<String> introCards;
    private final ScoreSettings scoreSettings;

    private final GameConfiguration gameConfiguration;
    private final HandleInfluencePillars handleInfluencePillars;
    
    public GameStateController(
            CardDeck cardDeck,
            List<String> introCards,
            ScoreSettings scoreSettings,
            GameConfiguration gameConfiguration,
            HandleInfluencePillars handleInfluencePillars
    ) {
        this.isIntroPhase = true;
        this.cardDeck = cardDeck;
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
        if (currentCard == null) {
            return getNextCard();
        }
        return currentCard;
    }

    public Card getNextCard() {
        if (cardDeck.isEmpty()) {
            Ending badEnding = ConfigurationLoader.getInstance().getBadEnding();
            if (badEnding != null) {
                GameView.getInstance().showEndScreen(badEnding);
            }
            return null;
        }
        currentCard = cardDeck.drawCard();
        return currentCard;
    }

    public ScoreSettings getScoreSettings() {
        return scoreSettings;
    }

    public HandleInfluencePillars getInfluencePillars() {
        return handleInfluencePillars;
    }

    public int getYearCount() {
        return gameConfiguration.getYearCount();
    }

    public void setYearCount(int newYearCount) {
        gameConfiguration.setYearCount(newYearCount);
    }
}
