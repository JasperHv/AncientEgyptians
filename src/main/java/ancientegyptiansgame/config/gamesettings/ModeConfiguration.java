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

public class ModeConfiguration {
    private static ModeConfiguration instance;

    private GameConfiguration gameConfig;
    private final PillarObserver pillarObserver;
    private final Mode currentMode;

    private ModeConfiguration(String modeName) {
        this.pillarObserver = new PillarObserver();
        ConfigurationLoader mainLoader = ConfigurationLoader.getInstance();

        Mode selectedMode = mainLoader.getModes().stream()
                .filter(m -> m.getName().equalsIgnoreCase(modeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mode found for: " + modeName));

        this.currentMode = selectedMode;
        loadModeConfig(selectedMode.getConfigPath());
    }

    // Test-only constructor for compatibility with existing tests
    private ModeConfiguration(String modeName, ConfigurationLoader configLoader, java.util.function.Function<String, InputStream> resourceLoader) {
        this.pillarObserver = new PillarObserver();

        Mode selectedMode = configLoader.getModes().stream()
                .filter(m -> m.getName().equalsIgnoreCase(modeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mode found for: " + modeName));

        this.currentMode = selectedMode;
        loadModeConfigWithLoader(selectedMode.getConfigPath(), resourceLoader);
    }

    private void loadModeConfig(String modeConfigPath) {
        try (InputStream input = getClass().getResourceAsStream("/" + modeConfigPath)) {
            if (input == null) {
                throw new ConfigurationNotFoundException("Mode config file not found: " + modeConfigPath);
            }

            ObjectMapper mapper = new ObjectMapper();
            gameConfig = mapper.readValue(input, GameConfiguration.class);
            GameConfiguration.setInstance(gameConfig);

            // Initialize pillars with default value (e.g., 1)
            for (Pillars pillar : Pillars.values()) {
                pillarObserver.addPillar(pillar, 1);
            }

        } catch (Exception e) {
            throw new ConfigurationNotFoundException("Error loading mode config: " + e.getMessage(), e);
        }
    }

    // Test-only method for loading config with custom resource loader
    private void loadModeConfigWithLoader(String modeConfigPath, java.util.function.Function<String, InputStream> resourceLoader) {
        try (InputStream input = resourceLoader.apply(modeConfigPath)) {
            if (input == null) {
                throw new ConfigurationNotFoundException("Mode config file not found: " + modeConfigPath);
            }

            ObjectMapper mapper = new ObjectMapper();
            gameConfig = mapper.readValue(input, GameConfiguration.class);
            GameConfiguration.setInstance(gameConfig);

            // Initialize pillars with default value (e.g., 1)
            for (Pillars pillar : Pillars.values()) {
                pillarObserver.addPillar(pillar, 1);
            }

        } catch (Exception e) {
            throw new ConfigurationNotFoundException("Error loading mode config: " + e.getMessage(), e);
        }
    }

    public static void initialize(String modeName) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName);
        }
    }

    // Test-only method for compatibility with existing tests
    public static void initialize(String modeName, ConfigurationLoader configLoader, java.util.function.Function<String, InputStream> resourceLoader) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName, configLoader, resourceLoader);
        }
    }

    public static ModeConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModeConfiguration not initialized. Call initialize(modeName) first.");
        }
        return instance;
    }

    public void updatePillarValues() {
        if (gameConfig == null) {
            throw new IllegalStateException("Game configuration not loaded");
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

    public void updatePillarValuesWithLoadedGame(Map<String, Integer> savedPillarValues) {
        if (savedPillarValues == null || savedPillarValues.isEmpty()) {
            // If no saved values, fall back to monarch initial values
            updatePillarValues();
            return;
        }

        // Load the saved pillar values
        for (Pillars pillar : Pillars.values()) {
            String pillarName = pillar.getName().toLowerCase();
            int savedValue = savedPillarValues.getOrDefault(pillarName, 0);

            PillarData pillarData = pillarObserver.getPillarData(pillar);
            if (pillarData != null) {
                pillarData.setValue(savedValue);
            } else {
                pillarObserver.addPillar(pillar, savedValue);
            }
        }
    }

    public static boolean isInitialized() {
        return instance != null;
    }

    public PillarData getPillarData(Pillars pillar) {
        return pillarObserver.getPillarData(pillar);
    }

    public PillarObserver getPillarObserver() {
        return pillarObserver;
    }

    public Mode getCurrentMode() {
        return currentMode;
    }

    // Test-only methods for compatibility with existing tests
    public static void reset() {
        instance = null;
    }

    public Map<String, Map<String, Integer>> getAllMonarchInitialValues() {
        if (gameConfig == null) {
            throw new IllegalStateException("Game configuration not loaded");
        }
        return gameConfig.getMonarchInitialValues();
    }

    public void setGameConfigForTest(GameConfiguration config) {
        this.gameConfig = config;
        if (config != null) {
            GameConfiguration.setInstance(config);
        }
    }
}