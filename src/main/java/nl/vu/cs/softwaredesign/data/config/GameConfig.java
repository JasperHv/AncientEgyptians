package nl.vu.cs.softwaredesign.data.config;

import nl.vu.cs.softwaredesign.data.model.Card;
import java.util.List;
import java.util.Map;

public class GameConfig {
    private Map<String, Map<String, Integer>> characterInitialValues;
    private List<Card> cards;
    public String gameMode = "";
    private String selectedCharacter = "";

    // Getters
    public Map<String, Map<String, Integer>> getCharacterInitialValues() {
        return characterInitialValues;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String mode) {
        this.gameMode = mode;
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
