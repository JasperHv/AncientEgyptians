package nl.vu.cs.softwaredesign.ui.components;
import nl.vu.cs.softwaredesign.data.model.Influence;
import com.almasb.fxgl.dsl.FXGL;
import javafx.animation.TranslateTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ParallelTransition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.beans.property.IntegerProperty;
import java.util.List;
import java.util.stream.Collectors;

public class SwipeRectangle extends Rectangle {

    private static final double SWIPE_THRESHOLD = FXGL.getAppWidth() / 6.0;
    private double initialX;

    public SwipeRectangle(double width, double height) {
        super(width, height);
        addSwipeBehaviour();
    }

    private void addSwipeBehaviour() {
        setOnMousePressed(event -> initialX = event.getSceneX());

        setOnMouseDragged(event -> {
            var deltaX = event.getSceneX() - initialX;
            var translateX = deltaX > 0 ? Math.min(deltaX, SWIPE_THRESHOLD) : Math.max(deltaX, -SWIPE_THRESHOLD);
            double rotationFactor = (translateX / SWIPE_THRESHOLD) * 15;

            setRotate(rotationFactor);
            setTranslateX(translateX);
        });

        setOnMouseReleased(event -> {
            double deltaX = getTranslateX();
            if (Math.abs(deltaX) < SWIPE_THRESHOLD) {
                resetPosition();
                return;
            }

            boolean isSwipeLeft = deltaX < 0;
            animateSwipe(isSwipeLeft);
        });
    }

    private void animateSwipe(boolean isSwipeLeft) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1), this);
        translateTransition.setToX(isSwipeLeft ? -FXGL.getAppWidth() : FXGL.getAppWidth());
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), this);
        rotateTransition.setToAngle(isSwipeLeft ? -90 : 90);

        new ParallelTransition(translateTransition, rotateTransition).play();
    }

    private void resetPosition() {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), this);
        rotateTransition.setToAngle(0);
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), this);
        translateTransition.setToX(0);

        new ParallelTransition(translateTransition, rotateTransition).play();
    }
}
