package config;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.scoresettings.BonusConfig;
import ancientegyptiansgame.config.scoresettings.ScoreConfig;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.data.model.Ending;
import ancientegyptiansgame.data.model.Mode;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConfigurationLoaderUnitTests {
    private ConfigurationLoader loader;

    @BeforeEach
    void setUp() {
        InputStream is = getClass().getClassLoader().getResourceAsStream("config/minimal_config.json");
        assertNotNull(is, "Config resource 'config/minimal_config.json' should be found in test resources");
        loader = new ConfigurationLoader(is);
    }

    // Valid JSON tests
    @Test
    void testGoldenAgeEnding() {
        Ending ga = loader.getGoldenAgeEnding();
        assertEquals("A test golden age ending.", ga.getDescription());
    }

    @Test
    void testBadEnding() {
        Ending bad = loader.getBadEnding();
        assertEquals("A test bad ending.", bad.getDescription());
    }

    @Test
    void testScoreSettings() {
        ScoreSettings s = loader.getScoreSettings();

        // Test ScoreConfig values
        ScoreConfig sc = s.getScoreConfig();
        assertEquals(0, sc.getInitialScore());
        assertEquals(0, sc.getInitialYearCount());
        assertEquals(10, sc.getYearThreshold());
        assertEquals(20, sc.getMaximumYearCount());

        // Test BonusConfig values
        BonusConfig bc = s.getBonusConfig();
        assertEquals(List.of(1, 2), bc.getThresholds());
        assertEquals(List.of(5, 10), bc.getBonusScores());
        assertEquals(2, bc.getBalancedBonus());

        // Test ScoreSettings simple fields
        assertEquals(1, s.getYearCountIncrease());
        assertEquals(1, s.getBaseScoreIncrease());
    }

    @Test
    void testModes() {
        List<Mode> modes = loader.getModes();
        assertEquals(2, modes.size());
        assertEquals("Test Mode 1", modes.get(0).getName());
        assertEquals("Test Mode 2", modes.get(1).getName());
    }

    @Test
    void testMonarchs() {
        List<String> mons = loader.getMonarchs();
        assertEquals(List.of("Testmonarch1", "Testmonarch2"), mons);
    }

    @Test
    void testPillarEnding() {
        Ending nobles = loader.getPillarEnding("nobles");
        assertEquals("Pillar ending for nobles.", nobles.getDescription());
    }

    // Invalid JSON tests
    @Test
    void testMalformedJsonThrowsException() {
        String badJson = "{ invalid json ";
        ByteArrayInputStream stream = new ByteArrayInputStream(badJson.getBytes(StandardCharsets.UTF_8));

        assertThrows(RuntimeException.class, () -> new ConfigurationLoader(stream));
    }

    @Test
    void testMissingModesThrowsException() {
        String json = "{ \"monarchs\": [\"Mon1\"], \"goldenAgeEnding\": {\"description\": \"desc\", \"image\": \"img\"} }";
        ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        assertThrows(RuntimeException.class, () -> new ConfigurationLoader(stream));
    }

    @Test
    void testWrongTypeInJsonThrowsException() {
        String json = "{ \"modes\": \"not an array\" }";
        ByteArrayInputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        assertThrows(RuntimeException.class, () -> new ConfigurationLoader(stream));
    }

    @Test
    void testUnknownPillarNameReturnsNull() {
        Ending unknown = loader.getPillarEnding("unknownpillar");
        assertNull(unknown);
    }
}
