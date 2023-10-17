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
        if (actor1 instanceof MovableBlock || actor2 instanceof MovableBlock) {
            if (actor1 instanceof Player || actor2 instanceof Player) {
                assert actor1 instanceof Player;
                handlePlayerBlockInteraction((Player) actor1, (MovableBlock) actor2, dx, dy, levelRenderer);
            }
        }

        // Handle key collection
        if (actor1 instanceof Key || actor2 instanceof Key) {
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
        if (nextTile.isPresent() && nextTile.get() instanceof LockedDoor && actor instanceof Player) {
            nextTile.get().onStep(actor, levelRenderer, null);
        }
    }

    private void handlePlayerBlockInteraction(Player player, MovableBlock block, double dx, double dy, LevelRenderer levelRenderer) {
        double blockNewX = block.currentPosition.getX() + dx;
        double blockNewY = block.currentPosition.getY() + dy;
        Optional<Tile> blockTargetTile = levelRenderer.getTileAtGridPosition((int) blockNewX, (int) blockNewY);

        if (blockTargetTile.isPresent() && blockTargetTile.get().isWalkable() && !blockTargetTile.get().isOccupied()) {
            block.move(dx, dy, levelRenderer, this);
            player.performMove(player.currentPosition.getX() + dx, player.currentPosition.getY() + dy, levelRenderer, Direction.fromDelta(dx, dy));
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
}
