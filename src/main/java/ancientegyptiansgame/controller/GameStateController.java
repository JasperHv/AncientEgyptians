package ancientegyptiansgame.controller;

import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.data.enums.SwipeSide;
import ancientegyptiansgame.data.model.Card;
import ancientegyptiansgame.data.model.CardDeck;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;

import java.util.*;

public class GameStateController {

    private boolean isIntroPhase;
    private int introCardIndex = 0;
    private final List<String> introCards;
    private Card currentCard;
    private final CardDeck cardDeck;
    private final ScoreSettings scoreSettings;

    private final GameConfiguration gameConfiguration;

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
            Map<String, Queue<Card>> legacyCardsMap
    ) {
        this.isIntroPhase = true;
        this.cardDeck = cardDeck;
        this.introCards = introCards;
        this.scoreSettings = scoreSettings;
        this.gameConfiguration = gameConfiguration;
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

        Pillars pillarsEnum = Pillars.fromName(pillar);
        PillarData pillarData = ModeConfiguration.getInstance().getPillarData(pillarsEnum);
        int currentPillarValue = pillarData.getValue();

        if (ls == null) {
            return;
        }

        updateCounts(ls, isPositive);

        if (shouldUnlock(ls, currentPillarValue)) {
            handleUnlock(pillar, ls);
        }

        if (shouldLock(ls, currentPillarValue)) {
            handleLock(pillar, ls);
        }
    }

    private void updateCounts(LegacyState ls, boolean isPositive) {
        if (isPositive) {
            ls.positiveCount++;
        } else {
            ls.negativeCount++;
        }
    }

    private boolean shouldUnlock(LegacyState ls, int currentPillarValue) {
        return !ls.unlocked && ls.positiveCount >= 5 && currentPillarValue > 60;
    }

    private boolean shouldLock(LegacyState ls, int currentPillarValue) {
        return ls.negativeCount >= 3 || currentPillarValue < 40;
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

    public String getCurrentIntroCard() {
        return introCards.get(introCardIndex);
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

}
