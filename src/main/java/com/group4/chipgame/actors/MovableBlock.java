package com.group4.chipgame.actors;

public class MovableBlock extends Actor {

    public MovableBlock(double x, double y) {
        super("/images/chipgame/actors/wood.png", x, y);
    }


    @Override
    public void onCollect(Actor actor) {
        System.out.println("Movable block collected!");
    }
}