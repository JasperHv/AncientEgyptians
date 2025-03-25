package nl.vu.cs.softwaredesign.data.model;

import java.util.*;

public class CardDeck {
    private List<Card> cards;

    public CardDeck(List<Card> initialCards) {
        this.cards = new ArrayList<>(initialCards);
        sortAndShuffleDeck();
    }


    /**
     * Sorts the deck by card frequency in descending order, groups cards by frequency,
     * shuffles within each group, and then rebuilds the deck.
     */
    public void sortAndShuffleDeck() {
        cards.sort((c1, c2) -> Integer.compare(c2.getFrequency(), c1.getFrequency()));

        Map<Integer, List<Card>> frequencyGroups = new HashMap<>();
        for (Card card : cards) {
            frequencyGroups.computeIfAbsent(card.getFrequency(), k -> new ArrayList<>()).add(card);
        }

        List<Card> newDeck = new ArrayList<>();
        frequencyGroups.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .forEach(freq -> {
                    List<Card> group = frequencyGroups.get(freq);
                    Collections.shuffle(group);
                    newDeck.addAll(group);
                });

        cards = newDeck;
    }

    /**
     * Draws the next available card from the deck.
     * @return the drawn Card or null if no valid card is found.
     */
    public Card drawCard() {
        if (isEmpty()) {
            return null;
        }
        Card selectedCard = null;

        while (!cards.isEmpty()) {
            selectedCard = cards.remove(0);
            if ("standard".equalsIgnoreCase(selectedCard.getType())) {
                break;
            } else {
                selectedCard = null;
            }
        }

        if (selectedCard != null) {
            Card updatedCard = selectedCard.decrementFrequency();
            if (updatedCard.getFrequency() > 0) {
                cards.add(updatedCard);
                sortAndShuffleDeck();
            }
            return updatedCard;
        }
        return null;
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
