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

public class ModeConfigurationUnitTests {

    @BeforeEach
    void setUp() {
        ModeConfiguration.reset();
    }

    @AfterEach
    void tearDown() {
        ModeConfiguration.reset();
    }

public class ModeConfigurationUnitTests {

    private static final String TEST_MODE_NAME   = "testMode";
    private static final String TEST_MODE_CONFIG = "config/modes/test1.json";

    @BeforeEach
    void setUp() {
        ModeConfiguration.reset();
    }

    @AfterEach
    void tearDown() {
        ModeConfiguration.reset();
    }

    private ConfigurationLoader createConfigLoaderWithTestMode() {
        Mode testMode = new Mode(TEST_MODE_NAME, TEST_MODE_CONFIG);
        List<Mode> modes = List.of(testMode);

        return new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return modes;
            }
        };
    }

    private Function<String, InputStream> createResourceLoader() {
        return path -> getClass().getClassLoader()
                .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);
    }

    @Test
    void testInitializeWithValidMode() {
        ConfigurationLoader configLoader    = createConfigLoaderWithTestMode();
        Function<String, InputStream> resourceLoader = createResourceLoader();

        assertNotNull(resourceLoader.apply("config/modes/test1.json"),
                "test1.json should be present in test resources");

        ModeConfiguration.initialize(TEST_MODE_NAME, configLoader, resourceLoader);
        ModeConfiguration instance = ModeConfiguration.getInstance();

        for (Pillars pillar : Pillars.values()) {
            PillarData data = instance.getPillarData(pillar);
            assertNotNull(data, "PillarData should not be null for " + pillar);
            assertEquals(1, data.getValue(),
                    "Default value for " + pillar + " should be 1");
        }
    }

    // ... apply similar refactoring to the other test methods ...
}

        // Initialize ModeConfiguration
        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
        ModeConfiguration instance = ModeConfiguration.getInstance();

        // Set gameConfig to null manually for testing
        instance.setGameConfigForTest(null);

        // Then: updatePillarValues should throw
        assertThrows(IllegalStateException.class, instance::updatePillarValues);
    }
}
