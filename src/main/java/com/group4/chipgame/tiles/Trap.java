package com.group4.chipgame.tiles;

/**
 * The Trap class represents a tile in the Chip's Challenge game that is linked to a Button.
 * When the Button is pressed, the Trap becomes inactive, allowing actors to move freely over it.
 */
public class Trap extends Tile {

    /**
     * The Button linked to the Trap. Pressing this Button deactivates the Trap.
     */
    public final Button linkedButton;

    /**
     * Constructs a Trap tile with the specified linked Button.
     *
     * @param linkedButton The Button linked to the Trap.
     */
    public Trap(Button linkedButton) {
        super("/images/chipgame/tiles/trap.png", true);
        this.linkedButton = linkedButton;
    }
}
