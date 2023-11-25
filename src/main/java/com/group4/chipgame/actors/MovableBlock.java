package com.group4.chipgame.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.tiles.Dirt;
import com.group4.chipgame.tiles.Water;

public class MovableBlock extends Actor {

    public MovableBlock(double x, double y) {
        super("/images/chipgame/actors/wood.png", x, y);
        this.moveInterval = 3;
    }

    public void push(double dx, double dy, LevelRenderer levelRenderer) {
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;

        if (canMove(dx, dy, levelRenderer) || isPushIntoWater(newX, newY, levelRenderer)) {
            performMove(newX, newY, levelRenderer, Direction.fromDelta(dx, dy));
            if (isPushIntoWater(newX, newY, levelRenderer)) {
                transformIntoDirt(newX, newY, levelRenderer);
            }
        }
    }

    private boolean isPushIntoWater(double newX, double newY, LevelRenderer levelRenderer) {
        return levelRenderer.getTileAtGridPosition((int) newX, (int) newY)
                .filter(tile -> tile instanceof Water)
                .isPresent();
    }

    private void transformIntoDirt(double x, double y, LevelRenderer levelRenderer) {
        levelRenderer.updateTile((int) x, (int) y, new Dirt());
        levelRenderer.remove(this);
    }
}
