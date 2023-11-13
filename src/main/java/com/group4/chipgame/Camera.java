package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Camera {
    private final Pane gamePane;
    private Rectangle2D viewPort;
    private Actor target;

    public Camera(Pane gamePane, double viewWidth, double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0, 0, viewWidth, viewHeight);
        this.gamePane.setClip(new javafx.scene.shape.Rectangle(viewWidth, viewHeight));
        addPaneSizeListeners();
        addWindowSizeListeners();
    }

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

    private void addWindowSizeListeners() {
        gamePane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                newScene.widthProperty().addListener((obs, oldVal, newVal) -> adjustCamera());
                newScene.heightProperty().addListener((obs, oldVal, newVal) -> adjustCamera());
            }
        });
    }

    public void setTarget(Actor target) {
        this.target = target;
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                adjustCamera();
            }
        }.start();
    }

    void adjustCamera() {
        if (target == null) return;

        double scaleX = gamePane.getScaleX();
        double scaleY = gamePane.getScaleY();
        double targetCenterX = target.getLayoutX() + target.getFitWidth() / 2.0;
        double targetCenterY = target.getLayoutY() + target.getFitHeight() / 2.0;
        double newTranslateX = (viewPort.getWidth() / 2.0 - targetCenterX) * scaleX;
        double newTranslateY = (viewPort.getHeight() / 2.0 - targetCenterY) * scaleY;
        double minX = 0;
        double minY = 0;
        double maxX = (viewPort.getWidth() - viewPort.getWidth() / scaleX);
        double maxY = (viewPort.getHeight() - viewPort.getHeight() / scaleY);
        newTranslateX = -clampTranslate(-newTranslateX, minX, maxX);
        newTranslateY = -clampTranslate(-newTranslateY, minY, maxY);

        double distance = Math.hypot(gamePane.getTranslateX() - newTranslateX, gamePane.getTranslateY() - newTranslateY);
        double speedFactor = 500;
        double scaledDuration = Math.max(speedFactor / (distance + 1), 50);

        Duration animationDuration = Duration.millis(scaledDuration);
        gamePane.translateXProperty().unbind();
        gamePane.translateYProperty().unbind();

        Timeline timeline = new Timeline(new KeyFrame(animationDuration, new KeyValue(gamePane.translateXProperty(), newTranslateX), new KeyValue(gamePane.translateYProperty(), newTranslateY)));
        timeline.play();
    }

    private double clampTranslate(double value, double paneSize, double viewportSize) {
        double scale = gamePane.getScaleX();
        double minTranslate = paneSize * scale - viewportSize;
        return minTranslate < 0 ? Math.min(0, Math.max(value, minTranslate)) : value;
    }
}
