package nl.vu.cs.ancientegyptiansgame.data.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.vu.cs.ancientegyptiansgame.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.data.model.Ending;
import nl.vu.cs.ancientegyptiansgame.data.model.Mode;
import nl.vu.cs.ancientegyptiansgame.exception.ConfigurationNotFoundException;
import nl.vu.cs.ancientegyptiansgame.exception.InvalidPillarConfigurationException;

import java.io.InputStream;
import java.util.List;

public class ConfigurationLoader {
    private static final String CONFIG_PATH = "/configuration/config.json";
    private static ConfigurationLoader instance;

    private Ending goldenAgeEnding;
    private Ending badEnding;
    private ScoreSettings scoreSettings;
    private List<Mode> modes;
    private List<String> monarchs;

    private ConfigurationLoader() {
        loadMainConfig();
    }

    public static ConfigurationLoader getInstance() {
        if (instance == null) {
            instance = new ConfigurationLoader();
        }
        return instance;
    }

    private void loadMainConfig() {
        try (InputStream input = getClass().getResourceAsStream(CONFIG_PATH)) {
            if (input == null) {
                throw new ConfigurationNotFoundException("Config file not found: " + CONFIG_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(input);

            goldenAgeEnding = mapper.convertValue(root.get("goldenAgeEnding"), Ending.class);
            badEnding = mapper.convertValue(root.get("bad ending"), Ending.class);
            scoreSettings = mapper.convertValue(root.get("scoreSettings"), ScoreSettings.class);
            modes = mapper.convertValue(
                    root.get("modes"),
                    mapper.getTypeFactory().constructCollectionType(List.class, Mode.class)
            );
            monarchs = mapper.convertValue(
                    root.get("monarchs"),
                    mapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );
            loadPillarEndings(mapper, root);

        } catch (Exception e) {
            throw new ConfigurationNotFoundException("Error loading main config: " + e.getMessage(), e);
        }
    }

    private void loadPillarEndings(ObjectMapper mapper, JsonNode root) {
        JsonNode pillarsNode = root.get("pillars");
        if (pillarsNode != null && pillarsNode.isArray()) {
            for (JsonNode pillarNode : pillarsNode) {
                String name = pillarNode.get("name").asText().toLowerCase();
                try {
                    mapper.convertValue(pillarNode.get("ending"), Ending.class);
                } catch (IllegalArgumentException e) {
                    throw new InvalidPillarConfigurationException("Unknown pillar name in configuration: " + name, e);
                }
            }
        }
    }

    public Ending getGoldenAgeEnding() {
        return goldenAgeEnding;
    }

    public Ending getBadEnding() {
        return badEnding;
    }

    public ScoreSettings getScoreSettings() {
        return scoreSettings;
    }

    public List<Mode> getModes() {
        return modes;
    }

    public List<String> getMonarchs() {
        return monarchs;
    }
}
