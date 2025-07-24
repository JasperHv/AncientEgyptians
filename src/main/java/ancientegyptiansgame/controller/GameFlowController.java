package ancientegyptiansgame.controller;

import ancientegyptiansgame.logging.*;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import ancientegyptiansgame.config.ConfigurationLoader;
import ancientegyptiansgame.config.gamesettings.GameConfiguration;
import ancientegyptiansgame.config.gamesettings.ModeConfiguration;
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

    public void resetIntroCard(Label messageLabel) {
        int currentIntroCardIndex = gameStateController.getIntroCardIndex();
        gameStateController.setIntroCardIndex(currentIntroCardIndex - 1);
        int introCardIndexNew = gameStateController.getIntroCardIndex();
        String cardName = gameStateController.getIntroCards().get(introCardIndexNew);
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
        log.info("=== ENTERING redoLastAction method ===");
        List<CommandLogEntry> savedCommands = CommandLogger.getLogEntries();
        log.info("Retrieved savedCommands list, size: {}", CommandLogger.getLogEntriesSize());
        if (savedCommands.isEmpty()) {
            log.info("savedCommands is empty, showing message box");
            FXGL.getDialogService().showMessageBox("No action to redo.");
            log.info("=== EXITING redoLastAction method (empty list) ===");
            return;
        } else {
            log.info("savedCommands is NOT empty, proceeding with redo logic");
            // What to actually do here?
            /*
             - reset the score and years in power to previous values
             - reset the pillarValues to previous values
             - show the last card again
             - update the UI accordingly
            */

            log.info("Redoing last action...");
            log.info("Total saved commands: {}", CommandLogger.getLogEntriesSize());
            for (CommandLogEntry entry : savedCommands) {
                log.info("Card Title: {}", entry.getCardTitle());
            }

            CommandLogEntry lastCommand = savedCommands.get(savedCommands.size() - 1);
            log.info("Last Card Title: {}", lastCommand.getCardTitle());
            log.info("Last Command Type: {}", lastCommand.getCommandType());

            if (lastCommand.getCommandType().equals("IntroSwipeCommand")) {
                log.info("Handling IntroSwipeCommand case");
                // Handle the specific case for intro swipes
                // This is the first card and involves choosing a Pharaoh -> reset entire game (not actually resetting, just reloading the choose-pharaoh card)
                log.info("BEFORE removal - savedCommands size: {}", CommandLogger.getLogEntriesSize());
                savedCommands.remove(savedCommands.size() - 1);
                CommandLogger.removeLogEntry();
                GameConfiguration.getInstance().resetSelectedMonarch();
                ModeConfiguration.getInstance().resetPillarValues();

                log.info("AFTER removal - savedCommands size: {}", CommandLogger.getLogEntriesSize());
                log.info("Total saved commands: {}", CommandLogger.getLogEntriesSize());
                log.info("=== EXITING redoLastAction method (IntroSwipeCommand) ===");

                return;
            }

            log.info("Checking if there's a second last command...");
            if (savedCommands.size() >= 2) {
                CommandLogEntry secondLastCommand = savedCommands.get(savedCommands.size() - 2);
                log.info("Second Last Command Type: {}", secondLastCommand.getCommandType());

                if (secondLastCommand.getCommandType().equals("IntroSwipeCommand")) {
                    log.info("Handling case where second last command is IntroSwipeCommand");
                    // Handle the case where the second last command is also an intro swipe
                    /* This means we are redoing the first card, so we need to reset the game state to
                     score 0 and years in power 0 without showing choose-pharaoh card again
                     --> basically same as normal reset but with score and YiP to 0 */
                    // For now, we just remove the last command
                    savedCommands.remove(savedCommands.size() - 1);
                    CommandLogger.removeLogEntry();
                } else {
                    log.info("Handling normal game action redo");
                    // Handle the case where the second last command is a normal game action
                    // This means we are redoing a normal game action, so we need to reset the game state
                    // to the previous values (score, years in power, pillar values)
                    // TODO implement this logic
                    // For now, we just remove the last command
                    savedCommands.remove(savedCommands.size() - 1);
                    CommandLogger.removeLogEntry();
                }
            } else {
                log.info("No second last command available (savedCommands size < 2)");
            }

            // Else we are redoing a normal game action, see comments above

            // Reset to the previous command (last command - 1)
            /* savedCommands.remove(savedCommands.size() - 1);
            log.info("Delete last command...");
            for (CommandLogEntry entry : savedCommands) {
                log.info("New Last Card Title: {}", entry.getCardTitle());
            }*/
        }
        log.info("=== EXITING redoLastAction method (end of method) ===");
        // This method can be used to redo the last action in the game.
    }
}
