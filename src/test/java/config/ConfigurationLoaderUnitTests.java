package config;

import java.io.ByteArrayInputStream;
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

class ConfigurationLoaderTest {
    private static final String MINIMAL_JSON = "{\n" +
            "  \"modes\": [\n" +
            "    { \"name\": \"Test Mode 1\", \"configPath\": \"configuration/modes/test1.json\" },\n" +
            "    { \"name\": \"Test Mode 2\", \"configPath\": \"configuration/modes/test2.json\" }\n" +
            "  ],\n" +
            "  \"monarchs\": [\"Testmonarch1\", \"Testmonarch2\"],\n" +
            "  \"goldenAgeEnding\": {\n" +
            "    \"description\": \"A test golden age ending.\",\n" +
            "    \"image\": \"endings/test-golden.png\"\n" +
            "  },\n" +
            "  \"bad ending\": {\n" +
            "    \"description\": \"A test bad ending.\",\n" +
            "    \"image\": \"endings/test-bad.png\"\n" +
            "  },\n" +
            "  \"scoreSettings\": {\n" +
            "    \"scoreConfig\": {\n" +
            "      \"initialScore\": 0,\n" +
            "      \"initialYearCount\": 0,\n" +
            "      \"yearThreshold\": 10,\n" +
            "      \"maximumYearCount\": 20\n" +
            "    },\n" +
            "    \"bonusConfig\": {\n" +
            "      \"thresholds\": [1, 2],\n" +
            "      \"bonusScores\": [5, 10],\n" +
            "      \"balancedBonus\": 2\n" +
            "    },\n" +
            "    \"yearCountIncrease\": 1,\n" +
            "    \"baseScoreIncrease\": 1\n" +
            "  },\n" +
            "  \"pillars\": [\n" +
            "    {\n" +
            "      \"name\": \"nobles\",\n" +
            "      \"ending\": {\n" +
            "        \"description\": \"Pillar ending for nobles.\",\n" +
            "        \"image\": \"endings/nobles-ending.png\"\n" +
            "      }\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    private ConfigurationLoader loader;

    @BeforeEach
    void setUp() {
        ByteArrayInputStream testStream =
                new ByteArrayInputStream(MINIMAL_JSON.getBytes(StandardCharsets.UTF_8));
        loader = new ConfigurationLoader(testStream);
    }

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
}
