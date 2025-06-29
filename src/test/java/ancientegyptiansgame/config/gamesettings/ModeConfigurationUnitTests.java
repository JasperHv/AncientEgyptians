package ancientegyptiansgame.config.gamesettings;

import ancientegyptiansgame.config.ConfigurationLoader;
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

    @Test
    void testInitializeWithValidMode() {
        initializeWithTestMode();
        ModeConfiguration instance = ModeConfiguration.getInstance();

        for (Pillars pillar : Pillars.values()) {
            PillarData data = instance.getPillarData(pillar);
            assertNotNull(data, "PillarData should not be null for " + pillar);
            assertEquals(1, data.getValue(), "Default value for " + pillar + " should be 1");
        }
    }

    @Test
    void testUpdatePillarValuesSucceedsIfMonarchIsSet() {
        initializeWithTestMode();
        ModeConfiguration instance = ModeConfiguration.getInstance();

        GameConfiguration.setInstance(null);
        GameConfiguration gameConfig = GameConfiguration.getInstance();

        String monarchName = "SomeMonarch";
        gameConfig.getMonarchInitialValues().put(monarchName, Map.of("nobles", 10));
        gameConfig.setSelectedMonarch(monarchName);

        instance.setGameConfigForTest(gameConfig);

        assertDoesNotThrow(instance::updatePillarValues);
    }

    @Test
    void testUpdatePillarValuesThrowsIfMonarchNotSet() {
        initializeWithTestMode();
        ModeConfiguration instance = ModeConfiguration.getInstance();

        GameConfiguration.setInstance(null);
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.getMonarchInitialValues().put("SomeMonarch", Map.of("nobles", 10));

        instance.setGameConfigForTest(gameConfig);

        assertThrows(IllegalStateException.class, instance::updatePillarValues);
    }

    @Test
    void testUpdatePillarValuesThrowsIfGameConfigIsNull() {
        initializeWithTestMode();
        ModeConfiguration instance = ModeConfiguration.getInstance();

        instance.setGameConfigForTest(null);

        assertThrows(IllegalStateException.class, instance::updatePillarValues);
    }
}
