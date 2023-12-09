package com.group4.chipgame;

import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Enemy;
import com.group4.chipgame.entities.actors.Player;
import com.group4.chipgame.ui.TimerUI;
import javafx.animation.AnimationTimer;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameLoop extends AnimationTimer {
    private List<Actor> actors;
    private final Queue<Direction> moveQueue = new LinkedList<>();
    private final LevelRenderer levelRenderer;
    private final Camera camera;
    private final TimerUI timerUI;
    private long ticksElapsed = 0;
    private long lastTimerUpdate = 0;

    public GameLoop(List<Actor> actors, LevelRenderer levelRenderer, Camera camera, TimerUI timerUI) {
        this.actors = actors;
        this.levelRenderer = levelRenderer;
        this.camera = camera;
        this.timerUI = timerUI;
    }

    public Queue<Direction> getMoveQueue() {
        return moveQueue;
    }

    @Override
    public void handle(long now) {
        ticksElapsed++;
        this.actors = new CopyOnWriteArrayList<>(actors);

        if (lastTimerUpdate == 0) {
            lastTimerUpdate = now;
        }

        if (now - lastTimerUpdate >= 1_000_000_000) {
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

    private void updateTimer() {
        int remainingTime = timerUI.getTimeRemaining() - 1;
        timerUI.updateTime(remainingTime);
        if (remainingTime <= 0) {
            this.stop();
        }
    }

    private void handlePlayerMovement(Player player) {
        camera.setTarget(player);
        if (!moveQueue.isEmpty() && player.isAlive() && !player.isMoving()) {
            Direction direction = moveQueue.poll();
            assert direction != null;
            double[] delta = Direction.toDelta(direction);
            player.move(delta[0], delta[1], levelRenderer);
        }
    }

    private void handleEnemyMovement(Enemy enemy) {
        if (!enemy.isMoving()) {
            enemy.makeMoveDecision(levelRenderer);
        }
    }
}
