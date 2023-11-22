package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.collectibles.Key;

/**
 * The LockedDoor class represents a tile in the Chip's Challenge game that requires a specific key to unlock.
 * When a player steps on it with the correct key, the door unlocks, and the player can proceed.
 */
public class LockedDoor extends Tile {

    private final Key.KeyColor requiredKeyColor;

    /**
     * Constructs a LockedDoor tile with the specified required key color.
     *
     * @param requiredKeyColor The color of the key required to unlock the door.
     */
    public LockedDoor(Key.KeyColor requiredKeyColor) {
        super(getImagePathForColor(requiredKeyColor), false);
        this.requiredKeyColor = requiredKeyColor;
    }

    /**
     * Gets the color of the key required to unlock the door.
     *
     * @return The required key color.
     */
    public Key.KeyColor getRequiredKeyColor() {
        return requiredKeyColor;
    }

    /**
     * Returns the image path for the specified key color.
     *
     * @param color The color of the key.
     * @return The image path for the corresponding key color.
     */
    private static String getImagePathForColor(Key.KeyColor color) {
        return switch (color) {
            case RED -> "/images/chipgame/tiles/doors/door_red.jpg";
            case GREEN -> "/images/chipgame/tiles/doors/door_green.jpg";
            case YELLOW -> "/images/chipgame/tiles/doors/door_yellow.jpg";
            case BLUE -> "/images/chipgame/tiles/doors/door.jpg";
            default -> "images/chipgame/tiles/doors/door.jpg";
        };
    }

    /**
     * Checks if the given key is the correct key to unlock the door.
     *
     * @param key The key to check.
     * @return True if the key is correct, false otherwise.
     */
    public boolean correctKey(Key key) {
        return key.getColor() == requiredKeyColor;
    }

    /**
     * Handles the event when an actor steps on the LockedDoor tile.
     * If the actor is a player with the correct key, the door unlocks and the player uses the key.
     *
     * @param actor             The actor stepping on the LockedDoor tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        System.out.println("Locked door!");
        if (actor instanceof Player player) {
            if (player.hasKey(requiredKeyColor)) {
                player.useKey(requiredKeyColor);
                levelRenderer.updateTile(this.gridX, this.gridY, new Path());
            }
        }
    }
}
