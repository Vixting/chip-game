package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.entities.actors.collectibles.Key;
import org.json.JSONObject;

/**
 * Represents a locked door tile in the game that requires a specific key color to open.
 */
public class LockedDoor extends Tile {

    private static final String RED_DOOR_IMAGE_PATH = "/images/chipgame/tiles/doors/redDoor.png";
    private static final String GREEN_DOOR_IMAGE_PATH = "/images/chipgame/tiles/doors/greenDoor.png";
    private static final String YELLOW_DOOR_IMAGE_PATH = "/images/chipgame/tiles/doors/yellowDoor.png";
    private static final String BLUE_DOOR_IMAGE_PATH = "/images/chipgame/tiles/doors/blueDoor.png";

    private final Key.KeyColor requiredKeyColor;

    /**
     * Constructs a LockedDoor with a specified key color.
     *
     * @param requiredKeyColor The color of the key required to open the door.
     */
    public LockedDoor(Key.KeyColor requiredKeyColor) {
        super(getImagePathForColor(requiredKeyColor), false);
        this.requiredKeyColor = requiredKeyColor;
    }

    /**
     * Serializes the locked door's state to JSON.
     *
     * @return A JSONObject representing the locked door's state.
     */
    @Override
    public JSONObject serialize() {
        JSONObject object = super.serialize();
        object.put("requiredKeyColor", this.requiredKeyColor.name());
        return object;
    }

    /**
     * Gets the required key color for this door.
     *
     * @return The required key color.
     */
    public Key.KeyColor getRequiredKeyColor() {
        return requiredKeyColor;
    }

    /**
     * Gets the image path for a door of a specific color.
     *
     * @param color The color of the door.
     * @return The image path for the door.
     */
    private static String getImagePathForColor(Key.KeyColor color) {
        return switch (color) {
            case RED -> RED_DOOR_IMAGE_PATH;
            case GREEN -> GREEN_DOOR_IMAGE_PATH;
            case YELLOW -> YELLOW_DOOR_IMAGE_PATH;
            case BLUE -> BLUE_DOOR_IMAGE_PATH;
        };
    }

    /**
     * Defines the action to be taken when an actor steps on this tile.
     * If the actor is a player with the correct key, the door opens.
     *
     * @param actor The actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from which the actor steps onto the tile.
     */
    @Override
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player player) {
            if (player.hasKey(requiredKeyColor)) {
                levelRenderer.updateTile(this.gridX, this.gridY, new Path());
            }
        }
    }
}
