package nl.vu.cs.ancientegyptiansgame;

import com.almasb.fxgl.app.GameSettings;
import nl.vu.cs.ancientegyptiansgame.config.ConfigurationLoader;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.*;
import nl.vu.cs.ancientegyptiansgame.config.scoresettings.ScoreSettings;
import nl.vu.cs.ancientegyptiansgame.handlers.EndingHandler;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;
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
        gameView = new GameView();
        getGameScene().setBackgroundRepeat("background.png");
        getGameScene().addUINodes(gameView);
        endingHandler.setGameView(gameView);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        try {
            if (ModeConfiguration.getInstance() == null) {
                logger.error("ModeConfiguration has not been initialized!");
                return;
            }

            GameConfiguration gameConfig = GameConfiguration.getInstance();
            ScoreSettings scoreConfig = ConfigurationLoader.getInstance().getScoreSettings();

            gameConfig.initializeScoreAndYear(scoreConfig);
            endingHandler = new EndingHandler(scoreConfig, null);
            ModeConfiguration config = ModeConfiguration.getInstance();
            logger.info("Mode configuration initialized successfully.");

            for (Pillars pillars : Pillars.values()) {
                PillarData pillarData = config.getPillarData(pillars);
                int value = pillarData.getValue();

                pillarData.addListener(endingHandler);
                vars.put(pillars.getName().toLowerCase(), value);
            }
        } catch (IllegalStateException e) {
            logger.warn("ModeConfiguration not initialized yet. Pillars values will be set later.");
        }
    }

}