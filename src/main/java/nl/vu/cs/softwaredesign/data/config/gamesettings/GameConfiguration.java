package nl.vu.cs.softwaredesign.data.config.gamesettings;

import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Monarch;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameConfiguration {
    private final Map<String, Map<String, Integer>> monarchInitialValues = new HashMap<>();
    private List<Card> cards;
    private Monarch selectedMonarch;
    private String gameMode;

    public List<Card> getCards() {
        return cards;
    }

    public Monarch getSelectedMonarch() {
        return selectedMonarch;
    }

    public void setSelectedMonarch(String monarchName) {
        Map<String, Integer> initialValues = monarchInitialValues.get(monarchName);
        if (initialValues == null) {
            throw new IllegalArgumentException("No initial values found for monarch: " + monarchName);
        }
        this.selectedMonarch = new Monarch(monarchName, initialValues);
    }

    public Map<String, Map<String, Integer>> getMonarchInitialValues() {
        return monarchInitialValues;
    }


    public String getGameMode() {
        return gameMode;
    }
}