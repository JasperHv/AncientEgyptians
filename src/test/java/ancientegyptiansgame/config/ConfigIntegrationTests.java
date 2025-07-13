package ancientegyptiansgame.config;

import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;

import static ancientegyptiansgame.config.gamesettings.ModeConfigurationTestAccess.*;

import java.util.List;
import java.util.function.Function;
import java.io.InputStream;

import ancientegyptiansgame.exception.ConfigurationNotFoundException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigIntegrationTests {

    @BeforeEach
    void resetConfigs() {
        ConfigurationLoader.reset();
        GameConfiguration.setInstance(null);
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

    @Test
    void testInitialSetUp_loadsExpectedPillarValuesFromConfig() {
        // Arrange
        String modeName = "testMode";
        String jsonPath = "config/modes/test1.json";

        Mode testMode = new Mode(modeName, jsonPath);
        ConfigurationLoader configLoader = mockConfigLoader(testMode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        assertNotNull(resourceLoader.apply(jsonPath), "test1.json should be present in test resources");
        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);

        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.setSelectedMonarch("TestMonarch1");

        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        setGameConfig(modeConfig, gameConfig);

        modeConfig.updatePillarValues();

        // Assert
        PillarData nobles = modeConfig.getPillarData(Pillars.NOBLES);
        PillarData priests = modeConfig.getPillarData(Pillars.PRIESTS);

        assertEquals(60, nobles.getValue());
        assertEquals(60, priests.getValue());
    }

    @Test
    void testFullConfigIntegration_fromMainAndModeConfigFiles() {
        // Arrange
        InputStream is = getClass().getClassLoader().getResourceAsStream("config/configCorrect.json");
        assertNotNull(is, "Config resource 'config/configCorrect.json' should be found in test resources");

        ConfigurationLoader loader = new ConfigurationLoader(is); // reads configCorrect.json

        Mode testMode = loader.getModes().stream()
                .filter(m -> m.getName().equals("test1"))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Mode 'test1' not found in configCorrect.json"));

        Function<String, InputStream> resourceLoader = getResourceLoader();

        assertNotNull(resourceLoader.apply(testMode.getConfigPath()), "Mode config file should be present");
        ModeConfiguration.initialize(testMode.getName(), loader, resourceLoader);
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();

        GameConfiguration gameConfig = GameConfiguration.getInstance();
        gameConfig.setSelectedMonarch("TestMonarch1");
        gameConfig.initializeScoreAndYear(loader.getScoreSettings());

        setGameConfig(modeConfig, gameConfig);
        modeConfig.updatePillarValues();

        // Assert
        assertEquals("TestMonarch1", gameConfig.getSelectedMonarch().getName());
        assertEquals(0, gameConfig.getInitialScoreCount());
        assertEquals(0, gameConfig.getInitialYearCount());
        assertEquals(60, modeConfig.getPillarData(Pillars.NOBLES).getValue());
        assertEquals(60, modeConfig.getPillarData(Pillars.PRIESTS).getValue());
    }

    @Test
    void testMissingModeConfigFile_throwsException() {
        var mode = new Mode("MissingMode", "config/modes/missing.json");
        ConfigurationLoader configLoader = mockConfigLoader(mode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        ConfigurationNotFoundException ex = assertThrows(ConfigurationNotFoundException.class, () ->
                ModeConfiguration.initialize("MissingMode", configLoader, resourceLoader)
        );

        assertTrue(ex.getMessage().contains("Mode config file not found"));
    }

    @Test
    void testInvalidJson_throwsException() {
        var mode = new Mode("InvalidJson", "config/modes/broken.json");
        ConfigurationLoader configLoader = mockConfigLoader(mode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        ConfigurationNotFoundException ex = assertThrows(ConfigurationNotFoundException.class, () ->
                ModeConfiguration.initialize("InvalidJson", configLoader, resourceLoader)
        );

        assertTrue(ex.getMessage().contains("Error loading mode config"));
    }

    @Test
    void testUnknownModeName_throwsException() {
        var mode = new Mode("ValidMode", "config/modes/test1.json");
        ConfigurationLoader configLoader = mockConfigLoader(mode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        Exception ex = assertThrows(RuntimeException.class, () ->
                ModeConfiguration.initialize("NonExistentMode", configLoader, resourceLoader)
        );

        assertTrue(ex.getMessage().contains("No mode found for:"));
    }

    @Test
    void testEmptyJson_throwsOrDefaults() {
        var mode = new Mode("Empty", "config/modes/empty.json");
        ConfigurationLoader configLoader = mockConfigLoader(mode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        ConfigurationNotFoundException ex = assertThrows(ConfigurationNotFoundException.class, () ->
                ModeConfiguration.initialize("Empty", configLoader, resourceLoader)
        );

        assertTrue(ex.getMessage().contains("Error loading mode config"));
    }

    @Test
    void testInvalidFieldTypes_throwsException() {
        var mode = new Mode("BadFieldTypes", "config/modes/invalid_field_types.json");
        ConfigurationLoader configLoader = mockConfigLoader(mode);
        Function<String, InputStream> resourceLoader = getResourceLoader();

        ConfigurationNotFoundException ex = assertThrows(ConfigurationNotFoundException.class, () ->
                ModeConfiguration.initialize("BadFieldTypes", configLoader, resourceLoader)
        );

        assertTrue(ex.getMessage().contains("Error loading mode config"));
    }
}