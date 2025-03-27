package nl.vu.cs.softwaredesign;

import com.almasb.fxgl.app.GameSettings;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.*;
import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.pillars.PillarData;
import nl.vu.cs.softwaredesign.ui.scenes.GameSceneFactory;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.almasb.fxgl.app.GameApplication;
import java.util.Map;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameScene;


public class AncientEgyptiansApp extends GameApplication {
    private static final Logger logger = LoggerFactory.getLogger(AncientEgyptiansApp.class);


    public static void main (String[] args){
        logger.info("Welcome to Software Design!");
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setTitle("Ancient Egyptians");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new GameSceneFactory());}

    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("background.png");
        getGameScene().addUINodes(
                GameView.getInstance()
        );
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        try {
            if (ModeConfiguration.getInstance() == null) {
                logger.error("ModeConfiguration has not been initialized!");
                return;
            }

            GameConfiguration gameConfig = ConfigurationLoader.getInstance().getGameConfiguration();
            ScoreSettings scoreConfig = ConfigurationLoader.getInstance().getScoreSettings();

            gameConfig.initializeScoreAndYear(scoreConfig);

            ModeConfiguration config = ModeConfiguration.getInstance();
            logger.info("Game configuration initialized successfully.");
            for (Pillar pillar : Pillar.values()) {
                System.out.println("Processing pillar: {}" + pillar.getName());
                PillarData pillarData = config.getPillarData(pillar);

                if (pillarData == null) {
                    System.out.println("PillarData for {} is null. Setting default value of 1." + pillar.getName());
                    vars.put(pillar.getName().toLowerCase(), 1);
                } else {
                    int value = pillarData.getValue();
                    System.out.println("PillarData for {} retrieved with value: {}" + pillar.getName() + value);
                    vars.put(pillar.getName().toLowerCase(), value);
                }
            }
        } catch (IllegalStateException e) {
            logger.warn("ModeConfiguration not initialized yet. Pillar values will be set later.");
        }
    }

}