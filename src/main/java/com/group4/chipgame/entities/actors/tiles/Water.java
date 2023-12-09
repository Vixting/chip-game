package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;

public class Water extends Tile {
    public Water() {
        super("/images/chipgame/tiles/water.jpg", true);
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {

    }
}
