package com.group4.chipgame;

import com.group4.chipgame.actors.*;
import com.group4.chipgame.collectibles.*;
import com.group4.chipgame.tiles.*;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

public class CollisionHandler {

    public boolean actorsCollide(Actor actor1, Actor actor2) {
        return actor1.getBoundsInParent().intersects(actor2.getBoundsInParent());
    }

    public void handleActorOnActorCollision(Entity actor1, Entity actor2, double dx, double dy, LevelRenderer levelRenderer) {
        if (actor1 instanceof MovableBlock && actor2 instanceof Player || actor1 instanceof Player && actor2 instanceof MovableBlock) {
            Player player = actor1 instanceof Player ? (Player) actor1 : (Player) actor2;
            MovableBlock block = actor1 instanceof MovableBlock ? (MovableBlock) actor1 : (MovableBlock) actor2;
        }
    }

    public boolean actorTileCollide(Actor actor, Tile tile, double dx, double dy) {
        Bounds futureBounds = new BoundingBox(
                actor.getBoundsInParent().getMinX() + dx,
                actor.getBoundsInParent().getMinY() + dy,
                actor.getBoundsInParent().getWidth(),
                actor.getBoundsInParent().getHeight());
        return futureBounds.intersects(tile.getBoundsInParent());
    }

    public boolean actorCollidesWithCollectible(Actor actor, Collectible collectible) {
        return actor.getBoundsInParent().intersects(collectible.getBoundsInParent());
    }

    public void handleActorOnCollectibleCollision(Actor actor, Collectible collectible, LevelRenderer levelRenderer) {
        if (actor instanceof Player player && collectible instanceof Key key) {

            player.onCollect(key); // Player collects the key
            levelRenderer.remove(collectible); // Remove the key from the game
        }
    }


    public void handleTileInteraction(Actor actor, double dx, double dy, LevelRenderer levelRenderer) {
        int newX = (int) (actor.getPosition().getX() + dx);
        int newY = (int) (actor.getPosition().getY() + dy);

        levelRenderer.getTileAtGridPosition(newX, newY)
                .filter(tile -> tile instanceof LockedDoor && actor instanceof Player)
                .ifPresent(tile -> tile.onStep(actor, levelRenderer, null));
    }


}
