package com.group4.chipgame.profile;

import javafx.scene.input.KeyCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ProfileManager {
    private static final String PROFILES_FILE_PATH = "src/main/resources/profiles/profile.json";
    private final List<Profile> profiles;
    private Profile currentProfile;
    private String lastUsedProfileName;

    public ProfileManager() throws IOException {
        this.profiles = new ArrayList<>();
        loadProfiles();
    }

    public void addProfile(String name) {
        Profile newProfile = new Profile(name);
        setDefaultKeybinds(newProfile);
        profiles.add(newProfile);
        currentProfile = newProfile;
    }

    private void setDefaultKeybinds(Profile profile) {
        profile.getKeybinds().put("moveUp", KeyCode.W);
        profile.getKeybinds().put("moveDown", KeyCode.S);
        profile.getKeybinds().put("moveLeft", KeyCode.A);
        profile.getKeybinds().put("moveRight", KeyCode.D);
    }

    public void markLevelAsCompleted(String levelName, int newScore) {
        if (currentProfile != null) {
            Integer existingScore = currentProfile.getLevelScores().get(levelName);

            if (existingScore == null || newScore > existingScore) {
                currentProfile.setLevelScore(levelName, newScore);
                try {
                    saveProfiles();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<String, List<Map.Entry<String, Integer>>> getHighScores() {
        Map<String, List<Map.Entry<String, Integer>>> highScores = new HashMap<>();

        for (Profile profile : profiles) {
            String profileName = profile.getName();
            profile.getLevelScores().forEach((levelName, score) -> {
                highScores.computeIfAbsent(levelName, k -> new ArrayList<>())
                        .add(new AbstractMap.SimpleEntry<>(profileName, score));
            });
        }

        highScores.forEach((levelName, scoreEntries) -> {
            scoreEntries.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));
            if (scoreEntries.size() > 10) {
                highScores.put(levelName, scoreEntries.subList(0, 10));
            }
        });

        return highScores;
    }


    private void loadProfiles() throws IOException {
        Path path = Paths.get(PROFILES_FILE_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profiles", new JSONArray());
            jsonObject.put("lastUsedProfile", "");
            Files.writeString(path, jsonObject.toString(4));
        }

        String content = Files.readString(path);
        JSONObject jsonObject = new JSONObject(content);
        JSONArray profilesArray = jsonObject.getJSONArray("profiles");
        lastUsedProfileName = jsonObject.optString("lastUsedProfile", "");

        for (int i = 0; i < profilesArray.length(); i++) {
            JSONObject profileObj = profilesArray.getJSONObject(i);
            String name = profileObj.getString("name");
            Profile profile = new Profile(name);

            JSONObject keybindsObj = profileObj.getJSONObject("keybinds");
            for (String action : keybindsObj.keySet()) {
                profile.getKeybinds().put(action, KeyCode.valueOf(keybindsObj.getString(action)));
            }

            JSONArray completedLevels = profileObj.getJSONArray("completedLevels");
            for (int j = 0; j < completedLevels.length(); j++) {
                profile.addCompletedLevel(completedLevels.getString(j));
            }

            if (profileObj.has("levelScores")) {
                JSONObject levelScoresObj = profileObj.getJSONObject("levelScores");
                for (String level : levelScoresObj.keySet()) {
                    profile.setLevelScore(level, levelScoresObj.getInt(level));
                }
            }

            JSONArray savesArray = profileObj.optJSONArray("saves");
            if (savesArray != null) {
                for (int j = 0; j < savesArray.length(); j++) {
                    profile.addSaveFilePath(savesArray.getString(j));
                }
            }

            profiles.add(profile);
        }

        if (!lastUsedProfileName.isEmpty()) {
            currentProfile = getProfileByName(lastUsedProfileName).orElse(null);
        }
        if (currentProfile == null && !profiles.isEmpty()) {
            currentProfile = profiles.get(0);
        }
    }


    public void saveProfiles() throws IOException {
        JSONObject jsonObject = new JSONObject();
        JSONArray profilesArray = new JSONArray();
        for (Profile profile : profiles) {
            JSONObject profileObj = new JSONObject();
            JSONObject scoresObj = new JSONObject();
            profile.getLevelScores().forEach(scoresObj::put);
            profileObj.put("levelScores", scoresObj);
            profileObj.put("name", profile.getName());
            JSONObject keybindsObj = new JSONObject();
            for (Map.Entry<String, KeyCode> entry : profile.getKeybinds().entrySet()) {
                keybindsObj.put(entry.getKey(), entry.getValue().toString());
            }
            profileObj.put("keybinds", keybindsObj);
            JSONArray completedLevels = new JSONArray(profile.getCompletedLevels());
            profileObj.put("completedLevels", completedLevels);
            JSONArray savesArray = new JSONArray();
            for (String savePath : profile.getSaveFilePaths()) {
                savesArray.put(savePath);
                System.out.println(savePath);
            }
            profileObj.put("saves", savesArray);
            profilesArray.put(profileObj);
        }
        jsonObject.put("profiles", profilesArray);
        jsonObject.put("lastUsedProfile", currentProfile != null ? currentProfile.getName() : "");
        Files.writeString(Paths.get(PROFILES_FILE_PATH), jsonObject.toString(4));
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile profile) {
        currentProfile = profile;
        lastUsedProfileName = profile != null ? profile.getName() : null;
    }

    public Optional<Profile> getProfileByName(String name) {
        return profiles.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    public void removeProfile(String profileName) {
        profiles.removeIf(p -> p.getName().equals(profileName));
        if (currentProfile != null && currentProfile.getName().equals(profileName)) {
            currentProfile = null;
        }
    }

    public List<String> getProfileNames() {
        List<String> names = new ArrayList<>();
        for (Profile profile : profiles) {
            names.add(profile.getName());
        }
        return names;
    }

    public void markLevelAsCompleted(String currentLevelPath) {
        currentProfile.addCompletedLevel(currentLevelPath);
    }

    public void addSaveToProfile(String profileName, String saveFilePath) throws IOException {
        Optional<Profile> profile = getProfileByName(profileName);
        profile.ifPresent(value -> value.addSaveFilePath(saveFilePath));
        saveProfiles();
    }
}

