package com.group4.chipgame;

import com.group4.chipgame.profile.ProfileManager;
import javafx.scene.input.KeyCode;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MovementHandler {
    private final GameLoop gameLoop;
    private final ProfileManager profileManager;
    private final Main mainClass;
    private long lastInputTime = 0;
    private final long inputDelay;
    private final int maxQueueSize;

    public MovementHandler(GameLoop gameLoop, ProfileManager profileManager, Main mainClass, long inputDelay, int maxQueueSize) {
        this.gameLoop = gameLoop;
        this.profileManager = profileManager;
        this.mainClass = mainClass;
        this.inputDelay = inputDelay;
        this.maxQueueSize = maxQueueSize;
    }

    public void handleKeyPress(KeyCode keyCode) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInputTime > inputDelay) {
            lastInputTime = currentTime;
            processKeyInput(keyCode);
        }
    }

    private void processKeyInput(KeyCode key) {
        Map<String, KeyCode> keybinds = profileManager.getCurrentProfile().getKeybinds();
        Direction newDirection = null;

        if (key.equals(keybinds.get("moveUp"))) {
            newDirection = Direction.UP;
        } else if (key.equals(keybinds.get("moveDown"))) {
            newDirection = Direction.DOWN;
        } else if (key.equals(keybinds.get("moveLeft"))) {
            newDirection = Direction.LEFT;
        } else if (key.equals(keybinds.get("moveRight"))) {
            newDirection = Direction.RIGHT;
        }

        if (newDirection != null) {
            handleMovement(newDirection);
            return;
        }

        handleSpecialKeys(key);
    }

    private void handleSpecialKeys(KeyCode key) {
        if (key == KeyCode.ESCAPE) {
            mainClass.toggleSettingsMenu();
        } else if (key == KeyCode.M) {
            saveQuickSave();
        } else if (key == KeyCode.L) {
            loadQuickSave();
        }
    }

    private void saveQuickSave() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String quickSaveName = "quickSave_" + timestamp;
            mainClass.saveGame(quickSaveName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQuickSave() {
        try {
            List<String> saves = profileManager.getCurrentProfile().getSaveFilePaths();
            if (!saves.isEmpty()) {
                String mostRecentSave = saves.get(saves.size() - 1);
                mainClass.loadGame(mostRecentSave);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleMovement(Direction newDirection) {
        Direction lastDirection = gameLoop.getMoveQueue().isEmpty() ? null : gameLoop.getMoveQueue().peek();

        if (newDirection != lastDirection && gameLoop.getMoveQueue().size() < maxQueueSize) {
            gameLoop.getMoveQueue().add(newDirection);
        }
    }
}
