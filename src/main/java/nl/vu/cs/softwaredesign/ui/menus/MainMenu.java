package nl.vu.cs.softwaredesign.ui.menus;

import nl.vu.cs.softwaredesign.data.config.ConfigManager;
import nl.vu.cs.softwaredesign.data.config.GameConfig;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

public class MainMenu extends FXGLMenu {

    String gameMode = "";

    public MainMenu() {
        super(MenuType.MAIN_MENU);
        getContentRoot().setPrefSize(1280, 720);
        initUI();

        Label mainLabel = new Label("Main Menu");
        mainLabel.setFont(new Font("Arial", 30));
        getContentRoot().getChildren().add(mainLabel);

        initButtons();
    }

    private void initUI() {
        var backgroundImage = FXGL.texture("background.png");
        getContentRoot().setBackground(new Background(new BackgroundImage(
                backgroundImage.getImage(),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        )));

        Text title = new Text("REIGNS - Ancient Egyptians");
        title.setFont(Font.font("Papyrus", 48));
        title.setTranslateX(1280 / 2 - 200); // center the text
        title.setTranslateY(150);
        title.setFill(Color.WHITE);
        getContentRoot().getChildren().add(title);
    }

    private void initButtons() {
        VBox menuBox = new VBox(20);
        menuBox.setTranslateX(1280 / 2 - 100); // center the buttons
        menuBox.setTranslateY(300);

        Button btnNewGame = new Button("New Game");
        btnNewGame.setPrefWidth(200);
        btnNewGame.setOnAction(e -> showGameModeMenu()); // show game mode

        Button btnLoadGame = new Button("Load Game");
        btnLoadGame.setPrefWidth(200);
        btnLoadGame.setOnAction(e -> {
            // load a previously saved game state here...
            // ensure a valid save file is available.
            FXGL.getSaveLoadService().readAndLoadTask("save1.json").run();
        });

        Button btnExit = new Button("Exit");
        btnExit.setPrefWidth(200);
        btnExit.setOnAction(e -> {
            FXGL.getGameController().exit();
        });

        menuBox.getChildren().addAll(btnNewGame, btnLoadGame, btnExit);
        getContentRoot().getChildren().add(menuBox);
    }

    private void showGameModeMenu() {
        getContentRoot().getChildren().clear();

        VBox modeBox = new VBox(20);
        modeBox.setTranslateX(1280 / 2 - 100);
        modeBox.setTranslateY(200);

        Label message = new Label("Choose your game mode");
        message.setFont(Font.font("Papyrus", 36));
        message.setTextFill(Color.WHITE);
        message.setTranslateX(1280 / 2 - 680);
        message.setTranslateY(0);

        Button btnEasy = new Button("Easy");
        btnEasy.setPrefWidth(200);
        btnEasy.setOnAction(e -> {
            ConfigManager.getInstance("Easy Mode").getGameConfig().setGameMode("Easy Mode");
            FXGL.getGameController().startNewGame();
        });

        Button btnMedium = new Button("Normal");
        btnMedium.setPrefWidth(200);
        btnMedium.setOnAction(e -> {
            ConfigManager.getInstance("Normal Mode").getGameConfig().setGameMode("Normal Mode");
            FXGL.getGameController().startNewGame();
        });

        Button btnHard = new Button("Hard");
        btnHard.setPrefWidth(200);
        btnHard.setOnAction(e -> {
            ConfigManager.getInstance("Hard Mode").getGameConfig().setGameMode("Hard Mode");

            FXGL.getGameController().startNewGame();
        });

        modeBox.getChildren().addAll(message, btnEasy, btnMedium, btnHard);
        getContentRoot().getChildren().add(modeBox);
    }
}