package ancientegyptiansgame.game;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        HandleScoreUnitTests.class,
        HandleInfluencePillarsUnitTests.class,
})
public class GameplaySimulationTestSuite {
    // no code needed, annotations do the work
}