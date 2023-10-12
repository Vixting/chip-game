package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import javafx.beans.binding.Bindings;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;

public class Camera {

    private final Pane gamePane;
    private final Rectangle2D viewPort;

    private Actor target; // The actor the camera should follow.

    public Camera(Pane gamePane, double viewWidth, double viewHeight) {
        this.gamePane = gamePane;
        this.viewPort = new Rectangle2D(0, 0, viewWidth, viewHeight);

        // Clipping to ensure only a portion of the gamePane is visible.
        this.gamePane.setClip(new javafx.scene.shape.Rectangle(viewWidth, viewHeight));
    }

    /**
     * Set the target for the camera to follow.
     */
    public void setTarget(Actor target) {
        this.target = target;
        adjustCamera(); // Adjust the camera immediately.

        // Ensure camera adjusts when the actor moves.
        target.xProperty().addListener(observable -> {
            System.out.println("X property changed!");
            adjustCamera();
        });

        target.yProperty().addListener(observable -> {
            System.out.println("Y property changed!");
            adjustCamera();
        });

    }

    /**
     * Adjust the camera's position to ensure the target actor is in the center.
     */
    private void adjustCamera() {
        double targetCenterX = target.getX() + target.getFitWidth() / 2.0;
        double targetCenterY = target.getY() + target.getFitHeight() / 2.0;

        System.out.println("Target Center X: " + targetCenterX);
        System.out.println("Target Center Y: " + targetCenterY);

        double newTranslateX = viewPort.getWidth() / 2.0 - targetCenterX;
        double newTranslateY = viewPort.getHeight() / 2.0 - targetCenterY;

        System.out.println("New Translate X: " + newTranslateX);
        System.out.println("New Translate Y: " + newTranslateY);

        newTranslateX = clampTranslate(newTranslateX, gamePane.getWidth() - viewPort.getWidth());
        newTranslateY = clampTranslate(newTranslateY, gamePane.getHeight() - viewPort.getHeight());

        gamePane.setTranslateX(newTranslateX);
        gamePane.setTranslateY(newTranslateY);

        System.out.println("Final Translate X: " + gamePane.getTranslateX());
        System.out.println("Final Translate Y: " + gamePane.getTranslateY());
    }


    private double clampTranslate(double value, double maxTranslate) {
        if (value > 0) return 0; // Ensure we don't move past the top or left bound
        return Math.max(value, -maxTranslate); // Ensure we don't move past the bottom or right bound
    }
}
