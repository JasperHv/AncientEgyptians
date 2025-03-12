package nl.vu.cs.softwaredesign.config;

import nl.vu.cs.softwaredesign.data.Card;

import java.util.List;

public class ModeConfiguration {

    private Integer initialValue;
    private List<Card> cards;

    public Integer getInitialValue() {
        return initialValue;
    }

    public List<Card> getCards() {
        return cards;
    }

    private ModeConfiguration() {}

    public ModeConfiguration(ModeConfiguration.Builder builder) {
        this.initialValue = builder.initialValue;
        this.cards = List.copyOf(builder.cards);
    }

    public static class Builder {
        private List<Card> cards;
        private Integer initialValue;

        public Builder withCards(Card... cards) {
            this.cards = List.of(cards);
            return this;
        }

        public Builder withInitialValue(Integer initialValue) {
            this.initialValue = initialValue;
            return this;
        }

        public ModeConfiguration build() {
            if(cards == null || cards.isEmpty()) {
                throw new IllegalStateException("cards can't be null or empty");
            }
            if(initialValue == null) {
                throw new IllegalStateException("initialValue can't be null");
            }
            return new ModeConfiguration(this);
        }


    }

}
