package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.MovableBlock;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.collectibles.Key;
import com.group4.chipgame.tiles.Dirt;
import com.group4.chipgame.tiles.LockedDoor;
import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.tiles.Water;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;

import java.util.Optional;

public class CollisionHandler {

    public boolean actorsCollide(Actor actor1, Actor actor2) {
        return actor1.getBoundsInParent().intersects(actor2.getBoundsInParent());
    }

    public void handleActorOnActorCollision(Actor actor1, Actor actor2, double dx, double dy, LevelRenderer levelRenderer) {
        if ((actor1 instanceof MovableBlock && actor2 instanceof Player) || (actor1 instanceof Player && actor2 instanceof MovableBlock)) {
            Player player = (actor1 instanceof Player) ? (Player) actor1 : (Player) actor2;
            MovableBlock block = (actor1 instanceof MovableBlock) ? (MovableBlock) actor1 : (MovableBlock) actor2;
            handlePlayerBlockInteraction(player, block, dx, dy, levelRenderer);
        } else if (actor1 instanceof Key || actor2 instanceof Key) {
            Actor player = (actor1 instanceof Player) ? actor1 : actor2;
            Actor key = (actor1 instanceof Key) ? actor1 : actor2;
            player.onCollect(key);
            levelRenderer.removeActor(key);
        }
    }

    public boolean actorTileCollide(Actor actor, Tile tile, double dx, double dy) {
        Bounds futureBounds = new BoundingBox(actor.getBoundsInParent().getMinX() + dx,
                actor.getBoundsInParent().getMinY() + dy,
                actor.getBoundsInParent().getWidth(),
                actor.getBoundsInParent().getHeight());
        return futureBounds.intersects(tile.getBoundsInParent());
    }

    public void handleTileInteraction(Actor actor, double dx, double dy, LevelRenderer levelRenderer) {
        int newX = (int) (actor.currentPosition.getX() + dx);
        int newY = (int) (actor.currentPosition.getY() + dy);

        Optional<Tile> nextTile = levelRenderer.getTileAtGridPosition(newX, newY);
        System.out.println(nextTile);
        if (nextTile.isPresent() && nextTile.get() instanceof LockedDoor  && actor instanceof Player) {
            nextTile.get().onStep(actor, levelRenderer, null);
        }
    }

    private void handlePlayerBlockInteraction(Player player, MovableBlock block, double dx, double dy, LevelRenderer levelRenderer) {
        double blockNewX = block.currentPosition.getX() + dx;
        double blockNewY = block.currentPosition.getY() + dy;
        Optional<Tile> blockTargetTile = levelRenderer.getTileAtGridPosition((int) blockNewX, (int) blockNewY);

        System.out.println(block);

        if (blockTargetTile.isPresent()) {
            if (blockTargetTile.get() instanceof Water) {
                Tile tile = levelRenderer.getTileAtGridPosition((int)block.getPosition().getX(), (int)block.getPosition().getY()).orElse(null);
                assert tile != null;
                System.out.println(tile.getOccupiedBy());

                levelRenderer.removeActor(block);
                levelRenderer.updateTile((int) blockNewX, (int) blockNewY, new Dirt()); 

            } else if (blockTargetTile.get().isWalkable() && blockTargetTile.get().isOccupied()) {
                block.move(dx, dy, levelRenderer, this);
                player.performMove(player.currentPosition.getX() + dx, player.currentPosition.getY() + dy, levelRenderer, Direction.fromDelta(dx, dy));
            }
        }
    }
}
