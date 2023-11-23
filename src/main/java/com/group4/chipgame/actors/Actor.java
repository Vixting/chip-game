package com.group4.chipgame.actors;

import com.group4.chipgame.*;
import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.tiles.Trap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Optional;

public abstract class Actor extends ImageView implements Entity {
    protected Point2D currentPosition;
    protected long moveInterval;
    protected boolean isMoving;

    public Actor(String imagePath, double x, double y) {
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        setImage(image);
        setSmooth(true);
        fitWidthProperty().bind(Main.ACTOR_SIZE);
        fitHeightProperty().bind(Main.ACTOR_SIZE);

        currentPosition = new Point2D(x, y);
        updatePosition();

        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updatePosition());
    }

    private void updatePosition() {
        double offset = (Main.TILE_SIZE.get() - fitWidthProperty().get()) / 2.0;
        setLayoutX(currentPosition.getX() * Main.TILE_SIZE.get() + offset);
        setLayoutY(currentPosition.getY() * Main.TILE_SIZE.get() + offset);
        EffectManager.applyDynamicShadowEffect(this);
    }


    public Point2D getPosition() {
        return currentPosition;
    }

    public boolean shouldMove(long ticksElapsed) {
        return ticksElapsed % moveInterval == 0;
    }

    public void move(double dx, double dy, LevelRenderer levelRenderer) {
        if (!isMoving) {
            Direction direction = Direction.fromDelta(dx, dy);
            double newX = currentPosition.getX() + dx;
            double newY = currentPosition.getY() + dy;

            if (canMove(dx, dy, levelRenderer)) {
                performMove(newX, newY, levelRenderer, direction);
            }
        }
    }

    boolean canMove(double dx, double dy, LevelRenderer levelRenderer) {
        double newX = currentPosition.getX() + dx;
        double newY = currentPosition.getY() + dy;
        Optional<Tile> targetTileOpt = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);

        if (targetTileOpt.isEmpty()) return false;

        Optional<Tile> currentTileOpt = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());
        if (currentTileOpt.isPresent() && currentTileOpt.get() instanceof Trap currentTrap && currentTrap.isActive()) {
            return false;
        }

        Tile targetTile = targetTileOpt.get();
        Entity occupiedBy = targetTile.getOccupiedBy();

        return targetTile.isWalkable() && (occupiedBy == null || (this instanceof Player && occupiedBy instanceof Collectible));
    }

    public void performMove(double newX, double newY, LevelRenderer levelRenderer, Direction direction) {
        isMoving = true;

        double offset = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        Timeline timeline = createTimeline(newX * Main.TILE_SIZE.get() + offset, newY * Main.TILE_SIZE.get() + offset);

        Optional<Tile> currentTile = levelRenderer.getTileAtGridPosition((int) currentPosition.getX(), (int) currentPosition.getY());
        currentTile.ifPresent(tile -> tile.setOccupiedBy(null)); // Mark old tile as unoccupied

        currentPosition = new Point2D(newX, newY);
        Optional<Tile> targetTile = levelRenderer.getTileAtGridPosition((int) newX, (int) newY);
        targetTile.ifPresent(tile -> tile.setOccupiedBy(this)); // Mark new tile as occupied
        targetTile.ifPresent(tile -> tile.onStep(this, levelRenderer, direction));

        isMoving = false;
        timeline.play();
    }

    private Timeline createTimeline(double targetX, double targetY) {
        KeyValue kvX = new KeyValue(this.layoutXProperty(), targetX);
        KeyValue kvY = new KeyValue(this.layoutYProperty(), targetY);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kvX, kvY);

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(kf);
        return timeline;
    }
}