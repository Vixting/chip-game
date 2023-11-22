package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

/**
 * The Path class represents a tile in the Chip's Challenge game that serves as a walkable path.
 * Actors can freely move on this tile, and it does not have any specific behavior when stepped on.
 */
public class Path extends Tile {

    /**
     * Constructs a Path tile with the default image path and walkable status.
     */
    public Path() {
        super("/images/chipgame/tiles/path.png", true);
    }

    /**
     * Determines if the Path tile is walkable.
     *
     * @return Always returns true since the Path tile is walkable.
     */
    @Override
    public boolean isWalkable() {
        return true;
    }

    /**
     * Handles the event when an actor steps on the Path tile.
     * This method is intentionally left blank, as Path tiles do not have specific behavior when stepped on.
     *
     * @param actor             The actor stepping on the Path tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // No specific behavior for Path tiles when an actor steps on them.
        // This method is intentionally left blank.
    }
}
