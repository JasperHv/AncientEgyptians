package nl.vu.cs.softwaredesign.data.config.gamesettings;

import nl.vu.cs.softwaredesign.data.model.Card;
import java.util.List;
import java.util.Map;

public class GameConfiguration {
    private Map<String, Map<String, Integer>> monarchInitialValues;
    private List<Card> cards;
    private String selectedMonarch = "";
    private String gameMode = "";

    // Getters
    public Map<String, Map<String, Integer>> getMonarchInitialValues() {
        return monarchInitialValues;
    }

    public List<Card> getCards() {
        return cards;
    }

    public String getSelectedMonarch() {
        return selectedMonarch;
    }

    public void setSelectedMonarch(String monarch) {
        selectedMonarch = monarch;
    }

    public Map<String, Integer> getInitialValuesForMonarch(String monarch) {
        return monarchInitialValues.getOrDefault(monarch, Map.of());
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String mode) {
        this.gameMode = mode;
    }
}
