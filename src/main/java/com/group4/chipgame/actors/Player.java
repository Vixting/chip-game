package com.group4.chipgame.actors;

public class Player extends Actor {
    private static final double MOVE_SPEED = 1;

    public Player(double x, double y) {
        super("/images/chipgame/actors/steve.png", x, y);
    }

    @Override
    public void onCollect(Actor actor) {
        // Implement logic as needed.
    }
}
