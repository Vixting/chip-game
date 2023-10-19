package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

public class Dirt extends Tile {
    public Dirt() {
        super("/images/chipgame/tiles/dirt.jpg", true);
    }

    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        super.onStep(actor, levelRenderer, incomingDirection);
        Path path = new Path();
        path.setOccupiedBy(actor);
        levelRenderer.updateTile(this.gridX, this.gridY,path);

    }
}
