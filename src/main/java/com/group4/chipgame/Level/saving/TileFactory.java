package com.group4.chipgame.Level.saving;

import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.collectibles.Key;
import com.group4.chipgame.entities.actors.tiles.*;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

/**
 * A factory class for creating Tile instances from JSON data.
 * This class is responsible for instantiating
 * different types of Tile objects based on JSON input.
 * @author William Buckley
 */
public class TileFactory {
    private static final Map<String, Button> BUTTON_MAP = new HashMap<>();
    private static final Map<String, Trap> PENDING_TRAPS = new HashMap<>();

    /**
     * Creates a Tile object from the provided JSON data.
     * The specific type of Tile (e.g., LockedDoor, Button,
     * Ice, etc.) is determined by the 'type' field in the JSON object.
     *
     * @param tileJson The JSON object containing the data for the tile.
     * @return An instance of a Tile, as specified in the JSON object.
     */
    public static Tile createTile(final JSONObject tileJson) {
        String type = tileJson.getString("type");

        return switch (type) {
            case "LockedDoor" -> createLockedDoor(tileJson);
            case "Button" -> createButton(tileJson);
            case "Dirt" -> new Dirt();
            case "Exit" -> new Exit();
            case "Ice" -> createIce(tileJson);
            case "Path" -> new Path();
            case "Trap" -> createTrap(tileJson);
            case "Wall" -> new Wall();
            case "Water" -> new Water();
            case "ChipSocket" -> createChipSocket(tileJson);
            default -> null;
        };
    }

    private static LockedDoor createLockedDoor(final JSONObject tileJson) {
        Key.KeyColor keyColor = Key.KeyColor.valueOf(tileJson.getString(
                "requiredKeyColor"));
        return new LockedDoor(keyColor);
    }

    private static ChipSocket createChipSocket(final JSONObject tileJson) {
        int requiredChips = tileJson.optInt("requiredChips", 0);
        return new ChipSocket(requiredChips);
    }

    private static Button createButton(final JSONObject tileJson) {
        String buttonId = tileJson.getString("id");
        Button button = new Button(buttonId);
        BUTTON_MAP.put(buttonId, button);
        linkPendingTraps(buttonId, button);
        return button;
    }

    private static Ice createIce(final JSONObject tileJson) {
        Direction.Corner corner = Direction.Corner.NONE;
        if (tileJson.has("corner")) {
            corner = Direction.Corner.valueOf(tileJson.getString("corner"));
        }
        return new Ice(corner);
    }

    private static Trap createTrap(final JSONObject tileJson) {
        String linkedButtonId = tileJson.getString("id");
        Button linkedButton = BUTTON_MAP.get(linkedButtonId);
        Trap trap;
        if (linkedButton != null) {
            trap = new Trap(linkedButton, linkedButtonId);
        } else {
            trap = new Trap(null, linkedButtonId);
            PENDING_TRAPS.put(linkedButtonId, trap);
        }
        return trap;
    }

    /**
     * Links pending traps to a button that has just been created.
     * This method is used to establish the relationship
     * between traps and their corresponding buttons.
     *
     * @param buttonId The identifier of the button.
     * @param button   The button object to link to the trap.
     */
    private static void linkPendingTraps(final String buttonId,
                                         final Button button) {
        if (PENDING_TRAPS.containsKey(buttonId)) {
            Trap pendingTrap = PENDING_TRAPS.get(buttonId);
            pendingTrap.setLinkedButton(button);
            PENDING_TRAPS.remove(buttonId);
        }
    }
}
