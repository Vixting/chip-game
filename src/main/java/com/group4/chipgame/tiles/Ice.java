package com.group4.chipgame.tiles;

import com.group4.chipgame.Direction;
import com.group4.chipgame.LevelRenderer;
import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
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
            Direction effectiveSlideDirection = determineSlideDirection(incomingDirection);
            double newX = actor.currentPosition.getX() + effectiveSlideDirection.getDx();
            double newY = actor.currentPosition.getY() + effectiveSlideDirection.getDy();

            handleActorOnIce(actor, newX, newY, levelRenderer, effectiveSlideDirection);
        });

        pause.play();
    }

    private Direction determineSlideDirection(Direction incomingDirection) {
        return switch (corner) {
            case BOTTOM_LEFT -> {
                if (incomingDirection == Direction.LEFT) yield Direction.UP;
                if (incomingDirection == Direction.DOWN) yield Direction.RIGHT;
                yield incomingDirection;
            }
            case BOTTOM_RIGHT -> {
                if (incomingDirection == Direction.RIGHT) yield Direction.UP;
                if (incomingDirection == Direction.DOWN) yield Direction.LEFT;
                yield incomingDirection;
            }
            case TOP_LEFT -> {
                if (incomingDirection == Direction.LEFT) yield Direction.DOWN;
                if (incomingDirection == Direction.UP) yield Direction.RIGHT;
                yield incomingDirection;
            }
            case TOP_RIGHT -> {
                if (incomingDirection == Direction.RIGHT) yield Direction.DOWN;
                if (incomingDirection == Direction.UP) yield Direction.LEFT;
                yield incomingDirection;
            }
            case NONE, default -> incomingDirection;
        };
    }

    private void handleActorOnIce(Actor actor, double newX, double newY, LevelRenderer levelRenderer, Direction incomingDirection) {
        Optional<Tile> optionalTargetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

        if (optionalTargetTile.isPresent()) {
            Tile targetTile = optionalTargetTile.get();
            if (targetTile.isWalkable()) {
                if (!targetTile.isOccupied()) {
                    actor.performMove(newX, newY, levelRenderer, incomingDirection);
                } else {
                    Actor actorOnTile = targetTile.getOccupiedBy();
                    if (actorOnTile instanceof Player) {
                        levelRenderer.removeActor(actorOnTile);
                        System.out.println("Player has been killed due to being hit by a sliding actor!");
                    } else {
                        handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
                    }
                }
            } else {
                handleReverseSlide(actor, levelRenderer, incomingDirection.getOpposite());
            }
        }
    }

    private void handleReverseSlide(Actor actor, LevelRenderer levelRenderer, Direction reverseDirection) {
        Direction effectiveReverseDirection = determineSlideDirection(reverseDirection);
        double reverseX = actor.currentPosition.getX() + effectiveReverseDirection.getDx();
        double reverseY = actor.currentPosition.getY() + effectiveReverseDirection.getDy();

        Optional<Tile> reverseTileOptional = levelRenderer.getTileAtGridPosition((int) reverseX, (int) reverseY);
        if (reverseTileOptional.isPresent() && reverseTileOptional.get().isWalkable() && !reverseTileOptional.get().isOccupied()) {
            actor.performMove(reverseX, reverseY, levelRenderer, effectiveReverseDirection);
        }  // Additional logic if needed, e.g., actor.stop();

    }
}
