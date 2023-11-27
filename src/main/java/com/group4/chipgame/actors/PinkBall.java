package com.group4.chipgame.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;

public class PinkBall extends Enemy {
    private static final String IMAGE_PATH = "/images/chipgame/actors/creeper.jpg";
    private Direction currentDirection;

    public PinkBall(double x, double y, Direction initialDirection) {
        super(IMAGE_PATH, x, y);
        this.currentDirection = initialDirection;
        this.moveInterval = 100;
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
}
