package com.group4.chipgame.actors;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.collectibles.Key;
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

            if (targetTileOpt.isPresent()) {
                Tile targetTile = targetTileOpt.get();
                Entity occupiedBy = targetTile.getOccupiedBy();

                if (occupiedBy instanceof MovableBlock block) {
                    block.push(dx, dy, levelRenderer);
                }
            }

            if (canMove(dx, dy, levelRenderer)) {
                checkForCollectibles(newX, newY, levelRenderer);
                performMove(newX, newY, levelRenderer, direction);
            }
        }
    }

    private void checkForCollectibles(double x, double y, LevelRenderer levelRenderer) {
        System.out.println("Checking for collectibles!");
        Optional<Tile> tileOpt = levelRenderer.getTileAtGridPosition((int) x, (int) y);
        tileOpt.ifPresent(tile -> {
            Entity entity = tile.getOccupiedBy();
            System.out.println("Entity: " + entity);
            if (entity instanceof Collectible collectible) {
                System.out.println("Player collected a collectible!");
                onCollect(collectible);
                collectible.onCollect(this);
                levelRenderer.remove(collectible);
            }
        });
    }


    public void kill(Pane gamePane) {
        isAlive = false;
        gamePane.getChildren().remove(this);
        System.out.println("Player has been killed and removed from the game!");
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void onCollect(Entity actor) {
        if (actor instanceof Key key) {
            addKey(key);
            System.out.println("Player collected a " + key.getColor() + " key!");
        }
    }
}
