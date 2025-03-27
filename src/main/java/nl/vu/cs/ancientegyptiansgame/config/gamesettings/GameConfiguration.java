package nl.vu.cs.ancientegyptiansgame.config.gamesettings;

import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreConfig;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Card;
import nl.vu.cs.ancientegyptiansgame.data.model.Monarch;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameConfiguration {
    private static GameConfiguration instance;

    private final Map<String, Map<String, Integer>> monarchInitialValues = new HashMap<>();
    private List<Card> cards;
    private Monarch selectedMonarch;
    private int scoreCount;
    private int yearCount;

    private GameConfiguration() {
    }

    public static synchronized void setInstance(GameConfiguration config) {
        instance = config;
    }
    public static synchronized GameConfiguration getInstance() {
        if (instance == null) {
            instance = new GameConfiguration();
        }
        return instance;
    }

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


    public void initializeScoreAndYear(ScoreSettings scoreSettings) {
        ScoreConfig scoreConfig = scoreSettings.getScoreConfig();
        this.scoreCount = scoreConfig.getInitialScore();
        this.yearCount = scoreConfig.getInitialYearCount();
    }

    public int getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(int scoreCount) {
        this.scoreCount = scoreCount;
    }

    public int getYearCount() {
        return yearCount;
    }

    public void setYearCount(int yearCount) {
        this.yearCount = yearCount;
    }

    public void incrementYearCount(int increment) {
        this.yearCount += increment;
    }

    public void incrementScoreCount(int increment) {
        this.scoreCount += increment;
    }
}