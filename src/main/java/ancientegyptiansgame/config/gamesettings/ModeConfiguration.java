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

    private ModeConfiguration(String modeName) {
        this.pillarObserver = new PillarObserver();
        ConfigurationLoader mainLoader = ConfigurationLoader.getInstance();

        Mode selectedMode = mainLoader.getModes().stream()
                .filter(m -> m.getName().equalsIgnoreCase(modeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mode found for: " + modeName));

        loadModeConfig(selectedMode.getConfigPath());
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

    public static void initialize(String modeName) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName);
        }
    }

    public static ModeConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModeConfiguration not initialized. Call initialize(modeName) first.");
        }
        return instance;
    }

    public void updatePillarValues() {
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
}
