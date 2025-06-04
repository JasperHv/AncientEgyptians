package nl.vu.cs.ancientegyptiansgame.config.gamesettings;

import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreConfig;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Card;
import nl.vu.cs.ancientegyptiansgame.data.model.Monarch;
import nl.vu.cs.ancientegyptiansgame.observer.ScoreObserver;
import nl.vu.cs.ancientegyptiansgame.observer.YearsInPowerObserver;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameConfiguration {
    private static GameConfiguration instance;

    private Monarch selectedMonarch;
    private final Map<String, Map<String, Integer>> monarchInitialValues = new HashMap<>();
    private List<Card> cards;
    private int scoreCount;
    private int yearCount;
    private final ScoreObserver scoreObserver = new ScoreObserver();
    private final YearsInPowerObserver yearsInPowerObserver = new YearsInPowerObserver();
    /**
     * Private constructor to enforce singleton pattern for GameConfiguration.
     */
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


    /**
     * Initializes the initial score and year count using the provided score settings.
     *
     * @param scoreSettings the settings containing score configuration values
     */
    public void initializeScoreAndYear(ScoreSettings scoreSettings) {
        ScoreConfig scoreConfig = scoreSettings.getScoreConfig();
        this.scoreCount = scoreConfig.getInitialScore();
        this.yearCount = scoreConfig.getInitialYearCount();
    }

    /**
     * Returns the initial score count configured for the game.
     *
     * @return the initial score value
     */
    public int getInitialScoreCount() {
        return scoreCount;
    }

    /**
     * Returns the initial year count configured for the game.
     *
     * @return the initial number of years in power at the start of the game
     */
    public int getInitialYearCount() {
        return yearCount;
    }

    /**
     * Returns the observer monitoring score changes in the game.
     *
     * @return the ScoreObserver instance
     */
    public ScoreObserver getScoreObserver() {
        return scoreObserver;
    }

    /**
     * Returns the observer monitoring the years in power.
     *
     * @return the YearsInPowerObserver instance
     */
    public YearsInPowerObserver getYearsInPowerObserver() {
        return yearsInPowerObserver;
    }
}