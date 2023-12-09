package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import javafx.geometry.Point2D;
import org.json.JSONObject;

public class Bug extends Enemy {
    private final boolean followLeftEdge;
    private Direction currentDirection;
    private static final String BUG_IMAGE_PATH = "/images/chipgame/actors/bug.jpg";


    public Bug(double x, double y, boolean followLeftEdge) {
        super(BUG_IMAGE_PATH, x, y);
        this.followLeftEdge = followLeftEdge;
        this.moveInterval = 100;
        this.currentDirection = Direction.LEFT;
    }

    @Override
    public void makeMoveDecision(LevelRenderer levelRenderer) {
        if (!canMoveInDirection(currentDirection, levelRenderer)) {
            Direction sideDirection = followLeftEdge ? currentDirection.turnLeft() : currentDirection.turnRight();
            if (isWallOnSide(sideDirection, levelRenderer)) {
                currentDirection = followLeftEdge ? currentDirection.turnRight() : currentDirection.turnLeft();
            } else {
                currentDirection = sideDirection;
            }
        } else {
            Direction sideDirection = followLeftEdge ? currentDirection.turnRight() : currentDirection.turnLeft();
            if (!isWallOnSide(sideDirection, levelRenderer)) {
                currentDirection = sideDirection;
            }
        }

        if (canMoveInDirection(currentDirection, levelRenderer)) {
            moveInDirection(currentDirection, levelRenderer);
        }
    }
    private boolean isWallOnSide(Direction sideDirection, LevelRenderer levelRenderer) {
        Point2D sidePosition = getPosition().add(sideDirection.getDx(), sideDirection.getDy());
        return !isMoveValid(sidePosition, levelRenderer);
    }


    private boolean canMoveInDirection(Direction direction, LevelRenderer levelRenderer) {
        Point2D newPosition = getPosition().add(direction.getDx(), direction.getDy());
        return isMoveValid(newPosition, levelRenderer);
    }

    private void moveInDirection(Direction direction, LevelRenderer levelRenderer) {
        double dx = direction.getDx();
        double dy = direction.getDy();
        if (canMove(dx, dy, levelRenderer)) {
            move(dx, dy, levelRenderer);
        }
    }

    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", currentPosition.getX());
        object.put("y", currentPosition.getY());
        object.put("followLeftEdge", followLeftEdge);
        return object;
    }
}
