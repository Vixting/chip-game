package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

public class Path extends Tile {
    public Path() {
        super("/images/chipgame/tiles/path.png", true); // Assuming the second argument for Tile's constructor is 'isWalkable'.
    }

    @Override
    public boolean isWalkable() {
        return true;
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {

    }


}
