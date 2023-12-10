package com.group4.chipgame.entities.actors;

import com.group4.chipgame.*;
import com.group4.chipgame.Level.LevelRenderer;
import com.group4.chipgame.entities.actors.collectibles.Collectible;
import com.group4.chipgame.entities.actors.tiles.Tile;
import com.group4.chipgame.entities.actors.tiles.Trap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Optional;

public abstract class Actor extends ImageView implements Entity {
    protected Point2D currentPosition;
    protected long moveInterval;
    protected boolean isMoving;

    public Actor(String imagePath, double x, double y) {
        initializeImage(imagePath);
        currentPosition = new Point2D(x, y);
        updatePosition();
        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    private void initializeImage(String imagePath) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);
    }

    public JSONObject serialize() {
        JSONObject object = new JSONObject();
        object.put("type", this.getClass().getSimpleName());
        object.put("x", currentPosition.getX());
        object.put("y", currentPosition.getY());
        return object;
    }

    private void updatePosition() {
        double offset = calculateOffset();
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
        EffectManager.applyDynamicShadowEffect(this);
    }

    private double calculateOffset() {
        return (Main.TILE_SIZE.get() - fitWidthProperty().get()) / 2.0;
    }

    public Point2D getPosition() {
        return currentPosition;
    }

    public boolean shouldMove(long ticksElapsed) {
        return ticksElapsed % this.moveInterval == 0;
    }

    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (isMoving) return;

        Direction direction = Direction.fromDelta(dx, dy);
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;

        performMove(newX, newY, levelRenderer, direction);
    }

    public boolean isMoving() {
        return isMoving;
    }

    protected boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        Point2D newPosition = currentPosition.add(dx, dy);
        return isMoveValid(newPosition, levelRenderer);
    }

    protected boolean isMoveValid(Point2D newPosition, LevelRenderer levelRenderer) {
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newPosition.getX(), (int) newPosition.getY());
        if (targetTileOpt.isEmpty()) return false;

        Optional<Tile> currentTileOpt = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());
        if (currentTileOpt.isPresent() && currentTileOpt.get() instanceof Trap currentTrap && currentTrap.isActive()) {
            return false;
        }

        Tile targetTile = targetTileOpt.get();
        Entity occupiedBy = targetTile.getOccupiedBy();

        return targetTile.isWalkable() && (occupiedBy == null || (this instanceof Player && occupiedBy instanceof Collectible ) || (this instanceof Enemy && occupiedBy instanceof Player));
    }

    public void performMove(double newX, double newY, LevelRenderer levelRenderer, Direction direction) {
        isMoving = true;
        Timeline timeline = createTimeline(newX, newY);

        updateTileOccupancy(levelRenderer, newX, newY, direction);

        isMoving = false;
        timeline.play();
    }

    private void updateTileOccupancy(LevelRenderer levelRenderer, double newX, double newY, Direction direction) {
        levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY())
                .ifPresent(tile -> tile.setOccupiedBy(null));

        currentPosition = new Point2D(newX, newY);

        levelRenderer.getTileAtGridPosition((int) newX, (int) newY)
                .ifPresent(tile -> {
                    tile.setOccupiedBy(this);
                    tile.onStep(this, levelRenderer, direction);
                });
    }

    private Timeline createTimeline(double newX, double newY) {
        double offset = calculateOffset();

        double distance = currentPosition.distance(newX, newY);
        double baseSpeed = 0.003;
        double durationMillis = distance / baseSpeed;

        KeyValue kvX = new KeyValue(this.layoutXProperty(), newX * Main.TILE_SIZE.get() + offset);
        KeyValue kvY = new KeyValue(this.layoutYProperty(), newY * Main.TILE_SIZE.get() + offset);
        KeyFrame kf = new KeyFrame(Duration.millis(durationMillis), kvX, kvY);

        return new Timeline(kf);
    }
}
