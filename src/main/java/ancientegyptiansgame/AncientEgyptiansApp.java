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

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.almasb.fxgl.app.GameApplication;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;

public class AncientEgyptiansApp extends GameApplication {
    private static final Logger logger = LoggerFactory.getLogger(AncientEgyptiansApp.class);
    private GameView gameView;
    private boolean isLoadingSavedGame = false;

    public void setLoadingSavedGame(boolean loading) {
        this.isLoadingSavedGame = loading;
    }
    public boolean getLoadingSavedGame() {
        return isLoadingSavedGame;
    }

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

            if (!isLoadingSavedGame) {
                gameConfig.initializeScoreAndYear(scoreConfig);
            }

            ModeConfiguration config = ModeConfiguration.getInstance();

            for (Pillars pillars : Pillars.values()) {
                PillarData pillarData = config.getPillarData(pillars);
                int value = pillarData.getValue();

                pillarData.addListener(endingHandler);
                vars.put(pillars.getName().toLowerCase(), value);
            }

            if (!isLoadingSavedGame) {
                scoreObserver.setScore(gameConfig.getInitialScoreCount());
                yearsObserver.setYearsInPower(gameConfig.getInitialYearCount());
            }

            // Reset flag after initialization
            isLoadingSavedGame = false;
        } catch (IllegalStateException e) {
            logger.warn("ModeConfiguration not initialized yet. Pillars values will be set later.");
        }
    }

    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("background.png");
        //CommandLogger.clearLogFile(); -- This is commented out to avoid clearing the log file on every run for development purposes

        if (gameView != null) {
            getGameScene().addUINodes(gameView);
            logger.info("GameView added to UI");

            // Add Undo button globally in the top-right
            Button undoButton = new Button("Undo");
            undoButton.setOnAction(e -> {
                gameView.getGameFlowController().redoLastAction();
                gameView.undoCard();
            });
            undoButton.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-padding: 10px; -fx-border-radius: 5px; -fx-border-color: white");
            undoButton.setPrefWidth(100);

            StackPane.setAlignment(undoButton, Pos.TOP_RIGHT);
            undoButton.setTranslateX(1175);
            undoButton.setTranslateY(10);

            FXGL.getGameScene().addUINode(undoButton);
            logger.info("Redo button added to global UI");

        } else {
            logger.error("GameView is null in initUI() - this should not happen!");
        }
    }

    @Override
    protected void onPreInit() {
        FXGL.getPrimaryStage().setOnCloseRequest(event -> GameConfiguration.saveGame());
    }
}