package nl.vu.cs.softwaredesign.data.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.Ending;
import nl.vu.cs.softwaredesign.data.model.Mode;
import nl.vu.cs.softwaredesign.exception.ConfigurationNotFoundExecption;

import java.io.InputStream;
import java.util.List;

public class ConfigurationLoader {
    private static final String CONFIG_PATH = "/configuration/config.json";
    private static ConfigurationLoader instance;

    private Ending goldenAgeEnding;
    private Ending badEnding;
    private ScoreSettings scoreSettings;
    private List<Mode> modes;

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
                throw new ConfigurationNotFoundExecption("Config file not found: " + CONFIG_PATH);
            }
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(input);

            goldenAgeEnding = mapper.convertValue(root.get("golden_age ending"), Ending.class);
            badEnding = mapper.convertValue(root.get("bad ending"), Ending.class);
            scoreSettings = mapper.convertValue(root.get("scoreSettings"), ScoreSettings.class);
            modes = mapper.convertValue(root.get("modes"), mapper.getTypeFactory().constructCollectionType(List.class, Mode.class));
            loadPillarEndings(mapper, root);

        } catch (Exception e) {
            throw new RuntimeException("Error loading main config: " + e.getMessage(), e);
        }
    }

    private void loadPillarEndings(ObjectMapper mapper, JsonNode root) {
        JsonNode pillarsNode = root.get("pillars");
        if (pillarsNode != null && pillarsNode.isArray()) {
            for (JsonNode pillarNode : pillarsNode) {
                String name = pillarNode.get("name").asText().toLowerCase();
                Pillar pillarEnum = Pillar.fromName(name);

                if (pillarEnum != null) {
                    Ending ending = mapper.convertValue(pillarNode.get("ending"), Ending.class);
                } else {
                    throw new RuntimeException("Unknown pillar name in configuration: " + name);
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
}
