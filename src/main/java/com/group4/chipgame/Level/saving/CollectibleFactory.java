package com.group4.chipgame.Level.saving;

import com.group4.chipgame.entities.actors.collectibles.Chip;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.collectibles.Key;
import org.json.JSONObject;

public class CollectibleFactory {
    public static Collectible createCollectible(JSONObject collectibleJson) {
        String type = collectibleJson.optString("type", "Unknown");
        double x = collectibleJson.optDouble("x", -1);
        double y = collectibleJson.optDouble("y", -1);

        return switch (type) {
            case "Key" -> createKey(collectibleJson, x, y);
            case "Chip" -> new Chip(x, y);
            default -> new Collectible(collectibleJson.optString("imagePath", "default/path.jpg"), x, y);
        };
    }

    private static Key createKey(JSONObject collectibleJson, double x, double y) {
        String keyColorStr = collectibleJson.optString("keyColor", "BLUE");
        try {
            Key.KeyColor keyColor = Key.KeyColor.valueOf(keyColorStr);
            return new Key(keyColor, x, y);
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid key color: " + keyColorStr);
            return null;
        }
    }
}
