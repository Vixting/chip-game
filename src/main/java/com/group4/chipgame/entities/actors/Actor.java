package com.group4.chipgame.entities.actors;

import com.group4.chipgame.*;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.entities.actors.tiles.Trap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an abstract actor in the ChipGame.
 * An actor is an entity that can move and interact with tiles and other actors.
 */
public abstract class Actor extends ImageView implements Entity {
    private Point2D currentPosition;
    protected long moveInterval;
    protected boolean isMoving;

    /**
     * Gets the current position of the actor.
     *
     * @return The current position as a Point2D object.
     */
    public Point2D getCurrentPosition() {
        return currentPosition;
    }

    /**
     * Constructs an Actor with the specified image, initial position, and move interval.
     *
     * @param imagePath The path to the image representing the actor.
     * @param x         The initial x-coordinate of the actor.
     * @param y         The initial y-coordinate of the actor.
     */
    public Actor(String imagePath, double x, double y) {
        initializeImage(imagePath);
        currentPosition = new Point2D(x, y);
        updatePosition();
        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    /**
     * Initializes the image of the actor.
     *
     * @param imagePath The path to the image file for this actor.
     */
    private void initializeImage(String imagePath) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);
    }

    /**
     * Serializes the state of the actor to a JSON object.
     *
     * @return A JSONObject representing the current state of the actor.
     */
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", currentPosition.getX());
        object.put("y", currentPosition.getY());
        return object;
    }

    /**
     * Updates the position of the actor on the screen based on its current grid position.
     */
    private void updatePosition() {
        double offset = calculateOffset();
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
        EffectManager.applyDynamicShadowEffect(this);
    }

    /**
     * Calculates the offset required to center the actor within a tile.
     *
     * @return The calculated offset value.
     */
    private double calculateOffset() {
        return (Main.TILE_SIZE.get() - fitWidthProperty().get()) / 2.0;
    }

    /**
     * Gets the current position of the actor.
     *
     * @return The current position as a Point2D object.
     */
    public Point2D getPosition() {
        return currentPosition;
    }

    /**
     * Determines whether the actor should move based on the elapsed game ticks.
     *
     * @param ticksElapsed The number of game ticks that have elapsed.
     * @return True if the actor should move, false otherwise.
     */
    public boolean shouldMove(long ticksElapsed) {
        return ticksElapsed % this.moveInterval == 0;
    }

    /**
     * Moves the actor by a specified delta in x and y direction.
     *
     * @param dx The change in the x-coordinate.
     * @param dy The change in the y-coordinate.
     * @param levelRenderer The renderer for the game level.
     */
    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (isMoving) return;

        Direction direction = Direction.fromDelta(dx, dy);
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;

        performMove(newX, newY, levelRenderer, direction);
    }

    /**
     * Checks if the actor is currently moving.
     *
     * @return True if the actor is moving, false otherwise.
     */
    public boolean isMoving() {
        return isMoving;
    }

    /**
     * Checks if the actor can move by a specified delta in x and y direction.
     *
     * @param dx The change in the x-coordinate.
     * @param dy The change in the y-coordinate.
     * @param levelRenderer The renderer for the game level.
     * @return True if the move is valid, false otherwise.
     */
    protected boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        Point2D newPosition = currentPosition.add(dx, dy);
        return isMoveValid(newPosition, levelRenderer);
    }

    /**
     * Validates if a move to a new position is valid.
     *
     * @param newPosition The new position to validate.
     * @param levelRenderer The renderer for the game level.
     * @return True if the move is valid, false otherwise.
     */
    protected boolean isMoveValid(Point2D newPosition, LevelRenderer levelRenderer) {
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());
        if (targetTileOpt.isEmpty()) return false;

        Optional<Tile> currentTileOpt = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());
        if (currentTileOpt.isPresent() && currentTileOpt.get() instanceof Trap currentTrap && currentTrap.isActive()) {
            return false;
        }

        Tile targetTile = targetTileOpt.get();
        Entity occupiedBy = targetTile.getOccupiedBy();

        return targetTile.isWalkable() && (occupiedBy == null || (this instanceof Player && occupiedBy instanceof Collectible ) || (this instanceof Enemy && occupiedBy instanceof Player));
    }

    /**
     * Performs the move action for the actor to a new position.
     *
     * @param newX The new x-coordinate.
     * @param newY The new y-coordinate.
     * @param levelRenderer The renderer for the game level.
     * @param direction The direction of the move.
     */
    public void performMove(double newX, double newY, LevelRenderer levelRenderer, Direction direction) {
        isMoving = true;
        Timeline timeline = createTimeline(newX, newY);

        updateTileOccupancy(levelRenderer, newX, newY, direction);

        isMoving = false;
        timeline.play();
    }

    /**
     * Updates the tile occupancy based on the actor's new position.
     *
     * @param levelRenderer The renderer for the game level.
     * @param newX The new x-coordinate of the actor.
     * @param newY The new y-coordinate of the actor.
     * @param direction The direction of the move.
     */
    private void updateTileOccupancy(LevelRenderer levelRenderer, double newX, double newY, Direction direction) {
        levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY())
                .ifPresent(tile -> tile.setOccupiedBy(null));

        currentPosition = new Point2D(newX, newY);

        levelRenderer.getTileAtGridPosition((int) newX, (int) newY)
                .ifPresent(tile -> {
                    tile.setOccupiedBy(this);
                    tile.onStep(this, levelRenderer, direction);
                });
    }

    /**
     * Creates a timeline for smooth animation of the actor's movement.
     *
     * @param newX The new x-coordinate of the actor.
     * @param newY The new y-coordinate of the actor.
     * @return The timeline object for the animation.
     */
    private Timeline createTimeline(double newX, double newY) {
        double offset = calculateOffset();

        double distance = currentPosition.distance(newX, newY);
        double baseSpeed = 0.003;
        double durationMillis = distance / baseSpeed;

        KeyValue kvX = new KeyValue(this.layoutXProperty(), newX * Main.TILE_SIZE.get() + offset);
        KeyValue kvY = new KeyValue(this.layoutYProperty(), newY * Main.TILE_SIZE.get() + offset);
        KeyFrame kf = new KeyFrame(Duration.millis(durationMillis), kvX, kvY);

        return new Timeline(kf);
    }
}
