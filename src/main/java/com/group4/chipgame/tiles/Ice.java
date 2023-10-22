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

public class Ice extends Tile {

    private final Direction.Corner corner;

    public Ice(Direction.Corner corner) {
        super(getImagePathForCorner(corner), true);
        this.corner = corner;
    }

    private static String getImagePathForCorner(Direction.Corner corner) {
        return switch (corner) {
            case NONE -> "/images/chipgame/tiles/ice/ice.jpg";
            case BOTTOM_LEFT -> "/images/chipgame/tiles/ice/ice_bottom_left.jpg";
            case BOTTOM_RIGHT -> "/images/chipgame/tiles/ice/ice_bottom_right.jpg";
            case TOP_LEFT -> "/images/chipgame/tiles/ice/ice_top_left.jpg";
            case TOP_RIGHT -> "/images/chipgame/tiles/ice/ice_top_right.jpg";
        };
    }

    public void onStep(Actor actor, LevelRenderer levelRenderer, Direction incomingDirection) {
        System.out.println("Sliding!");

        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(event -> {
            Direction slideDirection = determineSlideDirection(incomingDirection);
            double newX = actor.getPosition().getX() + slideDirection.getDx();
            double newY = actor.getPosition().getY() + slideDirection.getDy();
            handleActorOnIce(actor, newX, newY, levelRenderer, slideDirection);
        });

        pause.play();
    }

    private Direction determineSlideDirection(Direction incomingDirection) {
        return switch (corner) {
            case BOTTOM_LEFT -> (incomingDirection == Direction.LEFT) ? Direction.UP : (incomingDirection == Direction.DOWN) ? Direction.RIGHT : incomingDirection;
            case BOTTOM_RIGHT -> (incomingDirection == Direction.RIGHT) ? Direction.UP : (incomingDirection == Direction.DOWN) ? Direction.LEFT : incomingDirection;
            case TOP_LEFT -> (incomingDirection == Direction.LEFT) ? Direction.DOWN : (incomingDirection == Direction.UP) ? Direction.RIGHT : incomingDirection;
            case TOP_RIGHT -> (incomingDirection == Direction.RIGHT) ? Direction.DOWN : (incomingDirection == Direction.UP) ? Direction.LEFT : incomingDirection;
            default -> incomingDirection;
        };
    }

    private void handleActorOnIce(Actor actor, double newX, double newY, LevelRenderer levelRenderer, Direction incomingDirection) {
        Optional<Tile> targetTileOptional = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

        if (targetTileOptional.isPresent()) {
            Tile targetTile = targetTileOptional.get();

            if (actor instanceof Player player && targetTile instanceof LockedDoor door) {

                if (player.hasKey(door.getRequiredKeyColor())) {
                    targetTile.onStep(actor, levelRenderer, incomingDirection);
                    onStep(actor, levelRenderer, incomingDirection);
                } else {
                    handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
                }
                return;
            }

            if (targetTile.isWalkable()) {
                handleTileOccupancy(targetTile, actor, newX, newY, levelRenderer, incomingDirection);
            } else {
                handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
            }
        }
    }

    private void handleTileOccupancy(Tile targetTile, Actor actor, double newX, double newY, LevelRenderer levelRenderer, Direction incomingDirection) {
        if (targetTile.isOccupied()) {
            actor.performMove(newX, newY, levelRenderer, incomingDirection);
        } else {
            Entity actorOnTile = targetTile.getOccupiedBy();
                if (actor instanceof Player) {
                    if (actorOnTile instanceof Collectible) {
                        actor.onCollect(actorOnTile);
                        levelRenderer.removeCollectible((Collectible) actorOnTile);
                        onStep(actor, levelRenderer, incomingDirection);
                    } else if (actorOnTile instanceof MovableBlock) {
                        boolean blockMoved = handleMovableBlockInteraction(actor, (Actor) actorOnTile, incomingDirection, levelRenderer);
                        if (!blockMoved) {
                            handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
                        }
                    }
            } else {
                handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
            }
        }
    }

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
