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
 * The Camera class manages the view and focus of the game, allowing for smooth camera transitions.
 */
public class Camera {

    private final Pane gamePane;
    private Rectangle2D viewPort;
    private Actor target;
    private Timeline currentTimeline;
    private AnimationTimer cameraTimer;

    /**
     * Constructs a Camera with the specified game pane and view dimensions.
     *
     * @param gamePane   The game pane to manage.
     * @param viewWidth  The width of the view.
     * @param viewHeight The height of the view.
     */
    public Camera(Pane gamePane, double viewWidth, double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0, 0, viewWidth, viewHeight);
        this.gamePane.setClip(new javafx.scene.shape.Rectangle(viewWidth, viewHeight));
        addPaneSizeListeners();
        addWindowSizeListeners();
    }

    /**
     * Adds listeners to the game pane's width and height properties for updating the viewport dimensions.
     */
    private void addPaneSizeListeners() {
        gamePane.widthProperty().addListener((observable, oldValue, newValue) ->
                updateViewportWidth(newValue.doubleValue()));
        gamePane.heightProperty().addListener((observable, oldValue, newValue) ->
                updateViewportHeight(newValue.doubleValue()));
    }

    /**
     * Updates the viewport width based on the new width.
     *
     * @param newWidth The new width of the viewport.
     */
    private void updateViewportWidth(double newWidth) {
        viewPort = new Rectangle2D(viewPort.getMinX(), viewPort.getMinY(), newWidth, viewPort.getHeight());
        adjustCamera();
    }

    /**
     * Updates the viewport height based on the new height.
     *
     * @param newHeight The new height of the viewport.
     */
    private void updateViewportHeight(double newHeight) {
        viewPort = new Rectangle2D(viewPort.getMinX(), viewPort.getMinY(), viewPort.getWidth(), newHeight);
        adjustCamera();
    }

    /**
     * Adds listeners to the game pane's scene property for adjusting the camera when the scene changes.
     */
    private void addWindowSizeListeners() {
        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.widthProperty().addListener((obs, oldVal, newVal) -> adjustCamera());
                newScene.heightProperty().addListener((obs, oldVal, newVal) -> adjustCamera());
            }
        });
    }

    /**
     * Calculates the dynamic duration of a camera transition based on the target position.
     *
     * @param newTranslateX The new x-coordinate for the camera.
     * @param newTranslateY The new y-coordinate for the camera.
     * @return The calculated dynamic duration.
     */
    private Duration calculateDynamicDuration(double newTranslateX, double newTranslateY) {
        double currentX = gamePane.getTranslateX();
        double currentY = gamePane.getTranslateY();
        double distance = Math.sqrt(Math.pow(newTranslateX - currentX, 2) + Math.pow(newTranslateY - currentY, 2));
        return Duration.millis(Math.min(300 + distance * 0.4, 1000));
    }

    /**
     * Sets the target actor for the camera to follow.
     *
     * @param target The actor to follow.
     */
    public void setTarget(Actor target) {
        this.target = target;
        if (cameraTimer != null) {
            cameraTimer.stop();
        }
        cameraTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                adjustCamera();
            }
        };
        cameraTimer.start();
    }

    /**
     * Adjusts the camera position based on the target actor's position.
     */
    void adjustCamera() {
        if (target == null) {
            return;
        }

        double newTranslateX = calculateTranslateX();
        double newTranslateY = calculateTranslateY();
        animateCameraTransition(newTranslateX, newTranslateY);
    }

    /**
     * Animates the camera transition to the specified coordinates.
     *
     * @param newTranslateX The new x-coordinate for the camera.
     * @param newTranslateY The new y-coordinate for the camera.
     */
    private void animateCameraTransition(double newTranslateX, double newTranslateY) {
        Duration animationDuration = calculateDynamicDuration(newTranslateX, newTranslateY);

        if (currentTimeline != null) {
            currentTimeline.stop();
        }

        KeyValue kvX = new KeyValue(gamePane.translateXProperty(), newTranslateX);
        KeyValue kvY = new KeyValue(gamePane.translateYProperty(), newTranslateY);
        KeyFrame kf = new KeyFrame(animationDuration, kvX, kvY);

        currentTimeline = new Timeline(kf);
        currentTimeline.play();
    }

    /**
     * Calculates the new x-coordinate for the camera based on the target actor's position.
     *
     * @return The calculated x-coordinate.
     */
    private double calculateTranslateX() {
        double targetCenterX = target.getLayoutX() + target.getFitWidth() / 2.0;
        double newTranslateX = viewPort.getWidth() / 2.0 - targetCenterX;
        return -clampTranslate(-newTranslateX, gamePane.getScaleX());
    }

    /**
     * Calculates the new y-coordinate for the camera based on the target actor's position.
     *
     * @return The calculated y-coordinate.
     */
    private double calculateTranslateY() {
        double targetCenterY = target.getLayoutY() + target.getFitHeight() / 2.0;
        double newTranslateY = viewPort.getHeight() / 2.0 - targetCenterY;
        return -clampTranslate(-newTranslateY, gamePane.getScaleY());
    }

    /**
     * Clamps the translation value within the specified range.
     *
     * @param value The value to clamp.
     * @param scale The scale factor for the game pane.
     * @return The clamped translation value.
     */
    private double clampTranslate(double value, double scale) {
        double minTranslate = gamePane.getWidth() * scale - viewPort.getWidth();
        return minTranslate < 0 ? Math.min(0, Math.max(value, minTranslate)) : value;
    }
}
