package com.group4.chipgame.Level.saving;

import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.collectibles.Key;
import com.group4.chipgame.entities.actors.tiles.*;
import org.json.JSONObject;

import java.util.Map;
import java.util.HashMap;

public class TileFactory {
    private static final Map<String, Button> buttonMap = new HashMap<>();
    private static final Map<String, Trap> pendingTraps = new HashMap<>();

    public static Tile createTile(JSONObject tileJson) {
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
            default -> null;
        };
    }

    private static LockedDoor createLockedDoor(JSONObject tileJson) {
        Key.KeyColor keyColor = Key.KeyColor.valueOf(tileJson.getString("requiredKeyColor"));
        return new LockedDoor(keyColor);
    }

    private static Button createButton(JSONObject tileJson) {
        String buttonId = tileJson.getString("id");
        Button button = new Button(buttonId);
        buttonMap.put(buttonId, button);
        linkPendingTraps(buttonId, button);
        return button;
    }

    private static Ice createIce(JSONObject tileJson) {
        Direction.Corner corner = Direction.Corner.NONE;
        if (tileJson.has("corner")) {
            corner = Direction.Corner.valueOf(tileJson.getString("corner"));
        }
        return new Ice(corner);
    }

    private static Trap createTrap(JSONObject tileJson) {
        String linkedButtonId = tileJson.getString("id");
        Button linkedButton = buttonMap.get(linkedButtonId);
        Trap trap;
        if (linkedButton != null) {
            trap = new Trap(linkedButton, linkedButtonId);
        } else {
            trap = new Trap(null, linkedButtonId);
            pendingTraps.put(linkedButtonId, trap);
        }
        return trap;
    }

    private static void linkPendingTraps(String buttonId, Button button) {
        if (pendingTraps.containsKey(buttonId)) {
            Trap pendingTrap = pendingTraps.get(buttonId);
            pendingTrap.setLinkedButton(button);
            pendingTraps.remove(buttonId);
        }
    }
}
