package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.MovableBlock;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.collectibles.Key;
import com.group4.chipgame.tiles.LockedDoor;
import com.group4.chipgame.tiles.Tile;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.util.Optional;

public class CollisionHandler {

    public boolean actorsCollide(Actor actor1, Actor actor2) {
        return actor1.getBoundsInParent().intersects(actor2.getBoundsInParent());
    }

    public void handleActorOnActorCollision(Actor actor1, Actor actor2, double dx, double dy, LevelRenderer levelRenderer) {
        if (actor1 instanceof Player && actor2 instanceof MovableBlock) {
            handlePlayerBlockInteraction((Player) actor1, (MovableBlock) actor2, dx, dy, levelRenderer);
        } else if (actor2 instanceof Player && actor1 instanceof MovableBlock) {
            handlePlayerBlockInteraction((Player) actor2, (MovableBlock) actor1, -dx, -dy, levelRenderer);
        }

        if (actor1 instanceof Player && actor2 instanceof Key) {
            (actor1).onCollect(actor2);
            levelRenderer.removeActor(actor2);
        } else if (actor2 instanceof Player && actor1 instanceof Key) {
            (actor2).onCollect(actor1);
            levelRenderer.removeActor(actor1);
        }


    }

    private void handlePlayerBlockInteraction(Player player, MovableBlock block, double dx, double dy, LevelRenderer levelRenderer) {
        pushBlock(block, dx, dy, levelRenderer);
        if (!levelRenderer.getTileAtGridPosition((int) (player.currentPosition.getX() + dx), (int) (player.currentPosition.getY() + dy)).get().isOccupied()) {
            handleTileInteraction(player, dx, dy, levelRenderer); // Check for tile collisions before moving the player
            Direction direction = Direction.fromDelta(dx, dy);
            player.performMove(player.currentPosition.getX() + dx, player.currentPosition.getY() + dy, levelRenderer, direction);
        }
    }


    private void pushBlock(MovableBlock block, double dx, double dy, LevelRenderer levelRenderer) {
        double newX = block.currentPosition.getX() + dx;
        double newY = block.currentPosition.getY() + dy;
        Optional<Tile> optionalTargetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

        if (optionalTargetTile.isPresent() && optionalTargetTile.get().isWalkable() && !optionalTargetTile.get().isOccupied()) {
            block.move(dx, dy, levelRenderer, this);
        }
    }

//...

    public boolean actorTileCollide(Actor actor, Tile tile, double dx, double dy) {
        // Create a new bounding box based on actor's future position
        Bounds futureBounds = new BoundingBox(actor.getBoundsInParent().getMinX() + dx,
                actor.getBoundsInParent().getMinY() + dy,
                actor.getBoundsInParent().getWidth(),
                actor.getBoundsInParent().getHeight());
        return futureBounds.intersects(tile.getBoundsInParent());
    }

    public void handleTileInteraction(Actor actor, double dx, double dy, LevelRenderer levelRenderer) {
        System.out.printf("Actor is at (%.2f, %.2f)\n", actor.currentPosition.getX(), actor.currentPosition.getY());
        int newX = (int) (actor.currentPosition.getX() + dx);
        int newY = (int) (actor.currentPosition.getY() + dy);

        Optional<Tile> nextTile = levelRenderer.getTileAtGridPosition(newX, newY);
        if (nextTile.isPresent()) {
            System.out.println("Next tile is present!");
            System.out.println("Next tile class: " + nextTile.get().getClass().getSimpleName());

            if (nextTile.get() instanceof LockedDoor) {
                System.out.println("Next tile is a locked door!");
                if (actor instanceof Player) {
                    nextTile.get().onStep(actor, levelRenderer, null);
                }
            }
        }
    }

//...




    public void handleActorOnTileCollision(Actor actor, Tile tile, LevelRenderer levelRenderer){
        // Other tile interactions...
    }
}
