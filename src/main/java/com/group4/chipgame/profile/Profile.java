package com.group4.chipgame.profile;

import java.io.Serializable;
import java.util.HashMap;

public class Profile implements Serializable {
    private final String name;
    private String saveFilePath;
    private HashMap<String, String> keyBinds;

    public Profile(String name) {
        this.name = name;
        this.saveFilePath = "";
        this.keyBinds = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public String getSaveFilePath() {
        return saveFilePath;
    }

    public void setSaveFilePath(String saveFilePath) {
        this.saveFilePath = saveFilePath;
    }

    public HashMap<String, String> getKeyBinds() {
        return keyBinds;
    }

    public void setKeyBind(String action, String key) {
        keyBinds.put(action, key);
    }
}
