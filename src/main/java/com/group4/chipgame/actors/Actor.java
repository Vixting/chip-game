package com.group4.chipgame.actors;
import com.group4.chipgame.Direction;
import com.group4.chipgame.CollisionHandler;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.tiles.Tile;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;
import java.util.Optional;

public abstract class Actor extends ImageView {
    public Point2D currentPosition;  // Current position of the actor on the grid
    protected Point2D targetPosition;   // Target position for the actor's movement

    public Actor(String imagePath, double x, double y) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), Main.ACTOR_SIZE, Main.ACTOR_SIZE, true, true);
        setImage(image);
        this.setSmooth(true);

        this.currentPosition = new Point2D(x, y);
        this.targetPosition = new Point2D(x, y);
    }

    public Point2D getPosition() {
        return currentPosition;
    }

// Method to move the actor by a specified amount (dx, dy) on the grid
public void move(double dx, double dy, LevelRenderer levelRenderer, CollisionHandler collisionHandler) {
    double newX = this.currentPosition.getX() + dx;
    double newY = this.currentPosition.getY() + dy;
    Direction direction = Direction.fromDelta(dx, dy);
    Optional<Tile> optionalTargetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

    if (optionalTargetTile.isPresent()) {
        Tile targetTile = optionalTargetTile.get();
        System.out.println("Tile at (" + newX + ", " + newY + ") isWalkable: " + targetTile.isWalkable() + ", occupiedBy: " + targetTile.getOccupiedBy());

        if (targetTile.isWalkable() && targetTile.getOccupiedBy() == null) {
            performMove(newX, newY, levelRenderer, direction);
        } else if (targetTile.getOccupiedBy() != null) {
            collisionHandler.handleActorOnActorCollision(this, targetTile.getOccupiedBy(), dx, dy, levelRenderer);
        }
    }
}

    public void performMove(double newX, double newY, LevelRenderer levelRenderer, Direction direction) {
        double offsetX = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;
        double offsetY = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;

        // Get the current tile the actor is on
        Optional<Tile> currentTile = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());

        // If the actor is on a tile, mark it as unoccupied
        currentTile.ifPresent(tile -> tile.setOccupiedBy(null));

        // Update the actor's position
        this.currentPosition = new Point2D(newX, newY);
        this.setLayoutX(newX * Main.TILE_SIZE + offsetX);
        this.setLayoutY(newY * Main.TILE_SIZE + offsetY);

        // Get the tile the actor moved to
        Optional<Tile> targetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

        // Mark the new tile as occupied by the actor
        targetTile.ifPresent(tile -> tile.setOccupiedBy(this));
        targetTile.ifPresent(tile -> tile.onStep(this, levelRenderer, direction)); // Updated to pass direction
    }

    // Abstract method that must be implemented by subclasses to define behavior upon collection
    public abstract void onCollect(Actor actor);
}