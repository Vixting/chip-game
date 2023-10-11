package com.group4.chipgame.actors;

import javafx.geometry.Point2D;

public class Player extends Actor {

    public Player(double x, double y) {
        super("/images/chipgame/actors/steve.png", x, y);
    }

    @Override
    public void move(double dx, double dy) {
        // Basic movement. Modify as needed for more complex movement logic.
        Point2D newPosition = getPosition().add(dx, dy);
        setPosition(newPosition);
    }

    @Override
    public void onCollect(Actor actor) {
        // Behavior when the player collects an item or interacts with another actor.
        // For now, it's empty. Implement specific logic as needed.
    }
}
