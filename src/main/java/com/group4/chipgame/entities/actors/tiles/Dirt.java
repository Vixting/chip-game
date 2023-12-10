package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;

/**
 * Represents a Dirt tile in the ChipGame.
 */
public class Dirt extends Tile {
    private static final String DIRT_IMAGE_PATH = "/images/chipgame/tiles/dirt.jpg";

    /**
     * Constructs a new Dirt tile.
     */
    public Dirt() {
        super(DIRT_IMAGE_PATH, true);
    }

    /**
     * Defines the action to be taken when an Actor steps on this Dirt tile.
     *
     * @param actor The Actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from which the Actor steps onto the tile.
     */
    @Override
    public void onStep(final Actor actor, final LevelRenderer levelRenderer,
                       final Direction incomingDirection) {
        Path path = new Path();
        path.setOccupiedBy(actor);
        levelRenderer.updateTile(this.getGridX(), this.getGridY(), path);
    }
}
