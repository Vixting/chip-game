/**
 * The abstract class representing a tile in the Chip's Challenge game.
 * This class extends ImageView and provides common functionality for tiles.
 * Subclasses of Tile define specific behaviors for different types of tiles.
 */
package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

public abstract class Tile extends ImageView {

    /**
     * Indicates whether this tile is walkable or not.
     */
    private final boolean isWalkable;

    /**
     * Indicates if the tile is currently occupied by an entity.
     */
    private boolean occupied;

    /**
     * The entity currently occupying the tile.
     */
    private Entity occupiedBy;

    /**
     * The x-coordinate of the tile in the grid.
     */
    protected int gridX;

    /**
     * The y-coordinate of the tile in the grid.
     */
    protected int gridY;

    /**
     * Constructs a Tile object with the specified image path and walkability.
     *
     * @param imagePath   The path to the image representing the tile.
     * @param isWalkable  Indicates whether the tile is walkable.
     */
    public Tile(String imagePath, boolean isWalkable) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        this.isWalkable = isWalkable;
        bindSize();
    }

    /**
     * Sets the grid position of the tile.
     *
     * @param x The x-coordinate in the grid.
     * @param y The y-coordinate in the grid.
     */
    public void setGridPosition(int x, int y) {
        gridX = x;
        gridY = y;
        updatePosition();
    }

    /**
     * Binds the size of the tile to the TILE_SIZE property in the Main class.
     */
    private void bindSize() {
        fitWidthProperty().bind(Main.TILE_SIZE.asObject());
        fitHeightProperty().bind(Main.TILE_SIZE.asObject());
    }

    /**
     * Updates the position of the tile based on its grid coordinates.
     */
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
     * Gets the entity currently occupying the tile.
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
     * Checks if the tile is currently occupied.
     *
     * @return True if the tile is occupied, false otherwise.
     */
    public boolean isOccupied() {
        return !occupied;
    }

    /**
     * Method called when an actor steps on the tile.
     * To be implemented by subclasses.
     *
     * @param actor              The actor stepping on the tile.
     * @param levelRenderer      The level renderer object.
     * @param incomingDirection  The direction from which the actor is coming.
     */
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // To be implemented by subclasses
    }
}
