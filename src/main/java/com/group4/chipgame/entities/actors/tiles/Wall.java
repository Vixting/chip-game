package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;

/**
 * Represents a wall tile in the ChipGame.
 * Wall tiles are non-walkable barriers in the game level.
 */
public class Wall extends Tile {
    private static final String WALL_IMAGE_PATH = "/images/chipgame/tiles/iron.jpg";

    /**
     * Constructs a new Wall tile.
     */
    public Wall() {
        super(WALL_IMAGE_PATH, false);
    }

    /**
     * Determines if the tile is walkable.
     *
     * @return false, as Wall tiles are not walkable.
     */
    @Override
    public boolean isWalkable() {
        return false;
    }

    /**
     * Defines the action to be taken when an actor steps on this tile.
     * Currently, this method does not define any specific action.
     *
     * @param actor The actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from which the actor steps onto the tile.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // No specific action defined for stepping on a Wall tile.
    }
}
