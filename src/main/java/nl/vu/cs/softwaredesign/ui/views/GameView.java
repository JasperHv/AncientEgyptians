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
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.*;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;
import nl.vu.cs.softwaredesign.data.model.*;
import nl.vu.cs.softwaredesign.data.handlers.HandleScore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameView extends Parent {

    private static final double GAME_VIEW_WIDTH = FXGL.getAppWidth() / 2.5;
    private static final double GAME_VIEW_HEIGHT = FXGL.getAppHeight();

    private final PillarView pillarView = new PillarView(GAME_VIEW_WIDTH, 100);
    private final CardView cardView;
    private final Map<String, String> cardPillarToImageMap = new HashMap<>();

    private final List<String> introCards = List.of(
            "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
    );

    private List<Card> gameCards;
    private int introCardIndex = 0;
    private int gameCardIndex = 0;
    private boolean isIntroPhase = true;

    private Label messageLabel;
    private Label yearLabel;
    private Label scoreLabel;
    private static GameView instance;
    IntegerProperty yearCount = FXGL.getip("yearCount");
    IntegerProperty scoreCount = FXGL.getip("scoreCount");

    private final ScoreSettings scoreSettings;
    HandleInfluencePillars HandleInfluencePillars;
    private final SwipeHandler swipeHandler = new SwipeHandler(this);

    public GameView() {
        cardView = new CardView(GAME_VIEW_WIDTH * 0.8, this);
        this.HandleInfluencePillars = new HandleInfluencePillars(this);
        loadPillarImages();
        loadGameCards();
        initView();
        initChildren();
        updateCardAndMessage();

        scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        yearCount.set(scoreSettings.getInitialYearCount());
        scoreCount.set(scoreSettings.getInitialScore());
    }

    public static GameView getInstance() {
        if (instance == null) {
            instance = new GameView();
        }
        return instance;
    }

    private void loadPillarImages() {
        cardPillarToImageMap.put("priests", "priests-card");
        cardPillarToImageMap.put("farmers", "farmers-card");
        cardPillarToImageMap.put("nobles", "nobles-card");
        cardPillarToImageMap.put("military", "military-card");
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
        if (isIntroPhase) {
            String cardName = introCards.get(introCardIndex);
            cardView.updateCard(cardName);
            updateMessage(cardName);
        } else {
            if (gameCardIndex < gameCards.size()) {
                // This goes in the order of the json file for now... will change later
                Card currentCard = gameCards.get(gameCardIndex);
                if ("standard".equalsIgnoreCase(currentCard.getType())) {
                    String pillar = currentCard.getPillar().toLowerCase();
                    String imageName = cardPillarToImageMap.get(pillar);
                    cardView.updateCard(imageName);
                    updateMessage(currentCard.getScenario());
                } else {
                    // Skip "legacy" cards for now
                    gameCardIndex = (gameCardIndex >= gameCards.size() - 1) ? 0 : gameCardIndex + 1;
                    updateCardAndMessage();
                }
            } else {
                // Empty for now --> endless loop
            }
        }
    }


    private void updateMessage(String cardName) {
        switch (cardName) {
            case "welcome-card":
                messageLabel.setText("Welcome to Reigns - Ancient Egypt \n Are you ready to start the adventure? \n Swipe right or left!");
                break;
            case "choose-pharaoh":
                messageLabel.setText("Choose your Pharaoh:\n Swipe right for Tutankhamun \n Swipe left for Cleopatra");
                break;
            case "tutankhamun-card":
                messageLabel.setText("Great! You have chosen Tutankhamun, \n also known as the Young Pharaoh. \n Swipe right or left to continue.");
                break;
            case "cleopatra-card":
                messageLabel.setText("Great! You have chosen Cleopatra, \n also known as the Cunning Queen. \n Swipe right or left to continue.");
                break;
            default:
                messageLabel.setPrefWidth(GAME_VIEW_WIDTH * 0.8);
                messageLabel.setText(cardName);
        }

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), messageLabel); // fade in the new message
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
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

    public boolean getIntroPhase() {
        return getInstance().isIntroPhase;
    }

    public static void setIntroPhase(boolean isIntroPhase) {
        getInstance().isIntroPhase = isIntroPhase;
    }

    public List<String> getIntroCards() {
        return introCards;
    }


    public Card getCurrentGameCard() {
        return gameCards.get(gameCardIndex);
    }


    public int getGameCardIndex() {
        return gameCardIndex;
    }

    public ScoreSettings getScoreSettings() {
        return scoreSettings;
    }

    public IntegerProperty getYearCount() {
        return yearCount;
    }

    public HandleInfluencePillars getInfluencePillars() {
        return HandleInfluencePillars;
    }

    public int getIntroCardIndex() {
        return introCardIndex;
    }

    public void setIntroCardIndex(int index) {
        this.introCardIndex = index;
    }

    public static void setGameCardIndex(int newGameCardIndex) {
        getInstance().gameCardIndex = newGameCardIndex;
    }

    public static List<Card> getGameCards() {
        return getInstance().gameCards;
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
