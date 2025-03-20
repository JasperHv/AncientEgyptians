package nl.vu.cs.softwaredesign.data;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.ui.views.CardView;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.GameStateManager;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Map;

public class CardController {

    private final CardView cardView;
    private final GameView gameView;
    private final GameStateManager gameStateManager;
    private final ScoreSettings scoreSettings;
    HandleInfluencePillars handleInfluencePillars;

    private final List<String> introCards = List.of(
            "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
    );
    private List<Card> gameCards;
    private final Map<String, String> cardPillarToImageMap;

    // Modified constructor to accept yearCount as parameter
    public CardController(CardView cardView, GameView gameView, IntegerProperty yearCount) {
        this.cardView = cardView;
        this.gameView = gameView;
        scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        handleInfluencePillars = new HandleInfluencePillars(this.gameView);

        loadGameCards();
        this.cardPillarToImageMap = new HashMap<>();
        loadPillarImages();

        this.gameStateManager = new GameStateManager(gameCards, introCards, scoreSettings, yearCount, handleInfluencePillars);
    }


    private void loadGameCards() {
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        this.gameCards = modeConfig.getCards();
    }

    private void loadPillarImages() {
        cardPillarToImageMap.put("priests", "priests-card");
        cardPillarToImageMap.put("farmers", "farmers-card");
        cardPillarToImageMap.put("nobles", "nobles-card");
        cardPillarToImageMap.put("military", "military-card");
    }

    public void updateCard(Card card) {
        if ("standard".equalsIgnoreCase(card.getType())) {
            String pillar = card.getPillar().toLowerCase();
            String imageName = cardPillarToImageMap.get(pillar);
            cardView.updateCard(imageName);
        }
    }


    public void updateMessage(Label messageLabel, String cardName) {
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
                messageLabel.setText(cardName);
        }

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), messageLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    public void updateCardAndMessage(Label messageLabel) {
        if (gameStateManager.isIntroPhase()) {
            handleIntroPhase(messageLabel);
        } else {
            handleGamePhase(messageLabel);
        }
    }


    public void handleIntroPhase(Label messageLabel) {
        int introCardIndex = gameStateManager.getIntroCardIndex();
        String cardName = gameStateManager.getIntroCards().get(introCardIndex);
        cardView.updateCard(cardName);
        updateMessage(messageLabel, cardName);
    }

    public void handleGamePhase(Label messageLabel) {
        Card currentCard = gameStateManager.getCurrentGameCard();
        if ("standard".equalsIgnoreCase(currentCard.getType())) {
            String pillar = currentCard.getPillar().toLowerCase();
            String imageName = cardPillarToImageMap.get(pillar);
            cardView.updateCard(imageName);
            updateMessage(messageLabel, currentCard.getScenario());
        } else {
            gameStateManager.advanceGameCard();
            updateCardAndMessage(messageLabel);
        }
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }
}
