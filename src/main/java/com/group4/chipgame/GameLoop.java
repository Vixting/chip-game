package com.group4.chipgame;

import com.group4.chipgame.actors.Actor;
import com.group4.chipgame.actors.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;

import java.util.*;

/**
 * The GameLoop class is responsible for handling the game loop and updating the game state based on player input and actor movements.
 */
public class GameLoop extends AnimationTimer {
    private final List<Actor> actors;
    private final Queue<Direction> moveQueue = new LinkedList<>();

    private final LevelRenderer levelRenderer;
    private final Camera camera;
    private long ticksElapsed = 0;

    /**
     * Constructs a GameLoop instance with the specified actors, level renderer, and camera.
     *
     * @param actors        The list of actors in the game.
     * @param levelRenderer The LevelRenderer instance associated with the game.
     * @param camera        The Camera instance used to track the player.
     */
    public GameLoop(List<Actor> actors, LevelRenderer levelRenderer, Camera camera) {
        this.actors = actors;
        this.levelRenderer = levelRenderer;
        this.camera = camera;
    }

    /**
     * A mapping of KeyCode to the corresponding directional delta values for movement.
     */
    public static final Map<KeyCode, double[]> KEY_TO_DELTA = Map.of(
            KeyCode.UP, new double[]{0, -1},
            KeyCode.DOWN, new double[]{0, 1},
            KeyCode.LEFT, new double[]{-1, 0},
            KeyCode.RIGHT, new double[]{1, 0}
    );

    /**
     * Gets the move queue, which stores the player's movement directions.
     *
     * @return The move queue.
     */
    public Queue<Direction> getMoveQueue() {
        return moveQueue;
    }

    /**
     * Handles the game loop, updating the game state based on player input and actor movements.
     *
     * @param now The current timestamp.
     */
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
                        actor.move(delta[0], delta[1], levelRenderer);
                    }
                }
            }
        }
    }
}
