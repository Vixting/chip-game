package com.group4.chipgame.actors;

import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.tiles.Tile;
import javafx.geometry.Point2D;
import java.util.Optional;


public abstract class Enemy extends Actor {

    public Enemy(String imagePath, double x, double y) {
        super(imagePath, x, y);
    }

    public abstract void makeMoveDecision(LevelRenderer levelRenderer);

    @Override
    protected boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        Point2D newPosition = currentPosition.add(dx, dy);
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());

        if (targetTileOpt.isEmpty()) {
            return false;
        }

        Tile targetTile = targetTileOpt.get();
        if (targetTile.getOccupiedBy() instanceof Player) {
            ((Player) targetTile.getOccupiedBy()).kill(levelRenderer);
            targetTile.setOccupiedBy(null);
        }
        return targetTile.isWalkable() && !(targetTile.getOccupiedBy() instanceof Actor);
    }

    @Override
    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (canMove(dx, dy, levelRenderer)) {
            super.move(dx, dy, levelRenderer);
        }
    }
}
