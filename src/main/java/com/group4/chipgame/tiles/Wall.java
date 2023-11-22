package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

/**
 * The Wall class represents a non-walkable tile in the Chip's Challenge game, blocking the movement of actors.
 * It serves as an obstacle that actors cannot pass through.
 */
public class Wall extends Tile {

    /**
     * Constructs a Wall tile with the default image path and non-walkable status.
     */
    public Wall() {
        super("/images/chipgame/tiles/iron.jpg", true);
    }

    /**
     * Checks if the Wall tile is walkable.
     *
     * @return Always returns false since Wall tiles are non-walkable.
     */
    @Override
    public boolean isWalkable() {
        return false;
    }

    /**
     * Handles the event when an actor attempts to step on the Wall tile.
     * This method is intentionally left blank, as Wall tiles do not have specific behavior when stepped on.
     *
     * @param actor             The actor attempting to step on the Wall tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        // No specific behavior for Wall tiles when an actor attempts to step on them.
        // This method is intentionally left blank.
    }
}
