package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import javafx.animation.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Camera {
    // The main game pane that the camera will be applied to.
    private final Pane gamePane;

    // The viewport defines the visible area of the gamePane.
    private Rectangle2D viewPort;

    // The actor (e.g., player character) that the camera will follow.
    private Actor target;

    // Timer used to periodically adjust the camera's position.
    private AnimationTimer timer;

    /**
     * Constructor for the Camera class.
     *
     * @param gamePane The main game pane.
     * @param viewWidth Width of the viewport.
     * @param viewHeight Height of the viewport.
     */
    public Camera(Pane gamePane, double viewWidth, double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0, 0, viewWidth, viewHeight);

        // Set a clip to ensure only a portion of the gamePane is visible.
        this.gamePane.setClip(new javafx.scene.shape.Rectangle(viewWidth, viewHeight));

        // Add listeners to handle changes in the gamePane's size.
        addPaneSizeListeners();
    }

    /**
     * Set the target actor for the camera to follow.
     *
     * @param target The actor to be followed by the camera.
     */
    public void setTarget(Actor target) {
        this.target = target;
        startCameraAdjustmentTimer();
    }

    // Add listeners to the gamePane's width and height properties.
    private void addPaneSizeListeners() {
        gamePane.widthProperty().addListener((observable, oldValue, newValue) -> updateViewPortWidth(newValue.doubleValue()));
        gamePane.heightProperty().addListener((observable, oldValue, newValue) -> updateViewPortHeight(newValue.doubleValue()));
    }

    // Update the viewport's width and adjust the camera accordingly.
    private void updateViewPortWidth(double newWidth) {
        viewPort = new Rectangle2D(viewPort.getMinX(), viewPort.getMinY(), newWidth, viewPort.getHeight());
        adjustCamera();
    }

    // Update the viewport's height and adjust the camera accordingly.
    private void updateViewPortHeight(double newHeight) {
        viewPort = new Rectangle2D(viewPort.getMinX(), viewPort.getMinY(), viewPort.getWidth(), newHeight);
        adjustCamera();
    }

    // Start a timer to periodically adjust the camera's position.
    private void startCameraAdjustmentTimer() {
        if (timer != null) {
            timer.stop();
        }
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                adjustCamera();
            }
        };
        timer.start();
    }

    // Adjust the camera's position based on the target actor's position.
    private void adjustCamera() {
        if (target == null) {
            return;
        }

        // Calculate the center coordinates of the target actor.
        double targetCenterX = target.getLayoutX() + target.getFitWidth() / 2.0;
        double targetCenterY = target.getLayoutY() + target.getFitHeight() / 2.0;

        // Calculate the new translation values for the camera.
        double scaleX = gamePane.getScaleX();
        double scaleY = gamePane.getScaleY();
        double newTranslateX = (viewPort.getWidth() / 2.0 - targetCenterX) * scaleX;
        double newTranslateY = (viewPort.getHeight() / 2.0 - targetCenterY) * scaleY;

        // Clamp the translation values to ensure the camera stays within bounds.
        newTranslateX = -clampTranslate(-newTranslateX, viewPort.getWidth(), viewPort.getWidth() / scaleX);
        newTranslateY = -clampTranslate(-newTranslateY, viewPort.getHeight(), viewPort.getHeight() / scaleY);

        // Animate the camera's translation to the new position.
        animateCameraTranslation(newTranslateX, newTranslateY);
    }

    // Animate the camera's translation to smoothly move to the specified position.
    private void animateCameraTranslation(double newTranslateX, double newTranslateY) {
        gamePane.translateXProperty().unbind();
        gamePane.translateYProperty().unbind();

        Timeline timeline = new Timeline();
        KeyValue kvX = new KeyValue(gamePane.translateXProperty(), newTranslateX);
        KeyValue kvY = new KeyValue(gamePane.translateYProperty(), newTranslateY);
        KeyFrame kf = new KeyFrame(Duration.millis(1000), kvX, kvY);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    /**
     * Helper method to clamp a value within a specified range.
     *
     * @param value The value to be clamped.
     * @param paneSize The size of the game pane.
     * @param viewportSize The size of the viewport.
     * @return The clamped value.
     */
    private double clampTranslate(double value, double paneSize, double viewportSize) {
        double scale = gamePane.getScaleX();
        double minTranslate = paneSize * scale - viewportSize;

        if (minTranslate < 0) {
            return Math.min(0, Math.max(value, minTranslate));
        } else {
            return value;
        }
    }
}
