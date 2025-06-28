package ancientegyptiansgame.config.gamesettings;

public class ModeConfigurationTestAccess {
    public static void setGameConfig(ModeConfiguration instance, GameConfiguration config) {
        instance.setGameConfigForTest(config); // package-private, works here
    }
}