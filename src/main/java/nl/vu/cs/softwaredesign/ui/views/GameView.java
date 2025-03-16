package nl.vu.cs.softwaredesign.ui.views;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.config.*;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Influence;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.data.model.PillarEnding;
import nl.vu.cs.softwaredesign.data.HandleScore;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;

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
    ScoreSettings scoreSettings;

    public GameView() {
        cardView = new CardView(GAME_VIEW_WIDTH * 0.8, this);
        loadPillarImages();
        loadGameCards();
        initView();
        initChildren();
        updateCardAndMessage();

        scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        FXGL.getip("scoreCount").set(scoreSettings.getInitialScore());
        FXGL.getip("yearCount").set(scoreSettings.getInitialYearCount());
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

        yearLabel = createBoxLabel("Years in Power: " + FXGL.getip("yearCount").get());
        scoreLabel = createBoxLabel("Score: " + FXGL.getip("scoreCount").get());

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
        yearLabel.setText("Years in Power: " + FXGL.getip("yearCount").get());
        scoreLabel.setText("Score: " + FXGL.getip("scoreCount").get());
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



    public void onCardSwiped(boolean isSwipeLeft) {
        if (!isIntroPhase) {
            FXGL.inc("yearCount", scoreSettings.getYearCountIncrease());
            HandleScore handleScore = new HandleScore();
            handleScore.updateScore();
        }

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), messageLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(event -> {
            if (isIntroPhase) {
                handleIntroPhase(isSwipeLeft);
            } else {
                handleGamePhase(isSwipeLeft);
            }
            updateCardAndMessage();
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
    }

    private void handleGamePhase(boolean isSwipeLeft) {
        if (gameCardIndex < gameCards.size()) {
            Card currentCard = gameCards.get(gameCardIndex);
            applyInfluence(isSwipeLeft, currentCard.getInfluence());
            updateScoreAndYearBoxes();
        }
        gameCardIndex = (gameCardIndex >= gameCards.size() - 1) ? 0 : gameCardIndex + 1;
    }

    private void applyInfluence(boolean isSwipeLeft, List<Influence> influences) {
        if (influences == null || influences.isEmpty()) return;

        List<Influence> adjustedInfluences = influences.stream()
                .map(influence -> new Influence(
                        influence.getPillar(),
                        isSwipeLeft ? -influence.getValue() : influence.getValue()
                ))
                .collect(Collectors.toList());

        IntegerProperty yearCount = FXGL.getip("yearCount");
        int threshold = scoreSettings.getYearThreshold();

        boolean gameOverTriggered = false;
        boolean winTriggered = false;
        Pillar triggeredPillar = null;
        List<Pillar> pillars = ConfigurationLoader.getInstance().getPillars();

        for (Influence influence : adjustedInfluences) {
            String pillarName = influence.getPillar();
            int valueChange = influence.getValue();

            IntegerProperty pillarProgress = FXGL.getip(pillarName);
            int currentValue = pillarProgress.get();
            int newValue = Math.min(Math.max(currentValue + valueChange, 0), 100);

            pillarProgress.set(newValue);

            if (newValue == 0) {
                gameOverTriggered = true;
                triggeredPillar = pillars.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(pillarName))
                        .findFirst()
                        .orElse(null);
            } else if (newValue == 100 && yearCount.get() >= threshold) {
                winTriggered = true;
                triggeredPillar = pillars.stream()
                        .filter(p -> p.getName().equalsIgnoreCase(pillarName))
                        .findFirst()
                        .orElse(null);
            }
        }

        if (winTriggered) {
            triggerEndScreen(triggeredPillar, true);
        } else if (gameOverTriggered) {
            triggerEndScreen(triggeredPillar, false);
        }
    }

    private void triggerEndScreen(Pillar pillar, boolean isWin) {
        PillarEnding goldenAgeEnding = ConfigurationLoader.getInstance().getGoldenAgeEnding();
        PillarEnding ending = isWin ? goldenAgeEnding : (pillar.getEnding() != null ? pillar.getEnding() : goldenAgeEnding);

        String formattedDescription = ending.getDescription().replaceAll("([.!])", "$1\n");
        FXGL.getDialogService().showMessageBox(formattedDescription);

        // Display the ending image
        Image image = FXGL.image(ending.getImage());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(FXGL.getAppWidth());
        imageView.setFitHeight(FXGL.getAppHeight());

        StackPane screen = new StackPane(imageView);
        screen.setPrefSize(FXGL.getAppWidth(), FXGL.getAppHeight());
        FXGL.getGameScene().addUINode(screen);
    }
}
