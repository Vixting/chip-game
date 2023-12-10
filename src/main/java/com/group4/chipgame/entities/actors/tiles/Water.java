package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;

/**
 * Represents a water tile in the ChipGame.
 * Water tiles are non-walkable obstacles in the game level.
 */
public class Water extends Tile {
    private static final String WATER_IMAGE_PATH = "/images/chipgame/tiles/water.jpg";

    /**
     * Constructs a new Water tile.
     */
    public Water() {
        super(WATER_IMAGE_PATH, false);
    }

    /**
     * Determines if the tile is walkable.
     *
     * @return false, as Water tiles are not walkable.
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
        // No specific action defined for stepping on a Water tile.
    }
}
