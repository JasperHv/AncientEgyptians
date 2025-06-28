package config.gamesettings;

import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.data.model.Monarch;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameConfigurationUnitTests {

    @BeforeEach
    void setUp() {
        GameConfiguration.setInstance(null);
    }

    @AfterEach
    void tearDown() {
        GameConfiguration.setInstance(null);
    }

    @Test
    void testGetInstanceReturnsSingleton() {
        GameConfiguration config1 = GameConfiguration.getInstance();
        GameConfiguration config2 = GameConfiguration.getInstance();
        assertSame(config1, config2, "getInstance should return the same instance");
    }

    @Test
    void testSetAndGetSelectedMonarch() {
        GameConfiguration config = GameConfiguration.getInstance();
        String monarchName = "TestMonarch";
        config.getMonarchInitialValues().put(monarchName, Map.of("nobles", 5, "wealth", 7));
        config.setSelectedMonarch(monarchName);

        Monarch monarch = config.getSelectedMonarch();
        assertNotNull(monarch, "Selected monarch should not be null");
        assertEquals(monarchName, monarch.getName());
        assertEquals(5, monarch.getInitialValues().get("nobles"));
        assertEquals(7, monarch.getInitialValues().get("wealth"));
    }

    @Test
    void testMonarchInitialValuesMapIsMutable() {
        GameConfiguration config = GameConfiguration.getInstance();
        config.getMonarchInitialValues().put("A", Map.of("nobles", 1));
        assertTrue(config.getMonarchInitialValues().containsKey("A"));
    }

    @Test
    void testScoreAndYearInitialization() {
        GameConfiguration config = GameConfiguration.getInstance();
        // Mock ScoreSettings and ScoreConfig
        var scoreSettings = new ancientegyptiansgame.config.scoresettings.ScoreSettings() {
            @Override
            public ancientegyptiansgame.config.scoresettings.ScoreConfig getScoreConfig() {
                return new ancientegyptiansgame.config.scoresettings.ScoreConfig(42, 7, 0, 0);
            }
        };
        config.initializeScoreAndYear(scoreSettings);
        assertEquals(42, config.getInitialScoreCount());
        assertEquals(7, config.getInitialYearCount());
    }

    @Test
    void testSetSelectedMonarchThrowsIfNotInInitialValues() {
        GameConfiguration config = GameConfiguration.getInstance();
        assertThrows(IllegalArgumentException.class, () -> config.setSelectedMonarch("NonExistentMonarch"));
    }

    @Test
    void testGetSelectedMonarchReturnsNullIfNotSet() {
        GameConfiguration config = GameConfiguration.getInstance();
        assertNull(config.getSelectedMonarch(), "Selected monarch should be null if not set");
    }

    @Test
    void testInitializeScoreAndYearWithNullScoreSettingsThrows() {
        GameConfiguration config = GameConfiguration.getInstance();
        assertThrows(NullPointerException.class, () -> config.initializeScoreAndYear(null));
    }
}