package nl.vu.cs.softwaredesign.data.controller;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.config.scoresettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.ui.views.CardView;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import javafx.scene.control.Label;

import java.util.*;

public class GameFlowController {

    private final CardView cardView;
    private final GameStateController gameStateController;

    private List<Card> gameCardsList;


    public GameFlowController(CardView cardView, GameView gameView, GameConfiguration gameConfiguration) {
        this.cardView = cardView;
        ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        HandleInfluencePillars handleInfluencePillars = new HandleInfluencePillars(gameView);

        loadGameCards();

        List<String> introCards = List.of(
                "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
        );

        this.gameStateController = new GameStateController(
                gameCardsList,
                introCards,
                scoreSettings,
                gameConfiguration,
                handleInfluencePillars
        );
    }

    private void loadGameCards() {
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        List<Card> cards = modeConfig.getCards();

        // Sort by frequency (highest first)
        cards.sort((c1, c2) -> Integer.compare(c2.getFrequency(), c1.getFrequency()));

        // Group by frequency and shuffle within each frequency group
        Map<Integer, List<Card>> frequencyGroups = new HashMap<>();
        for (Card card : cards) {
            frequencyGroups.computeIfAbsent(card.getFrequency(), k -> new ArrayList<>()).add(card);
        }

        gameCardsList = new ArrayList<>();
        frequencyGroups.keySet().stream()
                .sorted(Comparator.reverseOrder()) // Highest frequency first
                .forEach(freq -> {
                    List<Card> group = frequencyGroups.get(freq);
                    Collections.shuffle(group); // Shuffle each frequency group
                    gameCardsList.addAll(group);
                });
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
        if ("standard".equalsIgnoreCase(currentCard.getType())) {
            String imageName = Pillar.fromName(currentCard.getPillar()).getCardImage();
            cardView.updateCard(imageName);
            updateMessage(messageLabel, currentCard.getScenario());
        }
    }

    public GameStateController getGameStateManager() {
        return gameStateController;
    }
}
