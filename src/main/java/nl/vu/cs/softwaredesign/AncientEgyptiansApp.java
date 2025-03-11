package nl.vu.cs.softwaredesign;

import com.almasb.fxgl.app.GameSettings;
import nl.vu.cs.softwaredesign.ui.scenes.GameSceneFactory;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.almasb.fxgl.app.GameApplication;
import java.util.Map;
import nl.vu.cs.softwaredesign.data.config.ConfigManager;

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
        settings.setVersion("0.1");
        settings.setMainMenuEnabled(true);
        settings.setSceneFactory(new GameSceneFactory());}

    @Override
    protected void initUI() {
        getGameScene().setBackgroundRepeat("background.png");
        getGameScene().addUINodes(
                new GameView()
        );
    }
    @Override
    protected void initGameVars(Map<String, Object> vars) {
        try {
            ConfigManager config = ConfigManager.getInstance(""); // Should be initialized after menu selection
            Map<String, Integer> pillarValues = config.getPillarValues();

            for (Map.Entry<String, Integer> entry : pillarValues.entrySet()) {
                vars.put(entry.getKey(), entry.getValue());
            }
        } catch (IllegalStateException e) {
            logger.error("ConfigManager not initialized before initGameVars. Game mode must be selected first!");
            throw e;
        }

        vars.put("yearCount", 0);
        vars.put("scoreCount", 0);
        vars.put("threshold", 60);
    }
}
