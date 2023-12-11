package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;

/**
 * Represents a path tile in the ChipGame.
 * Path tiles are walkable areas in the game level.
 * @author William Buckley
 */
public class Path extends Tile {

    private static final String PATH_IMAGE_PATH =
            "/images/chipgame/tiles/path.png";

    /**
     * Constructs a new Path tile.
     */
    public Path() {
        super(PATH_IMAGE_PATH, true);
    }

    /**
     * Determines if the tile is walkable.
     *
     * @return true, as Path tiles are always walkable.
     */
    @Override
    public boolean isWalkable() {
        return true;
    }

    /**
     * Defines the action to be taken when an actor steps on this tile.
     * Currently, this method does not define any specific action.
     *
     * @param actor The actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from
     *                          which the actor steps onto the tile.
     */
    @Override
    public void onStep(final Actor actor,
                       final LevelRenderer levelRenderer,
                       final Direction incomingDirection) {
        // No specific action defined for stepping on a Path tile.
    }
}
