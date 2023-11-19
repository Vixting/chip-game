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

        if (canMove(dx, dy, levelRenderer)) {
            performMove(newX, newY, levelRenderer, Direction.fromDelta(dx, dy));
        } else if (levelRenderer.getTileAtGridPosition((int) newX, (int) newY).filter(tile -> tile instanceof Water).isPresent()) {
            performMove(newX, newY, levelRenderer, Direction.fromDelta(dx, dy));
            levelRenderer.updateTile((int) newX, (int) newY, new Dirt());
            levelRenderer.remove(this);
        }
    }
}
