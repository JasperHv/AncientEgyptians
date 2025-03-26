package nl.vu.cs.softwaredesign.data.controller;

import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
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

    private final Map<String, LegacyState> legacyStates = new HashMap<>();
    private final Map<String, Queue<Card>> legacyCardsMap;

    private static class LegacyState {
        int positiveCount = 0;
        int negativeCount = 0;
        boolean unlocked = false;
        boolean accepted = false;
    }

    public GameStateController(
            CardDeck cardDeck,
            List<String> introCards,
            ScoreSettings scoreSettings,
            GameConfiguration gameConfiguration,
            HandleInfluencePillars handleInfluencePillars,
            Map<String, Queue<Card>> legacyCardsMap
    ) {
        this.isIntroPhase = true;
        this.cardDeck = cardDeck;
        this.introCards = introCards;
        this.scoreSettings = scoreSettings;
        this.gameConfiguration = gameConfiguration;
        this.handleInfluencePillars = handleInfluencePillars;
        this.legacyCardsMap = legacyCardsMap;

        initLegacyStates();
    }

    private void initLegacyStates() {
        legacyStates.put("priests", new LegacyState());
        legacyStates.put("nobles", new LegacyState());
        legacyStates.put("military", new LegacyState());
        legacyStates.put("farmers", new LegacyState());
    }

    /**
     * Updates the legacy state for a given pillar.
     * If the conditions are met (positiveCount >= 5 and currentPillarValue > 60),
     * the corresponding legacy card is unlocked and added to the card deck.
     * If negative conditions occur, the legacy state is reset.
     */
    public void updateLegacyState(String pillar, SwipeSide side) {
        String key = pillar.toLowerCase();
        LegacyState ls = legacyStates.get(key);
        boolean isPositive = side == SwipeSide.RIGHT;
        int currentPillarValue = FXGL.getip(pillar.toUpperCase()).get();

        if (ls != null) {
            if (isPositive) {
                ls.positiveCount++;
            } else {
                ls.negativeCount++;
            }
            if (!ls.unlocked && ls.positiveCount >= 5 && currentPillarValue > 60) {
                handleUnlock(pillar, ls);
            }
            if (ls.negativeCount >= 3 || currentPillarValue < 40) {
                handleLock(pillar, ls);
            }
        }
    }

    private void handleUnlock(String pillar, LegacyState ls) {
        // Unlock condition: add all corresponding legacy cards to the pool (if available)
        String key = pillar.toLowerCase();
        ls.unlocked = true;
        // Remove all corresponding legacy cards from the pool (if available)
        Queue<Card> pillarLegacyCards = legacyCardsMap.get(key);
        if (pillarLegacyCards != null) {
            while (!pillarLegacyCards.isEmpty()) {
                Card legacyCard = pillarLegacyCards.poll();
                cardDeck.addLegacyCard(legacyCard);
            }
        }
    }

    private void handleLock(String pillar, LegacyState ls) {
        // Lock condition: reset legacy state if too many negatives or low pillar value
        if (ls.unlocked) {
            cardDeck.removeLegacyCard(pillar);
        }
        ls.unlocked = false;
        ls.accepted = false;
        ls.positiveCount = 0;
        ls.negativeCount = 0;
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

    public Card getCurrentGameCard() {
        if (currentCard == null) {
            return getNextCard();
        }
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
