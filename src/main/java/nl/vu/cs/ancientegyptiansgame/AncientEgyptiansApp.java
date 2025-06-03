package nl.vu.cs.ancientegyptiansgame;

import com.almasb.fxgl.app.GameSettings;
import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.*;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.handlers.EndingHandler;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
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
    private EndingHandler endingHandler;


    public static void main(String[] args) {
        logger.info("Welcome to Software Design!");
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Ancient Egyptians");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new GameSceneFactory());
    }

    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("background.png");
        getGameScene().addUINodes(gameView);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        try {
            if (ModeConfiguration.getInstance() == null) {
                logger.error("ModeConfiguration has not been initialized!");
                return;
            }

            if (gameView == null) {
                gameView = new GameView();
            }

            GameConfiguration gameConfig = GameConfiguration.getInstance();
            ScoreSettings scoreConfig = ConfigurationLoader.getInstance().getScoreSettings();

            ScoreObserver scoreObserver = gameConfig.getScoreObserver();
            YearsInPowerObserver yearsObserver = gameConfig.getYearsInPowerObserver();

            gameConfig.initializeScoreAndYear(scoreConfig);
            endingHandler = new EndingHandler(scoreConfig, null);

            // Register the gameView as a listener for score and years changes
            if (gameView == null) {
                logger.warn("gameView is null before adding as a listener to scoreObserver.");
            } else {
                logger.info("gameView is not null, adding as a listener to scoreObserver.");
                scoreObserver.addListener(gameView);
            }
            yearsObserver.addListener(gameView);

            // Now endingHandler knows about gameView
            endingHandler.setGameView(gameView);

            ModeConfiguration config = ModeConfiguration.getInstance();
            logger.info("Mode configuration initialized successfully.");

            for (Pillars pillars : Pillars.values()) {
                PillarData pillarData = config.getPillarData(pillars);
                int value = pillarData.getValue();

                pillarData.addListener(endingHandler);
                vars.put(pillars.getName().toLowerCase(), value);
            }

            scoreObserver.setScore(gameConfig.getScoreCount());
            yearsObserver.setYearsInPower(gameConfig.getYearCount());
        } catch (IllegalStateException e) {
            logger.warn("ModeConfiguration not initialized yet. Pillars values will be set later.");
        }
    }

}