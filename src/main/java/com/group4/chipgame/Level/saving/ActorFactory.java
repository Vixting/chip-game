package com.group4.chipgame.Level.saving;

import com.group4.chipgame.entities.actors.*;
import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.collectibles.Key;
import org.json.JSONArray;
import org.json.JSONObject;

public class ActorFactory {

    public static Actor createActor(JSONObject actorJson) {
        String type = actorJson.getString("type");
        double x = actorJson.getDouble("x");
        double y = actorJson.getDouble("y");

        return switch (type) {
            case "Player" -> createPlayer(actorJson);
            case "Bug" -> new Bug(x, y, actorJson.getBoolean("followLeftEdge"));
            case "Frog" -> new Frog(x, y);
            case "MovableBlock" -> new MovableBlock(x, y);
            case "PinkBall" -> new PinkBall(x, y, Direction.valueOf(actorJson.getString("initialDirection")));
            default -> throw new IllegalStateException("Unexpected actor type: " + type);
        };
    }

    private static Player createPlayer(JSONObject actorJson) {
        Player player = new Player(actorJson.getDouble("x"), actorJson.getDouble("y"));
        player.setAlive(actorJson.getBoolean("isAlive"));
        player.setChipsCount(actorJson.getInt("chipsCount"));

        JSONArray keysArray = actorJson.getJSONArray("collectedKeys");
        for (int i = 0; i < keysArray.length(); i++) {
            String keyColorStr = keysArray.getString(i);
            Key.KeyColor keyColor = Key.KeyColor.valueOf(keyColorStr);
            player.addKey(new Key(keyColor, 0, 0));
        }
        return player;
    }
}
