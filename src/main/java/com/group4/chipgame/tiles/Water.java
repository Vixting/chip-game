package com.group4.chipgame.tiles;

public class Water extends Tile {
    public Water() {
        super("/images/chipgame/tiles/water.jpg", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }

    @Override
    public boolean isWalkable() {
        return false;
    }
}
