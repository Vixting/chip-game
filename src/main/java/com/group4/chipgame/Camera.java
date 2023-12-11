package com.group4.chipgame;

import com.group4.chipgame.entities.actors.Actor;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * This class represents a camera that focuses
 * on a target actor in a game scene.
 * It manages the viewport and adjusts the
 * view based on the position of the target actor.
 * @author William Buckley
 */
public class Camera {
    private final Pane gamePane;
    private Rectangle2D viewPort;
    private Actor target;
    private Timeline currentTimeline;
    private AnimationTimer cameraTimer;

    private static final double MIN_ANIMATION_DURATION = 300;
    private static final double MAX_ANIMATION_DURATION = 1000;
    private static final double ANIMATION_SPEED_MULTIPLIER = 0.4;

    /**
     * Constructs a Camera object.
     *
     * @param gamePane The game pane that the camera will be applied to.
     * @param viewWidth The initial width of the camera's viewport.
     * @param viewHeight The initial height of the camera's viewport.
     */
    public Camera(final Pane gamePane,
                  final double viewWidth,
                  final double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0,
                0,
                viewWidth,
                viewHeight);
        this.gamePane.setClip(new javafx.scene.shape.
                Rectangle(viewWidth, viewHeight));
        addPaneSizeListeners();
        addWindowSizeListeners();
    }

    /**
     * Adds listeners to adjust the viewport when the pane's size changes.
     */
    private void addPaneSizeListeners() {
        gamePane.widthProperty().
                addListener((observable, oldValue, newValue) ->
                updateViewportWidth(newValue.doubleValue()));
        gamePane.heightProperty().
                addListener((observable, oldValue, newValue) ->
                updateViewportHeight(newValue.doubleValue()));
    }

    /**
     * Updates the viewport's width.
     *
     * @param newWidth The new width of the viewport.
     */
    private void updateViewportWidth(final double newWidth) {
        viewPort = new Rectangle2D(viewPort.getMinX(),
                viewPort.getMinY(),
                newWidth,
                viewPort.getHeight());
        adjustCamera();
    }

    /**
     * Updates the viewport's height.
     *
     * @param newHeight The new height of the viewport.
     */
    private void updateViewportHeight(final double newHeight) {
        viewPort = new Rectangle2D(viewPort.getMinX(),
                viewPort.getMinY(),
                viewPort.getWidth(),
                newHeight);
        adjustCamera();
    }

    /**
     * Adds listeners to adjust the camera when the window size changes.
     */
    private void addWindowSizeListeners() {
        gamePane.sceneProperty().addListener(
                (observable, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.widthProperty().addListener(
                        (obs, oldVal, newVal) -> adjustCamera());
                newScene.heightProperty().addListener(
                        (obs, oldVal, newVal) -> adjustCamera());
            }
        });
    }

    /**
     * Calculates the dynamic duration for the
     * camera's movement animation based on distance.
     *
     * @param newTranslateX The target X translation.
     * @param newTranslateY The target Y translation.
     * @return A Duration object representing the calculated duration.
     */
    private Duration calculateDynamicDuration(final double newTranslateX,
                                              final double newTranslateY) {
        double currentX = gamePane.getTranslateX();
        double currentY = gamePane.getTranslateY();
        double distance = Math.sqrt(Math.pow(newTranslateX
                - currentX, 2)
                + Math.pow(newTranslateY
                - currentY, 2));
        return Duration.millis(Math.min(MIN_ANIMATION_DURATION
                        + distance
                        * ANIMATION_SPEED_MULTIPLIER, MAX_ANIMATION_DURATION));
    }

    /**
     * Sets the target actor for the camera to follow.
     *
     * @param target The actor that the camera should follow.
     */
    public void setTarget(final Actor target) {
        this.target = target;
        if (cameraTimer != null) {
            cameraTimer.stop();
        }
        cameraTimer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                adjustCamera();
            }
        };
        cameraTimer.start();
    }

    /**
     * Updates the viewport size to match the game pane's size.
     */
    public void updateViewportSize() {
        double width = gamePane.getWidth();
        double height = gamePane.getHeight();
        viewPort = new Rectangle2D(viewPort.getMinX(),
                viewPort.getMinY(),
                width,
                height);
    }

    /**
     * Adjusts the camera's position based on the target actor's location.
     */
    void adjustCamera() {
        if (target == null) {
            System.out.println("No target set for camera");
            return;
        }

        updateViewportSize();

        double newTranslateX = calculateTranslateX();
        double newTranslateY = calculateTranslateY();
        animateCameraTransition(newTranslateX, newTranslateY);
    }

    /**
     * Animates the camera's transition to a new position.
     *
     * @param newTranslateX The target X translation.
     * @param newTranslateY The target Y translation.
     */
    private void animateCameraTransition(final double newTranslateX,
                                         final double newTranslateY) {
        Duration animationDuration =
                calculateDynamicDuration(newTranslateX, newTranslateY);

        if (currentTimeline != null) {
            currentTimeline.stop();
        }

        KeyValue kvX = new KeyValue(
                gamePane.translateXProperty(), newTranslateX);
        KeyValue kvY = new KeyValue(
                gamePane.translateYProperty(), newTranslateY);
        KeyFrame kf = new KeyFrame(
                animationDuration, kvX, kvY);

        currentTimeline = new Timeline(kf);
        currentTimeline.play();
    }

    /**
     * Calculates the X translation based on the target's position.
     *
     * @return The calculated X translation.
     */
    private double calculateTranslateX() {
        double targetCenterX =
                target.getLayoutX() + target.getFitWidth() / 2.0;
        double newTranslateX =
                viewPort.getWidth() / 2.0 - targetCenterX;
        return -clampTranslate(
                -newTranslateX, gamePane.getScaleX());
    }

    /**
     * Calculates the Y translation based on the target's position.
     *
     * @return The calculated Y translation.
     */
    private double calculateTranslateY() {
        double targetCenterY = target.getLayoutY()
                + target.getFitHeight() / 2.0;
        double newTranslateY = viewPort.getHeight()
                / 2.0 - targetCenterY;
        return -clampTranslate(-newTranslateY, gamePane.getScaleY());
    }

    /**
     * Clamps the translation values to prevent
     * the camera from moving out of bounds.
     *
     * @param value The translation value to clamp.
     * @param scale The scale factor of the game pane.
     * @return The clamped translation value.
     */
    private double clampTranslate(final double value,
                                  final double scale) {
        double minTranslate = gamePane.getWidth()
                * scale
                - viewPort.getWidth();
        return minTranslate < 0
                ? Math.min(0, Math.max(value, minTranslate))
                : value;
    }
}
