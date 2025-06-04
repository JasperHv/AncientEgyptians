package nl.vu.cs.ancientegyptiansgame;

import com.almasb.fxgl.app.GameSettings;
import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.*;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.handlers.EndingHandler;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
import nl.vu.cs.ancientegyptiansgame.listeners.EndingListener;
import nl.vu.cs.ancientegyptiansgame.observer.ScoreObserver;
import nl.vu.cs.ancientegyptiansgame.observer.YearsInPowerObserver;
import nl.vu.cs.ancientegyptiansgame.ui.scenes.GameSceneFactory;
import nl.vu.cs.ancientegyptiansgame.ui.views.GameView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.almasb.fxgl.app.GameApplication;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class AncientEgyptiansApp extends GameApplication {
    private static final Logger logger = LoggerFactory.getLogger(AncientEgyptiansApp.class);
    private GameView gameView;

    public static void main(String[] args) {
        logger.info("Welcome to Software Design!");
        launch(args);
    }

    /**
     * Configures the initial game window settings, including size, title, main menu, and scene factory.
     *
     * @param settings the game settings to initialize
     */
    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Ancient Egyptians");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new GameSceneFactory());
    }

    /**
     * Initializes core game variables and components, including the game view, configuration, observers, and initial values for game pillars, score, and years in power.
     *
     * If the required mode configuration is not initialized, logs an error and skips further setup. Sets up listeners for score, years in power, and pillar data to enable game state updates and ending handling.
     *
     * @param vars a map to store initial values for each game pillar, keyed by pillar name in lowercase
     */
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        try {
            if (gameView == null) {
                gameView = new GameView();
                logger.info("GameView initialized in initGameVars()");
            }

            if (ModeConfiguration.getInstance() == null) {
                logger.error("ModeConfiguration has not been initialized!");
                return;
            }

            GameConfiguration gameConfig = GameConfiguration.getInstance();
            ScoreSettings scoreConfig = ConfigurationLoader.getInstance().getScoreSettings();
            ScoreObserver scoreObserver = gameConfig.getScoreObserver();
            YearsInPowerObserver yearsObserver = gameConfig.getYearsInPowerObserver();

            EndingHandler endingHandler = new EndingHandler(scoreConfig, gameView);
            scoreObserver.addListener(gameView);
            yearsObserver.addListener(gameView);

            gameConfig.initializeScoreAndYear(scoreConfig);

            ModeConfiguration config = ModeConfiguration.getInstance();

            for (Pillars pillars : Pillars.values()) {
                PillarData pillarData = config.getPillarData(pillars);
                int value = pillarData.getValue();

                pillarData.addListener(endingHandler);
                vars.put(pillars.getName().toLowerCase(), value);
            }

            scoreObserver.setScore(gameConfig.getInitialScoreCount());
            yearsObserver.setYearsInPower(gameConfig.getInitialYearCount());
        } catch (IllegalStateException e) {
            logger.warn("ModeConfiguration not initialized yet. Pillars values will be set later.");
        }
    }

    /**
     * Sets the game scene background and adds the game view to the UI if initialized.
     *
     * Logs an error if the game view is unexpectedly null.
     */
    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("background.png");
        if (gameView != null) {
            getGameScene().addUINodes(gameView);
            logger.info("GameView added to UI");
        } else {
            logger.error("GameView is null in initUI() - this should not happen!");
        }
    }
}