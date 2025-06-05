package nl.vu.cs.ancientegyptiansgame.ui.menus;

import nl.vu.cs.ancientegyptiansgame.config.gamesettings.ModeConfiguration;
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
        title.setTranslateX((double) 1280 / 2 - 200);
        title.setTranslateY(150);
        title.setFill(Color.WHITE);
        getContentRoot().getChildren().add(title);
    }

    private void initButtons() {
        VBox menuBox = new VBox(20);
        menuBox.setTranslateX((double) 1280 / 2 - 100);
        menuBox.setTranslateY(300);

        Button btnNewGame = new Button("New Game");
        btnNewGame.setPrefWidth(200);
        btnNewGame.setOnAction(e -> showGameModeMenu());

        Button btnLoadGame = new Button("Load Game");
        btnLoadGame.setPrefWidth(200);
        btnLoadGame.setOnAction(e -> showGameModeMenu());

        Button btnExit = new Button("Exit");
        btnExit.setPrefWidth(200);
        btnExit.setOnAction(e -> FXGL.getGameController().exit());

        menuBox.getChildren().addAll(btnNewGame, btnLoadGame, btnExit);
        getContentRoot().getChildren().add(menuBox);
    }

    private void showGameModeMenu() {
        getContentRoot().getChildren().clear();

        VBox modeBox = new VBox(20);
        modeBox.setTranslateX((double) 1280 / 2 - 100);
        modeBox.setTranslateY(200);

        Label message = new Label("Choose your game mode");
        message.setFont(Font.font("Papyrus", 36));
        message.setTextFill(Color.WHITE);

        message.setTranslateX((double) 1280 / 2 - 680);
        message.setTranslateY(0);

        Button btnVeryEasy = new Button("Very Easy");
        btnVeryEasy.setPrefWidth(200);
        btnVeryEasy.setOnAction(e -> {
            ModeConfiguration.initialize("Very Easy Mode");
            FXGL.getGameController().startNewGame();
        });

        Button btnEasy = new Button("Easy");
        btnEasy.setPrefWidth(200);
        btnEasy.setOnAction(e -> {
            ModeConfiguration.initialize("Easy Mode");
            FXGL.getGameController().startNewGame();
        });

        Button btnMedium = new Button("Normal");
        btnMedium.setPrefWidth(200);
        btnMedium.setOnAction(e -> {
            ModeConfiguration.initialize("Normal Mode");
            FXGL.getGameController().startNewGame();
        });

        Button btnHard = new Button("Hard");
        btnHard.setPrefWidth(200);
        btnHard.setOnAction(e -> {
            ModeConfiguration.initialize("Hard Mode");
            FXGL.getGameController().startNewGame();
        });

        Button btnVeryHard = new Button("Very Hard");
        btnVeryHard.setPrefWidth(200);
        btnVeryHard.setOnAction(e -> {
            ModeConfiguration.initialize("Very Hard Mode");
            FXGL.getGameController().startNewGame();
        });

        modeBox.getChildren().addAll(message, btnVeryEasy,btnEasy, btnMedium, btnHard, btnVeryHard);
        getContentRoot().getChildren().add(modeBox);
    }
}
