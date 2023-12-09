package com.group4.chipgame.entities.actors.tiles;

import org.json.JSONObject;

public class Trap extends Tile {
    private Button linkedButton;
    private String id;

    public Trap(Button linkedButton, String id) {
        super("/images/chipgame/tiles/trap.jpg", true);
        this.linkedButton = linkedButton;
        this.id = id;
    }

    public boolean isActive() {
        return linkedButton != null && !linkedButton.isActive();
    }


    public String getConnection() {
        return id;
    }

    public void setConnection(String id) {
        this.id = id;
    }

    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("id", this.getConnection());
        System.out.println("linkedButtonId: " + this.getConnection());
        return json;
    }

    public Button getLinkedButton() {
        return linkedButton;
    }

    public void setLinkedButton(Button linkedButton) {
        this.linkedButton = linkedButton;
    }
}