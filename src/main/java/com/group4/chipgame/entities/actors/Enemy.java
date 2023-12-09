package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.tiles.Button;
import com.group4.chipgame.entities.actors.tiles.Path;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.entities.actors.tiles.Trap;
import javafx.geometry.Point2D;
import java.util.Optional;


public abstract class Enemy extends Actor {

    public Enemy(String imagePath, double x, double y) {
        super(imagePath, x, y);
    }

    public abstract void makeMoveDecision(LevelRenderer levelRenderer);

    @Override
    protected boolean isMoveValid(Point2D newPosition, LevelRenderer levelRenderer) {
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());
        if (targetTileOpt.isEmpty()) return false;

        Tile targetTile = targetTileOpt.get();

        boolean isCorrectTileType = targetTile instanceof Path || targetTile instanceof Button || targetTile instanceof Trap;
        boolean isTileNotOccupied = targetTile.getOccupiedBy() == null || targetTile.getOccupiedBy() instanceof Player;

        return isCorrectTileType && isTileNotOccupied;
    }

    @Override
    protected boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        Point2D newPosition = currentPosition.add(dx, dy);
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