package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

public class Wall extends Tile {
    public Wall() {
        super("/images/chipgame/tiles/iron.jpg", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {

    }

}
