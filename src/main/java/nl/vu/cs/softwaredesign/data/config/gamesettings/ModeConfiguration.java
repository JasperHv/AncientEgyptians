package nl.vu.cs.softwaredesign.data.config.gamesettings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.Mode;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModeConfiguration {
    private static ModeConfiguration instance;

    private GameConfiguration gameConfig;
    private Map<Pillar, Integer> pillarValues; // Change to use Pillar enum

    // Private constructor: use initialize() to set up the configuration
    private ModeConfiguration(String modeName) {
        ConfigurationLoader mainLoader = ConfigurationLoader.getInstance();

        // Find the desired mode from the global modes list
        Mode selectedMode = mainLoader.getModes().stream()
                .filter(m -> m.getName().equalsIgnoreCase(modeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No mode found for: " + modeName));
        loadModeConfig(selectedMode.getConfigPath());
    }

    private void loadModeConfig(String modeConfigPath) {
        try (InputStream input = getClass().getResourceAsStream("/" + modeConfigPath)) {
            if (input == null) {
                throw new RuntimeException("Mode config file not found: " + modeConfigPath);
            }
            ObjectMapper mapper = new ObjectMapper();
            gameConfig = mapper.readValue(input, GameConfiguration.class);

            pillarValues = new HashMap<>();
            String character = gameConfig.getSelectedCharacter();
            Map<String, Integer> initialValues = gameConfig.getInitialValuesForCharacter(character);

            for (Pillar pillar : Pillar.values()) {
                String key = pillar.getName().toLowerCase();
                int value = initialValues.getOrDefault(key, 0);
                pillarValues.put(pillar, value);
                FXGL.set(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading mode config: " + e.getMessage(), e);
        }
    }

    /**
     * Initializes the mode configuration. This should be called once when the user selects a mode
     * (for example, in the menu view).
     */
    public static void initialize(String modeName) {
        if (instance == null) {
            instance = new ModeConfiguration(modeName);
        }
    }

    /**
     * Returns the initialized ModeConfiguration instance.
     *
     * @throws IllegalStateException if the configuration has not been initialized.
     */
    public static ModeConfiguration getInstance() {
        if (instance == null) {
            throw new IllegalStateException("ModeConfiguration not initialized. Call initialize(modeName) first.");
        }
        return instance;
    }

    // Getters
    public GameConfiguration getGameConfig() {
        return gameConfig;
    }

    public Map<Pillar, Integer> getPillarValues() {
        return pillarValues;
    }

    public void updatePillarValues() {
        String character = gameConfig.getSelectedCharacter();
        Map<String, Integer> initialValues = gameConfig.getInitialValuesForCharacter(character);
        pillarValues.clear();
        for (Pillar pillar : Pillar.values()) {
            String key = pillar.getName().toLowerCase();
            int value = initialValues.getOrDefault(key, 0);
            pillarValues.put(pillar, value);
            FXGL.set(key, value);
        }
    }

    public List<Card> getCards() {
        return gameConfig != null ? gameConfig.getCards() : List.of();
    }
}
