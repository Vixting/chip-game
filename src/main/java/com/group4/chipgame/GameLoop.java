package com.group4.chipgame;

import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Enemy;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.ui.TimerUI;
import javafx.animation.AnimationTimer;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This class handles the main game loop, including updating actors' movements,
 * rendering the level, and managing the game timer.
 * @author William Buckley
 */
public class GameLoop extends AnimationTimer {
    private List<Actor> actors;
    private final Queue<Direction> moveQueue = new LinkedList<>();
    private final LevelRenderer levelRenderer;
    private final Camera camera;
    private final TimerUI timerUI;
    private long ticksElapsed = 0;
    private long lastTimerUpdate = 0;
    private static final long NANOS_PER_SECOND = 1_000_000_000;

    /**
     * Constructs a GameLoop with the specified actors,
     * level renderer, camera, and timer UI.
     *
     * @param actors        The list of actors in the game.
     * @param levelRenderer The renderer for the game level.
     * @param camera        The camera following the player.
     * @param timerUI       The UI component for the game timer.
     */
    public GameLoop(final List<Actor> actors,
                    final LevelRenderer levelRenderer,
                    final Camera camera,
                    final TimerUI timerUI) {
        this.actors = actors;
        this.levelRenderer = levelRenderer;
        this.camera = camera;
        this.timerUI = timerUI;
    }

    /**
     * Returns the queue of directions for player movement.
     *
     * @return The queue of movement directions.
     */
    public Queue<Direction> getMoveQueue() {
        return moveQueue;
    }

    /**
     * The main game loop, called at each animation frame.
     * Handles game logic updates.
     *
     * @param now The timestamp of the current frame given in nanoseconds.
     */
    @Override
    public void handle(final long now) {
        ticksElapsed++;
        this.actors = new CopyOnWriteArrayList<>(actors);

        if (lastTimerUpdate == 0) {
            lastTimerUpdate = now;
        }

        if (now - lastTimerUpdate >= NANOS_PER_SECOND) {
            updateTimer();
            lastTimerUpdate = now;
        }

        for (Actor actor : actors) {
            if (actor.shouldMove(ticksElapsed)) {
                if (actor instanceof Player) {
                    handlePlayerMovement((Player) actor);
                } else if (actor instanceof Enemy enemy) {
                    handleEnemyMovement(enemy);
                }
            }
        }
    }

    /**
     * Updates the game timer and stops the game if time runs out.
     */
    private void updateTimer() {
        int remainingTime = timerUI.getTimeRemaining() - 1;
        timerUI.updateTime(remainingTime);
        if (remainingTime <= 0) {
            this.stop();
        }
    }

    /**
     * Handles movement for player characters.
     */
    private void handlePlayerMovement(final Player player) {
        camera.setTarget(player);
        if (!moveQueue.isEmpty()
                && player.isAlive()
                && !player.isMoving()) {
            Direction direction = moveQueue.poll();
            assert direction != null;
            double[] delta = Direction.toDelta(direction);
            player.move(delta[0], delta[1], levelRenderer);
        }
    }

    /**
     * Handles movement decisions for enemy characters.
     */
    private void handleEnemyMovement(final Enemy enemy) {
        if (!enemy.isMoving()) {
            enemy.makeMoveDecision(levelRenderer);
        }
    }
}
