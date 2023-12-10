package com.group4.chipgame.profile;

import javafx.scene.input.KeyCode;

import java.util.*;

/**
 * Represents a player's profile in the game, including their name, keybinds,
 * completed levels, level scores, and save file paths.
 */
public class Profile {
    private String name;
    private final Map<String, KeyCode> keybinds;
    private final Set<String> completedLevels;
    private final Map<String, Integer> levelScores = new HashMap<>();
    private final List<String> saveFilePaths;

    /**
     * Constructs a new Profile with the specified name.
     *
     * @param name The name of the profile.
     */
    public Profile(String name) {
        this.name = name;
        this.keybinds = new HashMap<>();
        this.completedLevels = new HashSet<>();
        this.saveFilePaths = new ArrayList<>();
    }

    /**
     * Gets the name of the profile.
     *
     * @return The name of the profile.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the profile.
     *
     * @param name The new name of the profile.
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Sets the score for a specified level.
     *
     * @param level The level for which the score is to be set.
     * @param score The score to be set for the level.
     */
    public void setLevelScore(String level, int score) {
        levelScores.put(level, score);
    }

    /**
     * Gets the keybinds set for this profile.
     *
     * @return A map of keybinds.
     */
    public Map<String, KeyCode> getKeybinds() {
        return keybinds;
    }

    /**
     * Gets the set of levels completed by this profile.
     *
     * @return A set of completed levels.
     */
    public Set<String> getCompletedLevels() {
        return completedLevels;
    }

    /**
     * Gets the list of save file paths associated with this profile.
     *
     * @return A list of save file paths.
     */
    public List<String> getSaveFilePaths() {
        return saveFilePaths;
    }

    /**
     * Adds a save file path to this profile.
     *
     * @param filePath The file path to be added.
     */
    public void addSaveFilePath(String filePath) {
        if (!saveFilePaths.contains(filePath)) {
            saveFilePaths.add(filePath);
        }
    }

    /**
     * Adds a level to the set of completed levels.
     *
     * @param levelName The name of the level to be marked as completed.
     */
    public void addCompletedLevel(String levelName) {
        completedLevels.add(levelName);
    }

    /**
     * Gets the scores for each level achieved by this profile.
     *
     * @return A map of level names to scores.
     */
    public Map<String, Integer> getLevelScores() {
        return levelScores;
    }
}
