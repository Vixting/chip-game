package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.MovableBlock;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.tiles.Tile;

public class CollisionHandler {

    // This method checks for a collision between two actors.
    public boolean actorsCollide(Actor actor1, Actor actor2) {
        return actor1.getBoundsInParent().intersects(actor2.getBoundsInParent());
    }

    // Handle the specific logic when an actor collides with another actor.
    public void handleActorOnActorCollision(Actor actor1, Actor actor2, double dx, double dy, LevelRenderer levelRenderer) {
        if (actor1 instanceof Player && actor2 instanceof MovableBlock) {
            pushBlock((MovableBlock) actor2, dx, dy, levelRenderer);
        } else if (actor2 instanceof Player && actor1 instanceof MovableBlock) {
            pushBlock((MovableBlock) actor1, -dx, -dy, levelRenderer);
        } else if (actor1 instanceof Player || actor2 instanceof Player) {
            System.out.println("Player collided with something else!");
        }
    }

    private void pushBlock(MovableBlock block, double dx, double dy, LevelRenderer levelRenderer) {
        block.move(dx, dy, levelRenderer);
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
