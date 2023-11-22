package com.group4.chipgame.profile;

import java.io.Serializable;
import java.util.HashMap;

/**
 * The Profile class represents a user profile with a name, save file path, and key bindings.
 * It implements the Serializable interface to allow for object serialization.
 */
public class Profile implements Serializable {

    private final String name;
    private String saveFilePath;
    private HashMap<String, String> keyBinds;

    /**
     * Constructs a Profile with the specified name, initializing save file path and key bindings.
     *
     * @param name The name of the profile.
     */
    public Profile(String name) {
        this.name = name;
        this.saveFilePath = "";
        this.keyBinds = new HashMap<>();
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
     * Gets the save file path associated with the profile.
     *
     * @return The save file path.
     */
    public String getSaveFilePath() {
        return saveFilePath;
    }

    /**
     * Sets the save file path for the profile.
     *
     * @param saveFilePath The new save file path.
     */
    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    /**
     * Gets the key bindings associated with the profile.
     *
     * @return A HashMap containing key bindings (action to key mapping).
     */
    public HashMap<String, String> getKeyBinds() {
        return keyBinds;
    }

    /**
     * Sets a key binding for a specific action.
     *
     * @param action The action for which the key binding is set.
     * @param key    The key associated with the action.
     */
    public void setKeyBind(String action, String key) {
        keyBinds.put(action, key);
    }
}
