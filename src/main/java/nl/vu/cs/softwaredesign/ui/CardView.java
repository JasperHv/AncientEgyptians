package nl.vu.cs.softwaredesign.ui;

import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import nl.vu.cs.softwaredesign.ui.components.SwipeRectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.stream.Collectors;


import static nl.vu.cs.softwaredesign.AncientEgyptiansApp.getModeConfiguration;

public class CardView extends StackPane {

    private final double size;


    public CardView(double size) {
        this.size = size;
        setPrefSize(size, size);
        setPadding(new Insets(20));
        initView();
    }


    private void initView() {
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        var cardHolder = generateCardHolder();
        getChildren().add(cardHolder);
        getChildren().addAll(generateCardsRectangles());
    }

    private List<SwipeRectangle> generateCardsRectangles() {
        var cards = new ArrayList<>(List.copyOf(getModeConfiguration().getCards()));
        Collections.shuffle(cards);
        return cards.stream().map(card -> {
            var swipeRectangle = new SwipeRectangle(card, size, size);
            var backgroundImage = FXGL.texture("cards/" + card.getImage());

            swipeRectangle.setStrokeWidth(5.0);
            swipeRectangle.setArcWidth(20.0);
            swipeRectangle.setArcHeight(20.0);
            swipeRectangle.setFill(new ImagePattern(backgroundImage.getImage()));

            return swipeRectangle;
        }).collect(Collectors.toList());
    }

    private Rectangle generateCardHolder() {
        var cardHolder = new Rectangle(size, size);
        var backgroundImage = FXGL.texture("cards/cards-background.png");

        cardHolder.setStrokeWidth(5.0);
        cardHolder.setArcWidth(20.0);
        cardHolder.setArcHeight(20.0);
        cardHolder.setFill(new ImagePattern(backgroundImage.getImage()));

        return cardHolder;
    }



}
