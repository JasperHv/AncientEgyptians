package ancientegyptiansgame;

import ancientegyptiansgame.config.ConfigTestSuite;
import ancientegyptiansgame.game.GameplaySimulationTestSuite;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ConfigTestSuite.class,
        GameplaySimulationTestSuite.class,
})
public class AllTestsSuite {
    // no code needed, annotations do the work
}