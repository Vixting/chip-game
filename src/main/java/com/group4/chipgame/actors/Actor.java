package com.group4.chipgame.actors;

import com.group4.chipgame.Main;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;

public abstract class Actor extends ImageView {
    private Point2D position;
    private Image image;

    public Actor(String imagePath, double x, double y) {
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), Main.ACTOR_SIZE, Main.ACTOR_SIZE, true, true);
        setImage(this.image);
        this.position = new Point2D(x, y);
        setX(x);
        setY(y);
    }

    public void bindSize(Pane gamePane) {
        this.fitWidthProperty().bind(gamePane.widthProperty().divide(10).subtract(10)); // Making it 10 pixels smaller than a tile
        this.fitHeightProperty().bind(gamePane.heightProperty().divide(10).subtract(10));
    }

    public Actor(String imagePath, Point2D position) {
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
        setX(newPosition.getX());
        setY(newPosition.getY());
    }


    // This can be overridden by subclasses to define how the actor moves.
    public abstract void move(double dx, double dy);

    // Override this in specific collectibles to define behavior upon collection.
    public abstract void onCollect(Actor actor);
}