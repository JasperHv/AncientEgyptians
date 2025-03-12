package nl.vu.cs.softwaredesign.config;

import com.google.common.io.Resources;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;

import nl.vu.cs.softwaredesign.data.Card;
import nl.vu.cs.softwaredesign.data.Mode;
import nl.vu.cs.softwaredesign.data.Pillar;
import nl.vu.cs.softwaredesign.exception.ConfigurationNotFoundException;

public class ConfigurationLoader {

    private static final ConfigurationLoader instance = new ConfigurationLoader();

    private ConfigurationLoader() {}

    public GameConfiguration loadConfig(String configPath) {
        try {
            String jsonConfig = Resources.toString(Resources.getResource(configPath), StandardCharsets.UTF_8);
            JSONObject object = JSON.parseObject(jsonConfig);
            JSONArray pillarJson = object.getJSONArray("pillars");
            JSONArray modeJson = object.getJSONArray("modes");
            var modes = modeJson.toJavaList(Mode.class);

            for (var mode : modes) {
                mode.setConfiguration(loadModeConfiguration(mode));
            }

            return new GameConfiguration.Builder()
                    .withPillars(pillarJson.toArray(Pillar.class))
                    .withModes(modes.toArray(new Mode[0]))
                    .build();
        } catch (IOException e) {
            throw new ConfigurationNotFoundException();
        }
    }

    private static ModeConfiguration loadModeConfiguration(Mode mode) throws IOException {
        String modeConfigPath = mode.getConfigPath();
        String modeJsonConfig = Resources.toString(Resources.getResource(modeConfigPath), StandardCharsets.UTF_8);
        JSONObject modeConfigObject = JSON.parseObject(modeJsonConfig);
        JSONArray cards = modeConfigObject.getJSONArray("cards");
        return new ModeConfiguration.Builder()
                .withCards(cards.toArray(Card.class))
                .withInitialValue(modeConfigObject.getIntValue("initial_value"))
                .build();
    }

    public static ConfigurationLoader getInstance() {
        return instance;
    }


}
