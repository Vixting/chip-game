package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class Camera {

    private final Pane gamePane;
    private  Rectangle2D viewPort;

    private Actor target; // The actor the camera should follow.

    public Camera(Pane gamePane, double viewWidth, double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0, 0, viewWidth, viewHeight);

        // Clipping to ensure only a portion of the gamePane is visible.
        this.gamePane.setClip(new javafx.scene.shape.Rectangle(viewWidth, viewHeight));
        // Add a listener to the width property of the gamePane
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
     * Set the target for the camera to follow.
     */
    public void setTarget(Actor target) {
        this.target = target;


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                adjustCamera();
            }
        };
        timer.start();
    }





    /**
     * Adjust the camera's position to ensure the target actor is in the center.
     */
    void adjustCamera() {
        if (target == null) {
            return;
        }

        double scaleX = gamePane.getScaleX();
        double scaleY = gamePane.getScaleY();

        System.out.println(System.out.printf("ScaleX: %f, ScaleY: %f\n", scaleX, scaleY));

        double targetCenterX = target.getLayoutX() + target.getFitWidth() / 2.0;
        double targetCenterY = target.getLayoutY() + target.getFitHeight() / 2.0;

        System.out.printf("TargetCenterX: %f, TargetCenterY: %f\n", targetCenterX, targetCenterY);

        double newTranslateX = (viewPort.getWidth() / 2.0 - targetCenterX) * scaleX;
        double newTranslateY = (viewPort.getHeight() / 2.0 - targetCenterY) * scaleY;

        System.out.printf("viewPort.getWidth(): %f, viewPort.getHeight(): %f\n", viewPort.getWidth(), viewPort.getHeight());

        double minX = 0;
        double minY = 0;
        double maxX = (viewPort.getWidth() - viewPort.getWidth() / scaleX);
        double maxY = (viewPort.getHeight() - viewPort.getHeight() / scaleY);

        newTranslateX = -clampTranslate(-newTranslateX, minX, maxX);
        newTranslateY = -clampTranslate(-newTranslateY, minY, maxY);

        System.out.printf("NewTranslateX: %f, NewTranslateY: %f\n", newTranslateX, newTranslateY);

        // Unbind the properties
        gamePane.translateXProperty().unbind();
        gamePane.translateYProperty().unbind();

        Timeline timeline = new Timeline();

        KeyValue kvX = new KeyValue(gamePane.translateXProperty(), newTranslateX);
        KeyValue kvY = new KeyValue(gamePane.translateYProperty(), newTranslateY);
        KeyFrame kf = new KeyFrame(Duration.millis(1000), kvX, kvY);

        timeline.getKeyFrames().add(kf);
        timeline.play();
    }





    private double clampTranslate(double value, double paneSize, double viewportSize) {
        double scale = gamePane.getScaleX();  // Assuming x and y scales are the same.
        double minTranslate = paneSize * scale - viewportSize;

        if (minTranslate < 0) {
            // When viewport is larger than pane, clamp between 0 and minTranslate
            double clampedValue = Math.min(0, Math.max(value, minTranslate));
            return clampedValue;
        } else {
            // When pane is larger than or equal to viewport, no clamping needed
            return value;
        }
    }


}
