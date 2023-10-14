package com.group4.chipgame.tiles;

public class Wall extends Tile {
    public Wall() {
        super("/images/chipgame/tiles/iron.jpg", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }

    @Override
    public boolean isWalkable() {
        return false;
    }
}
