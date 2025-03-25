package nl.vu.cs.softwaredesign.data.controller;

import javafx.animation.FadeTransition;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.data.config.ConfigurationLoader;
import nl.vu.cs.softwaredesign.data.config.gamesettings.GameConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ScoreSettings;
import nl.vu.cs.softwaredesign.data.handlers.HandleInfluencePillars;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.ui.views.CardView;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import javafx.scene.control.Label;

import java.util.List;

public class CardController {

    private final CardView cardView;
    private final GameView gameView;
    private final GameStateController gameStateController;
    private final ScoreSettings scoreSettings;
    private final HandleInfluencePillars handleInfluencePillars;

    private final List<String> introCards = List.of(
            "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
    );
    private List<Card> gameCards;

    /**
     * Updated constructor to accept GameConfiguration instead of yearCount property.
     */
    public CardController(CardView cardView, GameView gameView, GameConfiguration gameConfiguration) {
        this.cardView = cardView;
        this.gameView = gameView;
        this.scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();
        this.handleInfluencePillars = new HandleInfluencePillars(this.gameView);

        loadGameCards();

        // Pass the GameConfiguration to GameStateController
        this.gameStateController = new GameStateController(
                gameCards,
                introCards,
                scoreSettings,
                gameConfiguration,       // Use the configuration directly
                handleInfluencePillars
        );
    }

    private void loadGameCards() {
        ModeConfiguration modeConfig = ModeConfiguration.getInstance();
        this.gameCards = modeConfig.getCards();
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
        if (gameStateController.isIntroPhase()) {
            handleIntroPhase(messageLabel);
        } else {
            handleGamePhase(messageLabel);
        }
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
        } else {
            gameStateController.advanceGameCard();
            updateCardAndMessage(messageLabel);
        }
    }

    public GameStateController getGameStateManager() {
        return gameStateController;
    }
}
