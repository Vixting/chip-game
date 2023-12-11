package com.group4.chipgame.entities.actors.collectibles;

import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;

/**
 * Represents a chip collectible in the game.
 * Chips are collected by the player to achieve objectives or score points.
 * @author William Buckley
 */
public class Chip extends Collectible {

    /**
     * The path to the image representing a chip.
     */
    private static final String CHIP_IMAGE_PATH =
            "/images/chipgame/collectibles/keys/chip.png";

    /**
     * Constructs a new Chip object at the specified coordinates.
     *
     * @param x The x-coordinate of the chip's position.
     * @param y The y-coordinate of the chip's position.
     */
    public Chip(final double x, final double y) {
        super(CHIP_IMAGE_PATH, x, y);
    }

    /**
     * Defines the action to be taken when this chip is collected by an actor.
     * If the actor is a player, it increments the player's chip count.
     *
     * @param actor The actor that collected the chip.
     */
    @Override
    public void onCollect(final Actor actor) {
        if (actor instanceof Player player) {
            super.onCollect(actor);
            player.addChips(1);
            this.setVisible(false);
        }
    }
}
