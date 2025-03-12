package nl.vu.cs.softwaredesign.data.config;

import nl.vu.cs.softwaredesign.data.model.Card;
import java.util.List;
import java.util.Map;

public class GameConfiguration {
    private Map<String, Map<String, Integer>> characterInitialValues;
    private List<Card> cards;

    private String selectedCharacter = "";

    // Getters
    public Map<String, Map<String, Integer>> getCharacterInitialValues() {
        return characterInitialValues;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getSelectedCharacter() {
        return selectedCharacter;
    }

    public void setSelectedCharacter(String character) {
        selectedCharacter = character;
    }

    public Map<String, Integer> getInitialValuesForCharacter(String character) {
        return characterInitialValues.getOrDefault(character, Map.of());
    }
}
