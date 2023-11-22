package com.group4.chipgame.actors;

import com.group4.chipgame.*;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.tiles.Tile;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Optional;

/**
 * The Actor class is an abstract class representing a game entity that can move on the game grid.
 * It extends ImageView and implements the Entity interface.
 */
public abstract class Actor extends ImageView implements Entity {
    protected Point2D currentPosition;
    protected long moveInterval;
    protected boolean isMoving;

    /**
     * Constructs an Actor with the specified image path and initial position.
     *
     * @param imagePath The path to the image representing the actor.
     * @param x         The initial x-coordinate of the actor.
     * @param y         The initial y-coordinate of the actor.
     */
    public Actor(String imagePath, double x, double y) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);

        currentPosition = new Point2D(x, y);
        updatePosition();

        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    /**
     * Updates the position of the actor based on its current coordinates and the tile size.
     */
    private void updatePosition() {
        double offset = (Main.TILE_SIZE.get() - fitWidthProperty().get()) / 2.0;
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
    }

    /**
     * Gets the current position of the actor.
     *
     * @return The current position of the actor as a Point2D.
     */
    public Point2D getPosition() {
        return currentPosition;
    }

    /**
     * Determines if the actor should move based on the elapsed ticks.
     *
     * @param ticksElapsed The number of ticks elapsed.
     * @return True if the actor should move, false otherwise.
     */
    public boolean shouldMove(long ticksElapsed) {
        return ticksElapsed % moveInterval == 0;
    }

    /**
     * Moves the actor in the specified direction if it is not already moving.
     *
     * @param dx            The change in the x-coordinate to move the actor.
     * @param dy            The change in the y-coordinate to move the actor.
     * @param levelRenderer The level renderer responsible for rendering the game level.
     */
    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (!isMoving) {
            Direction direction = Direction.fromDelta(dx, dy);
            double newX = currentPosition.getX() + dx;
            double newY = currentPosition.getY() + dy;

            if (canMove(dx, dy, levelRenderer)) {
                performMove(newX, newY, levelRenderer, direction);
            }
        }
    }

    /**
     * Checks if the actor can move to the specified position on the grid.
     *
     * @param dx            The change in the x-coordinate to check.
     * @param dy            The change in the y-coordinate to check.
     * @param levelRenderer The level renderer responsible for rendering the game level.
     * @return True if the actor can move to the specified position, false otherwise.
     */
    boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

        if (targetTileOpt.isEmpty()) return false;

        Tile targetTile = targetTileOpt.get();
        Entity occupiedBy = targetTile.getOccupiedBy();

        return targetTile.isWalkable() && (occupiedBy == null || (this instanceof Player && occupiedBy instanceof Collectible));
    }

    /**
     * Performs the move of the actor to the specified position on the grid.
     *
     * @param newX          The new x-coordinate of the actor.
     * @param newY          The new y-coordinate of the actor.
     * @param levelRenderer The level renderer responsible for rendering the game level.
     * @param direction     The direction of the move.
     */
    public void performMove(double newX, double newY, LevelRenderer levelRenderer, Direction direction) {
        isMoving = true;

        double offset = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        Timeline timeline = createTimeline(newX * Main.TILE_SIZE.get() + offset, newY * Main.TILE_SIZE.get() + offset);

        Optional<Tile> currentTile = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());
        currentTile.ifPresent(tile -> tile.setOccupiedBy(null)); // Mark old tile as unoccupied

        currentPosition = new Point2D(newX, newY);
        Optional<Tile> targetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);
        targetTile.ifPresent(tile -> tile.setOccupiedBy(this)); // Mark new tile as occupied
        targetTile.ifPresent(tile -> tile.onStep(this, levelRenderer, direction));

        isMoving = false;
        timeline.play();
    }

    /**
     * Creates a timeline for the animation of the actor's movement.
     *
     * @param targetX The target x-coordinate of the actor.
     * @param targetY The target y-coordinate of the actor.
     * @return The timeline for the actor's movement animation.
     */
    private Timeline createTimeline(double targetX, double targetY) {
        KeyValue kvX = new KeyValue(this.layoutXProperty(), targetX);
        KeyValue kvY = new KeyValue(this.layoutYProperty(), targetY);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kvX, kvY);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        return timeline;
    }
}
