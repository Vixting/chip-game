package com.group4.chipgame;

import com.group4.chipgame.profile.ProfileManager;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * This class handles key press events and performs actions based on the key pressed,
 * including controlling player movement and executing special commands.
 */
public class KeybindHandler {
    private final GameLoop gameLoop;
    private final ProfileManager profileManager;
    private final Main mainClass;
    private long lastInputTime = 0;
    private final long inputDelay;
    private final int maxQueueSize;

    /**
     * Constructs a new KeybindHandler.
     *
     * @param gameLoop         The main game loop to interact with.
     * @param profileManager   The profile manager for keybind settings.
     * @param mainClass        The main class instance of the game.
     * @param inputDelay       The delay between inputs to prevent rapid firing.
     * @param maxQueueSize     The maximum size of the move queue.
     */
    public KeybindHandler(GameLoop gameLoop,
                          ProfileManager profileManager,
                          Main mainClass,
                          long inputDelay,
                          int maxQueueSize) {

        this.gameLoop = gameLoop;
        this.profileManager = profileManager;
        this.mainClass = mainClass;
        this.inputDelay = inputDelay;
        this.maxQueueSize = maxQueueSize;
    }

    /**
     * Handles key press events.
     *
     * @param keyCode The key code of the pressed key.
     */
    public void handleKeyPress(KeyCode keyCode) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInputTime > inputDelay) {
            lastInputTime = currentTime;
            processKeyInput(keyCode);
        }
    }

    private void processKeyInput(KeyCode key) {
        Direction newDirection = getDirectionForKey(key);
        if (newDirection != null) {
            handleMovement(newDirection);
        } else {
            handleSpecialKeys(key);
        }
    }

    private Direction getDirectionForKey(KeyCode key) {
        Map<String, KeyCode> keybinds = profileManager.getCurrentProfile().getKeybinds();
        if (key.equals(keybinds.get("moveUp"))) {
            return Direction.UP;
        }
        if (key.equals(keybinds.get("moveDown"))) {
            return Direction.DOWN;
        }
        if (key.equals(keybinds.get("moveLeft"))) {
            return Direction.LEFT;
        }
        if (key.equals(keybinds.get("moveRight"))) {
            return Direction.RIGHT;
        }
        return null;
    }

    private void handleSpecialKeys(KeyCode key) {
        switch (key) {
            case ESCAPE:
                mainClass.toggleSettingsMenu();
                break;
            case M:
                saveQuickSave();
                break;
            case L:
                loadQuickSave();
                break;
            default:

        }
    }

    private void saveQuickSave() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String quickSaveName = "quickSave_" + timestamp;
        try {
            mainClass.saveGame(quickSaveName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQuickSave() {
        List<String> saves = profileManager.getCurrentProfile().getSaveFilePaths();
        if (!saves.isEmpty()) {
            String mostRecentSave = saves.get(saves.size() - 1);
            try {
                mainClass.loadGame(mostRecentSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleMovement(Direction newDirection) {
        if (gameLoop.getMoveQueue().size() < maxQueueSize && !gameLoop.getMoveQueue().contains(newDirection)) {
            gameLoop.getMoveQueue().add(newDirection);
        }
    }
}
