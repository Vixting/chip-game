package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.tiles.Button;
import com.group4.chipgame.entities.actors.tiles.Path;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.entities.actors.tiles.Trap;
import javafx.geometry.Point2D;
import java.util.Optional;

/**
 * Represents an abstract Enemy in the ChipGame.
 * This class provides the basic functionality for all enemy types in the game.
 */
public abstract class Enemy extends Actor {

    /**
     * Constructs an Enemy with the specified image and initial position.
     *
     * @param imagePath The path to the image representing the enemy.
     * @param x         The initial x-coordinate of the enemy.
     * @param y         The initial y-coordinate of the enemy.
     */
    public Enemy(String imagePath, double x, double y) {
        super(imagePath, x, y);
    }

    /**
     * An abstract method that should be implemented to define the enemy's movement decision logic.
     *
     * @param levelRenderer The renderer for the game level.
     */
    public abstract void makeMoveDecision(LevelRenderer levelRenderer);

    /**
     * Determines if a move to a new position is valid for the enemy.
     * Checks if the target tile is a valid type (Path, Button, or Trap) and not occupied by another entity.
     *
     * @param newPosition  The new position to validate.
     * @param levelRenderer The renderer for the game level.
     * @return True if the move is valid, false otherwise.
     */
    @Override
    protected boolean isMoveValid(Point2D newPosition, LevelRenderer levelRenderer) {
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());
        if (targetTileOpt.isEmpty()) return false;

        Tile targetTile = targetTileOpt.get();

        boolean isCorrectTileType = targetTile instanceof Path || targetTile instanceof Button || targetTile instanceof Trap;
        boolean isTileNotOccupied = targetTile.getOccupiedBy() == null || targetTile.getOccupiedBy() instanceof Player;

        return isCorrectTileType && isTileNotOccupied;
    }

    /**
     * Checks if the enemy can move to a new position based on a specified delta in x and y direction.
     * Also handles the interaction if the target tile is occupied by a player.
     *
     * @param dx            The change in the x-coordinate.
     * @param dy            The change in the y-coordinate.
     * @param levelRenderer The renderer for the game level.
     * @return True if the enemy can move to the new position, false otherwise.
     */
    @Override
    protected boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        Point2D newPosition = getCurrentPosition().add(dx, dy);
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());

        if (!isMoveValid(newPosition, levelRenderer)) {
            return false;
        }

        Tile targetTile = targetTileOpt.orElseThrow(() -> new IllegalStateException("Target tile not found"));
        if (targetTile.getOccupiedBy() instanceof Player) {
            ((Player) targetTile.getOccupiedBy()).kill(levelRenderer);
            targetTile.setOccupiedBy(null);
        }
        return targetTile.isWalkable();
    }
}