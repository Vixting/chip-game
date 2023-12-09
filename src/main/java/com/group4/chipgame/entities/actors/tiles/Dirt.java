package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;


public class Dirt extends Tile {

    public Dirt() {
        super("/images/chipgame/tiles/dirt.jpg", true);
    }

    @Override
    public void onStep(final Actor actor, final LevelRenderer levelRenderer,
                       final Direction incomingDirection) {
        Path path = new Path();
        path.setOccupiedBy(actor);
        levelRenderer.updateTile(this.gridX, this.gridY, path);
    }
}
