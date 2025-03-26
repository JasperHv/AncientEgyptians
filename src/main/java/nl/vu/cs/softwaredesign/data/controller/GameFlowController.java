package nl.vu.cs.softwaredesign.data.controller;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.CardDeck;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.ui.views.CardView;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import javafx.scene.control.Label;

import java.util.*;

public class GameFlowController {

    private final CardView cardView;
    private final GameStateController gameStateController;
    private CardDeck cardDeck;
    private final Map<String, Queue<Card>> legacyCardsMap = new HashMap<>();

    public GameFlowController(CardView cardView, GameView gameView, GameConfiguration gameConfiguration) {
        this.cardView = cardView;
        ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        HandleInfluencePillars handleInfluencePillars = new HandleInfluencePillars(gameView);

        loadGameCards();
        List<String> introCards = List.of(
                "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
        );

        this.gameStateController = new GameStateController(
                cardDeck,
                introCards,
                scoreSettings,
                gameConfiguration,
                handleInfluencePillars,
                legacyCardsMap
        );
    }
    private void loadGameCards() {
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        List<Card> allCards = modeConfig.getCards();

        List<Card> standardCards = new ArrayList<>();
        for (Card card : allCards) {
            if ("legacy".equalsIgnoreCase(card.getType())) {
                String pillar = card.getPillar().toLowerCase();
                legacyCardsMap.computeIfAbsent(pillar, k -> new LinkedList<>()).add(card);
            } else {
                standardCards.add(card);
            }
        }
        cardDeck = new CardDeck(standardCards);
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

    public void handleIntroPhase(Label messageLabel) {
        int introCardIndex = gameStateController.getIntroCardIndex();
        String cardName = gameStateController.getIntroCards().get(introCardIndex);
        cardView.updateCard(cardName + ".png");
        updateMessage(messageLabel, cardName);
    }

    public void handleGamePhase(Label messageLabel) {
        Card currentCard = gameStateController.getCurrentGameCard();

        if (currentCard == null) {
            return;
        }

        String pillar = currentCard.getPillar().toLowerCase();
        String imageName = Pillar.fromName(pillar).getCardImage();
        cardView.updateCard(imageName);
        updateMessage(messageLabel, currentCard.getScenario());
    }

    public GameStateController getGameStateManager() {
        return gameStateController;
    }
}
