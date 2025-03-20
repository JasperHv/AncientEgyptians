package nl.vu.cs.softwaredesign.ui.views;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.handlers.SwipeHandler;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.*;
import nl.vu.cs.softwaredesign.data.model.*;
import nl.vu.cs.softwaredesign.data.handlers.HandleScore;
import nl.vu.cs.softwaredesign.data.GameStateManager;
import nl.vu.cs.softwaredesign.data.CardController;

import java.util.List;


public class GameView extends Parent {

    private static final double GAME_VIEW_WIDTH = FXGL.getAppWidth() / 2.5;
    private static final double GAME_VIEW_HEIGHT = FXGL.getAppHeight();

    private final PillarView pillarView = new PillarView(GAME_VIEW_WIDTH, 100);
    private final CardView cardView;
    private final List<String> introCards = List.of(
            "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
    );

    private List<Card> gameCards;
    private Label messageLabel;
    private Label yearLabel;
    private Label scoreLabel;
    private static GameView instance;
    IntegerProperty yearCount = FXGL.getip("yearCount");
    IntegerProperty scoreCount = FXGL.getip("scoreCount");

    private final ScoreSettings scoreSettings;
    HandleInfluencePillars handleInfluencePillars;
    GameStateManager gameStateManager;
    private final SwipeHandler swipeHandler;

    private final CardController cardController;

    public GameView() {
        cardView = new CardView(GAME_VIEW_WIDTH * 0.8, this);
        loadGameCards();
        initView();
        initChildren();

        cardController = new CardController(cardView, this);
        scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        handleInfluencePillars = new HandleInfluencePillars(this);
        yearCount.set(scoreSettings.getInitialYearCount());
        scoreCount.set(scoreSettings.getInitialScore());

        List<String> introCards = List.of("welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card");
        gameStateManager = new GameStateManager(gameCards, introCards, scoreSettings, yearCount, handleInfluencePillars);
        swipeHandler = new SwipeHandler(this, gameStateManager);
        updateCardAndMessage();
    }


    public static GameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    private void loadGameCards() {
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        this.gameCards = modeConfig.getCards();
    }

    private void initView() {
        setLayoutX((FXGL.getAppWidth() - GAME_VIEW_WIDTH) / 2);
        setLayoutY(0);

        var border = new Rectangle(GAME_VIEW_WIDTH, GAME_VIEW_HEIGHT);
        border.setStrokeWidth(1.0);
        border.setArcWidth(10.0);
        border.setArcHeight(10.0);
        border.setFill(Color.rgb(25, 25, 25, 0.8));
        border.setStroke(Color.BLACK);

        getChildren().add(border);
    }

    private void initChildren() {
        var vBox = new VBox();

        yearLabel = createBoxLabel("Years in Power: " + yearCount.get());
        scoreLabel = createBoxLabel("Score: " + scoreCount.get());

        HBox yearBox = createBox(yearLabel);
        HBox scoreBox = createBox(scoreLabel);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox statsBox = new HBox(10, yearBox, spacer, scoreBox);
        statsBox.setAlignment(Pos.TOP_CENTER);
        statsBox.setMaxWidth(Double.MAX_VALUE);

        messageLabel = new Label();
        messageLabel.setFont(Font.font("Papyrus", FontWeight.EXTRA_BOLD, 25));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setWrapText(true);
        messageLabel.setTextAlignment(TextAlignment.CENTER);

        var messageContainer = new VBox(messageLabel);
        messageContainer.setAlignment(Pos.CENTER);

        vBox.getChildren().addAll(statsBox, pillarView, messageContainer, cardView);

        getChildren().add(vBox);
    }

    private HBox createBox(Label label) {
        HBox box = new HBox(label);
        box.setStyle("-fx-background-color: black; -fx-padding: 10px; -fx-border-radius: 5px; -fx-border-color: white;");
        box.setAlignment(Pos.CENTER);
        box.setMinWidth(150);
        return box;
    }

    private Label createBoxLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.WHITE);
        return label;
    }

    public void updateScoreAndYearBoxes() {
        yearLabel.setText("Years in Power: " + yearCount.get());
        scoreLabel.setText("Score: " + scoreCount.get());
    }

    public void updateScore() {
        HandleScore handleScore = new HandleScore();
        handleScore.updateScore();
        scoreCount.set(FXGL.getip("scoreCount").get());
    }

    private void updateCardAndMessage() {
        if (gameStateManager.isIntroPhase()) {
            String cardName = introCards.get(gameStateManager.getIntroCardIndex());
            cardController.updateCard(gameCards.get(gameStateManager.getIntroCardIndex()));
            cardController.updateMessage(messageLabel, cardName);
        } else {
            Card currentCard = gameStateManager.getCurrentGameCard();
            if ("standard".equalsIgnoreCase(currentCard.getType())) {
                cardController.updateCard(currentCard);
                cardController.updateMessage(messageLabel, currentCard.getScenario());
            } else {

                gameStateManager.advanceGameCard();
                updateCardAndMessage();
            }
        }
    }


    public void onCardSwiped(SwipeSide side) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), messageLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> {
            swipeHandler.onSwipe(side);
            updateCardAndMessage();
        });

        fadeOut.play();
    }


    public void showEndScreen(PillarEnding ending) {
        String formattedDescription = ending.getDescription().replaceAll("([.!])", "$1\n");
        FXGL.getDialogService().showMessageBox(formattedDescription);

        Image image = FXGL.image(ending.getImage());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());

        StackPane screen = new StackPane(imageView);
        screen.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());

        FXGL.getGameScene().addUINode(screen);
    }
}
