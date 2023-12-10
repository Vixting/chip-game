package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import org.json.JSONObject;

/**
 * Represents a PinkBall enemy in the ChipGame.
 * PinkBall is a type of enemy that moves in a straight line until it hits an obstacle,
 * at which point it reverses direction.
 */
public class PinkBall extends Enemy {
    private static final String IMAGE_PATH = "/images/chipgame/actors/pinkBall.png";
    private Direction currentDirection;
    private static final int MOVE_INTERVAL = 100;

    /**
     * Constructs a PinkBall with a specified initial position and direction.
     *
     * @param x                The initial x-coordinate of the PinkBall.
     * @param y                The initial y-coordinate of the PinkBall.
     * @param initialDirection The initial direction in which the PinkBall will move.
     */
    public PinkBall(double x, double y, Direction initialDirection) {
        super(IMAGE_PATH, x, y);
        this.currentDirection = initialDirection;
        this.setMoveInterval(MOVE_INTERVAL);
    }

    /**
     * Decides the next move for the PinkBall based on its current direction and position.
     * The PinkBall moves in its current direction until it can no longer move, at which point it reverses direction.
     *
     * @param levelRenderer The renderer for the game level.
     */
    @Override
    public void makeMoveDecision(LevelRenderer levelRenderer) {
        double dx = currentDirection.getDx();
        double dy = currentDirection.getDy();

        if (canMove(dx, dy, levelRenderer)) {
            double[] delta = Direction.toDelta(currentDirection);
            super.move(delta[0], delta[1], levelRenderer);
        } else {
            currentDirection = currentDirection.getOpposite();

        }
    }

    /**
     * Serializes the state of the PinkBall to a JSON object.
     *
     * @return A JSONObject representing the current state of the PinkBall.
     */
    @Override
    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", getCurrentPosition().getX());
        object.put("y", getCurrentPosition().getY());
        object.put("initialDirection", currentDirection.name());
        return object;
    }
}
