package com.group4.chipgame.profile;

import javafx.scene.input.KeyCode;

import java.util.*;

public class Profile {
    private String name;
    private Map<String, KeyCode> keybinds;
    private Set<String> completedLevels;
    private final Map<String, Integer> levelScores = new HashMap<>();
    private final List<String> savedGames;
    private final List<String> saveFilePaths;

    public Profile(String name) {
        this.name = name;
        this.keybinds = new HashMap<>();
        this.completedLevels = new HashSet<>();
        this.savedGames = new ArrayList<>();
        this.saveFilePaths = new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public List<String> getSavedGames() {
        return savedGames;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevelScore(String level, int score) {
        levelScores.put(level, score);
    }

    public Optional<Integer> getLevelScore(String level) {
        return Optional.ofNullable(levelScores.get(level));
    }

    public Map<String, KeyCode> getKeybinds() {
        return keybinds;
    }

    public void setKeybinds(Map<String, KeyCode> keybinds) {
        this.keybinds = keybinds;
    }

    public Set<String> getCompletedLevels() {
        return completedLevels;
    }

    public List<String> getSaveFilePaths() {
        return saveFilePaths;
    }

    public void addSaveFilePath(String filePath) {
        if (!saveFilePaths.contains(filePath)) {
            saveFilePaths.add(filePath);
        }
    }

    public void removeSaveFilePath(String filePath) {
        saveFilePaths.remove(filePath);
    }

    public void setCompletedLevels(Set<String> completedLevels) {
        this.completedLevels = completedLevels;
    }

    public void addCompletedLevel(String levelName) {
        completedLevels.add(levelName);
    }

    public Map<String, Integer> getLevelScores() {
        return levelScores;
    }
}
