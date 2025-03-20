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
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.*;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.PillarEnding;
import nl.vu.cs.softwaredesign.data.service.HandleScore;
import nl.vu.cs.softwaredesign.data.service.InfluencePillars;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class GameView extends Parent {

    private static final double GAME_VIEW_WIDTH = FXGL.getAppWidth() / 2.5;
    private static final double GAME_VIEW_HEIGHT = FXGL.getAppHeight();

    private final PillarView pillarView = new PillarView(GAME_VIEW_WIDTH, 100);
    private final CardView cardView;
    private final Map<String, String> cardPillarToImageMap = new HashMap<>();

    private final List<String> introCards = List.of(
            "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
    );

    private PriorityQueue<Card> gameCardsQueue;
    private int introCardIndex = 0;
    private Card currentCard = null;
    private boolean isIntroPhase = true;

    private Label messageLabel;
    private Label yearLabel;
    private Label scoreLabel;
    private static GameView instance;
    IntegerProperty yearCount = FXGL.getip("yearCount");
    IntegerProperty scoreCount = FXGL.getip("scoreCount");

    private final ScoreSettings scoreSettings;
    InfluencePillars InfluencePillars;

    public GameView() {
        cardView = new CardView(GAME_VIEW_WIDTH * 0.8, this);
        this.InfluencePillars = new InfluencePillars(this);
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
        List<Card> cards = modeConfig.getCards();
        gameCardsQueue = new PriorityQueue<Card>((c1, c2) -> Integer.compare(c2.getFrequency(), c1.getFrequency()));
        gameCardsQueue.addAll(cards);
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

    private void updateScoreAndYearBoxes() {
        yearLabel.setText("Years in Power: " + yearCount.get());
        scoreLabel.setText("Score: " + scoreCount.get());
    }

    private void updateScore() {
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
            if (!gameCardsQueue.isEmpty()) {
                currentCard = null;
                while (!gameCardsQueue.isEmpty()) {
                    currentCard = gameCardsQueue.poll();
                    if ("standard".equalsIgnoreCase(currentCard.getType())) {
                        break;
                    } else {
                        // If not standard, skip it (for now)
                        currentCard = null;
                    }
                }
                if (currentCard != null) {
                    String pillar = currentCard.getPillar().toLowerCase();
                    String imageName = cardPillarToImageMap.get(pillar);
                    cardView.updateCard(imageName);
                    updateMessage(currentCard.getScenario());
                    currentCard.decrementFrequency();
                    System.out.println("Card " + currentCard.getTitle() + " frequency now: " + currentCard.getFrequency());
                    if (currentCard.getFrequency() > 0) {
                        gameCardsQueue.offer(currentCard);
                    }
                } else {
                    // No standard card found
                }
            } else {
                // No cards remaining -> lose game
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

    public void onCardSwiped(boolean isSwipeLeft) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), messageLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            if (isIntroPhase) {
                handleIntroPhase(isSwipeLeft);
            } else {
                handleGamePhase(isSwipeLeft);
            }
        });
        fadeOut.play();
    }

    private void handleIntroPhase(boolean isSwipeLeft) {
        String currentCard = introCards.get(introCardIndex);
        if ("choose-pharaoh".equals(currentCard)) {
            GameConfiguration config = ModeConfiguration.getInstance().getGameConfig();
            if (isSwipeLeft) {
                config.setSelectedCharacter("Cleopatra");
            } else {
                config.setSelectedCharacter("Tutankhamun");
            }
            introCardIndex = introCards.indexOf(isSwipeLeft ? "cleopatra-card" : "tutankhamun-card");
            ModeConfiguration.getInstance().updatePillarValues();
        } else if ("tutankhamun-card".equals(currentCard) || "cleopatra-card".equals(currentCard)) {
            isIntroPhase = false;
        } else {
            introCardIndex++;
        }
        updateCardAndMessage();
    }

    private void handleGamePhase(boolean isSwipeLeft) {
        yearCount.set(yearCount.get() + scoreSettings.getYearCountIncrease());
        if (currentCard != null) {
            InfluencePillars.applyInfluence(isSwipeLeft, currentCard.getInfluence());
        }
        updateScore();
        updateScoreAndYearBoxes();
        System.out.println("Year Count after update: " + yearCount.get());

        Card currentCard = gameCards.get(gameCardIndex);
        InfluencePillars.applyInfluence(isSwipeLeft, currentCard.getInfluence());
        updateScore();
        updateScoreAndYearBoxes();
        gameCardIndex++;

        if (gameCardIndex >= gameCards.size()) {
            PillarEnding badEnding = ConfigurationLoader.getInstance().getBadEnding();
            if (badEnding != null) {
                showEndScreen(badEnding);
            }
        } else {
            updateCardAndMessage();
        }
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
