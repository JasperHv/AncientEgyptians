package ancientegyptiansgame.game;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.config.scoresettings.BonusConfig;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;
import ancientegyptiansgame.handlers.HandleScore;
import ancientegyptiansgame.observer.ScoreObserver;
import ancientegyptiansgame.observer.YearsInPowerObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class HandleScoreUnitTests {

    private ScoreSettings scoreSettings;

    private ConfigurationLoader mockConfigLoader(Mode... modes) {
        return new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return List.of(modes);
            }
        };
    }

    private Function<String, InputStream> getResourceLoader() {
        return path -> getClass().getClassLoader()
                .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);
    }


    private void initializeWithTestMode() {
        String modeName = "testMode";
        String jsonPath = "config/modes/test1.json";

        Mode testMode = new Mode(modeName, jsonPath);
        ConfigurationLoader configLoader = mockConfigLoader(testMode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        assertNotNull(resourceLoader.apply(jsonPath), "test1.json should be present in test resources");
        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
    }

    @BeforeEach
    void setUp() {
        // Reset singletons
        GameConfiguration.setInstance(null);
        ModeConfiguration.reset();

        // 1. Load main configuration (configCorrect.json)
        InputStream is = getClass().getClassLoader().getResourceAsStream("config/configCorrect.json");
        assertNotNull(is, "Config resource 'config/configCorrect.json' should be found in test resources");
        ConfigurationLoader loader = new ConfigurationLoader(is);

        // Initialize GameConfiguration singleton with main config
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        GameConfiguration.setInstance(gameConfig);

        // 2. Initialize ModeConfiguration with test1.json
        initializeWithTestMode();
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();

        // 3. Merge monarchInitialValues from ModeConfiguration into GameConfiguration
        var modeMonarchValues = modeConfig.getAllMonarchInitialValues(); // Map<String, Map<String, Integer>>
        modeMonarchValues.forEach((monarch, values) -> {
            gameConfig.getMonarchInitialValues().put(monarch, values);
        });

        // 4. Now set the selected monarch (exists in the merged map)
        gameConfig.setSelectedMonarch("TestMonarch1");

        // Inject GameConfiguration into ModeConfiguration for test
        modeConfig.setGameConfigForTest(gameConfig);
        modeConfig.updatePillarValues();

        // 5. Check pillars
        for (Pillars pillar : Pillars.values()) {
            PillarData data = modeConfig.getPillarData(pillar);
            assertNotNull(data, "PillarData should not be null for " + pillar);
        }

        // 6. Set observers initial values
        ScoreObserver scoreObserver = gameConfig.getScoreObserver();
        YearsInPowerObserver yearsObserver = gameConfig.getYearsInPowerObserver();

        scoreObserver.setScore(gameConfig.getInitialScoreCount());
        yearsObserver.setYearsInPower(gameConfig.getInitialYearCount());

        // 7. Save ScoreSettings from loader for tests
        this.scoreSettings = loader.getScoreSettings();
    }

    @Test
    void testUpdateScoreWithBalancedPillars() {
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getScoreObserver().setScore(100);
        gameConfig.getYearsInPowerObserver().setYearsInPower(10);

        HandleScore handleScore = new HandleScore();
        handleScore.updateScore(scoreSettings);

        // At year 10, no threshold bonuses apply, so only baseScoreIncrease (5) is added.
        // Balanced pillars add balancedBonus (5), total increase = 10, resulting score = 110.
        assertEquals(110, gameConfig.getScoreObserver().getScore());
    }

    @Test
    void testUpdateScoreWithOutBalancedPillars() {
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getScoreObserver().setScore(100);
        gameConfig.getYearsInPowerObserver().setYearsInPower(10);

        // Make pillars unbalanced by setting PRIESTS pillar value to 20 (below balanced range)
        ModeConfiguration.getInstance().getPillarObserver().getPillarData(Pillars.PRIESTS).setValue(20);

        HandleScore handleScore = new HandleScore();
        handleScore.updateScore(scoreSettings);

        // At year 10, pillars are not balanced because PRIESTS pillar value is 20 (below 25)
        // No threshold bonuses apply, only baseScoreIncrease (5), so expected score = 105
        assertEquals(105, gameConfig.getScoreObserver().getScore());
    }

    @Test
    void testUpdateScoreWithAllThresholdBonusesAndBalancedPillars() {
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getScoreObserver().setScore(100);

        HandleScore handleScore = new HandleScore();

        int[] yearsToTest = {11, 21, 31};
        int[] expectedIncreases = {
                5 + 10 + 5,    // baseScoreIncrease + bonus for 10 threshold + balancedBonus
                5 + 10 + 20 + 5, // base + bonuses for 10 & 20 thresholds + balancedBonus
                5 + 10 + 20 + 30 + 5  // base + all bonuses + balancedBonus
        };

        for (int i = 0; i < yearsToTest.length; i++) {
            // Reset score before each test case
            gameConfig.getScoreObserver().setScore(100);
            gameConfig.getYearsInPowerObserver().setYearsInPower(yearsToTest[i]);

            handleScore.updateScore(scoreSettings);

            int expectedScore = 100 + expectedIncreases[i];
            assertEquals(expectedScore, gameConfig.getScoreObserver().getScore(),
                    "Failed for yearCount = " + yearsToTest[i]);
        }
    }

    @Test
    void testUpdateScoreWithAllThresholdBonusesAndNoBalancedPillars() {
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getScoreObserver().setScore(100);

        HandleScore handleScore = new HandleScore();

        int[] yearsToTest = {11, 21, 31};
        int[] expectedIncreases = {
                5 + 10,    // baseScoreIncrease + bonus for 10 threshold
                5 + 10 + 20, // base + bonuses for 10 & 20 thresholds
                5 + 10 + 20 + 30   // base + all bonuses
        };


        // Make pillars unbalanced by setting PRIESTS pillar value to 20 (below balanced range)
        ModeConfiguration.getInstance().getPillarObserver().getPillarData(Pillars.PRIESTS).setValue(20);

        for (int i = 0; i < yearsToTest.length; i++) {
            // Reset score before each test case
            gameConfig.getScoreObserver().setScore(100);
            gameConfig.getYearsInPowerObserver().setYearsInPower(yearsToTest[i]);

            handleScore.updateScore(scoreSettings);

            int expectedScore = 100 + expectedIncreases[i];
            assertEquals(expectedScore, gameConfig.getScoreObserver().getScore(),
                    "Failed for yearCount = " + yearsToTest[i]);
        }
    }
}