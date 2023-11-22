package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * The Tile class represents a basic building block for the Chip's Challenge game grid.
 * It is an abstract class extended by specific tile types, providing common functionality such as image rendering,
 * size binding, position updates, and occupancy status.
 */
public abstract class Tile extends ImageView {

    private final boolean isWalkable;
    private boolean occupied;
    private Entity occupiedBy;
    protected int gridX;
    protected int gridY;

    /**
     * Constructs a Tile with the specified image path and walkable status.
     *
     * @param imagePath  The path to the image representing the tile.
     * @param isWalkable True if the tile is walkable, false otherwise.
     */
    public Tile(String imagePath, boolean isWalkable) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        this.isWalkable = isWalkable;
        bindSize();
    }

    /**
     * Sets the grid position of the tile and updates its visual position.
     *
     * @param x The X-coordinate of the grid position.
     * @param y The Y-coordinate of the grid position.
     */
    public void setGridPosition(int x, int y) {
        gridX = x;
        gridY = y;
        updatePosition();
    }

    private void bindSize() {
        fitWidthProperty().bind(Main.TILE_SIZE.asObject());
        fitHeightProperty().bind(Main.TILE_SIZE.asObject());
    }

    private void updatePosition() {
        setLayoutX(gridX * Main.TILE_SIZE.get());
        setLayoutY(gridY * Main.TILE_SIZE.get());
    }

    /**
     * Sets the entity that occupies the tile.
     *
     * @param actor The entity occupying the tile.
     */
    public void setOccupiedBy(Entity actor) {
        occupiedBy = actor;
        occupied = actor != null;
    }

    /**
     * Gets the entity that occupies the tile.
     *
     * @return The entity occupying the tile.
     */
    public Entity getOccupiedBy() {
        return occupiedBy;
    }

    /**
     * Checks if the tile is walkable.
     *
     * @return True if the tile is walkable, false otherwise.
     */
    public boolean isWalkable() {
        return isWalkable;
    }

    /**
     * Checks if the tile is occupied.
     *
     * @return True if the tile is occupied, false otherwise.
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Handles the event when an actor steps on the tile.
     *
     * @param actor             The actor stepping on the tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // To be implemented by subclasses
    }
}

