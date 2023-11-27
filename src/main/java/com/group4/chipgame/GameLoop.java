package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Enemy;
import com.group4.chipgame.actors.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.*;

public class GameLoop extends AnimationTimer {
    private final List<Actor> actors;
    private final Queue<Direction> moveQueue = new LinkedList<>();

    private final LevelRenderer levelRenderer;
    private final Camera camera;
    private long ticksElapsed = 0;

    public GameLoop(List<Actor> actors, LevelRenderer levelRenderer, Camera camera) {
        this.actors = actors;
        this.levelRenderer = levelRenderer;
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
                    if (!moveQueue.isEmpty() && ((Player) actor).isAlive()) {
                        Direction direction = moveQueue.poll();
                        assert direction != null;
                        double[] delta = Direction.toDelta(direction);
                        actor.move(delta[0], delta[1], levelRenderer);
                    }
                } else if (actor instanceof Enemy enemy) {
                    enemy.makeMoveDecision(levelRenderer);
                }

            }
        }
    }

}
