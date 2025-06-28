package ancientegyptiansgame.config;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;


import ancientegyptiansgame.config.gamesettings.ModeConfigurationUnitTests;
import ancientegyptiansgame.config.gamesettings.GameConfigurationUnitTests;

@Suite
@SelectClasses({
        ConfigIntegrationTests.class,
        ConfigurationLoaderUnitTests.class,
        ModeConfigurationUnitTests.class,
        GameConfigurationUnitTests.class
})
public class ConfigTestSuite {
    // no code needed, annotations do the work
}