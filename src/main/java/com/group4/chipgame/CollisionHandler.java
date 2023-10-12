package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.tiles.Tile;

public class CollisionHandler {

    // This method checks for a collision between two actors.
    public boolean actorsCollide(Actor actor1, Actor actor2) {
        return actor1.getBoundsInParent().intersects(actor2.getBoundsInParent());
    }

    // Handle the specific logic when an actor collides with another actor.
    public void handleActorOnActorCollision(Actor actor1, Actor actor2) {
        if ((actor1 instanceof Player && actor2 != null) || (actor2 instanceof Player && actor1 != null)) {
            // Handle Player-Enemy collision. For now, I'm printing a message.
            // You can replace this with whatever game logic you want, such as reducing player health.
            System.out.println("Player collided with an enemy!");
        }
        // You can add more conditions here to handle other types of actor collisions.
    }

    // If you need to handle collisions between an actor and a tile.
    public boolean actorTileCollide(Actor actor, Tile tile) {
        return actor.getBoundsInParent().intersects(tile.getBoundsInParent());
    }

    public void handleActorOnTileCollision(Actor actor, Tile tile) {
        if (!tile.isWalkable() && actor instanceof Player) {
            // Handle logic for when player steps on a non-walkable tile.
            System.out.println("Player stepped on a non-walkable tile!");
        }
        // You can add more conditions here to handle other types of tile interactions.
    }

    // If there are other types of collisions or interactions you want to handle,
    // you can add more methods here.
}
