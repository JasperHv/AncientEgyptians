package nl.vu.cs.ancientegyptiansgame.config.gamesettings;

import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreConfig;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Card;
import nl.vu.cs.ancientegyptiansgame.data.model.Mode;
import nl.vu.cs.ancientegyptiansgame.data.model.Monarch;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.logging.GameStateEntry;
import nl.vu.cs.ancientegyptiansgame.logging.GameStateLogger;
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

    public int getInitialScoreCount() {
        return scoreCount;
    }

    public int getInitialYearCount() {
        return yearCount;
    }

    public ScoreObserver getScoreObserver() {
        return scoreObserver;
    }

    public YearsInPowerObserver getYearsInPowerObserver() {
        return yearsInPowerObserver;
    }

    public static void saveGame() {
        if (!ModeConfiguration.isInitialized()) {
            return;
        }

        Map<String, Integer> pillarMap = new HashMap<>();
        for (Pillars pillar : Pillars.values()) {
            int value = ModeConfiguration.getInstance().getPillarData(pillar).getValue();
            pillarMap.put(pillar.name().toLowerCase(), value);
        }

        int year = GameConfiguration.getInstance().getYearsInPowerObserver().getYearsInPower();
        int score = GameConfiguration.getInstance().getScoreObserver().getScore();

        Mode currentMode = ModeConfiguration.getInstance().getCurrentMode();

        GameStateEntry gameState = new GameStateEntry(
                currentMode,
                pillarMap,
                year,
                score,
                System.currentTimeMillis()
        );

        GameStateLogger.logGameState(gameState);
    }
}