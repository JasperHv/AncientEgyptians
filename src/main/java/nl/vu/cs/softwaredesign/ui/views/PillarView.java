package nl.vu.cs.softwaredesign.ui.views;

import com.almasb.fxgl.dsl.FXGL;
import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.texture.Texture;
import javafx.beans.property.IntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nl.vu.cs.softwaredesign.data.config.ConfigManager;
import nl.vu.cs.softwaredesign.data.model.Pillar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PillarView extends StackPane {

    private static final Logger logger = LoggerFactory.getLogger(PillarView.class);

    private final double height;
    private final List<Pillar> pillars;

    public PillarView(double width, double height) {
        this.height = height;

        this.pillars = ConfigManager.getInstance("Normal Mode").getPillars();

        for (Pillar pillar : pillars) {
            // Assuming getip returns the progress property for the pillar name
            IntegerProperty progressProp = getip(pillar.getName().toLowerCase());
            logger.info("Pillar {} initial progress: {}", pillar.getName(), progressProp.get());
        }

        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
        setPrefSize(width, height);

        initPillars();
    }

    private void initPillars() {
        var hbox = new HBox();
        int numPillars = pillars.size();

        logger.info("Initializing {} pillars...", numPillars);

        hbox.setPadding(new Insets(5, 10, 5, 10));

        var pillarImages = getPillarImages();

        for (int i = 0; i < numPillars; i++) {
            if (i > 0) {
                var spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                hbox.getChildren().add(spacer);
            }
            hbox.getChildren().add(pillarImages.get(i));
        }

        getChildren().addAll(hbox);
        logger.info("Pillars initialized successfully.");
    }

    private List<StackPane> getPillarImages() {
        return pillars.stream().map(pillar -> {
            logger.debug("Loading image for pillar -> {}", pillar.getName());
            var originalImage = FXGL.texture("pillars/" + pillar.getImage());
            originalImage.setFitWidth(height);
            originalImage.setFitHeight(height);

            var progressImage = generateProgressImage(pillar.getName(), originalImage);

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
        logger.debug("Generating progress image for {}", pillarName);
        var progressImage = originalImage.copy();
        IntegerProperty pillarProgressProp = getip(pillarName.toLowerCase()); // Ensure lowercase consistency

        progressImage.setFitWidth(originalImage.getFitWidth());
        adjustProgressImageSize(progressImage, pillarProgressProp.doubleValue());

        pillarProgressProp.addListener((observable, oldValue, newValue) -> {
            logger.info("Progress updated for {} -> {}", pillarName, newValue);
            adjustProgressImageSize(progressImage, newValue.doubleValue());
        });

        progressImage.setEffect(getImageEffect());
        return progressImage;
    }

    private void adjustProgressImageSize(Texture progressImage, double progress) {
        double imageWidth = progressImage.getImage().getWidth();
        double imageHeight = progressImage.getImage().getHeight();
        double visibleHeight = imageHeight * (progress) / height;

        progressImage.setViewport(new Rectangle2D(0, imageHeight - visibleHeight, imageWidth, visibleHeight));
        progressImage.setFitHeight(visibleHeight * height / imageHeight);
    }

}