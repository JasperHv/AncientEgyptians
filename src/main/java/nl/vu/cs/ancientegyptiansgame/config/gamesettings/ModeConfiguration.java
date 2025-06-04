package nl.vu.cs.ancientegyptiansgame.config.gamesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.data.model.Monarch;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.data.model.Mode;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
import nl.vu.cs.ancientegyptiansgame.exception.ConfigurationNotFoundException;
import nl.vu.cs.ancientegyptiansgame.observer.PillarObserver;

import java.io.InputStream;
import java.util.Map;

public class ModeConfiguration {
    private static ModeConfiguration instance;

    private GameConfiguration gameConfig;
    private final PillarObserver pillarObserver;

    /**
     * Constructs a ModeConfiguration for the specified game mode.
     *
     * Initializes the pillar observer and loads the game configuration for the given mode name.
     * Throws a RuntimeException if the mode name does not match any available mode.
     *
     * @param modeName the name of the game mode to load
     */
    private ModeConfiguration(String modeName) {
        this.pillarObserver = new PillarObserver();
        ConfigurationLoader mainLoader = ConfigurationLoader.getInstance();

        Mode selectedMode = mainLoader.getModes().stream()
                .filter(m -> m.getName().equalsIgnoreCase(modeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mode found for: " + modeName));

        loadModeConfig(selectedMode.getConfigPath());
    }

    /**
     * Loads the game mode configuration from the specified resource path and initializes default pillar values.
     *
     * @param modeConfigPath the resource path to the mode configuration JSON file
     * @throws ConfigurationNotFoundException if the configuration file is missing or cannot be loaded
     */
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

    /****
     * Initializes the singleton ModeConfiguration instance for the specified game mode.
     *
     * If the instance has already been initialized, this method does nothing.
     *
     * @param modeName the name of the game mode to load configuration for
     */
    public static void initialize(String modeName) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName);
        }
    }

    /**
     * Returns the singleton instance of ModeConfiguration.
     *
     * @return the ModeConfiguration instance
     * @throws IllegalStateException if initialize(String modeName) has not been called prior to this method
     */
    public static ModeConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModeConfiguration not initialized. Call initialize(modeName) first.");
        }
        return instance;
    }

    /**
     * Updates all pillar values in the observer based on the selected monarch's initial values.
     *
     * @throws IllegalStateException if no monarch is selected in the game configuration.
     */
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

    /**
     * Retrieves the data associated with the specified pillar.
     *
     * @param pillar the pillar whose data is to be retrieved
     * @return the PillarData for the given pillar, or null if the pillar does not exist
     */
    public PillarData getPillarData(Pillars pillar) {
        return pillarObserver.getPillarData(pillar);
    }

    /**
     * Returns the PillarObserver instance managing pillar data for the current game mode.
     *
     * @return the PillarObserver associated with this configuration
     */
    public PillarObserver getPillarObserver() {
        return pillarObserver;
    }
}
