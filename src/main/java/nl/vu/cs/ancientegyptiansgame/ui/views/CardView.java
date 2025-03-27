package nl.vu.cs.ancientegyptiansgame.ui.views;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import nl.vu.cs.ancientegyptiansgame.ui.components.SwipeRectangle;

import nl.vu.cs.ancientegyptiansgame.data.enums.SwipeSide;

public class CardView extends StackPane {

    private final double size;
    private final GameView gameView;
    private SwipeRectangle cardHolder;

    public CardView(double size, GameView gameView) {
        this.size = size;
        this.gameView = gameView;
        setPrefSize(size, size);
        setPadding(new Insets(20));
        initView();
    }

    private void initView() {
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        cardHolder = generateCardHolder();
        getChildren().add(cardHolder);
    }

    private SwipeRectangle generateCardHolder() {
        cardHolder = new SwipeRectangle(size, size);
        updateCard("welcome-card");

        cardHolder.setOnMouseReleased(event -> {
            double deltaX = cardHolder.getTranslateX();
            SwipeSide side = (deltaX < 0) ? SwipeSide.LEFT : SwipeSide.RIGHT;
            gameView.onCardSwiped(side);
        });

        return cardHolder;
    }



    public void updateCard(String imageName) {
        if (cardHolder != null) {
            var cardImage = FXGL.texture("cards/" + imageName);
            cardHolder.setFill(new ImagePattern(cardImage.getImage()));
            cardHolder.setRotate(0);
            cardHolder.setTranslateX(0);
        }
    }
}
