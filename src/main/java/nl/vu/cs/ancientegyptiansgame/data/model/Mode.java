package nl.vu.cs.ancientegyptiansgame.data.model;

public class Mode {
    private String name;
    private String configPath;

    public Mode() {}
    public Mode(String name, String configPath) {
        this.name = name;
        this.configPath = configPath;
    }

    public String getName() { return name; }
    public String getConfigPath() { return configPath; }
}
