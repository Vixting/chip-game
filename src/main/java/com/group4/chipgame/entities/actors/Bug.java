package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import javafx.geometry.Point2D;
import org.json.JSONObject;

/**
 * Represents a Bug enemy in the ChipGame.
 * A Bug is an enemy that moves around the level,
 * potentially following the left edge.
 * @author William Buckley
 */
public class Bug extends Enemy {
    private final boolean followLeftEdge;
    private Direction currentDirection;
    private static final String BUG_IMAGE_PATH =
            "/images/chipgame/actors/bug.png";
    private static final int MOVE_INTERVAL = 100;

    /**
     * Constructs a Bug with specified initial position and movement behavior.
     *
     * @param x              The initial x-coordinate of the Bug.
     * @param y              The initial y-coordinate of the Bug.
     * @param followLeftEdge Determines
     *                       whether the Bug should follow the left edge.
     */
    public Bug(final double x,
               final double y,
               final boolean followLeftEdge) {
        super(BUG_IMAGE_PATH, x, y);
        this.followLeftEdge = followLeftEdge;
        this.setMoveInterval(MOVE_INTERVAL);
        this.currentDirection = Direction.LEFT;
    }

    /**
     * Decides the next move for the Bug
     * based on its current position and movement rules.
     *
     * @param levelRenderer The renderer for the game level.
     */
    @Override
    public void makeMoveDecision(final LevelRenderer levelRenderer) {
        if (!canMoveInDirection(
                currentDirection, levelRenderer)) {
            Direction sideDirection =
                    followLeftEdge ? currentDirection.turnLeft()
                            : currentDirection.turnRight();
            if (isWallOnSide(sideDirection, levelRenderer)) {
                currentDirection =
                        followLeftEdge ? currentDirection.turnRight()
                                : currentDirection.turnLeft();
            } else {
                currentDirection = sideDirection;
            }
        } else {
            Direction sideDirection =
                    followLeftEdge ? currentDirection.turnRight()
                            : currentDirection.turnLeft();
            if (!isWallOnSide(sideDirection, levelRenderer)) {
                currentDirection = sideDirection;
            }
        }

        if (canMoveInDirection(currentDirection, levelRenderer)) {
            moveInDirection(currentDirection, levelRenderer);
        }
    }

    /**
     * Checks if there is a wall on a given side of the Bug.
     *
     * @param sideDirection The direction to check for a wall.
     * @param levelRenderer The renderer for the game level.
     * @return true if there is a wall on the given side, false otherwise.
     */
    private boolean isWallOnSide(final Direction sideDirection,
                                 final LevelRenderer levelRenderer) {
        Point2D sidePosition = getPosition()
                .add(sideDirection.getDx(), sideDirection.getDy());
        return !isMoveValid(sidePosition, levelRenderer);
    }

    /**
     * Checks if the Bug can move in a given direction.
     *
     * @param direction     The direction to check for possible movement.
     * @param levelRenderer The renderer for the game level.
     * @return true if the Bug can move in the given direction, false otherwise.
     */
    private boolean canMoveInDirection(final Direction direction,
                                       final LevelRenderer levelRenderer) {
        Point2D newPosition = getPosition()
                .add(direction.getDx(), direction.getDy());
        return isMoveValid(newPosition, levelRenderer);
    }

    /**
     * Moves the Bug in a specified direction.
     *
     * @param direction     The direction in which to move the Bug.
     * @param levelRenderer The renderer for the game level.
     */
    private void moveInDirection(final Direction direction,
                                 final LevelRenderer levelRenderer) {
        double dx = direction.getDx();
        double dy = direction.getDy();
        if (canMove(dx, dy, levelRenderer)) {
            move(dx, dy, levelRenderer);
        }
    }

    /**
     * Serializes the state of the Bug to a JSON object.
     *
     * @return A JSONObject representing the current state of the Bug.
     */
    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", getCurrentPosition().getX());
        object.put("y", getCurrentPosition().getY());
        object.put("followLeftEdge", followLeftEdge);
        return object;
    }
}
