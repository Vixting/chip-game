package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

public class Exit extends Tile {

    public Exit() {
        super("/images/chipgame/tiles/exit.png", true);
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        super.onStep(actor, levelRenderer, incomingDirection);
        System.out.println("Level complete");
    }
}
