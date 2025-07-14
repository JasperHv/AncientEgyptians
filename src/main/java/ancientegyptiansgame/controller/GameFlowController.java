package ancientegyptiansgame.controller;

import ancientegyptiansgame.logging.*;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.scoresettings.ScoreSettings;

import ancientegyptiansgame.data.model.Card;
import ancientegyptiansgame.data.model.CardDeck;
import ancientegyptiansgame.data.model.Pillars;
import ancientegyptiansgame.ui.views.CardView;

import javafx.scene.control.Label;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class GameFlowController {

    private static final Logger log = LoggerFactory.getLogger(GameFlowController.class);
    private final CardView cardView;
    private final GameStateController gameStateController;
    private CardDeck cardDeck;
    private final Map<String, Queue<Card>> legacyCardsMap = new HashMap<>();

    public GameFlowController(CardView cardView) {
        this.cardView = cardView;
        ScoreSettings scoreSettings = ConfigurationLoader.getInstance().getScoreSettings();

        GameConfiguration gameConfiguration = GameConfiguration.getInstance();
        loadGameCards();
        List<String> introCards = List.of(
                "welcome-card", "choose-pharaoh", "tutankhamun-card", "cleopatra-card"
        );

        this.gameStateController = new GameStateController(
                cardDeck,
                introCards,
                scoreSettings,
                gameConfiguration,
                legacyCardsMap
        );
    }
    private void loadGameCards() {
        GameConfiguration gameConfig = GameConfiguration.getInstance();
        List<Card> allCards = gameConfig.getCards();

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
        String imageName = Pillars.fromName(pillar).getCardImage();
        cardView.updateCard(imageName);
        updateMessage(messageLabel, currentCard.getScenario());
    }

    public GameStateController getGameStateManager() {
        return gameStateController;
    }

    public void redoLastAction() {
        List<CommandLogEntry> savedCommands = CommandLogger.getLogEntries();
        if (savedCommands.isEmpty()) {
            FXGL.getDialogService().showMessageBox("No action to redo.");
            return;
        } else {
            log.info("Redoing last action...");
            for (CommandLogEntry entry : savedCommands) {
                log.info("Card Title: {}", entry.getCardTitle());
            }
        }
        // This method can be used to redo the last action in the game.
    }
}
