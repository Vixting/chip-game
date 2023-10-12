package com.group4.chipgame.actors;

import com.group4.chipgame.LevelRenderer;
import javafx.geometry.Point2D;

public class Creeper extends Actor {

    public Creeper(double x, double y) {
        super("/images/chipgame/actors/creeper.jpg", x, y);
    }

    @Override
    public void onCollect(Actor actor) {

    }
}
