package nl.vu.cs.softwaredesign.ui.views;

import com.almasb.fxgl.dsl.FXGL;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import nl.vu.cs.softwaredesign.ui.components.SwipeRectangle;
import static com.almasb.fxgl.dsl.FXGLForKtKt.inc;

import nl.vu.cs.softwaredesign.data.enums.SwipeSide;

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
        var cardHolder = new SwipeRectangle(size, size);
        updateCard("welcome-card");

        cardHolder.setOnMouseReleased(event -> {
            double deltaX = cardHolder.getTranslateX();
            SwipeSide side = (deltaX < 0) ? SwipeSide.LEFT : SwipeSide.RIGHT;
            gameView.onCardSwiped(side);
        });

        return cardHolder;
    }



    public void updateCard(String cardName) {
        if (cardHolder != null) {
            var backgroundImage = FXGL.texture("cards/" + cardName + ".png");
            cardHolder.setFill(new ImagePattern(backgroundImage.getImage()));
            cardHolder.setRotate(0);
            cardHolder.setTranslateX(0);
        }
    }
}
