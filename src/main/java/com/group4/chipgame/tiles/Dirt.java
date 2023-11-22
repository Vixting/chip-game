package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;

/**
 * The Dirt class represents a type of tile in the ChipGame that can be stepped on by actors.
 * It extends the Tile class and inherits its properties and behavior.
 */
public class Dirt extends Tile {
    /**
     * Constructs a Dirt tile with the default image path and sets it as walkable.
     */
    public Dirt() {
        super("/images/chipgame/tiles/dirt.jpg", true);
    }

    /**
     * Overrides the onStep method in the Tile class to handle actions when an actor steps on the Dirt tile.
     * Marks the current tile as occupied by the actor and updates the LevelRenderer to reflect the change.
     *
     * @param actor             The actor that is stepping on the Dirt tile.
     * @param levelRenderer     The level renderer responsible for rendering the game level.
     * @param incomingDirection The direction from which the actor is approaching the tile.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        super.onStep(actor, levelRenderer, incomingDirection);

        // Create a new Path object to mark the tile as occupied by the actor
        Path path = new Path();
        path.setOccupiedBy(actor);

        // Update the LevelRenderer to reflect the change in the tile's occupancy
        levelRenderer.updateTile(this.gridX, this.gridY, path);
    }
}
