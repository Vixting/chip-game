package com.group4.chipgame.tiles;

/**
 * The Exit class represents a tile in the ChipGame that serves as an exit point.
 * It extends the Tile class and inherits its properties and behavior.
 */
public class Exit extends Tile {
    /**
     * Constructs an Exit tile with the specified image path and walkability.
     *
     * @param imagePath   The path to the image representing the Exit tile.
     * @param isWalkable  Indicates whether the tile is walkable or not.
     */
    public Exit(String imagePath, boolean isWalkable) {
        super(imagePath, isWalkable);
    }
}
