package com.group4.chipgame.actors;

import com.group4.chipgame.*;
import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.tiles.Trap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Optional;

public abstract class Actor extends ImageView implements Entity {
    protected Point2D currentPosition;
    protected Point2D targetPosition;
    protected boolean isMoving = false;

    public Actor(String imagePath, double x, double y) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)), Main.ACTOR_SIZE, Main.ACTOR_SIZE, true, true);
        setImage(image);
        setSmooth(true);

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

    // Check if the actor is currently on a Trap
    Optional<Tile> currentTileOpt = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());
    if (currentTileOpt.isPresent() && currentTileOpt.get() instanceof Trap currentTrap) {
        if (!currentTrap.linkedButton.isActive()) {
            return;
        }
    }

    if (optionalTargetTile.isPresent()) {
        Tile targetTile = optionalTargetTile.get();
        System.out.println("Tile at (" + newX + ", " + newY + ") isWalkable: " + targetTile.isWalkable() + ", occupiedBy: " + targetTile.getOccupiedBy());

        if (this instanceof Player && targetTile instanceof Trap currentTrap && targetTile.getOccupiedBy() != null) {
            if (currentTrap.linkedButton.isActive()){
                collisionHandler.handleActorOnActorCollision(this, targetTile.getOccupiedBy(), dx, dy, levelRenderer);
            } else {
                return;
            }
        }

        if (targetTile.isWalkable() && targetTile.getOccupiedBy() == null) {
            performMove(newX, newY, levelRenderer, direction);
        } else if (targetTile.getOccupiedBy() != null) {
            collisionHandler.handleActorOnActorCollision(this, targetTile.getOccupiedBy(), dx, dy, levelRenderer);
        }
    }
}

    public void performMove(double newX, double newY, LevelRenderer levelRenderer, Direction direction) {
        if (isMoving) return;  // Return if a movement is already in progress

        isMoving = true;
        double offsetX = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;
        double offsetY = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;

        // Get the current tile the actor is on
        Optional<Tile> currentTile = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());

        // If the actor is on a tile, mark it as unoccupied
        currentTile.ifPresent(tile -> tile.setOccupiedBy(null));

        // Calculate target layout positions
        double targetLayoutX = newX * Main.TILE_SIZE + offsetX;
        double targetLayoutY = newY * Main.TILE_SIZE + offsetY;

        // Create a timeline for the movement
        Timeline timeline = new Timeline();

        // Create keyframes for the timeline
        KeyValue kvX = new KeyValue(this.layoutXProperty(), targetLayoutX);
        KeyValue kvY = new KeyValue(this.layoutYProperty(), targetLayoutY);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kvX, kvY);  // 300ms duration

        // Add keyframes to timeline
        timeline.getKeyFrames().add(kf);

        // When the timeline is finished, update the actor's position and occupied tile
        this.currentPosition = new Point2D(newX, newY);
        Optional<Tile> targetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);
        targetTile.ifPresent(tile -> tile.setOccupiedBy(this));
        targetTile.ifPresent(tile -> tile.onStep(this, levelRenderer, direction));

        isMoving = false;
        timeline.play();
    }


    public void onCollect(Entity actorOnTile) {
    }
}