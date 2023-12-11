package com.group4.chipgame;

import com.group4.chipgame.profile.ProfileManager;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles key press events
 * and performs actions based on the key pressed,
 * including controlling player movement and executing special commands.
 * @author William Buckley
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
    public KeybindHandler(final GameLoop gameLoop,
                          final ProfileManager profileManager,
                          final Main mainClass,
                          final long inputDelay,
                          final int maxQueueSize) {

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
    public void handleKeyPress(final KeyCode keyCode) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastInputTime > inputDelay) {
            lastInputTime = currentTime;
            processKeyInput(keyCode);
        }
    }

    /**
     * Processes the key input to determine the action to take.
     */
    private void processKeyInput(final KeyCode key) {
        Direction newDirection = getDirectionForKey(key);
        if (newDirection != null) {
            handleMovement(newDirection);
        } else {
            handleSpecialKeys(key);
        }
    }

    /**
     * Converts a key press into a directional input based on the current profile's keybindings.
     */
    private Direction getDirectionForKey(final KeyCode key) {
        Map<String, KeyCode> keybinds = profileManager.getCurrentProfile().getKeybinds();

        Map<KeyCode, Direction> keyDirectionMap = new HashMap<>();
        keyDirectionMap.put(keybinds.get("moveUp"), Direction.UP);
        keyDirectionMap.put(keybinds.get("moveDown"), Direction.DOWN);
        keyDirectionMap.put(keybinds.get("moveLeft"), Direction.LEFT);
        keyDirectionMap.put(keybinds.get("moveRight"), Direction.RIGHT);

        return keyDirectionMap.getOrDefault(key, null);
    }


    /**
     * Handles actions for special keys, such as opening settings, saving, or loading the game.
     */
    private void handleSpecialKeys(final KeyCode key) {
        switch (key) {
            case ESCAPE -> mainClass.toggleSettingsMenu();
            case M -> saveQuickSave();
            case L -> loadQuickSave();
            default -> {
            }
        }
    }

    /**
     * Saves the current game state as a quick save with a timestamped filename.
     */
    private void saveQuickSave() {
        String timestamp = new
                SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String quickSaveName = "quickSave_"
                + timestamp;
        try {
            mainClass.saveGame(quickSaveName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the most recent quick save game state, if available.
     */
    private void loadQuickSave() {
        List<String> saves =
                profileManager.getCurrentProfile().getSaveFilePaths();
        if (!saves.isEmpty()) {
            String mostRecentSave = saves.get(saves.size() - 1);
            try {
                mainClass.loadGame(mostRecentSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Handles movement direction input by adding the direction to the movement queue if it's not
     * already in the queue and the queue size is within the maximum limit.
     */
    private void handleMovement(final Direction newDirection) {
        if (gameLoop.getMoveQueue().size()
                < maxQueueSize
                && !gameLoop.getMoveQueue().contains(newDirection)) {
            gameLoop.getMoveQueue().add(newDirection);
        }
    }
}
