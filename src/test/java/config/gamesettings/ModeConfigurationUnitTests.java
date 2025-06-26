package config.gamesettings;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.mockito.MockitoAnnotations;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ModeConfigurationUnitTests {

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        ModeConfiguration.reset(); // Reset singleton before each test
    }

    @AfterEach
    void tearDown() throws Exception {
        ModeConfiguration.reset(); // Clean up after each test
        closeable.close();
    }

    @Test
    void testInitializeWithValidMode() {
        // Given
        String modeName = "testMode";
        Mode testMode = new Mode(modeName, "config/modes/test1.json");
        List<Mode> modes = List.of(testMode);

        // Real config loader with test mode
        ConfigurationLoader configLoader = new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return modes;
            }
        };

        // Use lambda instead of mocking Function
        Function<String, InputStream> resourceLoader = path ->
                getClass().getClassLoader().getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);

        // Check the resource is found
        InputStream configStream = resourceLoader.apply("config/modes/test1.json");
        assertNotNull(configStream, "test1.json resource should be found");

        // When
        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
        ModeConfiguration instance = ModeConfiguration.getInstance();

        // Then
        assertNotNull(instance);
        assertNotNull(instance.getPillarObserver());

        for (Pillars pillar : Pillars.values()) {
            PillarData pillarData = instance.getPillarData(pillar);
            assertNotNull(pillarData);
            assertEquals(1, pillarData.getValue());
        }
    }
}