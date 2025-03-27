package nl.vu.cs.softwaredesign.ui.views;

import com.almasb.fxgl.dsl.FXGL;

import com.almasb.fxgl.texture.Texture;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nl.vu.cs.softwaredesign.data.config.gamesettings.ModeConfiguration;
import nl.vu.cs.softwaredesign.data.model.Pillar;
import nl.vu.cs.softwaredesign.pillars.PillarData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PillarView extends StackPane {

    private static final Logger logger = LoggerFactory.getLogger(PillarView.class);

    private final double height;
    private final List<Pillar> pillars;
    private final ModeConfiguration modeConfiguration;

    public PillarView(double width, double height) {
        this.height = height;
        this.modeConfiguration = ModeConfiguration.getInstance();

        this.pillars = List.of(Pillar.values());

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
            logger.info("Loading image for pillar -> {}", pillar.name());
            var originalImage = FXGL.texture("pillars/" + pillar.getImage());
            originalImage.setFitWidth(height);
            originalImage.setFitHeight(height);

            PillarData pillarData = modeConfiguration.getPillarData(pillar);
            var progressImage = generateProgressImage(originalImage, pillarData);

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

    private Texture generateProgressImage(Texture originalImage, PillarData pillarData) {
        var progressImage = originalImage.copy();

        progressImage.setFitWidth(originalImage.getFitWidth());
        adjustProgressImageSize(progressImage, pillarData.getValue().doubleValue());

        // Add a listener to update the progress image when the pillar value changes
        pillarData.addListener((updatedPillar, newValue) ->
            adjustProgressImageSize(progressImage, newValue.doubleValue()));

        progressImage.setEffect(getImageEffect());
        return progressImage;
    }

    private void adjustProgressImageSize(Texture progressImage, double progress) {
        double imageWidth = progressImage.getImage().getWidth();
        double imageHeight = progressImage.getImage().getHeight();
        double visibleHeight = imageHeight * (progress / 100.0);

        progressImage.setViewport(new Rectangle2D(0, imageHeight - visibleHeight, imageWidth, visibleHeight));
        progressImage.setFitHeight(visibleHeight * height / imageHeight);
    }
}