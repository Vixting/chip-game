package com.group4.chipgame.actors;

/**
 * The Creeper class represents a creeper actor in the chip game.
 * It extends the Actor class and adds functionality specific to creeper actors.
 */
public class Creeper extends Actor {

    /**
     * Constructs a Creeper with the specified initial position.
     *
     * @param x The initial x-coordinate of the creeper.
     * @param y The initial y-coordinate of the creeper.
     */
    public Creeper(double x, double y) {
        super("/images/chipgame/actors/creeper.jpg", x, y);
        this.moveInterval = 3;
    }

}
