package com.group4.chipgame.tiles;

public class Path extends Tile {
    public Path() {
        super("/images/chipgame/tiles/path.png", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }

    @Override
    public boolean isWalkable() {
        return true;
    }
}
