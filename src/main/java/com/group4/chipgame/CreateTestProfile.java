package com.group4.chipgame;

import com.group4.chipgame.profile.ProfileManager;
import javafx.scene.input.KeyCode;

import java.io.IOException;

public class CreateTestProfile {
    public static void main(String[] args) {
        try {
            ProfileManager profileManager = new ProfileManager();

            String profileName = "x";
            profileManager.addProfile(profileName);

            profileManager.getCurrentProfile().getKeybinds().put("moveUp", KeyCode.W);
            profileManager.getCurrentProfile().getKeybinds().put("moveDown", KeyCode.S);
            profileManager.getCurrentProfile().getKeybinds().put("moveLeft", KeyCode.A);
            profileManager.getCurrentProfile().getKeybinds().put("moveRight", KeyCode.D);

            profileManager.saveProfiles();

            System.out.println("Test profile created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to create test profile.");
        }
    }
}
