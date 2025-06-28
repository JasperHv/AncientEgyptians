package integration;

import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.config.scoresettings.ScoreConfig;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ConfigIntegrationTests {

    @BeforeEach
    void resetConfigs() {
        GameConfiguration.setInstance(null);
        ModeConfiguration.reset();
    }

    @Test
    void testFullConfigIntegration() {
        // Setup ScoreSettings
        ScoreSettings scoreSettings = new ScoreSettings() {
            @Override
            public ScoreConfig getScoreConfig() {
                return new ScoreConfig(100, 10, 5, 20);
            }
        };

        // Setup GameConfiguration
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getMonarchInitialValues().put("Cleopatra", Map.of("nobles", 10, "wealth", 20));
        gameConfig.setSelectedMonarch("Cleopatra");
        gameConfig.initializeScoreAndYear(scoreSettings);

        // Setup ModeConfiguration
        var mode = new ancientegyptiansgame.data.model.Mode("Classic", "config/modes/test1.json");
        List<ancientegyptiansgame.data.model.Mode> modes = List.of(mode);
        var configLoader = new ancientegyptiansgame.config.ConfigurationLoader() {
            @Override
            public List<ancientegyptiansgame.data.model.Mode> getModes() {
                return modes;
            }
        };
        Function<String, InputStream> resourceLoader = path -> {
            String cleanPath = path.startsWith("/") ? path.substring(1) : path;
            return getClass().getClassLoader().getResourceAsStream(cleanPath);
        };
        ModeConfiguration.initialize("Classic", configLoader, resourceLoader);
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        modeConfig.setGameConfigForTest(gameConfig);

        // Assertions: All configs are linked and initialized
        assertEquals("Cleopatra", gameConfig.getSelectedMonarch().getName());
        assertEquals(100, gameConfig.getInitialScoreCount());
        assertEquals(10, gameConfig.getInitialYearCount());

    }
}