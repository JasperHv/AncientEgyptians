package nl.vu.cs.softwaredesign.data.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.PillarEnding;
import com.almasb.fxgl.dsl.FXGL;
import nl.vu.cs.softwaredesign.exception.ConfigurationNotFoundExecption;

import java.io.InputStream;
import java.util.*;

public class ConfigManager {
    private static final String CONFIG_PATH = "/configuration/config.json";
    private static ConfigManager instance;

    private GameConfig gameConfig;
    private List<Pillar> pillars;
    private Map<String, Integer> pillarValues;
    private PillarEnding goldenAgeEnding;

    private ConfigManager(String mode) {
        loadMainConfig(mode);
    }

    public static ConfigManager getInstance(String mode) {
        if (instance == null) {
            instance = new ConfigManager(mode);
        }
        return instance;
    }

    private void loadMainConfig(String mode) {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_PATH)) {
            if (input == null) {
                throw new ConfigurationNotFoundExecption("Config file not found: " + CONFIG_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(input);

            pillars = mapper.convertValue(root.get("pillars"), new TypeReference<List<Pillar>>() {});
            goldenAgeEnding = mapper.convertValue(root.get("golden_age ending"), PillarEnding.class);

            String modeConfigPath = null;
            for (JsonNode modeNode : root.get("modes")) {
                if (modeNode.get("name").asText().equalsIgnoreCase(mode)) {
                    modeConfigPath = modeNode.get("configPath").asText();
                    break;
                }
            }
            if (modeConfigPath == null) {
                throw new RuntimeException("No mode found for: " + mode);
            }

            loadModeConfig(modeConfigPath);
        } catch (Exception e) {
            throw new RuntimeException("Error loading main config: " + e.getMessage(), e);
        }
    }

    private void loadModeConfig(String modeConfigPath) {
        try (InputStream input = getClass().getResourceAsStream("/" + modeConfigPath)) {
            if (input == null) {
                throw new RuntimeException("Mode config file not found: " + modeConfigPath);
            }
            ObjectMapper mapper = new ObjectMapper();
            gameConfig = mapper.readValue(input, GameConfig.class);

            pillarValues = new HashMap<>();
            String character = gameConfig.getSelectedCharacter();
            Map<String, Integer> initialValues = gameConfig.getInitialValuesForCharacter(character);
            for (Pillar pillar : pillars) {
                String key = pillar.getName().toLowerCase();
                int value = initialValues.getOrDefault(pillar.getName(), 0);
                pillarValues.put(key, value);
                FXGL.set(key, value);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading mode config: " + e.getMessage(), e);
        }
    }

    public GameConfig getGameConfig() {
        return gameConfig;
    }

    public List<Pillar> getPillars() {
        return pillars;
    }

    public Map<String, Integer> getPillarValues() {
        return pillarValues;
    }

    public List<Card> getCards() {
        return gameConfig != null ? gameConfig.getCards() : Collections.emptyList();
    }

    public PillarEnding getGoldenAgeEnding() {
        return goldenAgeEnding;
    }

    public void updatePillarValues() {
        String character = gameConfig.getSelectedCharacter();
        Map<String, Integer> initialValues = gameConfig.getInitialValuesForCharacter(character);
        pillarValues.clear();
        pillarValues.putAll(initialValues);
        for (Map.Entry<String, Integer> entry : initialValues.entrySet()) {
            FXGL.set(entry.getKey().toLowerCase(), entry.getValue());
        }
    }
}
