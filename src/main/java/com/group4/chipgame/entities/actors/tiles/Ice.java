package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Entity;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.entities.actors.MovableBlock;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.Level.LevelRenderer;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import org.json.JSONObject;

import java.util.Optional;

/**
 * Class representing an Ice tile in the game.
 */
public class Ice extends Tile {

    private final Direction.Corner corner;
    static final int DURATION = 500;
    static final String BASE_PATH = "/images/chipgame/tiles/ice/";
    static final String ICE = "ice.png";
    static final String ICE_BOTTOM_LEFT = "ice_bottom_left.png";
    static final String ICE_BOTTOM_RIGHT = "ice_bottom_right.png";
    static final String ICE_TOP_LEFT = "ice_top_left.png";
    static final String ICE_TOP_RIGHT = "ice_top_right.png";


    /**
     * Constructor for Ice class.
     *
     * @param corner The corner direction of the ice.
     */
    public Ice(Direction.Corner corner) {
        super(getImagePathForCorner(corner), true);
        this.corner = corner;
    }

    /**
     * Gets the image path for the specified corner of ice.
     *
     * @param corner The corner for which the image path is required.
     * @return The image path as a string.
     */
    private static String getImagePathForCorner(Direction.Corner corner) {
        return switch (corner) {
            case NONE -> BASE_PATH + ICE;
            case BOTTOM_LEFT -> BASE_PATH + ICE_BOTTOM_LEFT;
            case BOTTOM_RIGHT -> BASE_PATH + ICE_BOTTOM_RIGHT;
            case TOP_LEFT -> BASE_PATH + ICE_TOP_LEFT;
            case TOP_RIGHT -> BASE_PATH + ICE_TOP_RIGHT;
        };
    }

    /**
     * Handles the action when an actor steps on this tile.
     * It initiates the sliding process by creating a pause transition.
     *
     * @param actor The actor stepping on the tile.
     * @param levelRenderer The renderer for the level.
     * @param incomingDirection The direction from which the actor steps onto the tile.
     */
    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        System.out.println("Sliding!");
        PauseTransition pause = createPauseTransition(actor, levelRenderer, incomingDirection);
        pause.play();
    }

    /**
     * Serializes this tile's state to JSON.
     *
     * @return A JSONObject containing the state of this tile.
     */
    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("corner", this.corner);
        return json;
    }

    private PauseTransition createPauseTransition(
            Actor actor,
            LevelRenderer levelRenderer,
            Direction incomingDirection) {
        PauseTransition pause = new PauseTransition(Duration.millis(DURATION));
        pause.setOnFinished(event -> performSlide(actor, levelRenderer, incomingDirection));
        return pause;
    }

    private void performSlide(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        Direction slideDirection = determineSlideDirection(incomingDirection);
        double newX = actor.getPosition().getX() + slideDirection.getDx();
        double newY = actor.getPosition().getY() + slideDirection.getDy();

        if (actor instanceof Player) {
            ((Player) actor).checkForCollectibles(newX, newY, levelRenderer);
        }
        System.out.println("Sliding to " + newX + ", " + newY);
        handleActorOnIce(actor, newX, newY, levelRenderer, slideDirection);
    }

    private Direction determineSlideDirection(Direction incomingDirection) {
        return switch (corner) {
            case BOTTOM_LEFT ->
                    (incomingDirection == Direction.LEFT)
                            ? Direction.UP : (incomingDirection == Direction.DOWN)
                            ? Direction.RIGHT : incomingDirection;
            case BOTTOM_RIGHT ->
                    (incomingDirection == Direction.RIGHT)
                            ? Direction.UP : (incomingDirection == Direction.DOWN)
                            ? Direction.LEFT : incomingDirection;
            case TOP_LEFT ->
                    (incomingDirection == Direction.LEFT)
                            ? Direction.DOWN : (incomingDirection == Direction.UP)
                            ? Direction.RIGHT : incomingDirection;
            case TOP_RIGHT ->
                    (incomingDirection == Direction.RIGHT)
                            ? Direction.DOWN : (incomingDirection == Direction.UP)
                            ? Direction.LEFT : incomingDirection;
            default -> incomingDirection;
        };
    }

    private void handleActorOnIce(
            Actor actor,
            double newX,
            double newY,
            LevelRenderer levelRenderer,
            Direction incomingDirection) {
        Optional<Tile> targetTileOptional =
                levelRenderer.getTileAtGridPosition((int) newX, (int) newY);
        targetTileOptional.ifPresent(tile ->
                handleTileInteraction(
                        actor,
                        newX,
                        newY,
                        tile,
                        levelRenderer,
                        incomingDirection));
    }

    private void handleTileInteraction(
            Actor actor,
            double newX,
            double newY,
            Tile targetTile,
            LevelRenderer levelRenderer,
            Direction incomingDirection) {
        System.out.println("Handling tile interaction");
        if (actor instanceof Player player && targetTile instanceof LockedDoor door) {
            handlePlayerDoorInteraction(player, door, levelRenderer, incomingDirection);
            return;
        }

        if (targetTile.isWalkable()) {
            Entity actorOnTile = targetTile.getOccupiedBy();
            if (actorOnTile instanceof Player && actor instanceof MovableBlock) {
                ((Player) actorOnTile).kill(levelRenderer);

                continueSlide(actor, levelRenderer, incomingDirection);
            } else {
                handleTileOccupancy(targetTile, actor, newX, newY, levelRenderer, incomingDirection);
            }
        } else {
            handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
        }
    }

    private void continueSlide(Actor actor, LevelRenderer levelRenderer, Direction direction) {
        double nextX = actor.getPosition().getX() + direction.getDx();
        double nextY = actor.getPosition().getY() + direction.getDy();

        Optional<Tile> nextTileOpt = levelRenderer.getTileAtGridPosition((int) nextX, (int) nextY);
        if (nextTileOpt.isPresent() && nextTileOpt.get().isWalkable() && !nextTileOpt.get().isOccupied()) {
            actor.performMove(nextX, nextY, levelRenderer, direction);
        }
    }


    private void handlePlayerDoorInteraction(
            Player player,
            LockedDoor door,
            LevelRenderer levelRenderer,
            Direction incomingDirection) {
        if (player.hasKey(door.getRequiredKeyColor())) {
            door.onStep(player, levelRenderer, incomingDirection);
            onStep(player, levelRenderer, incomingDirection);
        } else {
            handleReverseSlide(player, levelRenderer, incomingDirection.getOpposite());
        }
    }

    private void handleTileOccupancy(
            Tile targetTile,
            Actor actor,
            double newX,
            double newY,
            LevelRenderer levelRenderer,
            Direction incomingDirection) {
        if (!targetTile.isOccupied()) {
            actor.performMove(newX, newY, levelRenderer, incomingDirection);
            System.out.println("Sliding to " + newX + ", " + newY);
        } else {

            Entity actorOnTile = targetTile.getOccupiedBy();
            System.out.println("Actor on tile: " + actorOnTile);
            if (actor instanceof Player) {
                if (actorOnTile instanceof Collectible) {
                    onStep(actor, levelRenderer, incomingDirection);
                } else if (actorOnTile instanceof MovableBlock) {
                    boolean blockMoved = handleMovableBlockInteraction(
                            actor,
                            (Actor) actorOnTile,
                            incomingDirection,
                            levelRenderer);
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

    private boolean handleMovableBlockInteraction(
            Actor actor,
            Actor block,
            Direction direction,
            LevelRenderer levelRenderer) {
        double blockNewX = block.getPosition().getX() + direction.getDx();
        double blockNewY = block.getPosition().getY() + direction.getDy();
        Optional<Tile> blockTargetTile = levelRenderer.getTileAtGridPosition((int) blockNewX, (int) blockNewY);
        if (blockTargetTile.isPresent() && blockTargetTile.get().isWalkable() && !blockTargetTile.get().isOccupied()) {
            block.performMove(blockNewX, blockNewY, levelRenderer, direction);
            actor.performMove(
                    actor.getPosition().getX() + direction.getDx(),
                    actor.getPosition().getY() + direction.getDy(),
                    levelRenderer, direction);
            return true;
        }
        return false;
    }

    private void handleReverseSlide(Actor actor, LevelRenderer levelRenderer, Direction reverseDirection) {
        Direction effectiveReverseDirection = determineSlideDirection(reverseDirection);
        double reverseX = actor.getPosition().getX() + effectiveReverseDirection.getDx();
        double reverseY = actor.getPosition().getY() + effectiveReverseDirection.getDy();
        Optional<Tile> reverseTileOptional = levelRenderer.getTileAtGridPosition((int) reverseX, (int) reverseY);
        if (reverseTileOptional.isPresent()
                && reverseTileOptional.get().isWalkable()
                && !reverseTileOptional.get().isOccupied()) {
            actor.performMove(reverseX, reverseY, levelRenderer, effectiveReverseDirection);
        }
    }
}
