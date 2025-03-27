package nl.vu.cs.softwaredesign.data.config.gamesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.handlers.PillarValueInitializer;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Monarch;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.Mode;
import nl.vu.cs.softwaredesign.exception.ConfigurationNotFoundException;
import nl.vu.cs.softwaredesign.pillars.PillarData;
import nl.vu.cs.softwaredesign.pillars.PillarListener;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
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
            pillarValues = new EnumMap<>(Pillar.class);
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

    public GameConfiguration getGameConfig() {
        return gameConfig;
    }

    /**
     * Returns a map of pillar values, converting PillarData to their integer values.
     */
    public Map<Pillar, Integer> getPillarValues() {
        Map<Pillar, Integer> values = new EnumMap<>(Pillar.class);
        pillarValues.forEach((pillar, pillarData) ->
                values.put(pillar, pillarData.getValue())
        );
        return values;
    }

    /**
     * Updates pillar values for the selected monarch using the initial values from the mode configuration.
     */
    public void updatePillarValues() {
        Monarch selectedMonarch = gameConfig.getSelectedMonarch();
        if (selectedMonarch == null) {
            throw new IllegalStateException("Selected monarch is not set in game configuration.");
        }

        pillarValues = new EnumMap<>(Pillar.class);
        Map<String, Map<String, Integer>> monarchInitialValues = gameConfig.getMonarchInitialValues();
        String monarchName = selectedMonarch.getName();
        Map<String, Integer> initialValues = monarchInitialValues.get(monarchName);

        for (Pillar pillar : Pillar.values()) {
            int value = (initialValues != null)
                    ? initialValues.getOrDefault(pillar.getName().toLowerCase(), 0)
                    : 0;

            pillarValues.put(pillar, new PillarData(value));
        }
    }


    public List<Card> getCards() {
        return gameConfig != null ? gameConfig.getCards() : List.of();
    }

    /**
     * Adds a listener to a specific pillar.
     *
     * @param pillar The pillar to add a listener to
     * @param listener The listener to be added
     */
    public void addPillarListener(Pillar pillar, PillarListener listener) {
        PillarData pillarData = pillarValues.get(pillar);
        if (pillarData != null) {
            pillarData.addListener(listener);
        }
    }

    /**
     * Gets the PillarData for a specific pillar.
     *
     * @param pillar The pillar to retrieve
     * @return The PillarData for the specified pillar
     */
    public PillarData getPillarData(Pillar pillar) {
        return pillarValues.getOrDefault(pillar, new PillarData(0)); // Default to 0 if missing
    }


    /**
     * Updates the value of a specific pillar.
     *
     * @param pillar The pillar to update
     * @param value The new value for the pillar
     */
    public void updatePillarValue(Pillar pillar, int value) {
        PillarData pillarData = pillarValues.get(pillar);
        if (pillarData != null) {
            pillarData.setValue(value);
        }
    }
}