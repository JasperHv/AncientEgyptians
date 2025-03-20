package nl.vu.cs.softwaredesign.data;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.ui.views.CardView;
import nl.vu.cs.softwaredesign.ui.views.GameView;
import nl.vu.cs.softwaredesign.data.model.Card;
import nl.vu.cs.softwaredesign.data.enums.SwipeSide;

import java.util.HashMap;
import java.util.Map;

public class CardController {
    private final CardView cardView;
    private final Map<String, String> cardPillarToImageMap;
    private GameView gameView;

    public CardController(CardView cardView, GameView gameView) {
        this.cardView = cardView;
        this.gameView = gameView;
        this.cardPillarToImageMap = new HashMap<>();
        loadPillarImages();
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
                messageLabel.setMaxWidth(Double.MAX_VALUE);
                messageLabel.setText(cardName);
        }

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), messageLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }
}
