package com.group4.chipgame.profile;

import javafx.scene.input.KeyCode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Manages profiles within the game. This includes loading and saving profiles,
 * adding and removing profiles, and handling high scores and level completions.
 */
public class ProfileManager {
    private static final String PROFILES_FILE_PATH = "src/main/resources/profiles/profile.json";
    private static final int MAX_SCORE_ENTRIES = 10;
    private static final int INDENT_FACTOR = 4;
    private final List<Profile> profiles;
    private Profile currentProfile;
    private String lastUsedProfileName;

    /**
     * Constructs a new ProfileManager and loads existing profiles from a file.
     *
     * @throws IOException if an error occurs during file reading.
     */
    public ProfileManager() throws IOException {
        this.profiles = new ArrayList<>();
        loadProfiles();
    }

    /**
     * Adds a new profile with the given name and sets it as the current profile.
     *
     * @param name The name of the new profile.
     */
    public void addProfile(String name) {
        Profile newProfile = new Profile(name);
        setDefaultKeybinds(newProfile);
        profiles.add(newProfile);
        currentProfile = newProfile;
    }

    /**
     * Sets default keybinds for a given profile.
     *
     * @param profile The profile for which default keybinds are set.
     */
    private void setDefaultKeybinds(Profile profile) {
        profile.getKeybinds().put("moveUp", KeyCode.W);
        profile.getKeybinds().put("moveDown", KeyCode.S);
        profile.getKeybinds().put("moveLeft", KeyCode.A);
        profile.getKeybinds().put("moveRight", KeyCode.D);
    }

    /**
     * Marks a level as completed with a new score, updates if it's higher than the existing score.
     *
     * @param levelName The name of the level.
     * @param newScore  The score achieved in the level.
     */
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

    /**
     * Gets the high scores for each level from all profiles.
     *
     * @return A sorted map of level names to lists of high score entries.
     */
    public Map<String, List<Map.Entry<String, Integer>>> getHighScores() {
        TreeMap<String, List<Map.Entry<String, Integer>>> highScores = new TreeMap<>();

        for (Profile profile : profiles) {
            String profileName = profile.getName();
            profile.getLevelScores().forEach((levelName, score) -> {
                highScores.computeIfAbsent(levelName, k -> new ArrayList<>())
                        .add(new AbstractMap.SimpleEntry<>(profileName, score));
            });
        }

        highScores.forEach((levelName, scoreEntries) -> {
            scoreEntries.sort((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()));
            if (scoreEntries.size() > MAX_SCORE_ENTRIES) {
                highScores.put(levelName, scoreEntries.subList(0, MAX_SCORE_ENTRIES));
            }
        });

        return highScores;
    }

    /**
     * Loads the profiles from a JSON file. If the file does not exist, it creates a new file with
     * default settings.
     *
     * @throws IOException if an error occurs during reading or writing to the file.
     */
    private void loadProfiles() throws IOException {
        Path path = Paths.get(PROFILES_FILE_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("profiles", new JSONArray());
            jsonObject.put("lastUsedProfile", "");
            Files.writeString(path, jsonObject.toString(INDENT_FACTOR));
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

    /**
     * Saves all profiles to a JSON file, including their keybinds, completed levels, and save files.
     *
     * @throws IOException if an error occurs during writing to the file.
     */
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
        Files.writeString(Paths.get(PROFILES_FILE_PATH), jsonObject.toString(INDENT_FACTOR));
    }

    /**
     * Gets the current active profile.
     *
     * @return The current profile.
     */
    public Profile getCurrentProfile() {
        return currentProfile;
    }

    /**
     * Sets the given profile as the current active profile.
     *
     * @param profile The profile to set as current.
     */
    public void setCurrentProfile(Profile profile) {
        currentProfile = profile;
        lastUsedProfileName = profile != null ? profile.getName() : null;
    }

    /**
     * Retrieves a profile by its name.
     *
     * @param name The name of the profile to find.
     * @return An Optional containing the profile if found, or empty otherwise.
     */
    public Optional<Profile> getProfileByName(String name) {
        return profiles.stream().filter(p -> p.getName().equals(name)).findFirst();
    }

    /**
     * Removes a profile with the given name.
     *
     * @param profileName The name of the profile to remove.
     */
    public void removeProfile(String profileName) {
        profiles.removeIf(p -> p.getName().equals(profileName));
        if (currentProfile != null && currentProfile.getName().equals(profileName)) {
            currentProfile = null;
        }
    }

    /**
     * Returns a list of all profile names.
     *
     * @return List of profile names.
     */
    public List<String> getProfileNames() {
        List<String> names = new ArrayList<>();
        for (Profile profile : profiles) {
            names.add(profile.getName());
        }
        return names;
    }

    /**
     * Marks a level as completed for the current profile.
     *
     * @param currentLevelPath The path of the level that was completed.
     */
    public void markLevelAsCompleted(String currentLevelPath) {
        currentProfile.addCompletedLevel(currentLevelPath);
    }

    /**
     * Adds a save file path to a specified profile.
     *
     * @param profileName The name of the profile to which the save file will be added.
     * @param saveFilePath The path of the save file to add.
     * @throws IOException if an error occurs during saving profiles.
     */
    public void addSaveToProfile(String profileName, String saveFilePath) throws IOException {
        Optional<Profile> profile = getProfileByName(profileName);
        profile.ifPresent(value -> value.addSaveFilePath(saveFilePath));
        saveProfiles();
    }
}

