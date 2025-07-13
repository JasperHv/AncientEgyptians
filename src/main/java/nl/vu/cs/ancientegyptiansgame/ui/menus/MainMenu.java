package nl.vu.cs.ancientegyptiansgame.ui.menus;

import javafx.geometry.Pos;
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
import nl.vu.cs.ancientegyptiansgame.logging.GameStateEntry;
import nl.vu.cs.ancientegyptiansgame.logging.GameStateLogger;

import java.util.List;

public class MainMenu extends FXGLMenu {
    private static final String PAPYRUS_FONT = "Papyrus";
    public MainMenu() {
        super(MenuType.MAIN_MENU);
        getContentRoot().setPrefSize(1280, 720);
        initUI();

        Label mainLabel = new Label("Main Menu");
        mainLabel.setFont(new Font("Arial", 30));
        getContentRoot().getChildren().add(mainLabel);

        showMainMenu();
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
        title.setFont(Font.font(PAPYRUS_FONT, 48));
        title.setTranslateX((double) 1280 / 2 - 200);
        title.setTranslateY(150);
        title.setFill(Color.WHITE);
        getContentRoot().getChildren().add(title);
    }

    private void showMainMenu() {
        getContentRoot().getChildren().clear();

        VBox menuBox = new VBox(20);
        menuBox.setTranslateX((double) 1280 / 2 - 100);
        menuBox.setTranslateY(300);

        Button btnNewGame = new Button("New Game");
        btnNewGame.setPrefWidth(200);
        btnNewGame.setOnAction(e -> showGameModeMenu());

        Button btnLoadGame = new Button("Load Game");
        btnLoadGame.setPrefWidth(200);
        btnLoadGame.setOnAction(e -> showSavedGameMenu());

        Button btnExit = new Button("Exit");
        btnExit.setPrefWidth(200);
        btnExit.setOnAction(e -> FXGL.getGameController().exit());

        menuBox.getChildren().addAll(btnNewGame, btnLoadGame, btnExit);
        getContentRoot().getChildren().add(menuBox);
    }

    private void showSavedGameMenu() {
        getContentRoot().getChildren().clear();

        VBox modeBox = new VBox(20);
        modeBox.setTranslateX((double) 1280 / 2 - 200);
        modeBox.setTranslateY(200);
        modeBox.setAlignment(Pos.CENTER);

        Label message = new Label("Choose your saved game");
        message.setFont(Font.font(PAPYRUS_FONT, 36));
        message.setTextFill(Color.WHITE);
        message.setTranslateX((double) 1280 / 2 - 650);
        message.setTranslateY(0);

        List<GameStateEntry> savedGames = GameStateLogger.loadGameStates();
        if (savedGames.isEmpty()) {
            Label noSavedGames = new Label("No saved games found");
            noSavedGames.setFont(Font.font(PAPYRUS_FONT, 24));
            noSavedGames.setTextFill(Color.WHITE);
            modeBox.getChildren().addAll(message, noSavedGames);
        } else {
            modeBox.getChildren().add(message);

            for (int i = 0; i < savedGames.size(); i++) {
                GameStateEntry gameState = savedGames.get(i);

                // Create main container for each save entry
                HBox saveEntry = new HBox();
                saveEntry.setPrefWidth(400);
                saveEntry.setSpacing(20);
                saveEntry.setAlignment(Pos.CENTER_LEFT);

                // Left side - Save name and number
                VBox leftSide = new VBox(5);
                leftSide.setPrefWidth(200);

                Label saveName = new Label("Game " + (i + 1));
                saveName.setFont(Font.font(PAPYRUS_FONT, 24));
                saveName.setTextFill(Color.WHITE);

                leftSide.getChildren().addAll(saveName);

                // Right side - Details stacked vertically
                VBox rightSide = new VBox(2);
                rightSide.setPrefWidth(200);
                rightSide.setAlignment(Pos.CENTER_RIGHT);

                Label modeLabel = new Label(gameState.getGameMode().getName());
                modeLabel.setFont(Font.font(PAPYRUS_FONT, 18));
                modeLabel.setTextFill(Color.WHITE);

                Label yearLabel = new Label("Year " + gameState.getYear());
                yearLabel.setFont(Font.font(PAPYRUS_FONT, 16));
                yearLabel.setTextFill(Color.LIGHTGRAY);

                Label scoreLabel = new Label("Score " + gameState.getScore());
                scoreLabel.setFont(Font.font(PAPYRUS_FONT, 16));
                scoreLabel.setTextFill(Color.LIGHTGRAY);

                rightSide.getChildren().addAll(modeLabel, yearLabel, scoreLabel);

                // Add both sides to the main container
                saveEntry.getChildren().addAll(leftSide, rightSide);

                // Make the entire entry clickable
                saveEntry.setOnMouseClicked(e -> loadSavedGame(gameState));

                // Add hover effect
                saveEntry.setOnMouseEntered(e -> saveEntry.setStyle("-fx-border-color: rgba(255, 255, 255, 0.5); -fx-border-width: 2; -fx-background-color: rgba(255, 255, 255, 0.1); -fx-background-radius: 5; -fx-border-radius: 5;"));
                saveEntry.setOnMouseExited(e -> saveEntry.setStyle("-fx-border-color: rgba(255, 255, 255, 0.3); -fx-border-width: 1; -fx-background-color: rgba(0, 0, 0, 0.2); -fx-background-radius: 5; -fx-border-radius: 5;"));

                modeBox.getChildren().add(saveEntry);
            }
        }

        // Add a go-back button
        Button backButton = new Button("Back to Main Menu");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> showMainMenu());
        modeBox.getChildren().add(backButton);

        getContentRoot().getChildren().add(modeBox);
    }

    private void loadSavedGame(GameStateEntry gameState) {
    }

    private void showGameModeMenu() {
        getContentRoot().getChildren().clear();

        VBox modeBox = new VBox(20);
        modeBox.setTranslateX((double) 1280 / 2 - 100);
        modeBox.setTranslateY(200);

        Label message = new Label("Choose your game mode");
        message.setFont(Font.font(PAPYRUS_FONT, 36));
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

        // Add a go-back button
        Button backButton = new Button("Back to Main Menu");
        backButton.setPrefWidth(200);
        backButton.setOnAction(e -> showMainMenu());
        modeBox.getChildren().add(backButton);

        getContentRoot().getChildren().add(modeBox);
    }
}
