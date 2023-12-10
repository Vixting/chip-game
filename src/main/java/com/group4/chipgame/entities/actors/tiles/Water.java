package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;

/**
 * Represents a water tile in the ChipGame.
 * Water tiles are non-walkable obstacles in the game level.
 * @author William Buckley
 */
public class Water extends Tile {
    private static final String WATER_IMAGE_PATH =
            "/images/chipgame/tiles/water.jpg";

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
        return true;
    }

    /**
     * Defines the action to be taken when an actor steps on this tile.
     * Currently, this method does not define any specific action.
     *
     * @param actor The actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction
     *                          from which the actor steps onto the tile.
     */
    @Override
    public void onStep(final Actor actor,
                       final LevelRenderer levelRenderer,
                       final Direction incomingDirection) {
        killPlayerAt(actor.getPosition().getX(),
                actor.getPosition().getY(),
                levelRenderer);
    }

    /**
     * Kills the player at the specified position.
     * This is called when a block is pushed onto a player's position.
     *
     * @param x             The x-coordinate where the player is located.
     * @param y             The y-coordinate where the player is located.
     * @param levelRenderer The renderer for the game level.
     */
    private void killPlayerAt(final double x,
                              final double y,
                              final LevelRenderer levelRenderer) {
        levelRenderer.getActors().stream()
                .filter(actor
                        -> actor instanceof Player
                        && actor.getPosition().getX() == x
                        && actor.getPosition().getY() == y)
                .findFirst()
                .ifPresent(player -> ((Player) player).kill(levelRenderer));
    }
}
