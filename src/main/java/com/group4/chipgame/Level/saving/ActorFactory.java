package com.group4.chipgame.Level.saving;

import com.group4.chipgame.entities.actors.*;
import com.group4.chipgame.Direction;
import org.json.JSONObject;

public class ActorFactory {
    public static Actor createActor(JSONObject actorJson) {
        String type = actorJson.getString("type");
        double x = actorJson.getDouble("x");
        double y = actorJson.getDouble("y");
        System.out.println(x + " " + y );


        return switch (type) {
            case "Player" -> new Player(x, y);
            case "Bug" -> {
                boolean followLeftEdge = actorJson.getBoolean("followLeftEdge");
                yield new Bug(x, y, followLeftEdge);
            }
            case "Creeper" -> new Creeper(x, y);
            case "Frog" -> new Frog(x, y);
            case "MovableBlock" -> new MovableBlock(x, y);
            case "PinkBall" -> {
                String dir = actorJson.getString("initialDirection");
                Direction initialDirection = Direction.valueOf(dir);
                yield new PinkBall(x, y, initialDirection);
            }
            default -> throw new IllegalStateException("Unexpected actor type: " + type);
        };
    }
}
