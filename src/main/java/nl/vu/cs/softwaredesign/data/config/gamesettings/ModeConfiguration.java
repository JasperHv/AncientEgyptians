package nl.vu.cs.softwaredesign.data.config.gamesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.handlers.PillarValueInitializer;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Monarch;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.Mode;
import nl.vu.cs.softwaredesign.exception.ConfigurationNotFoundException;

import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ModeConfiguration {
    private static ModeConfiguration instance;

    private GameConfiguration gameConfig;
    private Map<Pillar, Integer> pillarValues;

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

    public Map<Pillar, Integer> getPillarValues() {
        return pillarValues;
    }

    /**
     * Updates pillar values for the selected monarch using the initial values from the mode configuration.
     */
   public void updatePillarValues() {
        Monarch selectedMonarch = gameConfig.getSelectedMonarch();
        if (selectedMonarch == null) {
            throw new IllegalStateException("Selected monarch is not set in game configuration.");
        }

        pillarValues = PillarValueInitializer.initializePillarValues(
                selectedMonarch,
                gameConfig.getMonarchInitialValues()
        );
    }


    public List<Card> getCards() {
        return gameConfig != null ? gameConfig.getCards() : List.of();
    }
}
