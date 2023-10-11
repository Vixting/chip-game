package com.group4.chipgame.collectibles;

import com.group4.chipgame.actors.Actor;
import javafx.geometry.Point2D;

public class Collectible extends Actor {
    public Collectible(String imagePath, Point2D position) {
        super(imagePath, position);
    }

    @Override
    public void move(double dx, double dy) {

    }

    // Override this in specific collectibles to define behavior upon collection.
    @Override
    public void onCollect(Actor actor) {
        setVisible(false);
    }
}