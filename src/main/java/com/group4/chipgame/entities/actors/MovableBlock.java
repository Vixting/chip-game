package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.tiles.Path;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.entities.actors.tiles.Water;

import java.util.Optional;

/**
 * Represents a movable block in the ChipGame.
 * This class defines the behavior of a block that can be pushed by the player.
 * @author William Buckley
 */
public class MovableBlock extends Actor {
    private static final String BLOCK_IMAGE_PATH =
            "/images/chipgame/actors/wood.png";
    private static final int MOVE_INTERVAL = 100;
    /**
     * Constructs a MovableBlock with specified initial position.
     *
     * @param x The initial x-coordinate of the block.
     * @param y The initial y-coordinate of the block.
     */
    public MovableBlock(final double x,
                        final double y) {
        super(BLOCK_IMAGE_PATH, x, y);
        this.setMoveInterval(MOVE_INTERVAL);
    }

    /**
     * Pushes the block in a specified direction.
     * Handles interactions when the block is
     * pushed into water or against a player.
     *
     * @param dx            The delta x-coordinate for the push.
     * @param dy            The delta y-coordinate for the push.
     * @param levelRenderer The renderer for the game level.
     */
    public void push(final double dx,
                     final double dy,
                     final LevelRenderer levelRenderer) {
        double newX = getCurrentPosition().getX() + dx;
        double newY = getCurrentPosition().getY() + dy;

        if (levelRenderer.getTileAtGridPosition(
                (int) newX, (int) newY)
                .map(tile
                        -> tile.getOccupiedBy() instanceof Player)
                .orElse(false)) {
            killPlayerAt(newX, newY, levelRenderer);
            return;
        }

        if (canMove(dx, dy, levelRenderer)
                || isPushIntoWater(newX, newY, levelRenderer)) {
            performMove(newX, newY, levelRenderer, Direction.fromDelta(dx, dy));
            if (isPushIntoWater(newX, newY, levelRenderer)) {
                transformIntoPath(newX, newY, levelRenderer);
            }
        }
    }

    /**
     * Checks if the block is being pushed into water.
     *
     * @param newX          The new x-coordinate after the push.
     * @param newY          The new y-coordinate after the push.
     * @param levelRenderer The renderer for the game level.
     * @return True if the block is being pushed into water, false otherwise.
     */
    private boolean isPushIntoWater(final double newX,
                                    final double newY,
                                    final LevelRenderer levelRenderer) {
        return levelRenderer.getTileAtGridPosition(
                (int) newX, (int) newY)
                .filter(tile -> tile instanceof Water)
                .isPresent();
    }

    /**
     * Transforms the block into dirt at the specified position.
     * This is typically called when the block is pushed into water.
     *
     * @param x             The x-coordinate where the block is transformed.
     * @param y             The y-coordinate where the block is transformed.
     * @param levelRenderer The renderer for the game level.
     */
    private void transformIntoPath(final double x,
                                   final double y,
                                   final LevelRenderer levelRenderer) {
        Optional<Tile> currentTile =
                levelRenderer.getTileAtGridPosition((int)
                getCurrentPosition().getX(),
                (int) getCurrentPosition().getY());
        currentTile.ifPresent(tile
                -> tile.setOccupiedBy(null));
        levelRenderer.updateTile((int) x,
                (int) y, new Path());
        levelRenderer.remove(this);
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
