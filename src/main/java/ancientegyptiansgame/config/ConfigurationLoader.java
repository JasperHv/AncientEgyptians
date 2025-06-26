package ancientegyptiansgame.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.data.model.Ending;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.exception.ConfigurationNotFoundException;
import ancientegyptiansgame.exception.InvalidPillarConfigurationException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationLoader {
    private static ConfigurationLoader instance;
    private static final String CONFIG_PATH = "/configuration/config.json";

    private Ending goldenAgeEnding;
    private Ending badEnding;
    private Map<String, Ending> pillarEndings = new HashMap<>();
    private ScoreSettings scoreSettings;
    private List<Mode> modes;
    private List<String> monarchs;

    // Public accessor for production
    public static ConfigurationLoader getInstance() {
        if (instance == null) {
            instance = new ConfigurationLoader();
        }
        return instance;
    }

    // Default constructor uses the real file
    protected ConfigurationLoader() {
        this(ConfigurationLoader.class.getResourceAsStream(CONFIG_PATH));
    }

    // New constructor: takes any InputStream
    public ConfigurationLoader(InputStream configStream) {
        if (configStream == null) {
            throw new ConfigurationNotFoundException("Config stream is null");
        }
        loadMainConfig(configStream);
    }

    private void loadMainConfig(InputStream configStream) {
        try (InputStream input = configStream) {
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
            if (root.get("modes") == null) {
                throw new ConfigurationNotFoundException("Missing required 'modes' configuration");
            }
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
                String name = pillarNode.get("name").asText().toUpperCase();
                try {
                    Ending ending = mapper.convertValue(pillarNode.get("ending"), Ending.class);
                    pillarEndings.put(name, ending);
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

    public Ending getPillarEnding(String pillarName) {
        return pillarEndings.get(pillarName.toUpperCase());
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
