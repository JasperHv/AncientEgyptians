package config.gamesettings;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ModeConfigurationUnitTests {

    @BeforeEach
    void setUp() {
        ModeConfiguration.reset();
    }

    @AfterEach
    void tearDown() {
        ModeConfiguration.reset();
    }

    @Test
    void testInitializeWithValidMode() {
        String modeName = "testMode";
        Mode testMode = new Mode(modeName, "config/modes/test1.json");
        List<Mode> modes = List.of(testMode);

        // Anonymous subclass since ConfigurationLoader is a class
        ConfigurationLoader configLoader = new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return modes;
            }
        };

        // Functional interface for loading resources
        Function<String, InputStream> resourceLoader = path ->
                getClass().getClassLoader()
                        .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);

        assertNotNull(resourceLoader.apply("config/modes/test1.json"),
                "test1.json should be present in test resources");

        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
        ModeConfiguration instance = ModeConfiguration.getInstance();

        for (Pillars pillar : Pillars.values()) {
            PillarData data = instance.getPillarData(pillar);
            assertNotNull(data, "PillarData should not be null for " + pillar);
            assertEquals(1, data.getValue(),
                    "Default value for " + pillar + " should be 1");
        }
    }

    @Test
    void testUpdatePillarValuesThrowsIfMonarchNotSet() {
        String modeName = "testMode";
        Mode testMode = new Mode(modeName, "config/modes/test1.json");
        List<Mode> modes = List.of(testMode);

        ConfigurationLoader configLoader = new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return modes;
            }
        };

        Function<String, InputStream> resourceLoader = path ->
                getClass().getClassLoader()
                        .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);

        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
        ModeConfiguration instance = ModeConfiguration.getInstance();

        // Prepare a new GameConfiguration without a selected monarch
        GameConfiguration.setInstance(null);
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getMonarchInitialValues().put("SomeMonarch", Map.of("nobles", 10));
        // Do not call setSelectedMonarch(...)

        instance.setGameConfigForTest(gameConfig);

        assertThrows(IllegalStateException.class, instance::updatePillarValues);
    }

    @Test
    void testUpdatePillarValuesThrowsIfGameConfigIsNull() {
        // Given a fresh ModeConfiguration instance without setting a gameConfig
        ModeConfiguration.reset(); // Clear any previous singleton instance

        String modeName = "testMode";
        Mode testMode = new Mode(modeName, "config/modes/test1.json");
        List<Mode> modes = List.of(testMode);

        ConfigurationLoader configLoader = new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return modes;
            }
        };

        Function<String, InputStream> resourceLoader = path ->
                getClass().getClassLoader()
                        .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);

        // Initialize ModeConfiguration
        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
        ModeConfiguration instance = ModeConfiguration.getInstance();

        // Set gameConfig to null manually for testing
        instance.setGameConfigForTest(null);

        // Then: updatePillarValues should throw
        assertThrows(IllegalStateException.class, instance::updatePillarValues);
    }
}
