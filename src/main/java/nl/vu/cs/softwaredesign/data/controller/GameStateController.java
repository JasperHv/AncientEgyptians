package nl.vu.cs.softwaredesign.data.controller;

import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import javafx.beans.property.IntegerProperty;
import nl.vu.cs.softwaredesign.data.model.Ending;
import nl.vu.cs.softwaredesign.ui.views.GameView;

import java.util.*;

public class GameStateController {

    private boolean isIntroPhase;
    private int introCardIndex = 0;
    private final List<Card> gameCards;
    private Card currentCard;
    private final List<String> introCards;
    private final ScoreSettings scoreSettings;
    private final IntegerProperty yearCount;
    private final HandleInfluencePillars handleInfluencePillars;

    public GameStateController(List<Card> gameCards, List<String> introCards, ScoreSettings scoreSettings, IntegerProperty yearCount, HandleInfluencePillars handleInfluencePillars) {
        this.isIntroPhase = true;
        this.gameCards = gameCards;
        this.introCards = introCards;
        this.scoreSettings = scoreSettings;
        this.yearCount = yearCount;
        this.handleInfluencePillars = handleInfluencePillars;
    }

    public boolean isIntroPhase() {
        return isIntroPhase;
    }

    public void setIntroPhase(boolean isIntroPhase) {
        this.isIntroPhase = isIntroPhase;
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
        if (gameCards.isEmpty()) {
            Ending badEnding = ConfigurationLoader.getInstance().getBadEnding();
            if (badEnding != null) {
                GameView.getInstance().showEndScreen(badEnding);
            }
        }

        Card selectedCard = null;
        while (!gameCards.isEmpty()) {
            selectedCard = gameCards.remove(0);
            if ("standard".equalsIgnoreCase(selectedCard.getType())) {
                break;
            } else {
                selectedCard = null;
            }
        }

        if (selectedCard != null) {
            selectedCard.decrementFrequency();
            if (selectedCard.getFrequency() > 0) {
                gameCards.add(selectedCard);
                reshuffleList();
            }
            currentCard = selectedCard;
        }

        return selectedCard;
    }

    private void reshuffleList() {
        gameCards.sort((c1, c2) -> Integer.compare(c2.getFrequency(), c1.getFrequency()));

        Map<Integer, List<Card>> frequencyGroups = new HashMap<>();
        for (Card card : gameCards) {
            frequencyGroups.computeIfAbsent(card.getFrequency(), k -> new ArrayList<>()).add(card);
        }

        gameCards.clear();
        frequencyGroups.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .forEach(freq -> {
                    List<Card> group = frequencyGroups.get(freq);
                    Collections.shuffle(group);
                    gameCards.addAll(group);
                });
    }

    public ScoreSettings getScoreSettings() {
        return scoreSettings;
    }

    public IntegerProperty getYearCount() {
        return yearCount;
    }

    public HandleInfluencePillars getInfluencePillars() {
        return handleInfluencePillars;
    }
}
