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

/**
 * The Player class represents the player-controlled character in the game.
 * It extends the Actor class and implements methods specific to player actions.
 */
public class Player extends Actor {

    private static final String PLAYER_IMAGE_PATH = "/images/chipgame/actors/steve.png";

    private boolean isAlive = true;
    private final Set<Key.KeyColor> collectedKeys = EnumSet.noneOf(Key.KeyColor.class);

    /**
     * Constructs a Player object with the specified initial grid position.
     *
     * @param x The initial x-coordinate on the grid.
     * @param y The initial y-coordinate on the grid.
     */
    public Player(double x, double y) {
        super(PLAYER_IMAGE_PATH, x, y);
        this.moveInterval = 3;
    }

    /**
     * Adds a key to the player's collected keys.
     *
     * @param key The key to be added to the player's collection.
     */
    public void addKey(Key key) {
        collectedKeys.add(key.getColor());
    }

    /**
     * Checks if the player has a key of the specified color.
     *
     * @param keyColor The color of the key to check for.
     * @return True if the player has the key, false otherwise.
     */
    public boolean hasKey(Key.KeyColor keyColor) {
        return collectedKeys.contains(keyColor);
    }

    /**
     * Uses a key of the specified color, removing it from the player's collection.
     *
     * @param keyColor The color of the key to use.
     */
    public void useKey(Key.KeyColor keyColor) {
        collectedKeys.remove(keyColor);
    }

    /**
     * Moves the player in the specified direction on the grid, interacting with tiles and entities.
     *
     * @param dx            The change in x-coordinate.
     * @param dy            The change in y-coordinate.
     * @param levelRenderer The renderer for the level.
     */
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
                } else if (targetTile instanceof LockedDoor door) {
                    door.onStep(this, levelRenderer, direction);
                }
            }

            if (canMove(dx, dy, levelRenderer)) {
                checkForCollectibles(newX, newY, levelRenderer);
                performMove(newX, newY, levelRenderer, direction);
            }
        }
    }

    /**
     * Checks for collectibles on the current tile and collects them if present.
     *
     * @param x            The x-coordinate on the grid.
     * @param y            The y-coordinate on the grid.
     * @param levelRenderer The renderer for the level.
     */
    public void checkForCollectibles(double x, double y, LevelRenderer levelRenderer) {
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
                tile.setOccupiedBy(null);
            }
        });
    }

    /**
     * Marks the player as killed, removes them from the game pane, and prints a message.
     *
     * @param gamePane The game pane.
     */
    public void kill(Pane gamePane) {
        isAlive = false;
        gamePane.getChildren().remove(this);
        System.out.println("Player has been killed and removed from the game!");
    }

    /**
     * Checks if the player is alive.
     *
     * @return True if the player is alive, false otherwise.
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Handles the collection of entities by the player, specifically keys.
     *
     * @param actor The entity being collected.
     */
    public void onCollect(Entity actor) {
        if (actor instanceof Key key) {
            addKey(key);
            System.out.println("Player collected a " + key.getColor() + " key!");
        }
    }
}
