package com.group4.chipgame;

import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.actors.Actor;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class LevelRenderer {

    // Constants for offset calculations
    private static final double OFFSET_X = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;
    private static final double OFFSET_Y = (Main.TILE_SIZE - Main.ACTOR_SIZE) / 2.0;

    // Panes for rendering the game components
    private final Pane gamePane;
    private final Pane tilesPane;
    private final Pane actorsPane;
    private final Pane collectiblesPane;

    // 2D array to store tiles
    private Tile[][] tiles;

    public LevelRenderer() {
        this.collectiblesPane = new Pane();
        this.gamePane = new Pane();
        this.tilesPane = new Pane();
        this.actorsPane = new Pane();
        this.gamePane.getChildren().addAll(tilesPane, actorsPane, collectiblesPane);
    }

    public List<Collectible> getCollectibles() {
        return collectiblesPane.getChildren().stream()
                .filter(node -> node instanceof Collectible)
                .map(node -> (Collectible) node)
                .collect(Collectors.toList());
    }

    public void renderCollectibles(List<Collectible> collectibles) {
        Optional.ofNullable(collectibles).ifPresent(collectiblesList -> collectiblesList.forEach(this::positionAndAddCollectible));
    }

    private void positionAndAddCollectible(Collectible collectible) {
        Point2D collectiblePosition = collectible.getPosition();
        if (collectiblePosition != null) {
            double collectibleLayoutX = collectiblePosition.getX() * Main.TILE_SIZE + OFFSET_X;
            double collectibleLayoutY = collectiblePosition.getY() * Main.TILE_SIZE + OFFSET_Y;
            collectible.setLayoutX(collectibleLayoutX);
            collectible.setLayoutY(collectibleLayoutY);
            collectiblesPane.getChildren().add(collectible);
        }
    }

    public void removeCollectible(Collectible collectible) {
        Platform.runLater(() -> collectiblesPane.getChildren().remove(collectible));
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public List<Actor> getActors() {
        return actorsPane.getChildren().stream()
                .filter(node -> node instanceof Actor)
                .map(node -> (Actor) node)
                .collect(Collectors.toList());
    }

    public void renderTiles(Tile[][] tiles) {
        this.tiles = tiles;
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile != null) {
                    tile.setGridPosition(x, y);
                    tile.bindSize();
                    tile.setLayoutX(x * Main.TILE_SIZE);
                    tile.setLayoutY(y * Main.TILE_SIZE);
                    tilesPane.getChildren().add(tile);
                } else {
                    System.out.println("Warning: Tile at position (" + x + ", " + y + ") is null.");
                }
            }
        }
    }

    public void renderActors(List<Actor> actors) {
        Optional.ofNullable(actors).ifPresent(actorsList -> actorsList.forEach(this::positionAndAddActor));
    }

    public void clear() {
        tilesPane.getChildren().clear();
        actorsPane.getChildren().clear();
    }

    public void removeActor(Actor actor) {
        if (actor == null || actor.getPosition() == null) return;

        Point2D actorPosition = actor.getPosition();
        int x = (int) actorPosition.getX();
        int y = (int) actorPosition.getY();

        if (isOutOfBounds(x, y)) return;

        getTileAtGridPosition(x, y).ifPresent(tile -> tile.setOccupiedBy(null));

        Platform.runLater(() -> actorsPane.getChildren().remove(actor));
    }

    public void updateTile(int x, int y, Tile newTile) {
        if (isOutOfBounds(x, y) || newTile == null) return;

        newTile.setGridPosition(x, y);
        tiles[y][x] = newTile;

        System.out.printf("Tile at (%d, %d) updated to %s%n", x, y, newTile.getClass().getSimpleName());

        newTile.bindSize();
        newTile.setLayoutX(x * Main.TILE_SIZE);
        newTile.setLayoutY(y * Main.TILE_SIZE);
        tilesPane.getChildren().set(y * tiles[0].length + x, newTile);
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length;
    }

    public Optional<Tile> getTileAtGridPosition(int x, int y) {
        if (isOutOfBounds(x, y)) return Optional.empty();
        return Optional.of(tiles[y][x]);
    }

    private void positionAndAddActor(Actor actor) {
        Point2D actorPosition = actor.getPosition();
        if (actorPosition != null) {
            double actorLayoutX = actorPosition.getX() * Main.TILE_SIZE + OFFSET_X;
            double actorLayoutY = actorPosition.getY() * Main.TILE_SIZE + OFFSET_Y;
            actor.setLayoutX(actorLayoutX);
            actor.setLayoutY(actorLayoutY);
            actorsPane.getChildren().add(actor);
        }
    }

    public void initializeBindings(Scene scene) {
        DoubleBinding scaleBinding = calculateScaleBinding(scene);
        gamePane.scaleXProperty().bind(scaleBinding);
        gamePane.scaleYProperty().bind(scaleBinding);
        gamePane.translateXProperty().bind(Bindings.createDoubleBinding(() ->
                        calculateTranslateValue(scene.getWidth(), tiles[0].length),
                scene.widthProperty(), scaleBinding));
        gamePane.translateYProperty().bind(Bindings.createDoubleBinding(() ->
                        calculateTranslateValue(scene.getHeight(), tiles.length),
                scene.heightProperty(), scaleBinding));
    }

    private DoubleBinding calculateScaleBinding(Scene scene) {
        final double HORIZONTAL_TILE_COUNT = tiles[0].length;
        final double VERTICAL_TILE_COUNT = tiles.length;
        final double ORIGINAL_GAME_WIDTH = Main.TILE_SIZE * HORIZONTAL_TILE_COUNT;
        final double ORIGINAL_GAME_HEIGHT = Main.TILE_SIZE * VERTICAL_TILE_COUNT;

        return Bindings.createDoubleBinding(() -> {
            double scaleX = scene.getWidth() / ORIGINAL_GAME_WIDTH;
            double scaleY = scene.getHeight() / ORIGINAL_GAME_HEIGHT;
            return Math.min(scaleX, scaleY);
        }, scene.widthProperty(), scene.heightProperty());
    }

    private double calculateTranslateValue(double sceneDimension, int tileCount) {
        double scaleValue = sceneDimension / (Main.TILE_SIZE * tileCount);
        return (sceneDimension - (Main.TILE_SIZE * tileCount) * scaleValue) / 2;
    }
}
