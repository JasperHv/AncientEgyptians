package ancientegyptiansgame;

import com.almasb.fxgl.app.GameSettings;
import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.*;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;
import ancientegyptiansgame.handlers.EndingHandler;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.data.model.PillarData;
import ancientegyptiansgame.observer.ScoreObserver;
import ancientegyptiansgame.observer.YearsInPowerObserver;
import ancientegyptiansgame.ui.scenes.GameSceneFactory;
import ancientegyptiansgame.ui.views.GameView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.almasb.fxgl.app.GameApplication;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class AncientEgyptiansApp extends GameApplication {
    private static final Logger logger = LoggerFactory.getLogger(AncientEgyptiansApp.class);
    private GameView gameView;

    public static void main(String[] args) {
        logger.info("Welcome to AncientEgyptiansGame!");
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