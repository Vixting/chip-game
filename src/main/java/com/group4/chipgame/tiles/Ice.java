package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.Entity;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.MovableBlock;
import com.group4.chipgame.actors.Player;
import com.group4.chipgame.collectibles.Collectible;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.util.Optional;

/**
 * The Ice class represents a tile in the Chip's Challenge game that causes actors to slide in a specific direction when stepped on.
 * The direction of sliding is determined by the corner of the Ice tile.
 */
public class Ice extends Tile {

    private final Direction.Corner corner;

    /**
     * Constructs an Ice tile with the specified corner.
     *
     * @param corner The corner of the Ice tile, determining its sliding direction.
     */
    public Ice(Direction.Corner corner) {
        super(getImagePathForCorner(corner), true);
        this.corner = corner;
    }

    /**
     * Returns the image path for the specified corner of the Ice tile.
     *
     * @param corner The corner of the Ice tile.
     * @return The image path for the corresponding corner.
     */
    private static String getImagePathForCorner(Direction.Corner corner) {
        return switch (corner) {
            case NONE -> "/images/chipgame/tiles/ice/ice.jpg";
            case BOTTOM_LEFT -> "/images/chipgame/tiles/ice/ice_bottom_left.jpg";
            case BOTTOM_RIGHT -> "/images/chipgame/tiles/ice/ice_bottom_right.jpg";
            case TOP_LEFT -> "/images/chipgame/tiles/ice/ice_top_left.jpg";
            case TOP_RIGHT -> "/images/chipgame/tiles/ice/ice_top_right.jpg";
        };
    }

    /**
     * Handles the event when an actor steps on the Ice tile, causing sliding in the determined direction.
     *
     * @param actor             The actor stepping on the Ice tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        System.out.println("Sliding!");
        PauseTransition pause = createPauseTransition(actor, levelRenderer, incomingDirection);
        pause.play();
    }

    /**
     * Creates a PauseTransition for the sliding effect and sets the callback for performing the slide.
     *
     * @param actor             The actor sliding on the Ice tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     * @return The PauseTransition for the sliding effect.
     */
    private PauseTransition createPauseTransition(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(event -> performSlide(actor, levelRenderer, incomingDirection));
        return pause;
    }

    /**
     * Performs the slide of the actor in the determined direction.
     *
     * @param actor             The actor sliding on the Ice tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    private void performSlide(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        Direction slideDirection = determineSlideDirection(incomingDirection);
        double newX = actor.getPosition().getX() + slideDirection.getDx();
        double newY = actor.getPosition().getY() + slideDirection.getDy();

        if (actor instanceof Player) {
            ((Player) actor).checkForCollectibles(newX, newY, levelRenderer);
        }

        handleActorOnIce(actor, newX, newY, levelRenderer, slideDirection);
    }

    /**
     * Determines the direction of sliding based on the Ice tile's corner and the incoming direction.
     *
     * @param incomingDirection The direction from which the actor is coming.
     * @return The direction of sliding.
     */
    private Direction determineSlideDirection(Direction incomingDirection) {
        return switch (corner) {
            case BOTTOM_LEFT -> (incomingDirection == Direction.LEFT) ? Direction.UP : (incomingDirection == Direction.DOWN) ? Direction.RIGHT : incomingDirection;
            case BOTTOM_RIGHT -> (incomingDirection == Direction.RIGHT) ? Direction.UP : (incomingDirection == Direction.DOWN) ? Direction.LEFT : incomingDirection;
            case TOP_LEFT -> (incomingDirection == Direction.LEFT) ? Direction.DOWN : (incomingDirection == Direction.UP) ? Direction.RIGHT : incomingDirection;
            case TOP_RIGHT -> (incomingDirection == Direction.RIGHT) ? Direction.DOWN : (incomingDirection == Direction.UP) ? Direction.LEFT : incomingDirection;
            default -> incomingDirection;
        };
    }

    // ... (Continuation from the previous Javadoc)

    /**
     * Handles the actor's position and interactions when on an Ice tile.
     *
     * @param actor             The actor sliding on the Ice tile.
     * @param newX              The new X-coordinate after sliding.
     * @param newY              The new Y-coordinate after sliding.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    private void handleActorOnIce(Actor actor, double newX, double newY, LevelRenderer levelRenderer, Direction incomingDirection) {
        Optional<Tile> targetTileOptional = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);
        targetTileOptional.ifPresent(tile -> handleTileInteraction(actor, newX, newY, tile, levelRenderer, incomingDirection));
    }

    /**
     * Handles the interaction between the actor and the target tile.
     *
     * @param actor             The actor sliding on the Ice tile.
     * @param newX              The new X-coordinate after sliding.
     * @param newY              The new Y-coordinate after sliding.
     * @param targetTile        The tile that the actor is sliding onto.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    private void handleTileInteraction(Actor actor, double newX, double newY, Tile targetTile, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (actor instanceof Player player && targetTile instanceof LockedDoor door) {
            handlePlayerDoorInteraction(player, door, levelRenderer, incomingDirection);
            return;
        }

        if (targetTile.isWalkable()) {
            handleTileOccupancy(targetTile, actor, newX, newY, levelRenderer, incomingDirection);
        } else {
            handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
        }
    }

    /**
     * Handles the interaction between a player and a locked door.
     *
     * @param player            The player sliding on the Ice tile.
     * @param door              The locked door tile.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the player is coming.
     */
    private void handlePlayerDoorInteraction(Player player, LockedDoor door, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (player.hasKey(door.getRequiredKeyColor())) {
            door.onStep(player, levelRenderer, incomingDirection);
            onStep(player, levelRenderer, incomingDirection);
        } else {
            handleReverseSlide(player, levelRenderer, incomingDirection.getOpposite());
        }
    }

    /**
     * Handles the occupancy of the target tile by the actor.
     *
     * @param targetTile        The tile that the actor is sliding onto.
     * @param actor             The actor sliding on the Ice tile.
     * @param newX              The new X-coordinate after sliding.
     * @param newY              The new Y-coordinate after sliding.
     * @param levelRenderer     The renderer for the game level.
     * @param incomingDirection The direction from which the actor is coming.
     */
    private void handleTileOccupancy(Tile targetTile, Actor actor, double newX, double newY, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (targetTile.isOccupied()) {
            actor.performMove(newX, newY, levelRenderer, incomingDirection);
        } else {
            Entity actorOnTile = targetTile.getOccupiedBy();
            if (actor instanceof Player) {
                if (actorOnTile instanceof Collectible) {
                    onStep(actor, levelRenderer, incomingDirection);
                } else if (actorOnTile instanceof MovableBlock) {
                    boolean blockMoved = handleMovableBlockInteraction(actor, (Actor) actorOnTile, incomingDirection, levelRenderer);
                    if (!blockMoved) {
                        handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
                    }
                } else {
                    onStep(actor, levelRenderer, incomingDirection);
                }
            } else {
                handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
            }
        }
    }

    /**
     * Handles the interaction between an actor and a movable block.
     *
     * @param actor             The actor sliding on the Ice tile.
     * @param block             The movable block on the target tile.
     * @param direction         The direction in which the actor is sliding.
     * @param levelRenderer     The renderer for the game level.
     * @return True if the block is successfully moved, false otherwise.
     */
    private boolean handleMovableBlockInteraction(Actor actor, Actor block, Direction direction, LevelRenderer levelRenderer) {
        double blockNewX = block.getPosition().getX() + direction.getDx();
        double blockNewY = block.getPosition().getY() + direction.getDy();
        Optional<Tile> blockTargetTile = levelRenderer.getTileAtGridPosition((int) blockNewX, (int) blockNewY);
        if (blockTargetTile.isPresent() && blockTargetTile.get().isWalkable() && blockTargetTile.get().isOccupied()) {
            block.performMove(blockNewX, blockNewY, levelRenderer, direction);
            actor.performMove(actor.getPosition().getX() + direction.getDx(), actor.getPosition().getY() + direction.getDy(), levelRenderer, direction);
            return true;
        }
        return false;
    }

    /**
     * Handles the reverse sliding of the actor if unable to slide in the intended direction.
     *
     * @param actor             The actor sliding on the Ice tile.
     * @param levelRenderer     The renderer for the game level.
     * @param reverseDirection  The reverse direction from which the actor should slide.
     */
    private void handleReverseSlide(Actor actor, LevelRenderer levelRenderer, Direction reverseDirection) {
        Direction effectiveReverseDirection = determineSlideDirection(reverseDirection);
        double reverseX = actor.getPosition().getX() + effectiveReverseDirection.getDx();
        double reverseY = actor.getPosition().getY() + effectiveReverseDirection.getDy();
        Optional<Tile> reverseTileOptional = levelRenderer.getTileAtGridPosition((int) reverseX, (int) reverseY);
        if (reverseTileOptional.isPresent() && reverseTileOptional.get().isWalkable() && reverseTileOptional.get().isOccupied()) {
            actor.performMove(reverseX, reverseY, levelRenderer, effectiveReverseDirection);
        }
    }
}

