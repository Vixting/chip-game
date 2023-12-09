package com.group4.chipgame.entities.actors.tiles;

import com.group4.chipgame.entities.actors.Entity;
import org.json.JSONObject;

public class Button extends Tile {
    private String id;
    private boolean isActive = false;

    public Button(String id) {
        super("/images/chipgame/tiles/button.png", true);
        this.id = id;
    }

    public String getConnection() {
        return id;
    }

    public void setConnection(String id) {
        this.id = id;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setOccupiedBy(Entity actor) {
        super.setOccupiedBy(actor);
        setActive(actor != null);
    }

    @Override
    public JSONObject serialize() {
        JSONObject json = super.serialize();
        json.put("id", this.getConnection());
        json.put("isActive", this.isActive());
        return json;
    }
}