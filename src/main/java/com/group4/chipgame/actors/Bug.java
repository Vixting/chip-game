package com.group4.chipgame.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.tiles.Tile;

import java.util.Optional;

public class Bug extends Enemy{
    private static final String IMAGE_PATH = "/images/chipgame/actors/Bug.PNG";
    private Direction currentDirection;

    public Bug(double x, double y, Direction initialDirection) {
        super(IMAGE_PATH, x, y);
        this.currentDirection = initialDirection;
        this.moveInterval = 50;
    }

    @Override
    public void makeMoveDecision(LevelRenderer levelRenderer) {
        double dx = currentDirection.getDx();
        double dy = currentDirection.getDy();

        Optional<Tile> currentTileOptLeft = getTileLeftOfBug(levelRenderer);
        Optional<Tile> currentTileOptRight = getTileRightOfBug(levelRenderer);

        if(currentTileOptLeft.isPresent() && currentTileOptRight.isPresent()){
            Tile tile = currentTileOptLeft.get();
            Entity occupiedBy = tile.getOccupiedBy();

            Tile tileRight = currentTileOptRight.get();
            Entity occupiedByRight = tile.getOccupiedBy();

            if(tile.isWalkable() && occupiedBy == null){
                turnLeft();
                double[] delta = Direction.toDelta(currentDirection);
                super.move(delta[0], delta[1], levelRenderer);
            }else if (canMove(dx,dy,levelRenderer)){
                double[] delta = Direction.toDelta(currentDirection);
                super.move(delta[0], delta[1], levelRenderer);
            }else if (tileRight.isWalkable() && occupiedByRight == null){
                turnRight();
                double[] delta = Direction.toDelta(currentDirection);
                super.move(delta[0], delta[1], levelRenderer);
            }
        }
    }
    void turnRight() {
        switch (currentDirection) {
            case DOWN:
                currentDirection = Direction.LEFT;
                break;
            case UP:
                currentDirection = Direction.RIGHT;
                break;
            case LEFT:
                currentDirection = Direction.UP;
                break;
            case RIGHT:
                currentDirection = Direction.DOWN;
                break;
        }
    }
    void turnLeft() {
        switch (currentDirection) {
            case DOWN:
                currentDirection = Direction.RIGHT;
                break;
            case UP:
                currentDirection = Direction.LEFT;
                break;
            case LEFT:
                currentDirection = Direction.DOWN;
                break;
            case RIGHT:
                currentDirection = Direction.UP;
                break;
        }
    }
    Optional<Tile> getTileLeftOfBug(LevelRenderer levelRenderer) {
        int nextX = (int) currentPosition.getX();
        int nextY = (int) currentPosition.getY();

        switch (currentDirection) {
            case UP:
                nextX--;
                break;
            case DOWN:
                nextX++;
                break;
            case LEFT:
                nextY++;
                break;
            case RIGHT:
                nextY--;
                break;
        }
        return levelRenderer.getTileAtGridPosition(nextX, nextY);
    }

    Optional<Tile> getTileRightOfBug(LevelRenderer levelRenderer) {
        int nextX = (int) currentPosition.getX();
        int nextY = (int) currentPosition.getY();

        switch (currentDirection) {
            case UP:
                nextX++;
                break;
            case DOWN:
                nextX--;
                break;
            case LEFT:
                nextY--;
                break;
            case RIGHT:
                nextY++;
                break;
        }
        return levelRenderer.getTileAtGridPosition(nextX, nextY);
    }
}
