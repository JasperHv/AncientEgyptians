package nl.vu.cs.softwaredesign.ui;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import nl.vu.cs.softwaredesign.pillars.PillarData;

import java.util.List;
import java.util.stream.Collectors;

import static nl.vu.cs.softwaredesign.AncientEgyptiansApp.getConfiguration;
import static nl.vu.cs.softwaredesign.AncientEgyptiansApp.getPillarObserver;

public class PillarView extends StackPane {

    private final double height;

    public PillarView(double width, double height) {
        this.height = height;

        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        setPrefSize(width, height);


        initPillars();

    }

    private void initPillars() {
        var hbox = new HBox();
        int numPillars = getConfiguration().getPillars().size();

        hbox.setPadding(new Insets(5, 10, 5, 10));

        var pillars = getPillarImages();

        for (int i = 0; i < numPillars; i++) {
            if (i > 0) {
                var spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().add(spacer);
            }
            hbox.getChildren().add(pillars.get(i));
        }

        getChildren().addAll(hbox);
    }

    private List<StackPane> getPillarImages() {
        return getConfiguration().getPillars().stream().map(pillar -> {
            var pillarName = pillar.getName();
            var originalImage = FXGL.texture("pillars/" + pillar.getImage());
            originalImage.setFitWidth(height);
            originalImage.setFitHeight(height);

            var progressImage = generateProgressImage(pillarName, originalImage);

            StackPane stackPane = new StackPane(originalImage, progressImage);
            StackPane.setAlignment(progressImage, Pos.BOTTOM_CENTER);

            return stackPane;
        }).collect(Collectors.toList());
    }

    private ColorAdjust getImageEffect() {
        var colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-1.0);
        return colorAdjust;
    }

    private Texture generateProgressImage(String pillarName, Texture originalImage) {
        var progressImage = originalImage.copy();
        PillarData pillarData = getPillarObserver().getPillarData(pillarName);

        progressImage.setFitWidth(originalImage.getFitWidth());
        adjustProgressImageSize(progressImage, pillarData.getValue());
        pillarData.addListener(newValue -> adjustProgressImageSize(progressImage, newValue.doubleValue()));

        progressImage.setEffect(getImageEffect());
        return progressImage;
    }

    private void adjustProgressImageSize(Texture progressImage, double progress) {
        if(progress <= 0.0) {
            progressImage.setVisible(false);
            progressImage.setViewport(new Rectangle2D(0, 0, 0, 0));
            return;
        }

        progressImage.setVisible(true);
        double imageWidth = progressImage.getImage().getWidth();
        double imageHeight = progressImage.getImage().getHeight();
        double visibleHeight = imageHeight * (progress) / height;

        progressImage.setViewport(new Rectangle2D(0, imageHeight - visibleHeight, imageWidth, visibleHeight));
        progressImage.setFitHeight(visibleHeight * height / imageHeight);
    }

}
