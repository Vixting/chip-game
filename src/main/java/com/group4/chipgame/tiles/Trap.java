package com.group4.chipgame.tiles;

public class Trap extends Tile {

    public final Button linkedButton;

    public Trap(Button linkedButton) {
        super("/images/chipgame/tiles/trap.png", true);
        this.linkedButton = linkedButton;
    }
}