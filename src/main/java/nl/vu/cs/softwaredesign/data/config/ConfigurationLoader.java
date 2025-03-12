package nl.vu.cs.softwaredesign.data.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.PillarEnding;
import nl.vu.cs.softwaredesign.data.model.Mode;
import nl.vu.cs.softwaredesign.exception.ConfigurationNotFoundExecption;

import java.io.InputStream;
import java.util.List;

public class ConfigurationLoader {
    private static final String CONFIG_PATH = "/configuration/config.json";
    private static ConfigurationLoader instance;

    private List<Pillar> pillars;
    private PillarEnding goldenAgeEnding;
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

            // Load global configuration
            pillars = mapper.convertValue(root.get("pillars"), new TypeReference<List<Pillar>>() {});
            goldenAgeEnding = mapper.convertValue(root.get("golden_age ending"), PillarEnding.class);
            scoreSettings = mapper.convertValue(root.get("scoreSettings"), ScoreSettings.class);
            modes = mapper.convertValue(root.get("modes"), new TypeReference<List<Mode>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Error loading main config: " + e.getMessage(), e);
        }
    }


    public List<Pillar> getPillars() {
        return pillars;
    }

    public PillarEnding getGoldenAgeEnding() {
        return goldenAgeEnding;
    }

    public ScoreSettings getScoreSettings() {
        return scoreSettings;
    }

    public List<Mode> getModes() {
        return modes;
    }
}
