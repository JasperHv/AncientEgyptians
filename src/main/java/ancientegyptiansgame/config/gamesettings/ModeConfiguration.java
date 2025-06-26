package ancientegyptiansgame.config.gamesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.data.model.Monarch;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.PillarData;
import ancientegyptiansgame.exception.ConfigurationNotFoundException;
import ancientegyptiansgame.observer.PillarObserver;

import java.io.InputStream;
import java.util.Map;
import java.util.function.Function;

public class ModeConfiguration {
    private static ModeConfiguration instance;

    private GameConfiguration gameConfig;
    private final PillarObserver pillarObserver;
    private final Function<String, InputStream> resourceLoader;

    // --- Constructor ---
    private ModeConfiguration(String modeName,
                              ConfigurationLoader configLoader,
                              Function<String, InputStream> resourceLoader) {
        this.resourceLoader = resourceLoader;
        this.pillarObserver = new PillarObserver();

        Mode selectedMode = configLoader.getModes().stream()
                .filter(m -> m.getName().equalsIgnoreCase(modeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mode found for: " + modeName));

        loadModeConfig(selectedMode.getConfigPath());
    }

    private void loadModeConfig(String modeConfigPath) {
        try (InputStream input = resourceLoader.apply("/" + modeConfigPath)) {
            if (input == null) {
                throw new ConfigurationNotFoundException("Mode config file not found: " + modeConfigPath);
            }

            ObjectMapper mapper = new ObjectMapper();
            gameConfig = mapper.readValue(input, GameConfiguration.class);
            GameConfiguration.setInstance(gameConfig);

            for (Pillars pillar : Pillars.values()) {
                pillarObserver.addPillar(pillar, 1);
            }

        } catch (Exception e) {
            throw new ConfigurationNotFoundException("Error loading mode config: " + e.getMessage(), e);
        }
    }

    // --- Singleton Access ---
    public static void initialize(String modeName) {
        initialize(modeName, ConfigurationLoader.getInstance(), ModeConfiguration::defaultResourceLoader);
    }

    public static void initialize(String modeName,
                                  ConfigurationLoader configLoader,
                                  Function<String, InputStream> resourceLoader) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName, configLoader, resourceLoader);
        }
    }

    public static ModeConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModeConfiguration not initialized.");
        }
        return instance;
    }

    // Optional reset method (test-only)
    public static void reset() {
        instance = null;
    }

    // --- Helper for default resource loader ---
    private static InputStream defaultResourceLoader(String path) {
        return ModeConfiguration.class.getResourceAsStream(path);
    }

    // --- Public Methods ---
    public void updatePillarValues() {
        if (gameConfig == null) {
            throw new IllegalStateException("Game configuration is not initialized.");
        }

        Monarch selectedMonarch = gameConfig.getSelectedMonarch();
        if (selectedMonarch == null) {
            throw new IllegalStateException("Selected monarch is not set in game configuration.");
        }


        Map<String, Map<String, Integer>> monarchInitialValues = gameConfig.getMonarchInitialValues();
        String monarchName = selectedMonarch.getName();
        Map<String, Integer> initialValues = monarchInitialValues.get(monarchName);

        for (Pillars pillar : Pillars.values()) {
            int value = (initialValues != null)
                    ? initialValues.getOrDefault(pillar.getName().toLowerCase(), 0)
                    : 0;

            PillarData pillarData = pillarObserver.getPillarData(pillar);
            if (pillarData != null) {
                pillarData.setValue(value);
            } else {
                pillarObserver.addPillar(pillar, value);
            }
        }
    }

    public PillarData getPillarData(Pillars pillar) {
        return pillarObserver.getPillarData(pillar);
    }

    public PillarObserver getPillarObserver() {
        return pillarObserver;
    }

    // Add for testing purposes
    public void setGameConfigForTest(GameConfiguration gameConfig) {
        this.gameConfig = gameConfig;
    }

}