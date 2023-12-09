package com.group4.chipgame.entities.actors.collectibles;

import com.group4.chipgame.EffectManager;
import com.group4.chipgame.entities.actors.Entity;
import com.group4.chipgame.Main;
import com.group4.chipgame.entities.actors.Actor;
import com.group4.chipgame.entities.actors.Player;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.JSONObject;

import java.util.Objects;

public class Collectible extends ImageView implements Entity {
    protected Point2D currentPosition;
    private final String imagePath;

    public Collectible(String imagePath, double x, double y) {
        this.imagePath = imagePath; // Store the image path
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);

        this.currentPosition = new Point2D(x, y);
        updatePosition();

        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", currentPosition.getX());
        object.put("y", currentPosition.getY());
        return object;
    }

    public String getImagePath() {
        return imagePath;
    }

    private void updatePosition() {
        double offset = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
        EffectManager.applyDynamicShadowEffect(this);
    }

    @Override
    public Point2D getPosition() {
        return currentPosition;
    }

    public void onCollect(Actor actor) {
        if (actor instanceof Player) {
            this.setVisible(false);
        }
    }
}
