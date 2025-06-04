package nl.vu.cs.ancientegyptiansgame.ui.views;

import com.almasb.fxgl.dsl.FXGL;

import com.almasb.fxgl.texture.Texture;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import nl.vu.cs.ancientegyptiansgame.config.gamesettings.ModeConfiguration;
import nl.vu.cs.ancientegyptiansgame.data.model.Pillars;
import nl.vu.cs.ancientegyptiansgame.data.model.PillarData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class PillarView extends StackPane {

    private static final Logger logger = LoggerFactory.getLogger(PillarView.class);

    private final double height;
    private final List<Pillars> pillars;
    private final ModeConfiguration modeConfiguration;

    public PillarView(double width, double height) {
        this.height = height;
        this.modeConfiguration = ModeConfiguration.getInstance();

        this.pillars = List.of(Pillars.values());

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

    /**
     * Creates a texture representing the progress state of a pillar, updating its visible portion as the pillar's value changes.
     *
     * The returned texture visually reflects the current progress of the given pillar by displaying only the corresponding portion of the original image. The progress image automatically updates in response to changes in the pillar's value.
     *
     * @param originalImage the base texture for the pillar
     * @param pillarData the data source providing the current progress value
     * @return a texture showing the progress state of the pillar
     */
    private Texture generateProgressImage(Texture originalImage, PillarData pillarData) {
        var progressImage = originalImage.copy();

        progressImage.setFitWidth(originalImage.getFitWidth());
        adjustProgressImageSize(progressImage, pillarData.getValue().doubleValue());

        // Add a listener to update the progress image when the pillar value changes
        pillarData.addListener((updatedPillar, newValue) ->
                Platform.runLater(() -> adjustProgressImageSize(progressImage, newValue.doubleValue()))
        );

        progressImage.setEffect(getImageEffect());
        return progressImage;
    }


    /**
     * Adjusts the visible portion and display height of a progress image to reflect the given progress percentage.
     *
     * @param progressImage the texture representing the progress overlay
     * @param progress the progress value as a percentage (0–100)
     */
    private void adjustProgressImageSize(Texture progressImage, double progress) {
        double imageWidth = progressImage.getImage().getWidth();
        double imageHeight = progressImage.getImage().getHeight();
        double visibleHeight = imageHeight * (progress / 100.0);

        progressImage.setViewport(new Rectangle2D(0, imageHeight - visibleHeight, imageWidth, visibleHeight));
        progressImage.setFitHeight(visibleHeight * height / imageHeight);
    }
}