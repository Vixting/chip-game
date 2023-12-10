package com.group4.chipgame.entities.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import org.json.JSONObject;

public class PinkBall extends Enemy {
    private static final String IMAGE_PATH = "/images/chipgame/actors/pinkBall.png";
    private Direction currentDirection;
    private static final int MOVE_INTERVAL = 100;

    public PinkBall(double x, double y, Direction initialDirection) {
        super(IMAGE_PATH, x, y);
        this.currentDirection = initialDirection;
        this.moveInterval = MOVE_INTERVAL;
    }

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
