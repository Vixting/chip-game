package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Represents a camera that can follow a target actor
 */
public class Camera {
    private final Pane gamePane;
    private Rectangle2D viewPort;
    private Actor target; // The actor the camera should follow.

    /**
     * Constructs a new Camera object.
     *
     * @param gamePane   The game pane.
     * @param viewWidth  The width of the viewport.
     * @param viewHeight The height of the viewport.
     */
    public Camera(Pane gamePane, double viewWidth, double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0, 0, viewWidth, viewHeight);

        // Clipping to ensure only a portion of the gamePane is visible.
        this.gamePane.setClip(new javafx.scene.shape.Rectangle(viewWidth, viewHeight));
        addPaneSizeListeners();
        addWindowSizeListeners();
    }

    /**
     * Adds listeners to the width and height properties of the gamePane.
     */
    private void addPaneSizeListeners() {
        gamePane.widthProperty().addListener((observable, oldValue, newValue) -> {
            viewPort = new Rectangle2D(viewPort.getMinX(), viewPort.getMinY(), newValue.doubleValue(), viewPort.getHeight());
            adjustCamera();
        });
        gamePane.heightProperty().addListener((observable, oldValue, newValue) -> {
            viewPort = new Rectangle2D(viewPort.getMinX(), viewPort.getMinY(), viewPort.getWidth(), newValue.doubleValue());
            adjustCamera();
        });
    }

    /**
     * Adds listeners to the width and height properties of the window (scene).
     */
    private void addWindowSizeListeners() {
        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.widthProperty().addListener((obs, oldVal, newVal) -> {
                    adjustCamera();
                });
                newScene.heightProperty().addListener((obs, oldVal, newVal) -> {
                    adjustCamera();
                });
            }
        });
    }

    /**
     * Sets the target actor for the camera to follow.
     *
     * @param target The target actor.
     */
    public void setTarget(Actor target) {
        this.target = target;

        // Create an AnimationTimer to periodically adjust the camera
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                adjustCamera();
            }
        };
        timer.start();
    }

    /**
     * Adjusts the camera's position based on the target actor's position.
     */
    void adjustCamera() {
        if (target == null) {
            return;
        }


        // Get the current scaling factors for the gamePane
        double scaleX = gamePane.getScaleX();
        double scaleY = gamePane.getScaleY();

        // Calculate the center coordinates of the target actor
        double targetCenterX = target.getLayoutX() + target.getFitWidth() / 2.0;
        double targetCenterY = target.getLayoutY() + target.getFitHeight() / 2.0;

        // Calculate the new translation values for the camera
        double newTranslateX = (viewPort.getWidth() / 2.0 - targetCenterX) * scaleX;
        double newTranslateY = (viewPort.getHeight() / 2.0 - targetCenterY) * scaleY;

        // Define the minimum and maximum translation values to avoid going out of bounds
        double minX = 0;
        double minY = 0;
        double maxX = (viewPort.getWidth() - viewPort.getWidth() / scaleX);
        double maxY = (viewPort.getHeight() - viewPort.getHeight() / scaleY);

        // Clamp the translation values to stay within bounds
        newTranslateX = -clampTranslate(-newTranslateX, minX, maxX);
        newTranslateY = -clampTranslate(-newTranslateY, minY, maxY);

        // If the difference is small, update immediately; otherwise, animate over a duration
        Duration animationDuration = Duration.millis(800);

        // Unbind previous translations and create a Timeline to smoothly update the camera position
        gamePane.translateXProperty().unbind();
        gamePane.translateYProperty().unbind();

        Timeline timeline = new Timeline();

        // Define keyframes to animate the camera's translation
        KeyValue kvX = new KeyValue(gamePane.translateXProperty(), newTranslateX);
        KeyValue kvY = new KeyValue(gamePane.translateYProperty(), newTranslateY);
        KeyFrame kf = new KeyFrame(animationDuration, kvX, kvY);

        // Add the keyframe to the timeline and play the animation
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * Helper method to clamp a value within a specified range.
     *
     * @param value        The value to clamp.
     * @param paneSize     The size of the pane.
     * @param viewportSize The size of the viewport.
     * @return The clamped value.
     */
    private double clampTranslate(double value, double paneSize, double viewportSize) {
        double scale = gamePane.getScaleX();  // Assuming x and y scales are the same.
        double minTranslate = paneSize * scale - viewportSize;

        if (minTranslate < 0) {
            // When viewport is larger than pane, clamp between 0 and minTranslate
            return Math.min(0, Math.max(value, minTranslate));
        } else {
            // When pane is larger than or equal to viewport, no clamping needed
            return value;
        }
    }
}
