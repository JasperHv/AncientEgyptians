package ancientegyptiansgame.game;

import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import ancientegyptiansgame.data.enums.SwipeSide;
import ancientegyptiansgame.data.model.Influence;
import ancientegyptiansgame.data.model.Mode;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;
import ancientegyptiansgame.handlers.HandleInfluencePillars;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class HandleInfluencePillarsUnitTests {

    private void initializeWithTestMode() {
        String modeName = "testMode";
        String jsonPath = "config/modes/test1.json";

        Mode testMode = new Mode(modeName, jsonPath);
        ConfigurationLoader configLoader = new ConfigurationLoader() {
            @Override
            public List<Mode> getModes() {
                return List.of(testMode);
            }
        };

        Function<String, InputStream> resourceLoader = path -> getClass().getClassLoader()
                .getResourceAsStream(path.startsWith("/") ? path.substring(1) : path);

        assertNotNull(resourceLoader.apply(jsonPath), "test1.json should be present in test resources");
        ModeConfiguration.initialize(modeName, configLoader, resourceLoader);
    }

    @BeforeEach
    void setUp() {
        GameConfiguration.setInstance(null);
        ModeConfiguration.reset();

        InputStream is = getClass().getClassLoader().getResourceAsStream("config/configCorrect.json");
        assertNotNull(is, "Config resource 'config/configCorrect.json' should be found in test resources");

        GameConfiguration gameConfig = GameConfiguration.getInstance();
        GameConfiguration.setInstance(gameConfig);

        initializeWithTestMode();
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();

        var modeMonarchValues = modeConfig.getAllMonarchInitialValues();
        modeMonarchValues.forEach((monarch, values) -> gameConfig.getMonarchInitialValues().put(monarch, values));

        gameConfig.setSelectedMonarch("TestMonarch1");
        modeConfig.setGameConfigForTest(gameConfig);
        modeConfig.updatePillarValues();
    }

    @Test
    void testApplyPositiveInfluenceRightSwipe() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        PillarData priests = config.getPillarData(Pillars.PRIESTS);
        int originalValue = priests.getValue();

        handler.applyInfluence(SwipeSide.RIGHT, List.of(new Influence("PRIESTS", 10)));

        assertEquals(Math.min(originalValue + 10, 100), priests.getValue());
    }

    @Test
    void testApplyNegativeInfluenceRightSwipe() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        PillarData priests = config.getPillarData(Pillars.PRIESTS);
        int originalValue = priests.getValue();

        handler.applyInfluence(SwipeSide.RIGHT, List.of(new Influence("PRIESTS", -10)));

        assertEquals(Math.min(originalValue - 10, 100), priests.getValue());
    }


    @Test
    void testApplyPositiveInfluenceLeftSwipe() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        PillarData military = config.getPillarData(Pillars.MILITARY);
        int originalValue = military.getValue();

        handler.applyInfluence(SwipeSide.LEFT, List.of(new Influence("MILITARY", 15)));

        assertEquals(Math.max(originalValue - 15, 0), military.getValue());
    }

    void testApplyNegativeInfluenceLeftSwipe() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        PillarData military = config.getPillarData(Pillars.MILITARY);
        int originalValue = military.getValue();

        handler.applyInfluence(SwipeSide.LEFT, List.of(new Influence("MILITARY", -15)));

        assertEquals(Math.max(originalValue + 15, 0), military.getValue());
    }

    @Test
    void testApplyInfluenceCapsAtZeroAndHundred() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        PillarData farmers = config.getPillarData(Pillars.FARMERS);
        farmers.setValue(95);
        handler.applyInfluence(SwipeSide.RIGHT, List.of(new Influence("FARMERS", 10)));
        assertEquals(100, farmers.getValue());

        farmers.setValue(5);
        handler.applyInfluence(SwipeSide.LEFT, List.of(new Influence("FARMERS", 10)));
        assertEquals(0, farmers.getValue());
    }

    @Test
    void testApplyMultipleInfluences() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        int originalPriests = config.getPillarData(Pillars.PRIESTS).getValue();
        int originalNobles = config.getPillarData(Pillars.NOBLES).getValue();

        List<Influence> influences = List.of(
                new Influence("PRIESTS", 5),
                new Influence("NOBLES", 7)
        );

        handler.applyInfluence(SwipeSide.RIGHT, influences);

        assertEquals(Math.min(originalPriests + 5, 100), config.getPillarData(Pillars.PRIESTS).getValue());
        assertEquals(Math.min(originalNobles + 7, 100), config.getPillarData(Pillars.NOBLES).getValue());
    }

    @Test
    void testApplyInfluenceWithNullOrEmptyList() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        ModeConfiguration config = ModeConfiguration.getInstance();

        int originalValue = config.getPillarData(Pillars.FARMERS).getValue();

        handler.applyInfluence(SwipeSide.RIGHT, null);
        assertEquals(originalValue, config.getPillarData(Pillars.FARMERS).getValue());

        handler.applyInfluence(SwipeSide.LEFT, List.of());
        assertEquals(originalValue, config.getPillarData(Pillars.FARMERS).getValue());
    }

    @Test
    void testZeroInfluenceValueDoesNotChangePillar() {
        HandleInfluencePillars handler = new HandleInfluencePillars();
        PillarData nobles = ModeConfiguration.getInstance().getPillarData(Pillars.NOBLES);
        int original = nobles.getValue();

        handler.applyInfluence(SwipeSide.RIGHT, List.of(new Influence("NOBLES", 0)));

        assertEquals(original, nobles.getValue());
    }

    @Test
    void testUnknownPillarNameThrowsException() {
        HandleInfluencePillars handler = new HandleInfluencePillars();

        List<Influence> invalidInfluences = List.of(new Influence("UNKNOWN", 10));

        assertThrows(IllegalArgumentException.class, () ->
                handler.applyInfluence(SwipeSide.RIGHT, invalidInfluences)
        );
    }

}
