package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.Entity;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.Main;
import com.group4.chipgame.entities.actors.Actor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Represents a tile in the ChipGame.
 * A tile is a component of the game level and can be occupied by entities.
 * @author William Buckley
 */
public abstract class Tile extends ImageView {
    private final boolean isWalkable;
    private boolean occupied;
    private Entity occupiedBy;
    private int gridX;
    private int gridY;

    /**
     * Constructs a new Tile with the specified image path and walkability.
     *
     * @param imagePath  The path to the image representing the tile.
     * @param isWalkable Whether the tile is walkable or not.
     */
    public Tile(final String imagePath,
                final boolean isWalkable) {
        Image image = new Image(
                Objects.requireNonNull(
                        getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        this.isWalkable = isWalkable;
        bindSize();
    }

    /**
     * Serializes the tile's state to a JSON object.
     *
     * @return A JSONObject representing the tile's state.
     */
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", gridX);
        object.put("y", gridY);
        return object;
    }

    /**
     * Sets the grid position of the tile.
     *
     * @param x The x-coordinate on the grid.
     * @param y The y-coordinate on the grid.
     */
    public void setGridPosition(final int x,
                                final int y) {
        gridX = x;
        gridY = y;
        updatePosition();
    }

    /**
     * Binds the size of the tile to the main tile size.
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
     * Sets the entity occupying this tile.
     *
     * @param actor The entity to occupy the tile.
     */
    public void setOccupiedBy(final Entity actor) {
        occupiedBy = actor;
        occupied = actor != null;
    }

    /**
     * Gets the entity currently occupying this tile.
     *
     * @return The entity occupying the tile.
     */
    public Entity getOccupiedBy() {
        return occupiedBy;
    }

    /**
     * Checks if the tile is walkable.
     *
     * @return true if the tile is walkable, false otherwise.
     */
    public boolean isWalkable() {
        return isWalkable;
    }

    /**
     * Checks if the tile is currently occupied.
     *
     * @return true if the tile is occupied, false otherwise.
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Defines the action to be taken
     * when an actor steps on this tile.
     * This method should be overridden
     * by subclasses to define specific behavior.
     *
     * @param actor The actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction
     *                          from which the actor steps onto the tile.
     */
    public void onStep(final Actor actor,
                       final LevelRenderer levelRenderer,
                       final Direction incomingDirection) {
        // To be implemented in subclasses
    }


    /**
     * Retrieves the x-coordinate value in the grid.
     *
     * @return the x-coordinate value of the current grid position
     */
    public int getGridX() {
        return gridX;
    }

    /**
     * Retrieves the y-coordinate value in the grid.
     *
     * @return the y-coordinate value of the current grid position
     */
    public int getGridY() {
        return gridY;
    }
}
