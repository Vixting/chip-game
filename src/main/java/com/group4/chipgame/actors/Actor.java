package com.group4.chipgame.actors;

import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.tiles.Tile;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.Optional;

public abstract class Actor extends ImageView {
    protected Point2D currentPosition;  // Current position of the actor on the grid
    protected Point2D targetPosition;   // Target position for the actor's movement
    private Point2D position;            // Actor's position (not grid-based)

    // Constructor for the Actor class
    public Actor(String imagePath, double x, double y) {
        // Load the actor's image from the provided image path
        // Image for the actor
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), Main.ACTOR_SIZE, Main.ACTOR_SIZE, true, true);
        setImage(image);
        this.setSmooth(true);

        // Set the actor's initial position (both current and target)
        this.position = new Point2D(x, y);
        this.currentPosition = new Point2D(x, y);
        this.targetPosition = new Point2D(x, y);
    }

    // Getter for the actor's position
    public Point2D getPosition() {
        return position;
    }

    // Setter for the actor's position
    public void setPosition(Point2D newPosition) {
        this.position = newPosition;
    }

    // Method to move the actor by a specified amount (dx, dy) on the grid
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

            // Update the current position and layout position of the actor
            this.currentPosition = new Point2D(newX, newY);
            this.setLayoutX(newX * Main.TILE_SIZE + offsetX);
            this.setLayoutY(newY * Main.TILE_SIZE + offsetY);
        }
    }

    // Abstract method that must be implemented by subclasses to define behavior upon collection
    public abstract void onCollect(Actor actor);
}