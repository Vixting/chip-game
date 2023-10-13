package com.group4.chipgame.actors;

import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.tiles.Tile;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.Objects;
import java.util.Optional;

public abstract class Actor extends ImageView {
    protected Point2D currentPosition;
    protected Point2D targetPosition;
    private Point2D position;
    private Image image;

    public Actor(String imagePath, double x, double y) {
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), Main.ACTOR_SIZE, Main.ACTOR_SIZE, true, true);
        setImage(this.image);
        this.setSmooth(true);
        this.position = new Point2D(x, y);
        this.currentPosition = new Point2D(x, y);
        this.targetPosition = new Point2D(x, y);
    }

    public void bindSize(Pane gamePane) {
        this.setFitWidth(Main.ACTOR_SIZE);
        this.setFitHeight(Main.ACTOR_SIZE);
    }

    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }

    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        // Calculate the new target position
        double newX = this.currentPosition.getX() + dx;
        double newY = this.currentPosition.getY() + dy;

        // Convert the target position to grid coordinates
        int targetGridX = (int) newX;
        int targetGridY = (int) newY;

        // Fetch the tile at the target position using the Optional
        Optional<Tile> optionalTargetTile = levelRenderer.getTileAtGridPosition(targetGridX, targetGridY);

        // Use the Tile inside the Optional if it exists and is walkable
        if (optionalTargetTile.isPresent() && optionalTargetTile.get().isWalkable()) {
            // Now, update the actor's position on the pane
            double offsetX = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;
            double offsetY = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;

            this.currentPosition = new Point2D(newX, newY);
            this.setLayoutX(newX * Main.TILE_SIZE + offsetX);
            this.setLayoutY(newY * Main.TILE_SIZE + offsetY);

        }
    }


    // Override this in specific collectibles to define behavior upon collection.
    public abstract void onCollect(Actor actor);
}
