package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

/**
 * The Water class represents a non-walkable tile in the Chip's Challenge game, depicting a water obstacle.
 * Actors cannot move onto Water tiles, and it typically signifies an impassable terrain.
 */
public class Water extends Tile {

    /**
     * Constructs a Water tile with the default image path and non-walkable status.
     */
    public Water() {
        super("/images/chipgame/tiles/water.jpg", true);
    }

    /**
     * Checks if the Water tile is walkable.
     *
     * @return Always returns false since Water tiles are non-walkable.
     */
    @Override
    public boolean isWalkable() {
        return false;
    }

    /**
     * Handles the event when an actor attempts to step on the Water tile.
     * This method is intentionally left blank, as Water tiles do not have specific behavior when stepped on.
     *
     * @param actor             The actor attempting to step on the Water tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // No specific behavior for Water tiles when an actor attempts to step on them.
        // This method is intentionally left blank.
    }
}
