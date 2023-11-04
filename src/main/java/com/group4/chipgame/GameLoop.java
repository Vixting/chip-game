package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.*;

public class GameLoop extends AnimationTimer {
    private final List<Actor> actors;
    private final Queue<Direction> moveQueue = new LinkedList<>();

    private final LevelRenderer levelRenderer;
    private final CollisionHandler collisionHandler;
    private final Camera camera;
    private long ticksElapsed = 0;

    public GameLoop(List<Actor> actors, LevelRenderer levelRenderer, CollisionHandler collisionHandler, Camera camera) {
        this.actors = actors;
        this.levelRenderer = levelRenderer;
        this.collisionHandler = collisionHandler;
        this.camera = camera;
    }

    public static final Map<KeyCode, double[]> KEY_TO_DELTA = Map.of(
            KeyCode.UP, new double[]{0, -1},
            KeyCode.DOWN, new double[]{0, 1},
            KeyCode.LEFT, new double[]{-1, 0},
            KeyCode.RIGHT, new double[]{1, 0}
    );

    public Queue<Direction> getMoveQueue() {
        return moveQueue;
    }

    @Override
    public void handle(long now) {
        ticksElapsed++;
        for (Actor actor : actors) {
            if (actor.shouldMove(ticksElapsed)) {
                if (actor instanceof Player) {
                    camera.setTarget(actor);
                    if (!moveQueue.isEmpty()) {
                        Direction direction = moveQueue.poll();
                        double[] delta = Direction.toDelta(direction);
                        actor.move(delta[0], delta[1], levelRenderer, collisionHandler);
                    }
                }

            }
        }
    }

}
