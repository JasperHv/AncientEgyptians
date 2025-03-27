package nl.vu.cs.ancientegyptiansgame.data.config.gamesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.ancientegyptiansgame.data.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.data.model.Monarch;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillar;
import nl.vu.cs.ancientegyptiansgame.data.model.Mode;
import nl.vu.cs.ancientegyptiansgame.exception.ConfigurationNotFoundException;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map;

public class ModeConfiguration {
    private static ModeConfiguration instance;

    private GameConfiguration gameConfig;
    private Map<Pillar, PillarData> pillarValues;

    private ModeConfiguration(String modeName) {
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
            pillarValues = new EnumMap<>(Pillar.class);
            for (Pillar pillar : Pillar.values()) {
                pillarValues.put(pillar, new PillarData(pillar, 1));
            }
        } catch (Exception e) {
            throw new ConfigurationNotFoundException("Error loading mode config: " + e.getMessage(), e);
        }
    }

    /**
     * Initializes the mode configuration. Call this once when the user selects a mode.
     */
    public static void initialize(String modeName) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName);
        }
    }

    /**
     * Returns the initialized ModeConfiguration instance.
     *
     * @throws IllegalStateException if not yet initialized.
     */
    public static ModeConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModeConfiguration not initialized. Call initialize(modeName) first.");
        }
        return instance;
    }

    /**
     * Updates pillar values for the selected monarch using the initial values from the mode configuration.
     */
    public void updatePillarValues() {
        Monarch selectedMonarch = gameConfig.getSelectedMonarch();
        if (selectedMonarch == null) {
            throw new IllegalStateException("Selected monarch is not set in game configuration.");
        }

        Map<String, Map<String, Integer>> monarchInitialValues = gameConfig.getMonarchInitialValues();
        String monarchName = selectedMonarch.getName();
        Map<String, Integer> initialValues = monarchInitialValues.get(monarchName);

        for (Pillar pillar : Pillar.values()) {
            int value = (initialValues != null)
                    ? initialValues.getOrDefault(pillar.getName().toLowerCase(), 0)
                    : 0;

            if (pillarValues.containsKey(pillar)) {
                pillarValues.get(pillar).setValue(value);
            } else {
                pillarValues.put(pillar, new PillarData(pillar, value));
            }
        }
    }

    /**
     * Gets the PillarData for a specific pillar.
     *
     * @param pillar The pillar to retrieve
     * @return The PillarData for the specified pillar
     */
    public PillarData getPillarData(Pillar pillar) {
        return pillarValues.get(pillar);
    }
}