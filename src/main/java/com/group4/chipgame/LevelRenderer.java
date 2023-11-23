package com.group4.chipgame;

import com.group4.chipgame.collectibles.Collectible;
import com.group4.chipgame.tiles.Tile;
import com.group4.chipgame.actors.Actor;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LevelRenderer {

    private final Pane gamePane;
    private final Pane tilesPane;
    private final Pane actorsPane;
    private final Pane collectiblesPane;
    private Tile[][] tiles;

    public LevelRenderer() {
        tilesPane = new Pane();
        actorsPane = new Pane();
        collectiblesPane = new Pane();
        gamePane = new Pane(tilesPane, actorsPane, collectiblesPane);

        Main.TILE_SIZE.addListener((obs, oldVal, newVal) -> updateSizes());
        Main.ACTOR_SIZE.addListener((obs, oldVal, newVal) -> updateSizes());
    }

    public void updateSizes() {
        if (tiles != null) {
            renderTiles(tiles);
        }
        renderNodes(actorsPane, getActors());
        renderNodes(collectiblesPane, getCollectibles());

        if (tiles != null && tiles.length > 0 && tiles[0].length > 0) {
            gamePane.setLayoutX(Main.TILE_SIZE.get() * tiles[0].length);
            gamePane.setLayoutY(Main.TILE_SIZE.get() * tiles.length);
        }
    }

    public List<Collectible> getCollectibles() {
        return castToList(collectiblesPane, Collectible.class);
    }

    public List<Actor> getActors() {
        return castToList(actorsPane, Actor.class);
    }

    public void renderCollectibles(List<Collectible> collectibles) {
        renderNodes(collectiblesPane, collectibles);
    }

    public void renderActors(List<Actor> actors) {
        renderNodes(actorsPane, actors);
    }

    public void renderTiles(Tile[][] tiles) {
        this.tiles = tiles;
        tilesPane.getChildren().clear();
        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[y].length; x++) {
                Tile tile = tiles[y][x];
                if (tile != null) {
                    renderTile(tile, x, y);
                }
            }
        }
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void clear() {
        tilesPane.getChildren().clear();
        actorsPane.getChildren().clear();
        collectiblesPane.getChildren().clear();
    }

    public void remove(Actor actor) {
        modifyPaneLater(() -> actorsPane.getChildren().remove(actor));
    }

    public void remove(Collectible collectible) {
        modifyPaneLater(() -> collectiblesPane.getChildren().remove(collectible));
    }

    private void modifyPaneLater(Runnable action) {
        Platform.runLater(action);
    }

    private <T> List<T> castToList(Pane pane, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        pane.getChildren().forEach(node -> {
            if (clazz.isInstance(node)) {
                list.add(clazz.cast(node));
            }
        });
        return list;
    }

    private <T> void renderNodes(Pane pane, List<T> nodes) {
        Optional.ofNullable(nodes).ifPresent(list -> list.forEach(node -> {
            if (node != null && !pane.getChildren().contains(node)) {
                positionAndAddNode(pane, node);
            }
        }));
    }

    private void renderTile(Tile tile, int x, int y) {
        tile.setGridPosition(x, y);
        tile.setLayoutX(x * Main.TILE_SIZE.get());
        tile.setLayoutY(y * Main.TILE_SIZE.get());
        tilesPane.getChildren().add(tile);
    }

    private <T> void positionAndAddNode(Pane pane, T node) {
        if (node instanceof Collectible) {
            position((Collectible) node);
        } else if (node instanceof Actor) {
            position((Actor) node);
        }
        pane.getChildren().add((javafx.scene.Node) node);
    }

    private void position(Actor actor) {
        position(actor, actor.getPosition());
    }

    private void position(Collectible collectible) {
        position(collectible, collectible.getPosition());
    }

    private void position(javafx.scene.Node node, Point2D position) {
        if (position == null) return;
        double offsetX = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        double offsetY = (Main.TILE_SIZE.get() - Main.ACTOR_SIZE.get()) / 2.0;
        node.setLayoutX(position.getX() * Main.TILE_SIZE.get() + offsetX);
        node.setLayoutY(position.getY() * Main.TILE_SIZE.get() + offsetY);
    }

    public Optional<Tile> getTileAtGridPosition(int x, int y) {
        return isOutOfBounds(x, y) ? Optional.empty() : Optional.ofNullable(tiles[y][x]);
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= tiles[0].length || y < 0 || y >= tiles.length;
    }

    public void updateTile(int x, int y, Tile newTile) {
        if (isOutOfBounds(x, y) || newTile == null) return;

        newTile.setGridPosition(x, y);
        tiles[y][x] = newTile;
        newTile.setLayoutX(x * Main.TILE_SIZE.get());
        newTile.setLayoutY(y * Main.TILE_SIZE.get());
        tilesPane.getChildren().set(y * tiles[0].length + x, newTile);
    }
}
