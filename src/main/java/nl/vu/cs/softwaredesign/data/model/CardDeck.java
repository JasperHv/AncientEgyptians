package nl.vu.cs.softwaredesign.data.model;

import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CardDeck implements Iterable<Card> {
    private List<Card> cards;

    /**
     * Constructor to initialize the deck with a list of cards.
     * The deck is sorted and shuffled upon initialization.
     * @param initialCards The initial list of cards to populate the deck.
     */
    public CardDeck(List<Card> initialCards) {
        this.cards = new ArrayList<>(initialCards);
        sortAndShuffleDeck();
    }

    /**
     * Sorts the deck by card frequency in descending order, groups cards by frequency,
     * shuffles within each group, and then rebuilds the deck.
     */
    public void sortAndShuffleDeck() {
        cards.sort(Card::compareTo);

        Map<Integer, List<Card>> frequencyGroups = new HashMap<>();
        for (Card card : cards) {
            frequencyGroups.computeIfAbsent(card.getFrequency(), k -> new ArrayList<>()).add(card);
        }

        List<Card> newDeck = new ArrayList<>();
        frequencyGroups.keySet().stream()
                .sorted(Comparator.reverseOrder())
                .forEach(freq -> {
                    List<Card> group = frequencyGroups.get(freq);
                    Collections.shuffle(group); // Shuffle within each group
                    newDeck.addAll(group);
                });

        cards = newDeck;
    }

    /**
     * Draws the next available card from the deck.
     * @return the drawn Card or null if no card is available.
     */
    public Card drawCard() {
        if (isEmpty()) {
            return null;
        }

        Card selectedCard = cards.remove(0);
        // Decrement frequency and if still available, add it back
        Card updatedCard = selectedCard.decrementFrequency();
        if (updatedCard.getFrequency() > 0) {
            cards.add(updatedCard);
            sortAndShuffleDeck();
        }
        return updatedCard;
    }

    /**
     * Checks if the deck is empty.
     * @return true if the deck is empty, otherwise false.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Provides an iterator to allow iteration over the deck.
     * @return an Iterator for the deck of cards.
     */
    @NotNull
    @Override
    public Iterator<Card> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < cards.size();
            }

            @Override
            public Card next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return cards.get(index++);
            }
        };
    }

    public List<Card> getCards() {
        return cards;
    }

    /**
     * Adds a legacy card to the deck and re-sorts the deck.
     * @param legacyCard the legacy card to add.
     */
    public void addLegacyCard(Card legacyCard) {
        cards.add(legacyCard);
        sortAndShuffleDeck();
    }

    /**
     * Removes any legacy card for the specified pillar from the deck.
     * @param pillar The pillar for which to remove legacy cards.
     */
    public void removeLegacyCard(String pillar) {
        cards.removeIf(card -> "legacy".equalsIgnoreCase(card.getType())
                && card.getPillar().equalsIgnoreCase(pillar));
        sortAndShuffleDeck();
    }

}
