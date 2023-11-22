package com.group4.chipgame.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.tiles.Dirt;
import com.group4.chipgame.tiles.Water;

/**
 * The MovableBlock class represents a movable block in the chip game.
 * It extends the Actor class and adds functionality specific to movable blocks.
 */
public class MovableBlock extends Actor {

    /**
     * Constructs a MovableBlock with the specified initial position.
     *
     * @param x The initial x-coordinate of the movable block.
     * @param y The initial y-coordinate of the movable block.
     */
    public MovableBlock(double x, double y) {
        super("/images/chipgame/actors/wood.png", x, y);
        this.moveInterval = 3;
    }

    /**
     * Pushes the movable block in the specified direction.
     * If the block can move to the new position, it performs the move.
     * If the new position contains water, the block moves, updates the tile to dirt,
     * and removes itself from the levelRenderer.
     *
     * @param dx             The change in the x-coordinate to push the block.
     * @param dy             The change in the y-coordinate to push the block.
     * @param levelRenderer  The level renderer responsible for rendering the game level.
     */
    public void push(double dx, double dy, LevelRenderer levelRenderer) {
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;

        if (canMove(dx, dy, levelRenderer)) {
            performMove(newX, newY, levelRenderer, Direction.fromDelta(dx, dy));
        } else if (levelRenderer.getTileAtGridPosition((int) newX, (int) newY)
                .filter(tile -> tile instanceof Water).isPresent()) {
            performMove(newX, newY, levelRenderer, Direction.fromDelta(dx, dy));
            levelRenderer.updateTile((int) newX, (int) newY, new Dirt());
            levelRenderer.remove(this);
        }
    }
}
