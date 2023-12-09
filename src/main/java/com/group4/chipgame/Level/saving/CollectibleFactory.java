package com.group4.chipgame.Level.saving;

import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.collectibles.Key;
import org.json.JSONObject;

public class CollectibleFactory {
    public static Collectible createCollectible(JSONObject collectibleJson) {
        String type = collectibleJson.optString("type", "Unknown");
        double x = collectibleJson.optDouble("x", -1);
        double y = collectibleJson.optDouble("y", -1);

        switch (type) {
            case "Key":
                String keyColorStr = collectibleJson.optString("keyColor", "BLUE");
                try {
                    Key.KeyColor keyColor = Key.KeyColor.valueOf(keyColorStr);
                    return new Key(keyColor, x, y);
                } catch (IllegalArgumentException e) {
                    System.err.println("Invalid key color: " + keyColorStr);
                    return null;
                }

            default:
                String imagePath = collectibleJson.optString("imagePath", "default/path.jpg");
                return new Collectible(imagePath, x, y);
        }
    }
}
