package com.group4.chipgame.collectibles;

import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;
import javafx.geometry.Point2D;

public class Collectible extends Actor {

    public Collectible(String imagePath, double gridX, double gridY) {
        super(imagePath, gridX, gridY);
    }

    @Override
    public void move(double dx, double dy, LevelRenderer levelRenderer) {

    }

    @Override
    public void onCollect(Actor actor) {

    }
}