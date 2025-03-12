package nl.vu.cs.softwaredesign.data;

import com.alibaba.fastjson.annotation.JSONField;
import nl.vu.cs.softwaredesign.config.ModeConfiguration;

public class Mode {

    private String name;
    private String configPath;

    @JSONField(deserialize = false, serialize = false)
    private ModeConfiguration configuration;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigPath() {
        return configPath;
    }

    public void setConfigPath(String cardsPath) {
        this.configPath = cardsPath;
    }

    public ModeConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ModeConfiguration configuration) {
        this.configuration = configuration;
    }

}
