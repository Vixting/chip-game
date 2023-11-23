package com.group4.chipgame.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.collectibles.Key;
import com.group4.chipgame.tiles.LockedDoor;
import com.group4.chipgame.tiles.Tile;
import javafx.scene.layout.Pane;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class Player extends Actor {
    private static final String PLAYER_IMAGE_PATH = "/images/chipgame/actors/steve.png";

    private boolean isAlive = true;
    private final Set<Key.KeyColor> collectedKeys = EnumSet.noneOf(Key.KeyColor.class);

    public Player(double x, double y) {
        super(PLAYER_IMAGE_PATH, x, y);
        this.moveInterval = 3;
    }

    public void addKey(Key key) {
        collectedKeys.add(key.getColor());
    }

    public boolean hasKey(Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    public void useKey(Key.KeyColor keyColor) {
        collectedKeys.remove(keyColor);
    }

    @Override
    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (!isMoving) {
            Direction direction = Direction.fromDelta(dx, dy);
            double newX = currentPosition.getX() + dx;
            double newY = currentPosition.getY() + dy;

            Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);
            targetTileOpt.ifPresent(targetTile -> {
                Entity occupiedBy = targetTile.getOccupiedBy();
                if (occupiedBy instanceof MovableBlock block) {
                    block.push(dx, dy, levelRenderer);
                } else if (targetTile instanceof LockedDoor door) {
                    door.onStep(this, levelRenderer, direction);
                }
            });

            if (canMove(dx, dy, levelRenderer)) {
                checkForCollectibles(newX, newY, levelRenderer);
                performMove(newX, newY, levelRenderer, direction);
            }
        }
    }

    public void checkForCollectibles(double x, double y, LevelRenderer levelRenderer) {
        Optional<Tile> tileOpt = levelRenderer.getTileAtGridPosition((int) x, (int) y);
        tileOpt.ifPresent(tile -> {
            Entity entity = tile.getOccupiedBy();
            if (entity instanceof Collectible collectible) {
                onCollect(collectible);
                collectible.onCollect(this);
                levelRenderer.remove(collectible);
                tile.setOccupiedBy(null);
            }
        });
    }

    private void kill(Pane gamePane) {
        isAlive = false;
        gamePane.getChildren().remove(this);
    }

    private boolean isAlive() {
        return isAlive;
    }

    private void onCollect(Entity collectible) {
        if (collectible instanceof Key key) {
            addKey(key);
        }
    }
}
