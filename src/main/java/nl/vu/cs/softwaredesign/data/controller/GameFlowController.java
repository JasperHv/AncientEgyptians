package nl.vu.cs.softwaredesign.data.controller;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.ui.views.CardView;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import javafx.beans.property.IntegerProperty;
import javafx.scene.control.Label;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;

import java.util.*;

public class GameFlowController {

    private final CardView cardView;
    private final GameView gameView;
    private final GameStateController gameStateController;
    private final ScoreSettings scoreSettings;
    HandleInfluencePillars handleInfluencePillars;

    private final List<String> introCards = List.of(
            "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
    );
    private List<Card> gameCardsList;
    private final Map<String, String> cardPillarToImageMap;


    public GameFlowController(CardView cardView, GameView gameView, IntegerProperty yearCount) {
        this.cardView = cardView;
        this.gameView = gameView;
        scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        handleInfluencePillars = new HandleInfluencePillars(this.gameView);

        loadGameCards();
        this.cardPillarToImageMap = new HashMap<>();
        loadPillarImages();

        this.gameStateController = new GameStateController(gameCardsList, introCards, scoreSettings, yearCount, handleInfluencePillars);
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

    private void loadPillarImages() {
        cardPillarToImageMap.put("priests", "priests-card");
        cardPillarToImageMap.put("farmers", "farmers-card");
        cardPillarToImageMap.put("nobles", "nobles-card");
        cardPillarToImageMap.put("military", "military-card");
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
        cardView.updateCard(cardName);
        updateMessage(messageLabel, cardName);
    }

    public void handleGamePhase(Label messageLabel) {
        Card currentCard = gameStateController.getCurrentGameCard();
        if ("standard".equalsIgnoreCase(currentCard.getType())) {
            String pillar = currentCard.getPillar().toLowerCase();
            String imageName = cardPillarToImageMap.get(pillar);
            cardView.updateCard(imageName);
            updateMessage(messageLabel, currentCard.getScenario());
        }
    }

    public GameStateController getGameStateManager() {
        return gameStateController;
    }
}
